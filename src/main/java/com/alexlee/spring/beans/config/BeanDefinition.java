package com.alexlee.spring.beans.config;

import lombok.Data;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/4/22 20:53
 */
@Data
public class BeanDefinition {
    private boolean isSingleton=true;

    private boolean lazyInit=false;

    private String beanClassName;

    private String factoryBeanName;
}
