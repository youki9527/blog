package com.zyz.blog.controller;

import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.UserService;

import com.zyz.blog.vo.Params.PasswordParams;
import com.zyz.blog.vo.Params.UserParams;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 * 游客控制层
 */
@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 获取当前游客信息
	 * */
	@GetMapping("currentUser")
	@LogAnnotation(module = "游客", operator = "获取当前游客信息")
	// @Cache(expire = 5 * 60 * 1000, name = " currentUser")
	public Result currentUser(@RequestHeader("Authorization") String token) {
		return userService.findUserByToken(token);
	}

	/**
	 * 获取当前游客详细信息 currentUser::UserController::currentUserDetail::27b2fa8b193fe1ce965e35ec2d6e1bd2
	 * */
	@GetMapping("currentUserDetail")
	@LogAnnotation(module = "游客", operator = "获取当前游客详细信息")
	// @Cache(expire = 5* 60 * 1000, name = " currentUser")
	public Result currentUserDetail(@RequestHeader("Authorization") String token) {
		return userService.findUserDetailByToken(token);
	}

	/**
	 * 修改游客详细信息
	 * */
	@PostMapping("updateUserDetail")
	@LogAnnotation(module = "游客", operator = "修改游客详细信息")
	public Result updateUserDetail(@RequestHeader("Authorization") String token,@RequestBody UserParams userParams) {
		return userService.updateUserDetailByToken(token,userParams);
	}


	/**
	 * 修改游客密码
	 * */
	@PostMapping("updatePassword")
	@LogAnnotation(module = "游客", operator = "修改密码")
	public Result updatePassword(@RequestHeader("Authorization") String token, @RequestBody PasswordParams passwordParams) {
	 return userService.updatePassword(token,passwordParams);
	}

}
