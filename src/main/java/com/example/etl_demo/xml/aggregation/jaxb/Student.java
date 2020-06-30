package com.example.etl_demo.xml.aggregation.jaxb;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.*;

/**
 * @Author wangp
 * @Date 2020/6/29
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement
@XmlType(propOrder = {
        "name",
        "age",
        "salary"
})
public class Student {
    private Name name;
    private Integer age;
    private Integer salary;

    @Data
    @Accessors(chain = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static
    class Name{
        @XmlAttribute(name = "from")
        private String from;
        @XmlAttribute(name = "to")
        private String to;
        @XmlValue
        private String value;
    }

}
