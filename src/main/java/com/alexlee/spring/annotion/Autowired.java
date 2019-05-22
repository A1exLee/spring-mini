package com.alexlee.spring.annotion;

import java.lang.annotation.*;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/4/22 20:36
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
