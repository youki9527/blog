package com.zyz.blogadmin.service;

import com.zyz.blogadmin.dao.po.SysPermission;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.PageParam;

/**
 * @author zyz
 * @version 1.0
 */
public interface PermissionService {

	Result listPermission(PageParam pageParam);

	Result addPermission(SysPermission sysPermission);

	Result updatePermission(SysPermission sysPermission);

	Result deletePermission(Long id);
}
