package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyz.blogadmin.dao.po.User;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 *
 */
public interface UserMapper extends BaseMapper<User> {

	void writeOffUserList(List<Long> ids);
}
