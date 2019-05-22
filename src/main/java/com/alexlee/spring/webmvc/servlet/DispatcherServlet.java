package com.alexlee.spring.webmvc.servlet;

import com.alexlee.spring.annotion.Controller;
import com.alexlee.spring.annotion.RequestMapping;
import com.alexlee.spring.context.support.ClassPathPropertiesApplicationContext;
import com.alexlee.spring.framework.webmvc.HandlerAdapter;
import com.alexlee.spring.framework.webmvc.HandlerMapping;
import com.alexlee.spring.framework.webmvc.ModelAndView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispatcherServlet extends HttpServlet {
    public static final String LOCATION = "contextConfigLocation";

    private List<HandlerMapping> handlerMappings = Lists.newArrayList();
    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = Maps.newHashMap();
    private ClassPathPropertiesApplicationContext context;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //根据用户请求的 URL 来获得一个 Handler
        HandlerMapping handler = getHandler(req);
        if (handler == null) {
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }
        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        ModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);
        processDispatchResult(req, resp, modelAndView);

    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (handlerAdapters == null || handlerAdapters.isEmpty()) {
            return null;
        }
        HandlerAdapter handlerAdapter = handlerAdapters.get(handler);
        if (handlerAdapter.supports(handler)) {
            return handlerAdapter;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }
//       这里就不做解析了
        resp.getWriter().write(modelAndView.toString());

    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if (handlerMappings.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (HandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMapping;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new ClassPathPropertiesApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    /**
     * 初始化九大组件
     *
     * @param context
     */
    private void initStrategies(ClassPathPropertiesApplicationContext context) {
        initMultipartResolver(context);
        initLocalResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapter(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    private void initHandlerAdapter(ClassPathPropertiesApplicationContext context) {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = context.getBean(beanDefinitionName);
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                baseUrl = annotation.value();
            }
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (!declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                String value = declaredMethod.getAnnotation(RequestMapping.class).value();
                String regex = ("/" + baseUrl + value.replaceAll("\\*", ".*")).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                this.handlerMappings.add(new HandlerMapping(bean, declaredMethod, pattern));
            }

        }

    }

    private void initHandlerMappings(ClassPathPropertiesApplicationContext context) {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
        }
    }

    private void initThemeResolver(ClassPathPropertiesApplicationContext context) {
    }


    private void initFlashMapManager(ClassPathPropertiesApplicationContext context) {
    }

    private void initViewResolvers(ClassPathPropertiesApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(ClassPathPropertiesApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(ClassPathPropertiesApplicationContext context) {
    }

    private void initLocalResolver(ClassPathPropertiesApplicationContext context) {
    }

    private void initMultipartResolver(ClassPathPropertiesApplicationContext context) {
    }


}
