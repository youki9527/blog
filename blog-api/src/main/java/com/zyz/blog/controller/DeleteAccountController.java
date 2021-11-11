package com.zyz.blog.controller;

import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 * 注销账号
 */
@RestController
public class DeleteAccountController {

	@Autowired
	private LoginRegistLogoutService loginRegistLogoutService;

	@GetMapping("/deleteAccount")
	@LogAnnotation(module="注销",operator="注销账号")
	public Result deleteAccount(@RequestHeader("Authorization") String token){
		return loginRegistLogoutService.deleteAccount(token);
	}

}
