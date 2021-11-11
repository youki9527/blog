package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.po.Article;
import com.zyz.blogadmin.dao.po.Log;

/**
 * @author zyz
 * @version 1.0
 */
public interface LogMapper extends BaseMapper<Log> {

	IPage<Log> listLog(Page<Log> page,
					   Long createStartDate,
					   Long createEndDate);
}
