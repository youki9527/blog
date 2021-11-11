package com.zyz.blog.controller;

import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.vo.Params.RegisterParms;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 * 注册账号
 */
@RestController
@RequestMapping("register")
public class RegisterController {

	@Autowired
	private LoginRegistLogoutService loginRegistLogoutService;

	@PostMapping
	@LogAnnotation(module="注册",operator="注册处理")
	public Result register(@RequestBody RegisterParms registerParms){
		return loginRegistLogoutService.register(registerParms);
	}
}
