package com.java.kaige.week5.di;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;



/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/4 0:29
 * @className: com.java.kaige.week5.di.Test
 * @description: TODO
 */

@ComponentScan("com.java.kaige.week5.di")
public class Test {

    @Bean
    public JavaBean javaBeanLoad() {
        return new JavaBean();
    }

    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(Test.class);

        // 加载 XML 资源，解析并且生成 BeanDefinition
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        String xmlResourcePath = "classpath:applicationContext.xml";
        beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);

        applicationContext.refresh();

        //遍历Spring加载的bean
        String[] beans = applicationContext.getBeanDefinitionNames();
        for (String bean : beans) {
            Object object = applicationContext.getBean(bean);
            if (object instanceof AnnotatedLoadBean.BeanDemo) {
                AnnotatedLoadBean.BeanDemo beanDemo = (AnnotatedLoadBean.BeanDemo) applicationContext.getBean(bean);
                beanDemo.load();
            }
        }

        applicationContext.close();
    }

}
