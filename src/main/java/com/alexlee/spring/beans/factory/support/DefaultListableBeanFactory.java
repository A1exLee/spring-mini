package com.alexlee.spring.beans.factory.support;

import com.alexlee.spring.beans.BeanWrapper;
import com.alexlee.spring.beans.config.BeanDefinition;
import com.alexlee.spring.context.AbstractApplicationContext;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/4/22 20:50
 */
public abstract class DefaultListableBeanFactory extends AbstractApplicationContext {

    protected final Map<String, BeanDefinition> beanDefinitionMap = Maps.newConcurrentMap();
    protected  Map<String, BeanWrapper> beanWrapperMap=Maps.newConcurrentMap();

    protected Map<String,Object> singletonObjects= Maps.newConcurrentMap();

    public String[] getBeanDefinitionNames(){
        return beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

}
