package com.zbkj.common.annotation;


import com.zbkj.common.enums.MethodType;

import java.lang.annotation.*;

/**
 * 自定义注解，拦截service
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用在参数和方法上
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Documented//表明这个注解应该被 javadoc工具记录
public @interface LogControllerAnnotation {

    // 日志是否存入数据库
    boolean intoDB() default false;

    // 操作类型, 默认是查询
    MethodType methodType() default MethodType.SELECT;

    // 接口描述
    String description() default "";

}
