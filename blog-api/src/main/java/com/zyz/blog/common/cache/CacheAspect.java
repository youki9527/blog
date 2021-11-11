package com.zyz.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.zyz.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author zyz
 * @version 1.0
 * <p>
 * 缓存切面
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 切点
	 */
	@Pointcut("@annotation(com.zyz.blog.common.cache.Cache)")
	public void pt() {

	}

	/**
	 * 环绕通知
	 */
	@Around("pt()")
	public Object around(ProceedingJoinPoint joinPoint) {

		try {
			Signature signature = joinPoint.getSignature();
			//类名
			String className = joinPoint.getTarget().getClass().getSimpleName();
			//方法名
			String methodName = signature.getName();
			//构建参数类型数组
			Class[] paramsType = new Class[joinPoint.getArgs().length];
			//参数数组
			Object[] args = joinPoint.getArgs();
			//参数
			String params = "";
			for (int i = 0; i < args.length; i++) {
				if (args[i] != null) {
					params += JSON.toJSONString(args[i]);
					paramsType[i] = args[i].getClass();
				} else {
					paramsType[i] = null;
				}
			}
			if (StringUtils.isNotEmpty(params)) {
				//加密生成key
				params = DigestUtils.md5Hex(params);
			}
			//被增强的方法
			Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, paramsType);
			//被增强方法的注解
			Cache annotation = method.getAnnotation(Cache.class);
			//	缓存过期时间
			long expire = annotation.expire();
			//	缓存名称
			String name = annotation.name();
			//key
			String redisKey = name + "::" + className + "::" + methodName + "::" + params;
			//value
			String redisValue = redisTemplate.opsForValue().get(redisKey);
			//1.redis中有
			if (StringUtils.isNotEmpty(redisValue)) {
				log.info("走了缓存---{}，{}", className, methodName);
				//返回结果
				return JSON.parseObject(redisValue, Result.class);
			}
			//2.redis中没有
			// 执行原方法
			Object proceed  = joinPoint.proceed();
			//存入缓存
			log.info("存入缓存---{}，{}",className,methodName);
			redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
			//返回结果
			return proceed;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Result.fail(-99999,"系统错误");

	}

	public static void main(String[] args) {

		// eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzA5MjkxODMsInVzZXJJZCI6MTQzNDA2MjkyMDkwMzQzNDI0MiwiaWF0IjoxNjMwODQyNzgzfQ.ivfqTXvNvDJFb1_MK4Sq1DcJb4y3UjcmcGsRfBXWJVs
		String params=JSON.toJSONString("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzA5MjkxODMsInVzZXJJZCI6MTQzNDA2MjkyMDkwMzQzNDI0MiwiaWF0IjoxNjMwODQyNzgzfQ.ivfqTXvNvDJFb1_MK4Sq1DcJb4y3UjcmcGsRfBXWJVs");
		params = DigestUtils.md5Hex(params);
		System.out.println(params);
		 // 48dc48324a98639a7565306d53a9ac59
	}

}
