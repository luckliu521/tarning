package com.java.kaige.week5.di;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/4 10:08
 * @className: com.java.kaige.week5.di.AnnotatedLoadBean
 * @description: TODO
 */

@Component
public class AnnotatedLoadBean {
    public interface BeanDemo{
        void load();
    }

    @Service
    public class ServiceBean implements BeanDemo{

        @Override
        public void load() {
            System.out.println("1、基于Service注解自动装配: " + this.getClass().getName());
        }
    }

    @Component
    public class ComponentBean implements BeanDemo{

        @Override
        public void load() {
            System.out.println("2、基于Component注解自动装配: " + this.getClass().getName());
        }
    }

    @Controller
    public class ControllerBean implements BeanDemo{

        public void load() {
            System.out.println("3、基于Controller注解自动装配: " + this.getClass().getName());
        }
    }

    @Repository
    public class RepositoryBean implements BeanDemo{
        @Override
        public void load() {
            System.out.println("4、基于Resource注解自动装配: " + this.getClass().getName());
        }
    }

    @Configuration
    public class ConfigurationBean implements BeanDemo{

        @Override
        public void load() {
            System.out.println("5、基于Resource注解自动装配: " + this.getClass().getName());
        }
    }

}
