package com.java.kaige.week5.jdbc;

import com.java.kaige.week5.jdbc.dao.UserDao;
import com.java.kaige.week5.jdbc.dto.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/4 0:29
 * @className: com.java.kaige.week5.di.Test
 * @description: 使用 JDBC 原生接口，实现数据库的增删改查操作
 */

@Component
public class JdbcDemo implements UserDao {

    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(JdbcDemo.class);
        applicationContext.refresh();

        JdbcDemo jdbcDemo = applicationContext.getBean(JdbcDemo.class);

        //JDBC原生接口操作数据库
        User user = new User(100, "kaige", 30);
        int updateCount = jdbcDemo.addUser(user);
        System.out.println(updateCount);

        User user1 = jdbcDemo.queryUser(100);
        System.out.println(user1);

        //批处理操作数据库
        List<User> batchUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User u = new User(17 + i, "kaige" + i, i);
            batchUsers.add(u);
        }
        int[] batchUpdateCount = jdbcDemo.batchAddUser(batchUsers);
        for (int i = 0; i < batchUpdateCount.length; i++) {
            System.out.println("更新： " + batchUpdateCount[i] + "条数据");
        }


        applicationContext.close();
    }

    @Override
    public int addUser(User user) {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("建立连接失败");
            return 0;
        }
        String sql = "insert into user(id, name, old) values(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setNString(2, user.getName());
            preparedStatement.setInt(3, user.getOld());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public User queryUser(int id) {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("建立连接失败");
            return null;
        }
        String sql = "select * from user where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet == null) {
                return null;
            }

            User user = new User();
            resultSet.next();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setOld(resultSet.getInt("old"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int[] batchAddUser(List<User> users) {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("建立连接失败");
            return null;
        }
        String sql = "insert into user(id, name, old) values(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            users.stream().forEach(user -> {
                try {
                    preparedStatement.setInt(1, user.getId());
                    preparedStatement.setNString(2, user.getName());
                    preparedStatement.setInt(3, user.getOld());
                    preparedStatement.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection() {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&rewriteBatchedStatements=true";
        String userName = "root";
        String password = "Zyc1223))";
        try {
            Class.forName(driverClassName);
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
