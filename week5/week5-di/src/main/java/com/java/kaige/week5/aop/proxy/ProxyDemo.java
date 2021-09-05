package com.java.kaige.week5.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/5 11:02
 * @className: com.java.kaige.week5.aop.proxy.ProxyDemo
 * @description: TODO
 */
public class ProxyDemo {

    public static void main(String[] args) {

        IUser userService = new UserService();
        ProxyFactory proxyFactory = new ProxyFactory(userService, new IBeforeAdvice() {
            @Override
            public void before() {
                System.out.println("执行前");
            }
        }, new IAfterAdvice() {
            @Override
            public void after() {
                System.out.println("执行后");
            }
        });
        IUser proxyUser = (IUser) proxyFactory.createProxy();
        System.out.println(proxyUser.getUser());
    }

}
