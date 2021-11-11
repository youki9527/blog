package com.zyz.blog.service;

import com.zyz.blog.dao.po.User;
import com.zyz.blog.vo.Params.PasswordParams;
import com.zyz.blog.vo.Params.UserParams;
import com.zyz.blog.vo.Result;
import com.zyz.blog.vo.UserVo;

/**
 * @author zyz
 * @version 1.0
 */
public interface UserService {

	/**
	 * 根据id查询作者信息 头像和昵称
	 * */
	User findUserById(Long id);

	/**
	* 根据账户和密码查询游客信息
	* */
	User findUser(String account, String password);

	/**
	 * 根据Token获取游客信息
	 * */
	Result findUserByToken(String token);

	/**
	 * 根据账号查找游客信息
	 *
	 * */
	User findUserByAccount(String account);

	/**
	 * 保存游客信息
	 * */
	void save(User user);

	/**
	 * 根据id查询游客信息
	 * */
	UserVo findUserVoById(Long authorId);

	/**
	 * 根据Token获取游客详细信息
	 * */
	Result findUserDetailByToken(String token);

	/**
	 *修改游客信息
	 **/
	Result updateUserDetailByToken(String token, UserParams userParams);

	/**
	 * 注销游客 改变游客表的delete字段
	 * */
	int updateDeleteValueById(Long id);

	/**
	 * 修改游客密码
	 * */
	Result updatePassword(String token, PasswordParams passwordParams);

}
