package com.example.etl_demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.etl_demo.json.ETL;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @Author wangp
 * @Date 2020/7/1
 * @Version 1.0
 */
public class JsonUtils {

    public static Object jsonTobObject(String filePath, Class clazz) {
        return JSONObject.toJavaObject((JSON) JSONObject.parse(convertFileToStr(filePath)), clazz);
    }

    public static String convertFileToStr(String filePath) {
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            sb = new StringBuffer();
            String line = "";
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb == null ? "" : sb.toString();
    }

    public static String outputFileByJSON(JSONObject jsonObject) {
        String fileName = UUID.randomUUID().toString();
        String json = jsonObject.toJSONString();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("./" + fileName));
            bw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    public static String outputFileByListJSON(List<JSONObject> jsonObject) {
        String fileName = UUID.randomUUID().toString();
        String toJson = JSONObject.toJSON(jsonObject).toString();
        BufferedWriter bw = null;
        try {
            //一次写不完
            bw = new BufferedWriter(new FileWriter("./" + fileName + ".js"));
            int start = 0;
            int end = 512;
            while (true) {
                if (end >= toJson.length()) {
                    String writeContent = toJson.substring(start);
                    bw.write(writeContent);
                    break;
                }
                String writeContent = toJson.substring(start, end);
                bw.write(writeContent);
                start = end;
                end += 512;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }
}
