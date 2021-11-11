package com.zyz.blog.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 * 归档
 */
@Data
public class ArchivesVo {

	private Integer year;
	private Integer month;
	/**
	 *文章总数
	 */
	private Integer count;
}
