package com.zyz.blogadmin.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class CategoryVo {

	/**
	 * 这里id设置为String是为了
	 * 后期如何更改分类数据库id为分布式id，直接传Long类型 前端会出错
	 * */
	private String  id;
	private String avatar;
	private String categoryName;
	private String description;
	/**
	 * 文章总数
	 * */
	private Integer articles;
}
