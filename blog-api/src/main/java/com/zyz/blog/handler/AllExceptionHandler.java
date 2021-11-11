package com.zyz.blog.handler;

import com.zyz.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zyz
 * @version 1.0
 * 异常处理
 */
@ControllerAdvice
@Slf4j
public class AllExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result doException(Exception exception){
		exception.printStackTrace();
		// System.out.println("系统异常");
		// log.error("系统异常信息:{}",exception);
		return Result.fail(500,"系统异常");
	}
}
