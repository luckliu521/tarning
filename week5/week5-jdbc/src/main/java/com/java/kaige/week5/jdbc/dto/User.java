package com.java.kaige.week5.jdbc.dto;

import lombok.Data;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/4 23:45
 * @className: com.java.kaige.week5.crud.dto.User
 * @description: TODO
 */
@Data
public class User {
    private int id;
    private String name;
    private int old;

    public User() {
    }

    public User(int id, String name, int old) {
        this.id = id;
        this.name = name;
        this.old = old;
    }
}
