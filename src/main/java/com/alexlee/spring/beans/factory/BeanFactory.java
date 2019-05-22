package com.alexlee.spring.beans.factory;

/**
 * The interface Bean factory.
 *
 * @author alexlee
 */
public interface BeanFactory {
    /**
     * Gets bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    Object getBean(String beanName);

    /**
     * Gets bean.
     *
     * @param clazzName the clazz name
     * @return the bean
     */
    Object getBean(Class clazzName);
}
