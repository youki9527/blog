package com.zyz.blogadmin.service;

import com.zyz.blogadmin.dao.po.User;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.UserVo;
import com.zyz.blogadmin.vo.params.PageParam;
import com.zyz.blogadmin.vo.params.RegisterUserParams;
import com.zyz.blogadmin.vo.params.UpdateUserStatusParams;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface UserService {

	/**
	 *
	 * 查询游客列表*/
	Result listUser(PageParam pageParam);

	/**
	 * 添加游客
	 * */
	Result addUser(RegisterUserParams registerUserParams);

	/**
	 * 更新游客数据
	 * */
	Result updateUser(UserVo sysUser);

	/**
	 * 删除游客
	 * */
	Result deleteUser(Long id);

	/**
	 * 根据账号查找用户
	 * */
	User findUserByAccount(String account);

	/**
	 *添加游客
	 * */
	int save(User newUser);

	/**
	 * 批量删除游客
	 * */
	Result deleteUserList(List<Long> ids);

	/**
	 * 注销游客
	 * */
	Result writeOffUser(Long id);

	/**
	 * 更新用户状态
	 * */
	Result UpdateStatus(UpdateUserStatusParams updateUserStatusParams);
}
