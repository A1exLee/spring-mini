package com.alexlee.spring.beans.config.config;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/6 9:58
 */
public  interface BeanPostProcessor {
     Object postProcessBeforeInitialization(Object bean, String beanName);
     Object postProcessAfterInitialization(Object bean, String beanName);
}
