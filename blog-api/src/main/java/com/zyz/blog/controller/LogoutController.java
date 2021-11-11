package com.zyz.blog.controller;

import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 * 退出账号
 */
@RestController
@RequestMapping("/logout")
public class LogoutController {

	@Autowired
	private LoginRegistLogoutService loginRegistLogoutService;

	@GetMapping
	@LogAnnotation(module="登出",operator="登出处理")
	public Result logout(@RequestHeader("Authorization") String token){
		return loginRegistLogoutService.logout(token);
	}
}
