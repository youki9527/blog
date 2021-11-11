package com.zyz.blogadmin.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 * 文章vo
 */
@Data
public class ArticleVo {
	/***
	 * 前端解析出错
	 * */
	// @JsonSerialize(using = ToStringSerializer.class)
	private String id;
	private String title;
	private String summary;
	private Integer commentCounts;
	private Integer viewCounts;
	private Integer weight;
	private String  createDate;
	private String  updateDate;
	private ArticleBodyVo body;
	private List<TagVo> tags;
	private SysAdminVo author;
    private CategoryVo category;
}
