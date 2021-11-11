package com.zyz.blog.service;

import com.zyz.blog.vo.CategoryVo;
import com.zyz.blog.vo.Result;

/**
 * @author zyz
 * @version 1.0
 */
public interface CategoryService {

	/**
	 * 根据分类id查找文章分类(分类id和name)
	 * */
	CategoryVo findCategoryById(Long id);

	/**
	 * 查询所有文章分类的id和name
	 * */
	Result findAll();

	/**
	 * 查询所有文章分类的全部信息和文章总数
	 * */
	Result findAllDetail();

	/**
	 *根据id文章分类的全部信息和文章总数
	 * */
	Result findCategoryDetailById(Long id);
}
