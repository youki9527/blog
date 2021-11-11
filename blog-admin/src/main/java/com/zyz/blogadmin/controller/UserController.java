package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.service.UserService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.UserVo;
import com.zyz.blogadmin.vo.params.PageParam;
import com.zyz.blogadmin.vo.params.RegisterUserParams;
import com.zyz.blogadmin.vo.params.UpdateUserStatusParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 * 游客管理控制层
 */
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("userList")
	@LogAnnotation(module="游客管理",operator="游客列表")
	public Result listUser(@RequestBody PageParam pageParam) {
		return userService.listUser(pageParam);
	}

	@PostMapping("add")
	@LogAnnotation(module="游客管理",operator="添加游客")
	public Result addSysUser(@RequestBody RegisterUserParams registerUserParams) {
		return userService.addUser(registerUserParams);
	}

	@PostMapping("update")
	@LogAnnotation(module="游客管理",operator="更新游客信息")
	public Result updateUser(@RequestBody UserVo userVo) {
		return userService.updateUser(userVo);
	}

	/**
	 * 更新用户状态
	 * 禁言正常
	 * */
	@PostMapping("updatestatus")
	@LogAnnotation(module="游客管理",operator="更新游客状态")
	public Result updateWeight(@RequestBody UpdateUserStatusParams updateUserStatusParams) {
		return userService.UpdateStatus(updateUserStatusParams);
	}
	@GetMapping("delete/{id}")
	@LogAnnotation(module="游客管理",operator="删除游客")
	public Result deleteUser(@PathVariable("id") Long id) {
		return userService.deleteUser(id);
	}

	@PostMapping("delete/userList")
	@LogAnnotation(module="游客管理",operator="批量删除游客")
	public Result deleteUserList(@RequestBody() List<Long> ids) {
		return userService.deleteUserList(ids);
	}

	@GetMapping("writeoff/{id}")
	@LogAnnotation(module="游客管理",operator="注销游客")
	public Result writeOffUser(@PathVariable("id") Long id) {
		return userService.writeOffUser(id);
	}

}
