package com.zyz.blogadmin.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class LogVo {

	private String  id;
	private String  createDate;
	private String ip;
	private String method;
	private String module;
	private String operation;
	private Long time;
}
