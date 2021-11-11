package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.dao.po.SysPermission;
import com.zyz.blogadmin.service.PermissionService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 * 权限控制层
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	@PostMapping("/permissionList")
	@LogAnnotation(module="权限管理",operator="获取权限列表")
	public Result listPermission(@RequestBody PageParam pageParam) {
		return permissionService.listPermission(pageParam);
	}

	@PostMapping("/add")
	@LogAnnotation(module="权限管理",operator="添加权限")
	public Result addPermission(@RequestBody SysPermission sysPermission) {
		return permissionService.addPermission(sysPermission);
	}

	@PostMapping("/update")
	@LogAnnotation(module="权限管理",operator="更新权限")
	public Result updatePermission(@RequestBody SysPermission sysPermission) {
		return permissionService.updatePermission(sysPermission);
	}

	@GetMapping("/delete/{id}")
	@LogAnnotation(module="权限管理",operator="删除权限")
	public Result deletePermission(@PathVariable("id") Long id) {
		return permissionService.deletePermission(id);
	}


}
