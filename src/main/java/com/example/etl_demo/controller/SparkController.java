package com.example.etl_demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.etl_demo.pojo.WordCount;
import com.example.etl_demo.service.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SparkController {
 
    @Autowired
    private SparkService sparkService;
 
    @GetMapping("/wordCount")
    public Object wordCount() {
        List<WordCount> list = sparkService.doWordCount();
        return list;
    }
 
}
