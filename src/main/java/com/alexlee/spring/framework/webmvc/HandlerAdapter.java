package com.alexlee.spring.framework.webmvc;

import com.alexlee.spring.annotion.RequestParam;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/22 10:52
 */
public class HandlerAdapter {
    public static final Logger logger = LoggerFactory.getLogger(HandlerMapping.class);

    public boolean supports(Object handle) {
        return handle instanceof HandlerMapping;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!supports(handler)) {
            logger.error("handler not supported");
            return null;
        }
//保存RequestParam参数index映射
        HandlerMapping handlerMapping = (HandlerMapping) handler;
        Map<String, Integer> paramMapping = Maps.newHashMap();
        Annotation[][] annotations = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof RequestParam) {
                    String paramName = ((RequestParam) annotation).value();
                    if (paramName.trim().length() > 0) {
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }
//        保存Request和Response index映射
        Class[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class paramType = paramTypes[i];
            if (paramType == HttpServletResponse.class || paramType == HttpServletRequest.class) {
                paramMapping.put(paramType.getSimpleName(), i);
            }
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
//        参数列表
        Object[] params = new Object[paramTypes.length];
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]]", "").replaceAll("\\s", "");
            if (!paramMapping.containsKey(entry.getKey())) {
                continue;
            }
            int index = paramMapping.get(entry.getKey());
            params[index] = value;
        }
        if (paramMapping.containsKey(HttpServletRequest.class.getSimpleName())) {
            params[paramMapping.get(HttpServletRequest.class.getSimpleName())] = request;
        }
        if (paramMapping.containsKey(HttpServletResponse.class.getSimpleName())) {
            params[paramMapping.get(HttpServletResponse.class.getSimpleName())] = response;
        }
        Object result =
                handlerMapping.getMethod().invoke(handlerMapping.getController(), params);
        if (result == null) {
            return null;
        }
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() ==
                ModelAndView.class;
        if (isModelAndView) {
            return (ModelAndView) result;
        } else {
            return null;
        }
    }
}
