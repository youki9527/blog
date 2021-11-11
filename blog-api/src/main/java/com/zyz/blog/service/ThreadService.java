package com.zyz.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyz.blog.dao.mapper.ArticleMapper;
import com.zyz.blog.dao.mapper.UserMapper;
import com.zyz.blog.dao.po.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zyz
 * @version 1.0
 */
@Component
public class ThreadService {


	/**
	 * 期待更新操作在线程池 不会影响到主线程
	 * */
	@Async("taskExecutor")
	public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
		//设置要更新的字段的值封装为对应的po对象 文章数版本号加一
		Article newArticle = new Article();
		newArticle.setViewCounts(article.getViewCounts()+1);
		newArticle.setViewCounts(article.getObjectVersion()+1);
		//更新条件
		LambdaUpdateWrapper<Article> updateWrapper=new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(Article::getId,article.getId()).
				eq(Article::getObjectVersion,article.getObjectVersion());
		//更新操作
		articleMapper.update(newArticle,updateWrapper);
	}

	/**
	 * 期待更新操作在线程池 不会影响到主线程
	 * */
	@Async("taskExecutor")
	public void updateLastLoginByAccount(String account, UserMapper userMapper) {
		/**
		 * 在linux部署环境需要加8个小时
		 * */
		Long time=System.currentTimeMillis()+8*60*60*1000;
		userMapper.updateLastLoginByAccount(account,time);
	}

	/**
	 * 期待更新操作在线程池 不会影响到主线程
	 * */
	@Async("taskExecutor")
	public void updateArticleCommentCount(ArticleMapper articleMapper, Long id) {
		//查询到版本号
		LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.
				select(Article::getObjectVersion).
				eq(Article::getId,id).
				last("limit 1");
		//查询到版本号
		Article article = articleMapper.selectOne(queryWrapper);
		//更新
		articleMapper.updateArticleCommentCountById(id,article.getObjectVersion());
	}
}
