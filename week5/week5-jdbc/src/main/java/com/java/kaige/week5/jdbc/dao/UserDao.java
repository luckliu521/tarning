package com.java.kaige.week5.jdbc.dao;

import com.java.kaige.week5.jdbc.dto.User;

import java.util.List;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/4 23:44
 * @className: com.java.kaige.week5.crud.dao.User
 * @description: TODO
 */
public interface UserDao {
    int addUser(User user);

    User queryUser(int id);

    int[] batchAddUser(List<User> users);
}
