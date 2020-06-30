package com.example.etl_demo.xml.aggregation;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @Author wangp
 * @Date 2020/6/29
 * @Version 1.0
 */
@XmlRootElement(name = "operators")
@Data
@Accessors(chain = true)
@XmlType(propOrder = {
        "operator",
//        "attribute",
        "obj"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class JAXBOperator {
    private Map<String,String> operator;
//    private List<String> attribute;
    private String obj;

//    @Data
//    @Accessors(chain = true)
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class Operator {
//        @XmlAttribute(name = "add")
//        private String add;
//        @XmlAttribute(name = "sub")
//        private String sub;
//    }
}
