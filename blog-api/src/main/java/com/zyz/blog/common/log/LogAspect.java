package com.zyz.blog.common.log;

import com.alibaba.fastjson.JSON;
import com.zyz.blog.dao.po.Log;
import com.zyz.blog.service.LogService;
import com.zyz.blog.util.HttpContextUtils;
import com.zyz.blog.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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


	@Autowired
	private LogService logService;

	/**
	 * 切点
	 */
	@Pointcut("@annotation(com.zyz.blog.common.log.LogAnnotation)")
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
			if (args[0] instanceof MultipartFile){
				log.info("params:{}","图片上传参数....");
			}else {
				String params = JSON.toJSONString(args[0]);
				log.info("params:{}",params);
			}

		}
		HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
		log.info("ip:{}", IpUtils.getIpAddress(httpServletRequest));
		log.info("excute time:{} ms",time);
		log.info("====================log end====================");
		/**
		 * 插入博客日志数据表
		 * */
		Log blogLog = new Log();
		blogLog.setIp(IpUtils.getIpAddress(httpServletRequest));
		/**
		 * 在linux部署环境需要加8个小时
		 * */
		blogLog.setCreateDate(System.currentTimeMillis()+8*60*60*1000);
		blogLog.setOperation(logannotation.operator());
		blogLog.setModule(logannotation.module());
		blogLog.setMethod(className+"."+methodName+"()");
		blogLog.setTime(time);
		if (args.length==0){
			log.info("params:{}","无参数");
		}
		else {
			if (args[0] instanceof MultipartFile){
				log.info("params:{}","图片上传参数....");
				blogLog.setParams("图片上传参数....");
			}else {
				String params = JSON.toJSONString(args[0]);
				blogLog.setParams(params);
			}
		}
		//插入日志表
		logService.insert(blogLog);



	}
}
