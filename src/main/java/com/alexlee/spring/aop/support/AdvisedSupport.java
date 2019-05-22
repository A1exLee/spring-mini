package com.alexlee.spring.aop.support;

import com.alexlee.spring.aop.AopConfig;
import com.alexlee.spring.aop.aspect.AfterReturningAdvice;
import com.alexlee.spring.aop.aspect.AfterThrowingAdvice;
import com.alexlee.spring.aop.aspect.MethodBeforeAdvice;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 11:44
 */
public class AdvisedSupport {
    private Class targetClass;
    private Object target;
    private Pattern pointCutPattern;
    private transient Map<Method, List<Object>> methodCache = Maps.newHashMap();
    private AopConfig aopConfig;

    public AdvisedSupport(AopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    private void parse() {
        try {
            String pointCut = aopConfig.getPointCut().replaceAll("\\.", "\\\\.").replaceAll("\\\\.\\*", ".*").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
            String pointClasses = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
            pointCutPattern = Pattern.compile("class " + pointClasses.substring(pointClasses.lastIndexOf(" ") + 1));
            Pattern methodPattern = Pattern.compile(pointCut);
            Class aspect = Class.forName(aopConfig.getAspectClass());
            Map<String,Method> mCache=Maps.newHashMap();
            Method[] declaredMethods = aspect.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
//                简单实现name-Method，暂时不考虑同名方法的问题
                mCache.put(declaredMethod.getName(),declaredMethod);

            }
            for (Method method : targetClass.getMethods()) {
                String str = method.toString();
                if (str.contains("throws")) {
                    str = str.substring(0, str.lastIndexOf("throws")).trim();
                }
                Matcher matcher = methodPattern.matcher(str);
                if (matcher.matches()) {
                    List<Object> advices = Lists.newLinkedList();
                    Object instance = aspect.newInstance();
                    if (null != aopConfig.getAspectBefore() && !"".equals(aopConfig.getAspectBefore().trim())) {
                        advices.add(new MethodBeforeAdvice(mCache.get(aopConfig.getAspectBefore()), instance));
                    }
                    if (null != aopConfig.getAspectAfter() && !"".equals(aopConfig.getAspectAfter().trim())) {
                        advices.add(new AfterReturningAdvice(mCache.get(aopConfig.getAspectAfter()), instance));
                    }
                    if (null != aopConfig.getAspectAfterThrow() && !"".equals(aopConfig.getAspectAfterThrow().trim())) {
                        advices.add(new AfterThrowingAdvice(mCache.get(aopConfig.getAspectAfterThrow()), instance));
                    }
                    methodCache.put(method, advices);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Pattern getPointCutPattern() {
        return pointCutPattern;
    }

    public void setPointCutPattern(Pattern pointCutPattern) {
        this.pointCutPattern = pointCutPattern;
    }

    public Map<Method, List<Object>> getMethodCache() {
        return methodCache;
    }

    public void setMethodCache(Map<Method, List<Object>> methodCache) {
        this.methodCache = methodCache;
    }
    public boolean pointCutMatch(){
        return pointCutPattern.matcher(this.targetClass.toString()).matches();
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method,Class clazz) throws Exception{
        List<Object> interceptors = methodCache.get(method);
        if(interceptors==null){
//            用方法名查找拦截器链
            Method pointCutMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
            interceptors=methodCache.get(pointCutMethod);
            this.methodCache.put(method,interceptors);
        }
        return interceptors;
    }

}
