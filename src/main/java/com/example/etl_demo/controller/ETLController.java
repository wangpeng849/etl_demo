package com.example.etl_demo.controller;

import com.example.etl_demo.json.ETL;
import com.example.etl_demo.json.Loads;
import com.example.etl_demo.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return etl.getTransfers();
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

    private void gotoTransfer(String lastOperatorId) {
        if(lastOperatorId.startsWith("s")) gotoExtract(lastOperatorId);
    }


}
