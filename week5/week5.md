# 第五周作业说明

* 第2题.（必做）写代码实现 Spring Bean 的装配。
  * 具体见示例代码中的：[week5-di](.\week5-di\src\main\java\com\java\kaige\week5\di\Test.java)
  * 1、基于Service注解自动装配: com.java.kaige.week5.di.AnnotatedLoadBean$ServiceBean
  * 2、基于Component注解自动装配: com.java.kaige.week5.di.AnnotatedLoadBean$ComponentBean
  * 3、基于Controller注解自动装配: com.java.kaige.week5.di.AnnotatedLoadBean$ControllerBean
  * 4、基于Resource注解自动装配: com.java.kaige.week5.di.AnnotatedLoadBean$RepositoryBean
  * 5、基于Resource注解自动装配: com.java.kaige.week5.di.AnnotatedLoadBean$ConfigurationBean$$EnhancerBySpringCGLIB$$bf113f79
  * 6、基于JAVA的bean装配: com.java.kaige.week5.di.JavaBean
  * 7、基于Xml配置的bean装配: com.java.kaige.week5.di.XmlBean
    

* 第8题.（必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。
    * 具体见示例代码中的：[week5-starter](.\week5-stater\src\test\java\com\java\kaige\week5\stater\SpringBootStarterTest.java)
        
  
* 第10题.（必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
  * 具体见示例代码中的：[week5-jdbc](.\week5-jdbc\src\main\java\com\java\kaige\week5\jdbc\JdbcDemo.java)
  * 1)、使用 JDBC 原生接口，实现数据库的增删改查操作：见[JdbcDemo](.\week5-jdbc\src\main\java\com\java\kaige\week5\jdbc\JdbcDemo.java)
  * 2)、使用事务，PrepareStatement 方式，批处理方式，改进上述操作：见[batchAddUser](.\week5-jdbc\src\main\java\com\java\kaige\week5\jdbc\JdbcDemo.java)
  * 3)、配置 Hikari 连接池，改进上述操作：见[HikariDemo](.\week5-jdbc\src\main\java\com\java\kaige\week5\jdbc\HikariDemo.java)
           
* 第1题1.（选做）使 Java 里的动态代理，实现一个简单的 AOP
* 见示例代码中的：[aop-proxy](.\week5-di\src\main\java\com\java\kaige\week5\aop\proxy\ProxyFactory.java)

* 第5题（选做）总结一下，单例的各种写法，比较它们的优劣
  * synchronized方式  
  * 内部类方式: 懒加载，推荐
  * 枚举方式： 不是懒加载
  * vloatile+duble-check方式: 懒加载、稍复杂 
  
* 第7题.（选做）总结 Hibernate 与 MyBatis 的各方面异同点
  * 相同：都是持久化框架，二级缓存都采用系统默认的缓存机制
  * 不同：
    * Mybatis 优点：原生SQL（XML 语法），直观，对 DBA 友好
    * Hibernate 优点：简单场景不用写 SQL（HQL、Cretiria、SQL）
    * Mybatis 缺点：繁琐，可以用 MyBatis-generator、MyBatis-Plus 之类的插件
    * Hibernate 缺点：对DBA 不友好
  
* 其它题等上完课再来做，现在时间不够用  