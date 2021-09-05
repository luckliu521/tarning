package com.java.kaige.week5.aop.proxy;

import com.java.kaige.week5.aop.User;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/5 11:04
 * @className: com.java.kaige.week5.aop.proxy.IUser
 * @description: TODO
 */
public class UserService implements IUser{
    @Override
    public User getUser() {
        User user = new User(100, "proxy demo");
        System.out.println("生成user: " + user);
        return user;
    }
}
