package com.zyz.blog.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zyz
 * @version 1.0
 */
public class HttpContextUtils {
	public static HttpServletRequest getHttpServletRequest(){

		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
}
