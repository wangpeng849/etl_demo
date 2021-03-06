package com.example.etl_demo.json;

import lombok.Data;

import java.util.List;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 */
@Data
public class Transfers {
    private String stepId;
    private String extractId;
    private String nextId;
    private String action;
    private List<String> fields;
    private String addStr;
}
