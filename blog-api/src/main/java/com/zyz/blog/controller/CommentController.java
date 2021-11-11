package com.zyz.blog.controller;

import com.zyz.blog.common.cache.Cache;
import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.CommentService;
import com.zyz.blog.vo.Params.CommentParms;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("comments")
public class CommentController {

	@Autowired
	private CommentService commentService;
	/**
	 * 获取文章评论列表
	 */
	@GetMapping("article/{id}")
	@LogAnnotation(module="评论",operator="获取文章评论列表")
	@Cache(expire = 5*60*1000,name = "comments")
	public Result comments(@PathVariable("id") Long id) {
		return commentService.findCommentsByArticleId(id);
	}
	/**
	 * 评论
	 */
	@PostMapping("create/change")
	@LogAnnotation(module="评论",operator="添加评论")
	public Result Comment(@RequestBody CommentParms commentParms) {
		return commentService.comment(commentParms);
	}

}
