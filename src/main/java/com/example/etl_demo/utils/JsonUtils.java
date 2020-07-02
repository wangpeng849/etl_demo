package com.example.etl_demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Cleanup;

import javax.annotation.WillClose;
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

    public static String convertFileToStr(String lastFilePath) {
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new FileReader(lastFilePath));
            sb = new StringBuffer();
            String line = "";
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb == null ? "" : sb.toString();
    }

    public static String outputFileByJSON(JSONObject jsonObject) {
        String fileName = UUID.randomUUID().toString();
        String json = jsonObject.toJSONString();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("./" + fileName));
            bw.write(json);
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


    public static String outputFileByListJSON(List<JSONObject> jsonObject, String stepId) {
        String toJson = JSONObject.toJSON(jsonObject).toString();
        BufferedWriter bw = null;
        try {
            //一次写不完
            bw = new BufferedWriter(new FileWriter("./" + stepId + ".json"));
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
        return stepId;
    }


    public static String outputFileByJSONArray(JSONArray array, String stepId) {
        String json = array.toJSONString();
        BufferedWriter bw = null;
        try {
            //一次写不完
            bw = new BufferedWriter(new FileWriter("./" + stepId + ".json"));
            int start = 0;
            int end = 512;
            while (true) {
                if (end >= json.length()) {
                    String writeContent = json.substring(start);
                    bw.write(writeContent);
                    break;
                }
                String writeContent = json.substring(start, end);
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
        return stepId;
    }
}
