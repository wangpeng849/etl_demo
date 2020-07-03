package com.example.etl_demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.etl_demo.json.*;
import com.example.etl_demo.utils.JsonUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.*;

/**
 * @Author wangp
 * @Date 2020/7/1
 * @Version 1.0
 */
@RestController("/etl")
@Api(tags = "ETL")
public class ETLController {

    ETL etl = (ETL) JsonUtils.jsonTobObject("./etl.json", ETL.class);


    @GetMapping("test")
    @ApiOperation("测试")
    public Object test() {
        return JsonUtils.jsonTobObject("./etl.json", ETL.class);
    }


    @GetMapping("extract")
    @ApiOperation("抽取数据")
    public Object extract() {
        List<Extract> extracts = etl.getExtracts();
        for (Extract extract : extracts) {
            String extractId = extract.getExtractId();
            Connection connection = getConnection(extract.getDataSource());
            extractOutMysql(extract.getDataSource(), extractId, connection,extract.getCount());
        }
        return etl.getExtracts();
    }

    private void extractOutMysql(ETLDataSource dataSource, String extractId, Connection connection,Integer count) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = generateExtractSQL(dataSource);
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            List<String> fields = dataSource.getFields();
            List<Map<String,Object>> resList = new ArrayList<>();
            while (resultSet.next() && resList.size() < count) {
                int size = 0;
                Map<String,Object> res = new HashMap<>();
                while(size != fields.size()){
                    res.put(fields.get(size),resultSet.getObject(size+1));
                    size++;
                }
                resList.add(res);
            }
            JsonUtils.outputFileByJSONArray((JSONArray) JSONArray.toJSON(resList),extractId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateExtractSQL(ETLDataSource dataSource) {
        List<String> fields = dataSource.getFields();
        String fieldStr = "";
        for (String field : fields) {
            fieldStr += "`" + field + "`,";
        }
        fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
        return  "SELECT "+ fieldStr + " from " + dataSource.getTable();
    }

    @GetMapping("transfer")
    @ApiOperation("转换数据")
    public Object transfer() {
        List<Extract> extracts = etl.getExtracts();
        for (Extract extract : extracts) {
            gotoTransfer(extract.getNextOperatorId(), extract.getExtractId());
        }
        return etl.getTransfers();
    }


    @GetMapping("load")
    @ApiOperation("加载数据")
    public Object load() {
        List<Loads> loads = etl.getLoads();
        for (Loads load : loads) {
            String lastOperatorId = load.getLastOperatorId();
            Connection connection = connectionMysql(load);
            loadInMysql(load.getDataSource(), lastOperatorId, connection);
        }
        return loads;
    }

    private void loadInMysql(ETLDataSource dataSource, String lastOperatorId, Connection connection) {
        JSONArray json = (JSONArray) JSONArray.parse(JsonUtils.convertFileToStr("./" + lastOperatorId + ".json"));
        List<String> fields = dataSource.getFields();
        String sql = generateLoadSQL(dataSource, fields);
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (Object o : json) {
                //数据库不存在数据时
                int flag = 0;
                JSONObject jsonObject = (JSONObject) o;
                for (int i = 0; i < fields.size(); i++) {
                    Object obj = jsonObject.get(fields.get(i));
                    if (obj == null) break;
                    flag++;
                    ps.setString(i + 1, obj.toString());
                }
                if (flag != 0) {
                    while (flag < fields.size()) {
                        ps.setString(flag + 1, "");
                    }
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateLoadSQL(ETLDataSource dataSource, List<String> fields) {
        StringBuffer sql = new StringBuffer("insert into " + dataSource.getTable() + " (");
        for (String field : fields) {
            sql.append("`" + field + "`,");
        }
        String sqlStr = sql.substring(0, sql.length() - 1);
        sqlStr += ") ";
        String valueStr = "values (";
        for (int i = 0; i < fields.size(); i++) {
            valueStr += "? ,";
        }
        valueStr = valueStr.substring(0, valueStr.length() - 1);
        valueStr += ")";
        return sqlStr + valueStr;
    }

    private void gotoExtract(String lastOperatorId) {

    }

    private String gotoTransfer(String curOperatorId, String lastOperatorId) {
        if (!curOperatorId.startsWith("s")) gotoExtract(curOperatorId);
        while (!curOperatorId.equals("load")) {
            for (Transfers transfer : etl.getTransfers()) {
                if (transfer.getStepId().equals(curOperatorId)) {
                    curOperatorId = executorTransfer(transfer, lastOperatorId);
                    lastOperatorId = transfer.getStepId();
                }
            }
        }
        return curOperatorId;
    }

    private String executorTransfer(Transfers transfer, String lastOperatorId) {
        String str = JsonUtils.convertFileToStr("./" + lastOperatorId + ".json");
        List<JSONObject> data = (List<JSONObject>) JSONObject.parse(str);


        String action = transfer.getAction();
        if ("sum".equals(action)) {
            return sumAction(data, transfer);
        }
        if ("add".equals(action)) {
            return addAction(data, transfer);
        }
        if ("join".equals(action)) {
            return joinAction(transfer);
        }
        return "";
    }

    private String sumAction(List<JSONObject> data, Transfers transfer) {
        int count = 0;
        for (JSONObject datum : data) {
            for (Map.Entry<String, Object> entry : datum.entrySet()) {
                if (entry.getKey().equals(transfer.getFields().get(0))) {
                    count += (int) entry.getValue();
                }
            }
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("count", count);
        data.add(0, new JSONObject(map));
        JsonUtils.outputFileByListJSON(data, transfer.getStepId());
        return transfer.getNextId();
    }

    private String addAction(List<JSONObject> data, Transfers transfer) {
        int index = 0;
        for (JSONObject datum : data) {
            Map<String, Object> map = Maps.newHashMap();
            for (Map.Entry<String, Object> entry : datum.entrySet()) {
                if (entry.getKey().equals(transfer.getFields().get(0))) {
                    map.put(entry.getKey(), entry.getValue() + transfer.getAddStr());
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            data.set(index, new JSONObject(map));
            index++;
        }
        JsonUtils.outputFileByListJSON(data, transfer.getStepId());
        return transfer.getNextId();
    }

    private String joinAction(Transfers transfer) {
        JSONArray array = new JSONArray();
        for (String field : transfer.getFields()) {
            String s = JsonUtils.convertFileToStr("./" + field + ".json");
            array.addAll((Collection<? extends Object>) JSONArray.parse(s));
        }
        JsonUtils.outputFileByJSONArray(array, transfer.getStepId());
        return transfer.getNextId();
    }


    private Connection connectionMysql(Loads loads) {
        Connection connection = getConnection(loads.getDataSource());
        Assert.notNull(connection, "连接失败！");
        return connection;
    }

    private Connection getConnection(ETLDataSource dataSource) {
        try {
            Class.forName(dataSource.getDriverClassName());
            return DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }
}
