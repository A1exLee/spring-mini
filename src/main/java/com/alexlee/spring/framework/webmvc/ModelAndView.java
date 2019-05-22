package com.alexlee.spring.framework.webmvc;

import lombok.Data;

import java.util.Map;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/22 21:03
 */
@Data
public class ModelAndView {
    private String viewName;
    private Map<String,?> model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    @Override
    public String toString() {
        return "ModelAndView{" +
                "viewName='" + viewName + '\'' +
                ", model=" + model +
                '}';
    }
}
