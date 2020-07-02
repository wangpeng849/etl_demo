package com.example.etl_demo.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 */
public class ETLDemo {
    @Test
    public void readJsonToObject() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(".\\etl.json"));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while(null != (line = br.readLine())){
            sb.append(line);
        }
        ETL etl = JSONObject.toJavaObject((JSON) JSONObject.parse(sb.toString()), ETL.class);
        System.out.println(etl);
    }
}
