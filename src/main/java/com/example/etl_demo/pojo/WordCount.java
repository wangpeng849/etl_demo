package com.example.etl_demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author wangp
 * @Date 2020/7/1
 * @Version 1.0
 */

@Data
@ToString
@AllArgsConstructor
public class WordCount implements Serializable {
    private String word;
    private Integer count;
}
