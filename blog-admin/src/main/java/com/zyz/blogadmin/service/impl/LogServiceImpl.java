package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.mapper.LogMapper;
import com.zyz.blogadmin.dao.po.Article;
import com.zyz.blogadmin.dao.po.Log;
import com.zyz.blogadmin.service.LogService;
import com.zyz.blogadmin.util.MyDateUtils;
import com.zyz.blogadmin.vo.ArticleVo;
import com.zyz.blogadmin.vo.LogVo;
import com.zyz.blogadmin.vo.PageResult;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.LogPageParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zyz
 * @version 1.0
 */
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogMapper logManager;

	@Override
	public Result logList(LogPageParams pageParam) {
		Page<Log> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
		//没有开始结束时间处理
		Long startDate = Long.valueOf(0);
		Long endDate = Long.valueOf(0);
		if (pageParam.getCreateStartDate().equals("")) {
			startDate = null;
			endDate = null;
		} else {
			startDate = MyDateUtils.DateStringToLong(pageParam.getCreateStartDate());
			endDate = MyDateUtils.DateStringToLong(pageParam.getCreateEndDate());
		}
		IPage<Log> logIPage = logManager.listLog(
				page,
				startDate,
				endDate);
		List<Log> logs = logIPage.getRecords();
		long total = logIPage.getTotal();

		PageResult<LogVo> objectPageResult = new PageResult<>();
		objectPageResult.setList(copyToLogVoList(logs));
		objectPageResult.setTotal(total);
		return Result.success(objectPageResult);
	}

	@Override
	public Result deleteById(Long id) {
		logManager.deleteById(id);
		return Result.success(null);
	}

	private List<LogVo> copyToLogVoList(List<Log> logs) {
		List<LogVo> logVos=new ArrayList<>();
		for (Log log : logs) {
			logVos.add(copyToLogVo(log));
		}
		return logVos;
	}

	private LogVo copyToLogVo(Log log) {
		LogVo logVo = new LogVo();
		logVo.setId(String.valueOf(log.getId()));
		logVo.setCreateDate(MyDateUtils.LongToDateString(log.getCreateDate()));
		BeanUtils.copyProperties(log,logVo);
		return logVo;
	}

}
