package com.example.etl_demo.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Author wangp
 * @Date 2020/7/3
 * @Version 1.0
 */
public class SparkSQlUsed {


    //jdbc.url=jdbc:mysql://localhost:3306/database
    static String url = "jdbc:mysql://127.0.0.1/homework?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowMultiQueries=true&serverTimezone=UTC&serverTimezone=Asia/Shanghai";
    //查找的表名
    static String table = "user_test";
    //增加数据库的用户名(user)密码(password),指定test数据库的驱动(driver)
    static Properties connectionProperties = new Properties();

    static {
        connectionProperties.put("user","root");
        connectionProperties.put("password","root");
        connectionProperties.put("driver","com.mysql.cj.jdbc.Driver");
    }


    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "D:\\hadoop-2.7.0");
        //初始化sparkContext
//        SparkSession sparkSession = SparkSession.builder().appName("Spark Java MySQL").master("local").getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(new SparkConf().setAppName("SparkMysql").setMaster("local"));
        sparkContext.setLogLevel("ERROR");
        SQLContext sqlContext = new SQLContext(sparkContext);
        //读
        readMySQL(sqlContext);
        //写
        writeMySQL(sparkContext,sqlContext);
        sparkContext.stop();
    }

    private static void writeMySQL(JavaSparkContext sparkContext,SQLContext sqlContext) {
//    private static void writeMySQL(SparkSession sparkSession,SQLContext sqlContext) {
        //写入数据
        // 第一步：在RDD的基础上创建类型为Row的RDD
        JavaRDD<String> insertData = sparkContext.parallelize(Arrays.asList("1 tom 5", "2 jack 6", "3 alex 7"));
        JavaRDD<Row> insertRDD = insertData.map((Function<String, Row>) line -> {
            String[] split = line.split(" ");
            return RowFactory.create(Integer.valueOf(split[0]), split[1], Integer.valueOf(split[2]));
        });

        //第二步：动态构造DataFrame的元数据
        List structFields = new ArrayList();
        structFields.add(DataTypes.createStructField("id", DataTypes.IntegerType, true));
        structFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("age", DataTypes.IntegerType, true));
        StructType structType = DataTypes.createStructType(structFields);

        //第三步：基于已有的元数据以及RDD<Row>来构造DataFrame
        Dataset<Row> dataFrame = sqlContext.createDataFrame(insertRDD, structType);

        //第四步：将数据写入到表中
        dataFrame.write().mode(SaveMode.Append).jdbc(url, table, connectionProperties);
    }


    private static void readMySQL(SQLContext sqlContext) {
        //SparkJdbc读取Postgresql的products表内容
        System.out.println("读取test数据库中的user_test表内容");
        // 读取表中所有数据
        Dataset<Row> dataset = sqlContext.read().jdbc(url, table, connectionProperties).select("*");
        //显示数据
        dataset.show();
    }

    private static void myPrint(Object str) {
        System.out.println("**************************************************************************");
        System.out.println("****************\t\t" + str + "\t\t**********************");
        System.out.println("**************************************************************************");
    }
}
