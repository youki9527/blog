package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blogadmin.auth.AdminUser;
import com.zyz.blogadmin.dao.mapper.AdminMapper;
import com.zyz.blogadmin.dao.mapper.PermissionMapper;
import com.zyz.blogadmin.dao.po.SysAdmin;
import com.zyz.blogadmin.dao.po.SysPermission;
import com.zyz.blogadmin.service.SysAdminService;
import com.zyz.blogadmin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class SysAdminServiceImpl implements SysAdminService {

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public SysAdmin findAdminByUsername(String userName) {

		LambdaQueryWrapper<SysAdmin> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.eq(SysAdmin::getUsername,userName).
				      last("limit 1");
		SysAdmin sysAdmin = adminMapper.selectOne(queryWrapper);
		return sysAdmin;
	}

	public List<SysPermission> findPermissionsByAdminId(Long adminId){
		return permissionMapper.findPermissionsByAdminId(adminId);
	}


	@Override
	public Result getSysAdminInfo() {
		//获取当前登录用户管理员信息,在登录授权的时候存入系统中
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AdminUser principal =(AdminUser) authentication.getPrincipal();
		//构造换回的对象
		SysAdmin sysAdmin = new SysAdmin();
		sysAdmin.setId(principal.getId());
		sysAdmin.setUsername(principal.getUsername());
		sysAdmin.setNickname(principal.getNickname());
		sysAdmin.setAvatar(principal.getAvatar());
		if (principal==null){
			return Result.fail(301,"获取当前用户失败");
		}else {
			return Result.success(sysAdmin);
		}

	}

}
