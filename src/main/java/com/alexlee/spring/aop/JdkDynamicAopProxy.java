package com.alexlee.spring.aop;

import com.alexlee.spring.aop.intercept.MethodInvocation;
import com.alexlee.spring.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 11:43
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private AdvisedSupport config;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.config.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicInterceptionAdvice = config.getInterceptorsAndDynamicInterceptionAdvice(method, config.getTargetClass());
        MethodInvocation methodInvocation = new MethodInvocation(proxy, method, config.getTarget(), config.getTargetClass(), args, interceptorsAndDynamicInterceptionAdvice);
        return methodInvocation.proceed();
    }
}
