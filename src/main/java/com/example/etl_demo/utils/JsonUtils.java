package com.example.etl_demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.etl_demo.json.ETL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author wangp
 * @Date 2020/7/1
 * @Version 1.0
 */
public class JsonUtils {

    public static Object jsonTobObject(String filePath,Class clazz){
        BufferedReader br = null;
        Object obj = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while(null != (line = br.readLine())){
                sb.append(line);
            }
            obj = JSONObject.toJavaObject((JSON) JSONObject.parse(sb.toString()), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
