package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.mapper.PermissionMapper;
import com.zyz.blogadmin.dao.po.SysPermission;
import com.zyz.blogadmin.service.PermissionService;
import com.zyz.blogadmin.vo.PageResult;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.PageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;


	@Override
	public Result listPermission(PageParam pageParam) {
		Page<SysPermission> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
		LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<SysPermission>();
		if (StringUtils.isNotBlank(pageParam.getQueryString())){
			queryWrapper.eq(SysPermission::getName,pageParam.getQueryString());
		}
		Page<SysPermission> permissionPage = permissionMapper.selectPage(page, queryWrapper);
		PageResult<SysPermission> pageResult = new PageResult<>();
		pageResult.setList(permissionPage.getRecords());
		pageResult.setTotal(permissionPage.getTotal());
		return Result.success(pageResult);
	}

	@Override
	public Result addPermission(SysPermission sysPermission) {
		permissionMapper.insert(sysPermission);
		return Result.success(null);
	}

	@Override
	public Result updatePermission(SysPermission sysPermission) {
		permissionMapper.updateById(sysPermission);
		return Result.success(null);
	}

	@Override
	public Result deletePermission(Long id) {
		permissionMapper.deleteById(id);
		return Result.success(null);
	}
}
