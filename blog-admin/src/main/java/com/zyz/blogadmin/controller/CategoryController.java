package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.dao.po.Category;
import com.zyz.blogadmin.service.CategoryService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.CategoryPageParams;
import com.zyz.blogadmin.vo.params.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("categoryList")
	@LogAnnotation(module="分类管理",operator="分类列表")
	public Result listCategory(@RequestBody CategoryPageParams pageParam) {
		return categoryService.listCategory(pageParam);
	}

	@PostMapping("add")
	@LogAnnotation(module="分类管理",operator="添加分类")
	public Result addCategory(@RequestBody Category category) {
		return categoryService.addCategory(category);
	}

	@PostMapping("update")
	@LogAnnotation(module="分类管理",operator="修改分类")
	public Result updateCategory(@RequestBody Category category) {
		return categoryService.updateCategory(category);
	}

	@GetMapping("delete/{id}")
	@LogAnnotation(module="分类管理",operator="删除分类")
	public Result deleteCategory(@PathVariable("id") Long id) {
		return categoryService.deleteCategory(id);
	}

	/**
	 * 查找所有文章分类的id和name
	 * */
	@GetMapping("getAllIdAndName")
	@LogAnnotation(module="分类管理",operator="查找所有文章分类的id和name")
	public Result categories() {
		return categoryService.findAll();
	}

}
