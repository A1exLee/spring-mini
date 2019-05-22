package com.alexlee.spring.aop.aspect;

import com.alexlee.spring.aop.intercept.MethodInterceptor;
import com.alexlee.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 15:12
 */
public class MethodBeforeAdvice  extends AbstractAspectJAdvice implements MethodInterceptor {

    private JoinPoint joinPoint;

    public MethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable{
        this.joinPoint=methodInvocation;
        this.before(methodInvocation.getMethod(),methodInvocation.getArguments(),methodInvocation.getThis());
        return methodInvocation.proceed();
    }

    public void before(Method method,Object[] args,Object target) throws Throwable{
        invokeAdviceMethod(this.joinPoint,null,null);
    }
}
