package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blog.dao.mapper.CategoryMapper;
import com.zyz.blog.dao.po.Category;
import com.zyz.blog.service.CategoryService;
import com.zyz.blog.vo.CategoryVo;

import com.zyz.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public CategoryVo findCategoryById(Long id) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.
				select(Category::getId,Category::getCategoryName).
				eq(Category::getId,id).
				last("limit 1");
		Category category = categoryMapper.selectOne(queryWrapper);
		if (category==null){
			CategoryVo categoryVo = new CategoryVo();
			categoryVo.setCategoryName("分类已删除");
			categoryVo.setId("0");
			return  categoryVo;
		}else {
			CategoryVo categoryVo = new CategoryVo();
			categoryVo.setCategoryName(category.getCategoryName());
			categoryVo.setId(String.valueOf(category.getId()));
			return categoryVo;
		}

	}

	@Override
	public Result findAll() {
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
		//只需要id和name
		queryWrapper.
				select(Category::getId,Category::getCategoryName);
		List<Category> categories = categoryMapper.selectList(queryWrapper);
		return Result.success(copylist(categories));
	}

	@Override
	public Result findAllDetail() {
		List<CategoryVo> allCategoryAndArticleNumber = categoryMapper.findAllCategoryAndArticleNumber();
		return Result.success(allCategoryAndArticleNumber);
	}

	@Override
	public Result findCategoryDetailById(Long id) {
		CategoryVo category = categoryMapper.findCategoryAndArticleNumberById(id);
		return Result.success(category);
	}


	private List<CategoryVo> copylist(List<Category> categories) {

		List<CategoryVo> categoryVos = new ArrayList<>();
		for (Category category : categories) {
			categoryVos.add(copy(category,true));
		}
		return categoryVos;
	}

	private CategoryVo copy(Category category,boolean isCount) {
		CategoryVo categoryVo = new CategoryVo();
		BeanUtils.copyProperties(category, categoryVo);
		categoryVo.setId(String.valueOf(category.getId()));
		if (isCount==true){

		}
		return categoryVo;
	}
}
