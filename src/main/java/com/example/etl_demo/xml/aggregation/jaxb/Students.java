package com.example.etl_demo.xml.aggregation.jaxb;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @Author wangp
 * @Date 2020/6/29
 * @Version 1.0
 */
@XmlRootElement
@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class Students {
    @XmlAttribute
    private String id;
    private List<Student> student;
}
