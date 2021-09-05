package com.java.kaige.week5.aop;

import lombok.Data;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/5 11:06
 * @className: com.java.kaige.week5.aop.proxy.User
 * @description: TODO
 */
@Data
public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
