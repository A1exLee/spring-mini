package com.alexlee.spring.aop.aspect;

import com.alexlee.spring.aop.intercept.MethodInterceptor;
import com.alexlee.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 15:12
 */
public class AfterReturningAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable{
        Object returnValue=methodInvocation.proceed();
        this.joinPoint=methodInvocation;
        this.afterReturning(returnValue,methodInvocation.getMethod(),methodInvocation.getArguments(),methodInvocation.getThis());
        return returnValue;
    }

    public void afterReturning(Object returnValue,Method method,Object[] args,Object target) throws Throwable{
        invokeAdviceMethod(this.joinPoint,returnValue,null);
    }

}
