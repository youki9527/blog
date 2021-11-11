package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.service.ArticleService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.ArticlePageParams;
import com.zyz.blogadmin.vo.params.ArticleParams;
import com.zyz.blogadmin.vo.params.UpdateWeightParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("article")
public class ArticleController {


	@Autowired
	private ArticleService articleService;

	@PostMapping("articleList")
	@LogAnnotation(module="文章管理",operator="文章列表")
	public Result listArticle(@RequestBody ArticlePageParams pageParam) {
		return articleService.listArticle(pageParam);
	}

	@GetMapping("detail/{id}")
	@LogAnnotation(module="文章管理",operator="删除文章")
	public Result articleDetail(@PathVariable("id") Long id) {
		return articleService.articleDetail(id);
	}

	@PostMapping("add")
	@LogAnnotation(module="文章管理",operator="写文章")
	public Result addArticle(@RequestBody ArticleParams articleParams) {
		return articleService.addArticle(articleParams);
	}

	@PostMapping("update")
	@LogAnnotation(module="文章管理",operator="更新文章")
	public Result updateCategory(@RequestBody ArticleParams articleParams) {
		return articleService.updateArticle(articleParams);
	}


	/**
	 * 更新置顶信息
	 */
	@PostMapping("updateweight")
	public Result updateWeight(@RequestBody UpdateWeightParams updateWeightParams) {
		return articleService.UpdateWeight(updateWeightParams);
	}

	@GetMapping("delete/{id}")
	public Result deleteArticle(@PathVariable("id") Long id) {
		return articleService.deleteArticle(id);
	}

}
