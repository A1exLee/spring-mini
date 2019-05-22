package com.alexlee.spring.aop.intercept;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 13:56
 */
public interface MethodInterceptor {
    Object invoke(MethodInvocation methodInvocation) throws  Throwable;
}
