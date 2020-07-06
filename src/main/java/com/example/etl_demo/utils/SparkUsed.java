package com.example.etl_demo.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;

/**
 * @Author wangp
 * @Date 2020/7/3
 * @Version 1.0
 */
public class SparkUsed {
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "D:\\hadoop-2.7.0");
//        System.setProperties();
        //初始化SparkContext
        SparkConf conf = new SparkConf().setAppName("sparkTest").setMaster("local");
        JavaSparkContext context = new JavaSparkContext(conf);

        //日志打印级别 (没用)
        context.setLogLevel("ERROR");

        //使用parallelize方法
        JavaRDD<String> lines = context.parallelize(Arrays.asList("apple", "you are an apple"));
        System.out.println(lines.collect());


        //RDD操作（filter)
        JavaRDD<String> inputRDD = context.textFile("./test.txt");
        System.out.println("第一行数据："+inputRDD.first());
        JavaRDD<String> isRdd = inputRDD.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.contains("is");
            }
        });
        System.out.println("is显示为："+isRdd.collect());
        System.out.println("is个数为："+isRdd.count());

        //map方法
        JavaRDD<Integer> rdd = context.parallelize(Arrays.asList(1, 3, 5, 7));
        JavaRDD<Integer> mapResult = rdd.map((x) -> x * x);
        myPrint("["+StringUtils.join(mapResult.collect(),",")+"]");



    }


    private static void myPrint(String str){
        System.out.println("**************************************************************************");
        System.out.println("****************\t\t"+str+"\t\t**********************");
        System.out.println("**************************************************************************");
    }
}
