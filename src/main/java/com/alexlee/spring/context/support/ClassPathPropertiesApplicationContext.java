package com.alexlee.spring.context.support;

import com.alexlee.spring.annotion.Autowired;
import com.alexlee.spring.annotion.Component;
import com.alexlee.spring.annotion.Controller;
import com.alexlee.spring.aop.AopConfig;
import com.alexlee.spring.aop.JdkDynamicAopProxy;
import com.alexlee.spring.aop.support.AdvisedSupport;
import com.alexlee.spring.beans.BeanWrapper;
import com.alexlee.spring.beans.config.BeanDefinition;
import com.alexlee.spring.beans.config.config.BeanPostProcessor;
import com.alexlee.spring.beans.factory.BeanFactory;
import com.alexlee.spring.beans.factory.support.DefaultListableBeanFactory;
import com.alexlee.spring.beans.support.BeanDefinitionReader;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/4/22 21:02
 */
public class ClassPathPropertiesApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathPropertiesApplicationContext.class);

    private String configLocation;

    private BeanDefinitionReader reader;

    public ClassPathPropertiesApplicationContext(String configLocation) {
        this.configLocation = configLocation;
        refresh();
    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = instantiateBean(beanDefinition);
        applyBeanPostProcessorsBeforeInitialization(instance, beanName);
        BeanWrapper beanWrapper = new BeanWrapper(instance);
        this.beanWrapperMap.put(beanName, beanWrapper);
        applyBeanPostProcessorsAfterInitialization(instance, beanName);
        populateBean(instance);
        return this.beanWrapperMap.get(beanName).getWarpperdInstance();
    }

    private List<BeanPostProcessor> getBeanPostProcessors() {
//         获取实现了BeanPostProcessor接口的类
        return Lists.newArrayList();
    }

    private void applyBeanPostProcessorsBeforeInitialization(Object instance, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
        }
    }

    private void applyBeanPostProcessorsAfterInitialization(Object instance, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);
        }
    }

    private void populateBean(Object instance) {
        try {
            Class clazz = instance.getClass();
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Controller.class)) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        field.setAccessible(true);
                        String autowiredBeanName = field.getAnnotation(Autowired.class).value();
                        if (autowiredBeanName.trim().length() < 1) {
                            autowiredBeanName = field.getType().getSimpleName();
                        }
                        if(!this.beanWrapperMap.containsKey(autowiredBeanName)){
                            getBean(autowiredBeanName);
                        }
                        if(!this.beanWrapperMap.containsKey(autowiredBeanName)){
                            continue;
                        }
                        field.set(instance, this.beanWrapperMap.get(autowiredBeanName).getWarpperdInstance());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("自动装配异常:" + e);
            logger.debug("堆栈", e);
        }


    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        String clazzName = beanDefinition.getBeanClassName();
        if (this.singletonObjects.containsKey(clazzName)) {
            return this.singletonObjects.get(clazzName);
        }
        Object instance = null;
        try {
            Class clazz = Class.forName(clazzName);
            Constructor declaredConstructor = clazz.getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            instance = declaredConstructor.newInstance();
            AdvisedSupport advisedSupport = initAopConfig();
            advisedSupport.setTarget(instance);
            advisedSupport.setTargetClass(clazz);
            if (advisedSupport.pointCutMatch()) {
                instance = new JdkDynamicAopProxy(advisedSupport).getProxy();
            }
        } catch (Exception e) {
            logger.error("发生异常", e);
        }
        if (beanDefinition.isSingleton()) {
            this.singletonObjects.put(clazzName, instance);
        }
        return instance;
    }

    private AdvisedSupport initAopConfig() {
        AopConfig config = new AopConfig();
        config.setPointCut(reader.getProps().getProperty("pointCut"));
        config.setAspectClass(reader.getProps().getProperty("aspectClass"));
        config.setAspectBefore(reader.getProps().getProperty("aspectBefore"));
        config.setAspectAfter(reader.getProps().getProperty("aspectAfter"));
        config.setAspectAfterThrow(reader.getProps().getProperty("aspectAfterThrow"));
        return new AdvisedSupport(config);
    }


    @Override
    public Object getBean(Class clazzName) {
        return getBean(clazzName.getName());
    }

    @Override
    public void refresh() {
//        定位
        reader = new BeanDefinitionReader(this.configLocation);
//        加载
        List<BeanDefinition> definitions = reader.loadBeanDefinition();
//        注册beanDefinition
        for (BeanDefinition definition : definitions) {
            registerBeanDefinition(definition.getFactoryBeanName(), definition);
            registerBeanDefinition(definition.getBeanClassName(),definition);
        }
//        注入非延时加载实例
        doAutowired();
    }

    private void doAutowired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanDefinitionEntry.getKey());
            }
        }
    }

}
