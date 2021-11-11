package com.zyz.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyz.blog.dao.po.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author zyz
 * @version 1.0
 */
public interface UserMapper extends BaseMapper<User> {

	void updateLastLoginByAccount(@Param("account") String account, @Param("time") Long time);
}
