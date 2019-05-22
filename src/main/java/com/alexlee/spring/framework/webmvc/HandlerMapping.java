package com.alexlee.spring.framework.webmvc;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/22 10:50
 */
@Data
public class HandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;

    public HandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }
}
