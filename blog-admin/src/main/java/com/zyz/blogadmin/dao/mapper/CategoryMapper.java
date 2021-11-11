package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.po.Category;
import com.zyz.blogadmin.vo.CategoryVo;

/**
 * @author zyz
 * @version 1.0
 */
public interface CategoryMapper extends BaseMapper<Category> {

	IPage<CategoryVo> listCategory(Page<CategoryVo> page, String categoryName);

}
