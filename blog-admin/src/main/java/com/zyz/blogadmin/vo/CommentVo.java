package com.zyz.blogadmin.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class CommentVo {

	private String id;
	private String articleTitle;
	private String content;
	private String authorAccount;
	private String authorNickName;
	private String createDate;
}
