package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blog.constants.UserConstant;
import com.zyz.blog.dao.mapper.ArticleMapper;
import com.zyz.blog.dao.po.Comment;
import com.zyz.blog.dao.mapper.CommentMapper;
import com.zyz.blog.dao.po.User;
import com.zyz.blog.service.CommentService;
import com.zyz.blog.service.ThreadService;
import com.zyz.blog.service.UserService;
import com.zyz.blog.util.MyDateUtils;
import com.zyz.blog.util.UserThreadLocal;
import com.zyz.blog.vo.CommentVo;
import com.zyz.blog.vo.Params.CommentParms;
import com.zyz.blog.vo.Result;
import com.zyz.blog.vo.UserVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private ThreadService threadService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public Result findCommentsByArticleId(Long id) {
		//查询所有一级评论
		LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				eq(Comment::getArticleId, id).
				eq(Comment::getLevel, 1).
				orderByDesc(Comment::getCreateDate);
		List<Comment> comments = commentMapper.selectList(queryWrapper);
		List<CommentVo> commentVos = copyList(comments);
		return Result.success(commentVos);
	}

	@Override
	@Transactional
	public Result comment(CommentParms commentParms) {

		//获取当前用户信息
		User user = UserThreadLocal.get();
		if (user.getStatus().equals(UserConstant.BANNED_STATE)) {
			return Result.fail(301, "用户被禁言");
		}

		Comment comment = new Comment();
		comment.setArticleId(commentParms.getArticleId());
		comment.setAuthorId(user.getId());
		comment.setContent(commentParms.getContent());
		/**
		 * 在linux部署环境需要加8个小时
		 * */
		comment.setCreateDate(System.currentTimeMillis()+8*60*60*1000);
		//设置评论等级及父级评论
		if (commentParms.getParent() == null || commentParms.getParent() == 0) {
			comment.setLevel(1);
			comment.setParentId(null);
		} else {
			comment.setLevel(2);
			comment.setParentId(commentParms.getParent());
		}
		//设置给那个用户评论的id
		comment.setToUid(commentParms.getToUserId() == null ? 0 : commentParms.getToUserId());
		//1.插入评论表
		int insert = commentMapper.insert(comment);
		if (insert > 0) {
			//2.更改文章表评论数量在线程池中进行
			threadService.updateArticleCommentCount(articleMapper, commentParms.getArticleId());
		}
		//3.删除redis中对应文章的评论缓存
		String params=String.valueOf(commentParms.getArticleId());
		params = DigestUtils.md5Hex(params);
		String key="comments::CommentController::comments::"+params;
		redisTemplate.delete(key);
		return Result.success(null);
	}


	private List<CommentVo> copyList(List<Comment> comments) {
		List<CommentVo> commentVos = new ArrayList<>();
		for (Comment comment : comments) {
			commentVos.add(copy(comment));
		}
		return commentVos;
	}


	private CommentVo copy(Comment comment) {
		CommentVo commentVo = new CommentVo();
		BeanUtils.copyProperties(comment, commentVo);
		commentVo.setId(String.valueOf(comment.getId()));
		commentVo.setCreateDate(MyDateUtils.LongToDateString(comment.getCreateDate()));
		//评论人游客的信息
		Long authorId = comment.getAuthorId();
		UserVo userVo = userService.findUserVoById(authorId);
		commentVo.setAuthor(userVo);
		//如果子评论
		Integer level = comment.getLevel();
		if (1 == level) {
			Long id = comment.getId();
			List<CommentVo> commentVoList = findCommentsByParentId(id);
			commentVo.setChildrens(commentVoList);
		}
		//如果有父评论
		if (level > 1) {
			Long toUid = comment.getToUid();
			UserVo toUser = userService.findUserVoById(toUid);
			commentVo.setToUser(toUser);
		}
		return commentVo;
	}

	/**
	 * 子评论列表
	 * */
	private List<CommentVo> findCommentsByParentId(Long id) {

		LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				eq(Comment::getParentId, id).
				eq(Comment::getLevel, 2);
		return copyList(commentMapper.selectList(queryWrapper));
	}
}
