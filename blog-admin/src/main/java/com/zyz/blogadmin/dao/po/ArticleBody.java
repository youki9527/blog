package com.zyz.blogadmin.dao.po;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class ArticleBody {

	private Long id;
	private String content;
	private String contentHtml;
	private Long articleId;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;
}
