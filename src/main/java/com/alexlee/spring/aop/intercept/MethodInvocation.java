package com.alexlee.spring.aop.intercept;

import com.alexlee.spring.aop.aspect.JoinPoint;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 13:57
 */
public class MethodInvocation implements JoinPoint {
    private Object proxy;
    /**
     * 目标方法
     */
    private Method method;
    /**
     * 目标类实例
     */
    private Object target;
    private Class targetClass;
    /**
     * 目标方法参数
     */
    private Object[] arguments;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    private int currentInterceptorIndex = -1;

    public MethodInvocation(Object proxy, Method method, Object target, Class targetClass, Object[] arguments, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            //执行到最后一个通知拦截器，则执行目标方法
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptor = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if (interceptor instanceof MethodInterceptor) {
            return ((MethodInterceptor) interceptor).invoke(this);
        } else {
            return proceed();
        }
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }
}
