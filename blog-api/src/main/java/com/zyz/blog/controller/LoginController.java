package com.zyz.blog.controller;

import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.vo.Params.LoginParams;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 * 登录账号
 */
@RestController
public class LoginController {

	@Autowired
	private LoginRegistLogoutService loginRegistLogoutService;

	@PostMapping("/login")
	@LogAnnotation(module="登录",operator="登录验证")
	public Result login(@RequestBody LoginParams loginParams){
		return  loginRegistLogoutService.login(loginParams);
	}

}
