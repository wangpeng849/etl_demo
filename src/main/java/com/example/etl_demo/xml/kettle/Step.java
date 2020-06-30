package com.example.etl_demo.xml.kettle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 */
@Data
@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "name",
        "type",
        "description",
        "distribute",
        "copies"
})
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    private String name;
    private String type;
    private String description;
    private String distribute;
    private Integer copies;
}
