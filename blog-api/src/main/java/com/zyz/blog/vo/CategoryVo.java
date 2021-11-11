package com.zyz.blog.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class CategoryVo {
	private String id;
	private String avatar;
	private String categoryName;
	private String description;
	/**
	 * 文章总数
	 * */
	private Integer articles;
}
