package com.example.etl_demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.etl_demo.json.ETL;
import com.example.etl_demo.json.Extract;
import com.example.etl_demo.json.Loads;
import com.example.etl_demo.json.Transfers;
import com.example.etl_demo.utils.JsonUtils;
import com.google.common.collect.Maps;
import com.rabbitmq.tools.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author wangp
 * @Date 2020/7/1
 * @Version 1.0
 */
@RestController("/etl")
@Api(tags = "ETL")
public class ETLController {

    ETL etl = (ETL) JsonUtils.jsonTobObject("./etl.js", ETL.class);


    @GetMapping("test")
    @ApiOperation("测试")
    public Object test() {
        return JsonUtils.jsonTobObject("./etl.js", ETL.class);
    }


    @GetMapping("extract")
    @ApiOperation("抽取数据")
    public Object extract() {
        return etl.getExtracts();
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
        for (int i = 0; i < loads.size(); i++) {
            String lastOperatorId = loads.get(i).getLastOperatorId();
            //TODO
            gotoTransfer("",lastOperatorId);
        }
        return loads;
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
//        String extractId = transfer.getExtractId();
        String str = JsonUtils.convertFileToStr("./" + lastOperatorId + ".js");
        List<JSONObject> data = (List<JSONObject>) JSONObject.parse(str);
        String action = transfer.getAction();
        if ("sum".equals(action)) {
            return sumAction(data, transfer);
        }
        if ("add".equals(action)) {
            return addAction(data, transfer);
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
                    map.put(entry.getKey(), entry.getValue() + "xx");
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

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }
}
