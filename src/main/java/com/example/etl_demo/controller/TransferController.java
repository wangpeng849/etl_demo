package com.example.etl_demo.controller;

import com.example.etl_demo.xml.aggregation.JAXBOperator;
import com.example.etl_demo.xml.aggregation.XMLAggregation;
import com.example.etl_demo.xml.aggregation.jaxb.Student;
import com.example.etl_demo.xml.aggregation.jaxb.Students;
import com.example.etl_demo.xml.jaxb.XMLUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author wangp
 * @Date 2020/6/29
 * @Version 1.0
 */
@RestController("/trans")
@Api(tags = "转换")
public class TransferController {

    @GetMapping("/op")
    @ApiOperation("操作")
    public String operator(@RequestParam("operator") String operator, @RequestParam("attribute") String attribute) {
        Student stu1 = new Student().setName(new Student.Name().setFrom("深圳").setTo("北京").setValue("张三")).setAge(10).setSalary(1000);
        Student stu2 = new Student().setName(new Student.Name().setFrom("深圳").setTo("上海").setValue("李四")).setAge(20).setSalary(2000);

        XMLAggregation xmlAggregation = new XMLAggregation() {
            @Override
            public void add() {
                stu1.setAge(stu1.getAge() + 1);
            }

            @Override
            public void sub() {
                stu1.setAge(stu1.getAge() - 1);

            }
        };
        if (attribute.equals("age")) {
            if (operator.equals("add")) {
                xmlAggregation.add();
            } else {
                xmlAggregation.sub();
            }
        }
        XMLUtils.convertToXml(stu1, "D://stu.xml");
        return "OK";
    }

    @PostMapping("dowhat")
    @ApiOperation("对xml文件做点什么")
    public String doSomething(@RequestBody Map<String, String> operator, @RequestParam("xmlData") String xmlData) {
        JAXBOperator jaxbOperator = new JAXBOperator().setOperator(operator).setObj(xmlData);
        XMLUtils.convertToXml(jaxbOperator, "D:\\operator.xml");
        return "ok";
    }

    @PostMapping("doMulWhat")
    @ApiOperation("对xml文件做点什么(list)")
    public Object doSomethingForMultiply(@RequestBody Map<String, String> operator, @RequestParam("xmlData") String xmlData) {
        JAXBOperator jaxbOperator = new JAXBOperator().setOperator(operator).setObj(xmlData);
        XMLUtils.convertToXml(jaxbOperator, "D:\\multi_operator.xml");
        return jaxbOperator;
    }

    @PostMapping("parse")
    @ApiOperation("解析要做的事")
    public String parseFile() {
        JAXBOperator jaxbOperator = (JAXBOperator) XMLUtils.convertXmlFileToObject(JAXBOperator.class, "D:\\operator.xml");
        Student student = (Student) XMLUtils.convertXmlStrToObject(Student.class, jaxbOperator.getObj());
        Map<String, String> operator = jaxbOperator.getOperator();
        operatorObject(operator, student);
        XMLUtils.convertToXml(student, "D:\\stu_modify.xml");
        return "OK";
    }

    @PostMapping("multi/parse")
    @ApiOperation("解析要做的事(list)")
    public Object parseMultiFile() {
        JAXBOperator jaxbOperator = (JAXBOperator) XMLUtils.convertXmlFileToObject(JAXBOperator.class, "D:\\multi_operator.xml");
        Students students = (Students) XMLUtils.convertXmlStrToObject(Students.class, jaxbOperator.getObj());
        Map<String, String> operator = jaxbOperator.getOperator();
        operatorMulObject(operator, students.getStudent());
        XMLUtils.convertToXml(students, "D:\\stu_mul_modify.xml");
        return XMLUtils.convertXmlFileToObject(Students.class, "D:\\stu_mul_modify.xml");
    }

    private void operatorObject(Map<String, String> operator, Student student) {
        for (Map.Entry<String, String> entry : operator.entrySet()) {
            try {
                Class<Student> studentClass = Student.class;
                Method getMethod = studentClass.getDeclaredMethod("get" + entry.getValue().substring(0, 1).toUpperCase() + entry.getValue().substring(1));
                Object getValue = getMethod.invoke(student);
                Method setMethod = studentClass.getDeclaredMethod("set" + entry.getValue().substring(0, 1).toUpperCase() + entry.getValue().substring(1), Integer.class);
                if ("add".equals(entry.getKey())) {
                    setMethod.invoke(student, (int) getValue + 1);
                }
                if ("sub".equals(entry.getKey())) {
                    setMethod.invoke(student, (int) getValue - 1);

                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void operatorMulObject(Map<String, String> operator, List<Student> students) {
        for (Map.Entry<String, String> entry : operator.entrySet()) {
            try {
                for (Student student : students) {
                    String[] value = entry.getValue().split(",");

                    Class<Student> studentClass = Student.class;
//                    Method getMethod = studentClass.getDeclaredMethod("get" + value[0].substring(0, 1).toUpperCase() + value[0].substring(1));
//                    Object getValue = getMethod.invoke(student);
                    Method setMethod = studentClass.getDeclaredMethod("set" + value[0].substring(0, 1).toUpperCase() + value[0].substring(1), Integer.class);
                    if ("add".equals(entry.getKey())) {
                        setMethod.invoke(student, Integer.parseInt(value[1]));
                    }
                    if ("sub".equals(entry.getKey())) {
                        setMethod.invoke(student, Integer.parseInt(value[1]));
                    }
//                    if ("change".equals(entry.getKey())) {
//                        Class<Student.Name> nameClass = Student.Name.class;
//                        Student.Name name = nameClass.newInstance();
//                        BeanUtils.copyProperties(student.getName(), name);
//                        name.setFrom(value[1]);
//                        setMethod.invoke(student, name);
//                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException  e) {
                e.printStackTrace();
            }
        }
    }
}
