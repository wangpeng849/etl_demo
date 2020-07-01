package com.example.etl_demo.service;

import com.example.etl_demo.pojo.WordCount;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * spark 业务类
 * @Author Lion Li
 * @Date 2020/4/8
 */
@Service
public class SparkService implements Serializable {
 
    @Autowired
    private transient JavaSparkContext javaSparkContext;
 
    public List<WordCount> doWordCount() {
        // 获取本地文件 生成javaRDD
        JavaRDD<String> file = javaSparkContext.textFile(".\\test.txt");
        // 按空格分解为数组 生成新的javaRDD
        JavaRDD<String> words = file.flatMap(
            line -> Arrays.asList(line.split(" ")).iterator()
        );
        // 统计每个词出现的次数 生成新的javaRDD
        JavaRDD<WordCount> wordcount = words.map(
                word -> new WordCount(word, 1)
        );
        // 将词与数转换为 key-value形式
        JavaPairRDD<String, Integer> pair = wordcount.mapToPair(
                wordCount -> new Tuple2<>(wordCount.getWord(), wordCount.getCount())
        );
        // 根据key进行整合
        JavaPairRDD<String, Integer> wordcounts = pair.reduceByKey(
                Integer::sum
        );
        // 将结果转换为 WordCount对象
        JavaRDD<WordCount> map = wordcounts.map(
                (tuple2) -> new WordCount(tuple2._1, tuple2._2)
        );
        // 将结果转换为 list并返回
        List<WordCount> result = map.collect();
        return result;
    }
 
}