package com.zyz.blog.service;

import com.zyz.blog.dao.po.SysAdmin;

/**
 * @author zyz
 * @version 1.0
 */
public interface SysAdminService {

	/**
	 * 根据id获取文章作者的昵称和头像
	 * */
	SysAdmin findSysAuthorByID(Long id);

}
