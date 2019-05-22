package com.alexlee.bussiness;

import com.alexlee.bussiness.aspect.Service;
import com.alexlee.spring.annotion.Autowired;
import com.alexlee.spring.annotion.Component;
import com.alexlee.spring.context.support.ClassPathPropertiesApplicationContext;

/**
 * class Startup.
 *
 * @author alexlee
 */
@Component
public class Startup {

    @Autowired("testService")
    public static Service testService;

    public static void main(String[] args) {
        new ClassPathPropertiesApplicationContext("application.properties");
        testService.divide(1,0);
        try {
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
