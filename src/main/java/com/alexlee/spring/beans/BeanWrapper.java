package com.alexlee.spring.beans;

import lombok.Data;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/5 15:36
 */
@Data
public class BeanWrapper {
    private Object warpperdInstance;
    private Class wrappedClass;

    public BeanWrapper(Object warpperdInstance) {
        this.warpperdInstance = warpperdInstance;
    }
    public Class getWrappedClass(){
        return this.warpperdInstance.getClass();
    }
}
