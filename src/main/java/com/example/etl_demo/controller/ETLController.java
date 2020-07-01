package com.example.etl_demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.etl_demo.json.ETL;
import com.example.etl_demo.json.Loads;
import com.example.etl_demo.json.Transfers;
import com.example.etl_demo.utils.JsonUtils;
import com.google.common.collect.Maps;
import com.rabbitmq.tools.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return gotoTransfer("s_1232");
//        return etl.getTransfers();
    }


    @GetMapping("load")
    @ApiOperation("加载数据")
    public Object load() {
        List<Loads> loads = etl.getLoads();
        for (int i = 0; i < loads.size(); i++) {
            String lastOperatorId = loads.get(i).getLastOperatorId();
            gotoTransfer(lastOperatorId);
        }
        return loads;
    }

    private void gotoExtract(String lastOperatorId) {

    }

    private List<JSONObject> gotoTransfer(String lastOperatorId) {
        if (!lastOperatorId.startsWith("s")) gotoExtract(lastOperatorId);
        List<JSONObject> res = null;
        for (Transfers transfer : etl.getTransfers()) {
//            if(transfer.getStepId().equals(lastOperatorId)){
            res = executorTransfer(transfer);
//           }
        }
        return res;
    }

    private List<JSONObject> executorTransfer(Transfers transfer) {
        String extractId = transfer.getExtractId();
        String str = JsonUtils.convertFileToStr("./676415bf-4157-44ad-952c-0e2f6a852394.js");
        List<JSONObject> data = (List<JSONObject>) JSONObject.parse(str);
        String action = transfer.getAction();
        Integer count = 0;
        int index = 0;
        for (JSONObject datum : data) {
            System.out.println(index);
            if ("sum".equals(action)) {
                for (Map.Entry<String, Object> entry : datum.entrySet()) {
                    if (entry.getKey().equals(transfer.getFields().get(0))) {
                        count += (int) entry.getValue();
                    }
                }
            }
            if("add".equals(action)){
                Map<String, Object> map = Maps.newHashMap();
                for (Map.Entry<String, Object> entry : datum.entrySet()) {
                    if (entry.getKey().equals(transfer.getFields().get(0))) {
                        map.put(entry.getKey(),entry.getValue()+"xx");
                    }else{
                        map.put(entry.getKey(),entry.getValue());
                    }
                }
                data.set(index,new JSONObject(map));
            }
            index++;
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("count",count);
        data.add(0, new JSONObject(map));
        String s = JsonUtils.outputFileByListJSON(data);
        return data;
    }


}
