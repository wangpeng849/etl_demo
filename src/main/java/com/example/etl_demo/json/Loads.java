package com.example.etl_demo.json;

import lombok.Data;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 */
@Data
public class Loads {
    private String loadId;
    private String lastOperatorId;
    private ETLDataSource dataSource;
}
