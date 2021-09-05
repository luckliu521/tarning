package com.java.kaige.week5.di;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/4 0:08
 * @className: com.java.kaige.week5.di.DemoBean
 * @description: TODO
 */
public class XmlBean implements AnnotatedLoadBean.BeanDemo {

    @Override
    public void load() {
        System.out.println("7、基于Xml配置的bean装配: " + this.getClass().getName());
    }
}
