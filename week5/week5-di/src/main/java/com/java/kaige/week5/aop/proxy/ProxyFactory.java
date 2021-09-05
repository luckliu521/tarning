package com.java.kaige.week5.aop.proxy;

import com.java.kaige.week5.aop.User;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @version 1.0
 * @author: liujinchang
 * @create: 2021/9/5 11:30
 * @className: com.java.kaige.week5.aop.proxy.ProxyFactory
 * @description: TODO
 */
@Data
public class ProxyFactory {
    private Object targetObject;
    private IBeforeAdvice beforeAdvice;
    private IAfterAdvice afterAdvice;

    public ProxyFactory(Object targetObject, IBeforeAdvice beforeAdvice, IAfterAdvice afterAdvice) {
        this.targetObject = targetObject;
        this.beforeAdvice = beforeAdvice;
        this.afterAdvice = afterAdvice;
    }

    /**
     * 创建增强后的代理类
     *
     * @return
     */
    public Object createProxy() {
        ClassLoader classLoader = ProxyFactory.class.getClassLoader();
        Class[] interfaces = targetObject.getClass().getInterfaces();

        //这里创建一个空实现的调用处理器。
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (beforeAdvice != null) {
                    beforeAdvice.before();
                }
                Object result = method.invoke(targetObject, args);

                if (afterAdvice != null) {
                    afterAdvice.after();
                }
                return result;
            }
        };
        Object proxyObject = Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        return proxyObject;
    }

}
