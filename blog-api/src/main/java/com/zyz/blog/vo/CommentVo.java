package com.zyz.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class CommentVo {


	/**
	 * 防止前端精度损失
	 * */
	// @JsonSerialize(using = ToStringSerializer.class)
	private String id;

	private String content;

	private String createDate;

	private UserVo author;

	private List<CommentVo> childrens;

	private UserVo toUser;

	private Integer level;



}
