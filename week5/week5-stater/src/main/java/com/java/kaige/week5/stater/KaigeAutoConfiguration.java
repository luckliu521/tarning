/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.java.kaige.week5.stater;

import com.java.kaige.week5.stater.prop.SpringBootPropertiesConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring boot starter configuration.
 * @author Administrator
 */
@Configuration
@ComponentScan("com.java.kaige.week5.stater")
@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
@ConditionalOnProperty(prefix = "spring.kaige", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class KaigeAutoConfiguration  {

    @Autowired
    private SpringBootPropertiesConfiguration props;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Klass klass() {
        Klass klass = new Klass();
        klass.setStudents(props.getStudents());
//        klass.dong();
        return klass;
    }

    @Bean
    public Student student() {
        Student student = new Student();
        student.setId(props.getId());
        student.setName(props.getName());
        student.setBeanName(props.getBeanName());
        student.setApplicationContext(applicationContext);
//        student.init();
//        student.print();
        return student;
    }

    @Bean
    public Student student100(){
        Student student = props.getStudent100();
        student.setApplicationContext(applicationContext);
        return student;
    }

    @Bean
    public School school() {
        School school = new School();
        school.setStudent100(student100());
//        school.ding();
        return school;
    }
    

}
