package com.zyz.blogadmin.common.log;

import java.lang.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

	String module() default "";

	String operator() default "";
}
