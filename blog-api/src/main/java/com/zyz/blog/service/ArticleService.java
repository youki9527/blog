package com.zyz.blog.service;

import com.zyz.blog.vo.Params.ArticleParms;
import com.zyz.blog.vo.Params.PageParams;
import com.zyz.blog.vo.Result;

/**
 * @author zyz
 * @version 1.0
 */
public interface ArticleService {


	/**
	 * 分页查询文章
	 * */
	Result listArticle(PageParams pageParams);

	/**
	 * 最热文章
	 * */
	Result hotArticle(int limit);

	/**
	 * 最新文章
	 * */
	Result newArticle(int limit);

	/**
	*文章归档
	* */
	Result listArchives();

	/**
	 *
	 * 根据文章id查找文章详情
	 * */
	Result findArticleById(Long id);

	/**
	 * 发布文章
	 * */
	Result publish(ArticleParms articleParms);

}
