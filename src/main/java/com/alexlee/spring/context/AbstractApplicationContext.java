package com.alexlee.spring.context;

import com.alexlee.spring.beans.factory.BeanFactory;

/**
 * class Abstract application context.
 *
 * @author alexlee
 * @version 1.0
 * @date 2019 /4/22 20:46
 */
public abstract class AbstractApplicationContext  implements ApplicationContext,BeanFactory {

    /**
     * ioc入口.
     */
    public  abstract  void refresh();

}
