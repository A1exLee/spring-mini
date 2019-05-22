package com.alexlee.spring.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 13:53
 */
public interface JoinPoint {
    Method getMethod();

    Object[] getArguments();

    Object getThis();
}
