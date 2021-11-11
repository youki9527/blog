package com.zyz.blogadmin.handler;

import com.zyz.blogadmin.vo.Result;
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
		log.error("系统异常信息:{}",exception);
		return Result.fail(500,exception.toString());
	}
}
