package com.example.etl_demo.json;

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
        BufferedReader br = new BufferedReader(new FileReader("E:\\jaxb\\etl.js"));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while(null != (line = br.readLine())){
            sb.append(line);
        }
        JSONObject jsonObject = (JSONObject) JSONObject.parse(sb.toString());
        ETL etl = jsonObject.toJavaObject(ETL.class);
        System.out.println(etl);
    }
}
