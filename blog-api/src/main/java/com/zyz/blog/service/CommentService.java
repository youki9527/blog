package com.zyz.blog.service;

import com.zyz.blog.vo.Params.CommentParms;
import com.zyz.blog.vo.Result;

/**
 * @author zyz
 * @version 1.0
 */
public interface CommentService {

	/**
	 *
	 * 根据文章id查找评论
	 * */
	Result findCommentsByArticleId(Long id);

	/**
	 *发起评论
	 * */
	Result comment(CommentParms commentParms);
}
