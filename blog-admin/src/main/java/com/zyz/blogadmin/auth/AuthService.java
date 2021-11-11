package com.zyz.blogadmin.auth;


import com.zyz.blogadmin.dao.po.SysAdmin;
import com.zyz.blogadmin.dao.po.SysPermission;
import com.zyz.blogadmin.service.SysAdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zyz
 * @version 1.0
 *
 * 授权
 */
@Service
@Slf4j
public class AuthService {

	@Autowired
	private SysAdminService sysAdminService;

	/**
	 * 授权
	 * */
	public boolean auth(HttpServletRequest request, Authentication authentication) {
		String requestURI = request.getRequestURI();
		log.info("request url:{}", requestURI);
		Object principal = authentication.getPrincipal();
		//拦截未登录
		if (principal == null || "anonymousUser".equals(principal)) {
			return false;
		}
		UserDetails userDetails = (UserDetails) principal;
		String username = userDetails.getUsername();
		SysAdmin sysAdmin = sysAdminService.findAdminByUsername(username);
		//拦截没有该用户
		if (sysAdmin == null) {
			return false;
		}
		//认为是超级管理员
		if (sysAdmin.getId() == 1) {
			return true;
		}
		//权限路径列表
		List<SysPermission> sysPermissions = sysAdminService.findPermissionsByAdminId(sysAdmin.getId());
		//请求路径
		requestURI = StringUtils.split(requestURI, '?')[0];
		//请求路径包含路径末尾变量替换为*
		int i = requestURI.lastIndexOf('/');
		char c = requestURI.charAt(i + 1);
		if (c >= '0' && c <= '9') {
			StringBuffer stringBuffer = new StringBuffer(requestURI);
			StringBuffer replace = stringBuffer.replace(i + 1, requestURI.length(), "*");
			requestURI= replace.toString();
		}
		//遍历权限路径列表
		for (SysPermission sysPermission : sysPermissions) {
			if (requestURI.equals(sysPermission.getPath())) {
				log.info("权限通过");
				return true;
			}
		}
		//没有权限拦截
		return false;
	}

	public static void main(String[] args) {
		String s = "/category/delete/10";
		int i = s.lastIndexOf('/');
		char c = s.charAt(i + 1);
		if (c >= '0' && c <= '9') {
			StringBuffer stringBuffer = new StringBuffer(s);
			StringBuffer replace = stringBuffer.replace(i + 1, s.length(), "*");
			s= replace.toString();
			System.out.println(s);
		}
	}
}
