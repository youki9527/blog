package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyz.blogadmin.dao.po.SysPermission;

import java.util.List;


/**
 * @author zyz
 * @version 1.0
 */
public interface PermissionMapper extends BaseMapper<SysPermission> {


	List<SysPermission> findPermissionsByAdminId(Long adminId);
}
