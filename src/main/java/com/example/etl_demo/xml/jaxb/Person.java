package com.example.etl_demo.xml.jaxb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Author wangp
 * @Date 2020/6/30
 * @Version 1.0
 *
 *
 *  @XmlRootElement：
 *  作用和用法：
 *  类级别的注解，将类映射为xml全局元素，也就是根元素。就像spring配置文件中的beans。上面的例子中我将该注解用在了person类上，生成了<person>根元素。常与@XmlType，@XmlAccessorType，@XmlAccessorOrder连用。
 *  属性：
 *  该注解含有name和namespace两个属性。namespace属性用于指定生成的元素所属的命名空间。name属性用于指定生成元素的名字，若不指定则默认使用类名小写作为元素名。修改上面的例子，在该注解上使用name属性：
 *
 * @XmlElement
 * 作用和用法：
 * 字段，方法，参数级别的注解。该注解可以将被注解的字段(非静态)，或者被注解的get/set方法对应的字段映射为本地元素，也就是子元素。默认使用字段名或get/set方法去掉前缀剩下部分小写作为元素名(在字段名和get/set方法符合命名规范的情况下)。上面例子中，id、addr、name、gender、area都被映射成了<person>元素的子元素。下文会配合@XmlAccessorType注解详细讲解该注解的用法。常与@XmlValue，@XmlJavaTypeAdapter，@XmlElementWrapper连用。
 * 属性：
 * 该注解的属性常用的属性有有：name、nillable、required、namespace、defaultValue
 * -name属性可以指定生成元素的名字，同@XmlRootElement注解的name属性一样，不再举例。
 * -nillable属性可以指定元素的文本值是否可以为空，默认为false.
 * -required属性可以指定该元素是否必须出现，默认为false，所以在xsd中会有对应的属性minOccurs="0"。修改该属性为true
 * -namespace属性可以指定该元素所属的命名空间
 * -defaultValue属性可以指定该元素默认的文本值
 *
 * @XmlAttribute
 * 作用和用法：
 * 字段和方法级别的注解。该注解会将字段或get/set方法对应的字段映射成本类对应元素的属性，属性名默认使用字段名或get/set方法去掉前缀剩下部分首字母小写(在字段名和get/set方法符合命名规范的情况下)。
 *
 * @XmlAccessorType
 * 作用和用法：
 * 包和类级别的注解。javaEE的API对该注解的解释是：控制字段是否被默认序列化。通俗来讲，就是决定哪些字段或哪些get/set方法对应的字段会被映射为xml元素，需要注意的是字段或get/set方法的访问权限(public/private)会影响字段是否被映射为xml元素，下面会详细讲解。
 * 属性：
 * 该注解只有一个value属性，可取的值是一个名为XmlAccessType的枚举类型里的值，下面详细看一下这几个值分别有什么用：
 *
 * @XmlAccessorOrder
 * 作用和用法：
 * 包和类级别的注解。控制生成元素的顺序。
 * 属性：
 * 只有一个value属性，可取的值是一个名为XmlAccessOrder的枚举类型的两个值，XmlAccessOrder.ALPHABETICAL 和 XmlAccessOrder.UNDEFINED。默认为XmlAccessOrder.UNDEFINED，代表按照类中字段的顺序生成元素的顺序。
 * 另一个值则代表按照字母表的顺序对生成的元素排序。但奇怪的是，只有jaxb按照field生成元素时，默认值才会生效，否则总是按照字母表的顺序排序。
 *
 * @XmlElementWrapper
 * 作用和用法：
 * 字段和方法级别的注解。围绕被映射的xml元素生成包装元素。主要用在集合对象映射后生成包装映射结果的xml元素。
 * 修改上面的例子，添加一个Key类，在Person类中添加一个Key类的Set集合.
 *
 * @XmlValue：
 * 作用和用法：
 * 字段和方法级别的注解。该注解的作用，简单理解就是定义xml元素文本值的类型，例如在一个类的String类型字段上使用该注解，则生成的元素文本值类型就是xsd:string，也就是定义一个xsd中的simpleType.若类中还有一个字段并使用了@XmlAttribute注解，则是定义一个xsd中的complexType。
 *
 * @XmlType：
 * 作用和用法：
 * 类级别的注解。该注解有些复杂，主要使用的是它的propOrder属性，简单来说是用来定义xsd中的simpleType或complexType，从生成的xml中来看，它的作用就是指定生成元素的顺序
 */

@Data
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    private int id;
    private String name;
    private String gender;
    private String addr;
    private String area;
}
