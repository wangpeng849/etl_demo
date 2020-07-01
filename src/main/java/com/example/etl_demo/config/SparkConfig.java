package com.example.etl_demo.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {
 
    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                // 设置模式为本地模式 [*] 为使用本机核数
                .setMaster("local[*]")
                // 设置应用名
                .setAppName("springboot-spark-demo");
    }
 
    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }
 
}