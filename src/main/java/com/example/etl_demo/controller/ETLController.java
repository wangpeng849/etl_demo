package com.example.etl_demo.controller;

import com.example.etl_demo.json.ETL;
import com.example.etl_demo.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wangp
 * @Date 2020/7/1
 * @Version 1.0
 */
@RestController("/etl")
@Api(tags = "ETL")
public class ETLController {

    @GetMapping("test")
    @ApiOperation("测试")
    public Object test(){
        return JsonUtils.jsonTobObject("./etl.js",ETL.class);
    }


    @GetMapping("extract")
    @ApiOperation("抽取数据")
    public Object extract(){
        return "";
    }
}
