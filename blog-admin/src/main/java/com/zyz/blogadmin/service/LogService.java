package com.zyz.blogadmin.service;

import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.LogPageParams;

/**
 * @author zyz
 * @version 1.0
 */
public interface LogService {

	Result logList(LogPageParams logPageParams);

	Result deleteById(Long id);
}
