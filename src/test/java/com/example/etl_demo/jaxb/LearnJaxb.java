package com.example.etl_demo.jaxb;

import com.example.etl_demo.xml.jaxb.Person;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 */
public class LearnJaxb {

    @Test
    public void generateXml(){
        Person person = Person.builder().name("abcd").gender("男").area("广东").addr("深圳").build();
        Person person2 = Person.builder().name("ABCD").gender("男").area("广东").addr("广州").build();
        File file = new File("E:\\jaxb\\person.xml");
        try {
            //根据Person类生成上下文对象
            JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
            //从上下文中获取Marshaller对象，用作将bean编组(转换)为xml
            Marshaller marshaller = jaxbContext.createMarshaller();
            //以下是为生成xml做的一些配置
            //格式化输出，即按标签自动换行，否则就是一行输出
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //设置编码（默认编码就是utf-8）
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //是否省略xml头信息，默认不省略（false）
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            //写入文件
            marshaller.marshal(person,file);
            marshaller.marshal(person2,file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void generateBean(){
        File file = new File("E:\\jaxb\\person.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Person person = (Person) unmarshaller.unmarshal(file);
            System.out.println(person);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
