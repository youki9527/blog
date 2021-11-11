package com.zyz.blog.controller;

import com.zyz.blog.common.cache.Cache;
import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.CategoryService;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@RestController()
@RequestMapping("categorys")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 查找所有文章分类的id和name
	 * */
	@GetMapping
	@LogAnnotation(module="分类",operator="查找所有文章分类的id和name")
	@Cache(expire = 5*60*1000,name = "categories")
	public Result categories() {
		return categoryService.findAll();
	}

	/**
	 * 查询所有文章分类的全部消息和对应分类的文章总数
	 * */
	@LogAnnotation(module="分类",operator="查询所有文章分类的全部消息")
	@Cache(expire = 5*60*1000,name = "categoriesDetail")
	@GetMapping("detail")
	public Result categoriesDetail() {
		return categoryService.findAllDetail();
	}

	/**
	 * 根据分类id查询文章分类的全部消息和该分类的文章总数
	 * */
	@GetMapping("detail/{id}")
	@LogAnnotation(module="分类",operator="查询文章分类的全部消息根据id")
	@Cache(expire = 5*60*1000,name = "categoriesDetailById")
	public Result  categoriesDetailById(@PathVariable("id") Long id) {
		return categoryService.findCategoryDetailById(id);
	}
}
