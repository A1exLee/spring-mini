package com.alexlee.spring.aop.aspect;

import com.alexlee.spring.aop.intercept.MethodInterceptor;
import com.alexlee.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 15:12
 */
public class AfterThrowingAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue = null;
        this.joinPoint = methodInvocation;
        try {
            returnValue = methodInvocation.proceed();
        } catch (Throwable e) {
            this.afterThrow(e);
            throw e;
        }

        return returnValue;
    }

    public void afterThrow(Throwable e) throws Throwable {
        invokeAdviceMethod(this.joinPoint, null, e);
    }
}
