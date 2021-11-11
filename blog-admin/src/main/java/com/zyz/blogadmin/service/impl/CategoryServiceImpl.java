package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.mapper.CategoryMapper;
import com.zyz.blogadmin.dao.po.Category;
import com.zyz.blogadmin.service.CategoryService;
import com.zyz.blogadmin.vo.CategoryVo;
import com.zyz.blogadmin.vo.PageResult;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.TagVo;
import com.zyz.blogadmin.vo.params.CategoryPageParams;
import com.zyz.blogadmin.vo.params.PageParam;
import org.apache.commons.lang3.StringUtils;
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
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public Result listCategory(CategoryPageParams pageParam) {

		Page<CategoryVo> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
		IPage<CategoryVo> tagIPage = categoryMapper.listCategory(page, pageParam.getCategoryName());
		List<CategoryVo> categoryVos = tagIPage.getRecords();
		long total =  tagIPage.getTotal();
		//返回结果封装
		PageResult<CategoryVo> objectPageResult = new PageResult<>();
		objectPageResult.setList(categoryVos);
		objectPageResult.setTotal(total);
		return Result.success(objectPageResult);
	}

	@Override
	@Transactional
	public Result addCategory(Category category) {

		if (category==null||category.getCategoryName()==null){
			return Result.fail(301,"参数不能为null");
		}
		//1.查询是否已经存在
		LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.
				select(Category::getCategoryName).
				eq(Category::getCategoryName,category.getCategoryName()).
				last("limit 1");
		Category category1 = categoryMapper.selectOne(queryWrapper);
		if (category1!=null){
			return Result.fail(302,"分类名称已存在");
		}
		int insert = categoryMapper.insert(category);
		if (insert>0){
			return Result.success(null);
		}else {
			return Result.fail(303,"系统错误");
		}
	}

	@Override
	@Transactional
	public Result updateCategory(Category category) {

		if (category==null||category.getCategoryName()==null){
			return Result.fail(301,"参数不能为null");
		}
		// //查看是否已经存在
		// LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		// queryWrapper.
		// 		select(Category::getCategoryName).
		// 		eq(Category::getCategoryName, category.getCategoryName()).
		// 		last("limit 1");
		// Category category1= categoryMapper.selectOne(queryWrapper);
		// if (category1!=null){
		// 	return Result.fail(302,"已经存在相同的标签");
		// }
		//1.修改前查出版本号
		LambdaQueryWrapper<Category> queryWrapperVersion=new LambdaQueryWrapper<>();
		queryWrapperVersion.
				select(Category::getObjectVersion).
				eq(Category::getId,category.getId()).
				last("limit 1");
		Category oldTagVersionCategory = categoryMapper.selectOne(queryWrapperVersion);
		//2.更新条件
		LambdaUpdateWrapper<Category> updateWrapper=new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(Category::getId,category.getId()).
				eq(Category::getObjectVersion,oldTagVersionCategory.getObjectVersion());
		//更新版本号+1
		category.setObjectVersion(oldTagVersionCategory.getObjectVersion()+1);
		//更新
		int update= categoryMapper.update(category, updateWrapper);
		if (update>0){
			return Result.success(null);
		}else {
			return Result.fail(303,"更新失败");
		}

	}

	@Override
	@Transactional
	public Result deleteCategory(Long id) {
		categoryMapper.deleteById(id);
		return Result.success(null);
	}

	@Override
	public Result findAll() {
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
		//只需要id和name
		queryWrapper.select(Category::getId,Category::getCategoryName);
		List<Category> categories = categoryMapper.selectList(queryWrapper);
		return Result.success(copylist(categories));
	}
	private List<CategoryVo> copylist(List<Category> categories) {
		List<CategoryVo> categoryVos = new ArrayList<>();
		for (Category category : categories) {
			categoryVos.add(copy(category));
		}
		return categoryVos;
	}

	private CategoryVo copy(Category category) {
		CategoryVo categoryVo = new CategoryVo();
		BeanUtils.copyProperties(category, categoryVo);
		categoryVo.setId(String.valueOf(category.getId()));
		return categoryVo;
	}
}
