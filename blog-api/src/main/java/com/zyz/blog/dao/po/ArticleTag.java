package com.zyz.blog.dao.po;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class ArticleTag {

	private Long id;
	private Long articleId;
	private Long tagId;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;

}
