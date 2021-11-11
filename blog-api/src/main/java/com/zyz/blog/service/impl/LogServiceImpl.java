package com.zyz.blog.service.impl;

import com.zyz.blog.dao.mapper.LogMapper;
import com.zyz.blog.dao.po.Log;
import com.zyz.blog.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class LogServiceImpl implements LogService {
	@Autowired
	private LogMapper logMapper;

	@Override
	public void insert(Log log) {
		logMapper.insert(log);
	}
}
