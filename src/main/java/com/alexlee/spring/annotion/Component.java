package com.alexlee.spring.annotion;

import java.lang.annotation.*;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/4/22 20:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component  {
}
