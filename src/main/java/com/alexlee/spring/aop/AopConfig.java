package com.alexlee.spring.aop;

import lombok.Data;

/**
 * @author alexlee
 * @version 1.0
 * @date 2019/5/14 13:52
 */
@Data
public class AopConfig {
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;

}
