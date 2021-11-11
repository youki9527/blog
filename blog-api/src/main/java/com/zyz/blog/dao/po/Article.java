package com.zyz.blog.dao.po;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class Article {

	public static final int Article_TOP = 1;

	public static final int Article_Common = 0;

	private Long id;
	private String title;
	private String summary;
	private Integer commentCounts;
	private Integer viewCounts;
	/**
	 * 作者id
	 */
	private Long authorId;
	/**
	 * 内容id
	 */
	private Long bodyId;
	/**
	 *类别id
	 */
	private Long categoryId;
	/**
	 * 置顶
	 */
	private Integer weight;
	/**
	 * 是否发布
	 * */
	private Integer isPublished;
	/**
	 * 创建时间
	 */
	private Long createDate;
	/**
	 * 更新时间
	 */
	private Long updateDate;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;
}
