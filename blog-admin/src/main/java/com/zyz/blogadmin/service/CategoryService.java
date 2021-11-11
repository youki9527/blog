package com.zyz.blogadmin.service;

import com.zyz.blogadmin.dao.po.Category;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.CategoryPageParams;
import com.zyz.blogadmin.vo.params.PageParam;

/**
 * @author zyz
 * @version 1.0
 */
public interface CategoryService {

	Result listCategory(CategoryPageParams pageParam);

	Result addCategory(Category category);

	Result updateCategory(Category category);

	Result deleteCategory(Long id);

	Result findAll();
}
