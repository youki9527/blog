package com.zyz.blog.handler;

import com.alibaba.fastjson.JSON;
import com.zyz.blog.dao.po.User;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.util.UserThreadLocal;
import com.zyz.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zyz
 * @version 1.0
 * <p>
 * 登录拦截器
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


	/**
	 * 校验是否登录 没有登录进行拦截
	 */
	@Autowired
	private LoginRegistLogoutService loginRegistLogoutService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		String token = request.getHeader("Authorization");
		log.info("========request start=======");
		log.info("request url:{}", request.getRequestURI());
		log.info("request method:{}", request.getMethod());
		log.info("token", token);
		log.info("==========request end======");
		//1.token为null拦截
		if (StringUtils.isBlank(token)) {
			Result res = Result.fail(204, "未登录");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(JSON.toJSONString(res));
			return false;
		}
		//2.token不合法或者过期拦截
		User user = loginRegistLogoutService.checkToken(token);
		if (user == null) {
			Result res = Result.fail(204, "未登录");
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(JSON.toJSONString(res));
			return false;
		}
        //登录验证成功，放行，将用户信息存在本地线程
		UserThreadLocal.put(user);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserThreadLocal.remove();
	}
}
