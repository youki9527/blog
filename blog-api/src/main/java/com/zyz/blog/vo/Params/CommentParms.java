package com.zyz.blog.vo.Params;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class CommentParms {

	private long articleId;

	private String content;

	private Long parent;

	private Long toUserId;
}
