package com.zyz.blog.controller;

import com.zyz.blog.common.cache.Cache;
import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.ArticleService;
import com.zyz.blog.vo.Params.ArticleParms;
import com.zyz.blog.vo.Params.PageParams;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */

@RestController
@RequestMapping("articles")
public class ArticleController {


	@Autowired
	private ArticleService articleService;

	/**
	 * 分页文章列表
	 */
	@PostMapping
	@LogAnnotation(module="文章",operator="获取文章列表")
	@Cache(expire = 1*30*1000,name = "listArticle")
	public Result listArticles(@RequestBody PageParams pageParams) {
		Result result = articleService.listArticle(pageParams);
		return result;
	}

	/**
	 * 最热文章
	 */
	@PostMapping("hot")
	@LogAnnotation(module="文章",operator="最热文章")
	@Cache(expire = 5*60*1000,name = "hotArticles")
	public Result hotArticles() {
		//浏览量前5的文章
		int limit = 5;
		Result result = articleService.hotArticle(limit);
		return result;
	}

	/**
	 * 最新文章
	 */
	@PostMapping("new")
	@LogAnnotation(module="文章",operator="最新文章")
	@Cache(expire = 5*60*1000,name = "newArticles")
	public Result newArticles() {
		int limit = 3;
		Result result = articleService.newArticle(limit);
		return result;
	}

	/**
	 * 文章归档
	 */
	@PostMapping("listArchives")
	@LogAnnotation(module="文章",operator="文章归档")
	@Cache(expire = 5*60*1000,name = "listArchives")
	public Result listArchives() {
		Result result = articleService.listArchives();
		return result;
	}

	/**
	 * 根据文章id 查询文章详情 阅读文章
	 */
	@PostMapping("/view/{id}")
	@LogAnnotation(module="文章",operator="根据文章id 查询文章详情")
	@Cache(expire = 5*60*1000,name = "findArticleById")
	public Result findArticleById(@PathVariable("id") Long id) {
		return articleService.findArticleById(id);
	}

	/**
	 * 发布文章
	 * */
	@PostMapping("publish")
	@LogAnnotation(module="文章",operator="发布文章")
	public Result publish(@RequestBody ArticleParms articleParms) {
		return articleService.publish(articleParms);
	}

}
