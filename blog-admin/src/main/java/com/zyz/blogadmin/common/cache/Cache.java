package com.zyz.blogadmin.common.cache;

import java.lang.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

	//默认时间
	long expire() default 1 * 60 * 1000;
	//缓存标识key
	String name() default "";
}
