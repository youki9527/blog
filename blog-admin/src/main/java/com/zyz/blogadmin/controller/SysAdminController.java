package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.service.SysAdminService;
import com.zyz.blogadmin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("admin")
public class SysAdminController {

	@Autowired
	private SysAdminService sysAdminService;

	@GetMapping("/adminInfo")
	@LogAnnotation(module="管理员管理",operator="获取管理员信息")
	public Result getSysAdminInfo(){
		return sysAdminService.getSysAdminInfo();
	}


}


