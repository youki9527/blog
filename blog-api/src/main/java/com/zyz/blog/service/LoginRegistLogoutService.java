package com.zyz.blog.service;

import com.zyz.blog.dao.po.User;
import com.zyz.blog.vo.Params.LoginParams;
import com.zyz.blog.vo.Params.RegisterParms;
import com.zyz.blog.vo.Result;

/**
 * @author zyz
 * @version 1.0
 */
public interface LoginRegistLogoutService {

	/**
	 * 注册
	 * */
	Result register(RegisterParms registerParms);

	/**
	 * 登录
	 * */
	Result login(LoginParams loginParams);

	/**
	 * 校验token更新token
	 * */
	User checkToken(String token);

	/**
	 * 登出
	 * */
	Result logout(String token);

	/**
	 *注销账号
	 * */
	Result deleteAccount(String token);

}
