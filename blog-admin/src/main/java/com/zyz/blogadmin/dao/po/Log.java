package com.zyz.blogadmin.dao.po;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class Log {
	private Long id;
	private Long createDate;
	private String ip;
	private String method;
	private String module;
	private String operation;
	private String params;
	private Long time;
}
