package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blog.dao.mapper.SysAdminMapper;
import com.zyz.blog.dao.po.SysAdmin;
import com.zyz.blog.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class SysAdminServiceImpl implements SysAdminService {

	@Autowired
	private SysAdminMapper sysAdminMapper;

	@Override
	public SysAdmin findSysAuthorByID(Long id) {
		LambdaQueryWrapper<SysAdmin> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.select(SysAdmin::getNickname,
				            SysAdmin::getAvatar).
				     eq(SysAdmin::getId,id).
				     last("limit 1");
		SysAdmin sysAdmin = sysAdminMapper.selectOne(queryWrapper);
		return sysAdmin;
	}
}
