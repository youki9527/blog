package com.zyz.blog.dao.po;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class Comment {

	private Long id;
	private String content;
	private Long createDate;
	private Long articleId;
	private Long authorId;
	private Long parentId;
	private  Long toUid;
	private Integer level;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;
}
