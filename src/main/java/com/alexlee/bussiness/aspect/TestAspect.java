package com.alexlee.bussiness.aspect;

import com.alexlee.spring.aop.aspect.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/15 14:44
 */
public class TestAspect {
    private final static Logger logger = LoggerFactory.getLogger(TestAspect.class);

    public void before(JoinPoint joinPoint) {
        logger.info("方法之前切入~~~");
    }
    public void after(JoinPoint joinPoint,Object returnValue) {
        logger.info("方法之后切入~~~，方法返回值："+returnValue);
    }
    public void afterThrow(JoinPoint joinPoint,Throwable throwable) {
        logger.info("异常后切入~~~,异常："+throwable.getCause());
    }
}
