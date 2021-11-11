package com.zyz.blogadmin.service;

import com.zyz.blogadmin.dao.po.SysAdmin;
import com.zyz.blogadmin.dao.po.SysPermission;
import com.zyz.blogadmin.vo.Result;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface SysAdminService {

	SysAdmin findAdminByUsername(String userName);

	List<SysPermission> findPermissionsByAdminId(Long adminId);

	Result getSysAdminInfo();
}
