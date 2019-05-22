package com.alexlee.spring.aop;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 11:43
 */
public interface AopProxy {
    Object getProxy();
    Object getProxy(ClassLoader classLoader);
}
