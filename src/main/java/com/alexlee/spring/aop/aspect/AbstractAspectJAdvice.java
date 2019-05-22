package com.alexlee.spring.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 14:46
 */
public class AbstractAspectJAdvice implements Advice {

    /**
     * 被调用的通知方法
     */
    private Method aspectMethod;

    /**
     * 被调用的目标通知类实例
     */
    private Object aspectTarget;

    public AbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    Object invokeAdviceMethod(JoinPoint joinPoint, Object value, Throwable throwable) throws Throwable {
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        }
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == JoinPoint.class) {
                args[i] = joinPoint;
            } else if (parameterTypes[i] == Throwable.class) {
                args[i] = throwable;
            } else if (parameterTypes[i] == Object.class) {
                args[i] = value;
            }
        }
        return this.aspectMethod.invoke(aspectTarget, args);
    }
}
