package com.zyz.blog.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class TagVo {

	private String id;
	private String tagName;
	private String avatar;
	/**
	 * 文章数量
	 * */
	private Integer articles;
}
