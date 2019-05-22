package com.alexlee.bussiness.aspect;

import com.alexlee.spring.annotion.Component;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/15 15:01
 */
@Component
public class TestService implements Service{

    public  int add(int a,int b){
        System.out.println("a:"+a+" b:"+b);
        return a+b;
    }
    public  double divide(int a, int b){
        System.out.println("a:"+a+" b:"+b);
        return a/b;
    }

}
