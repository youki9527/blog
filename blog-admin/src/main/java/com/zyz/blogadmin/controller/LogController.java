package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.service.LogService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.LogPageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("/log")
public class LogController {

	@Autowired
	private LogService logService;

	@RequestMapping("/logList")
	private Result logList(@RequestBody LogPageParams logPageParams){
		return logService.logList(logPageParams);
	}

	@RequestMapping("/delete/{id}")
	private Result delete(@PathVariable("id") Long id){
		return logService.deleteById(id);
	}

}
