package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.service.CommentService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.CommentPageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("commentList")
	@LogAnnotation(module="评论管理",operator="获取评论列表")
	public Result listComment(@RequestBody CommentPageParams pageParam) {
		return commentService.listComment(pageParam);
	}

	@GetMapping("delete/{id}")
	@LogAnnotation(module="评论管理",operator="删除评论")
	public Result deleteById(@PathVariable("id") Long id){
		return commentService.deleteById(id);
	}

	@PostMapping("delete/commentList")
	@LogAnnotation(module="评论管理",operator="批量删除评论")
	public Result deleteCommentList(@RequestBody List<Long> ids){
		return commentService.deleteCommentList(ids);
	}

}
