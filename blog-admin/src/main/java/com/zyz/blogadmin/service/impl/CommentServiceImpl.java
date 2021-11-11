package com.zyz.blogadmin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.mapper.ArticleMapper;
import com.zyz.blogadmin.dao.mapper.CommentMapper;
import com.zyz.blogadmin.dao.mapper.UserMapper;
import com.zyz.blogadmin.dao.po.Article;
import com.zyz.blogadmin.dao.po.Comment;
import com.zyz.blogadmin.dao.po.User;
import com.zyz.blogadmin.service.CommentService;
import com.zyz.blogadmin.util.MyDateUtils;
import com.zyz.blogadmin.vo.CommentVo;
import com.zyz.blogadmin.vo.PageResult;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.CommentPageParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	private UserMapper userMapper;

	@Override
	public Result listComment(CommentPageParams commentPageParams) {

		Page<Comment> page = new Page<>(commentPageParams.getCurrentPage(), commentPageParams.getPageSize());
		IPage<Comment> commentIPage = commentMapper.listComment(page,
				commentPageParams.getArticleTitle(),
				commentPageParams.getAuthorAccount());
		List<Comment> comments = commentIPage.getRecords();
		long total = commentIPage.getTotal();

		PageResult<CommentVo> objectPageResult = new PageResult<>();
		objectPageResult.setList(copyToCommentVoList(comments));
		objectPageResult.setTotal(total);
		return Result.success(objectPageResult);
	}

	@Override
	public Result deleteById(Long id) {
		int i = commentMapper.deleteById(id);
		if (i>0){
			return Result.success(null);
		}
		return Result.fail(301,"系统异常");

	}

	@Override
	@Transactional
	public Result deleteCommentList(List<Long> ids) {
		commentMapper.deleteBatchIds(ids);
		return Result.success(null);
	}


	private List<CommentVo> copyToCommentVoList(List<Comment> comments) {
		List<CommentVo> commentVos = new ArrayList();
		for (Comment comment : comments) {
			commentVos.add(copyToCommentVo(comment));
		}
		return commentVos;
	}

	private CommentVo copyToCommentVo(Comment comment) {
		CommentVo commentVo = new CommentVo();
		//id
		commentVo.setId(String.valueOf(comment.getId()));
		BeanUtils.copyProperties(comment, commentVo);
		//评论时间
		commentVo.setCreateDate(MyDateUtils.LongToDateString(comment.getCreateDate()));
		//文章标题
		Long articleId = comment.getArticleId();
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(Article::getTitle).
				eq(Article::getId, articleId).
				last("limit 1");
		Article article = articleMapper.selectOne(queryWrapper);
		commentVo.setArticleTitle(article.getTitle());
		//评论人账号
		Long authorId = comment.getAuthorId();
		LambdaQueryWrapper<User> queryWrapperUser = new LambdaQueryWrapper<>();
		queryWrapperUser.
				select(User::getAccount, User::getNickname).
				eq(User::getId, authorId).
				last("limit 1");
		User user = userMapper.selectOne(queryWrapperUser);
		if (user == null) {
			commentVo.setAuthorAccount("已删除");
			commentVo.setAuthorNickName("已删除");
			return commentVo;

		}
		commentVo.setAuthorAccount(user.getAccount());
		commentVo.setAuthorNickName(user.getNickname());
		return commentVo;
	}
}
