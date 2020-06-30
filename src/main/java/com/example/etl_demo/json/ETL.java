package com.example.etl_demo.json;

import lombok.Data;

import java.util.List;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 */
@Data
public class ETL {
    private List<Extract> extracts;
    private List<Transfers> transfers;
    private List<Loads> loads;
}
