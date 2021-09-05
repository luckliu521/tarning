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

package com.java.kaige.week5.stater.prop;

import com.java.kaige.week5.stater.Student;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Properties;

/**
 * Spring boot properties configuration.
 */
@ConfigurationProperties(prefix = "spring.kaige")
@Data
public final class SpringBootPropertiesConfiguration {

    private int id;
    private String name;
    private String beanName;

    private List<Student> students;

    private Student student100;
    
    private Properties props = new Properties();
    

}
