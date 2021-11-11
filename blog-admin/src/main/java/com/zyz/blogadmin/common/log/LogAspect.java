package com.zyz.blogadmin.common.log;

import com.alibaba.fastjson.JSON;
import com.zyz.blogadmin.util.HttpContextUtils;
import com.zyz.blogadmin.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author zyz
 * @version 1.0
 *
 * 日志切面
 */
@Component
@Aspect
@Slf4j
public class LogAspect {


	/**
	 * 切点
	 */
	@Pointcut("@annotation(com.zyz.blogadmin.common.log.LogAnnotation)")
	public void pt() {

	}

	/**
	 * 环绕通知
	 */
	@Around("pt()")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		//开始时间
		long start = System.currentTimeMillis();
		//	执行方法
		Object res = joinPoint.proceed();
		//	结束时间
		long end = System.currentTimeMillis();
		//总时间
		long time = end - start;
		//保存日志
		recodeLog(joinPoint, time);
		return res;
	}

	/**
	 * 记录日志
	 */
	private void recodeLog(ProceedingJoinPoint joinPoint, long time) {

		//方法签名
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		LogAnnotation logannotation = method.getAnnotation(LogAnnotation.class);
		log.info("====================log start====================");
		log.info("module:{}", logannotation.module());
		log.info("operation:{}", logannotation.operator());
		//方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		log.info("request method:{}",className+"."+methodName+"()");
		//参数
		Object[] args = joinPoint.getArgs();
		if (args.length==0){
			log.info("params:{}","无参数");
		}
		else {
			String params = JSON.toJSONString(args[0]);
			log.info("params:{}",params);
		}

		HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
		log.info("ip:{}", IpUtils.getIpAddress(httpServletRequest));
		log.info("excute time:{} ms",time);
		log.info("====================log end====================");

	}
}
