package com.zyz.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyz.blog.dao.po.Category;
import com.zyz.blog.vo.CategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface CategoryMapper extends BaseMapper<Category> {

	/**
	 * 查询所有分类详细信息和该分类的文章总数
	 * */
	List<CategoryVo> findAllCategoryAndArticleNumber();

	/**
	 * 根据id查询所有分类详细信息和该分类的文章总数
	 * */
	CategoryVo findCategoryAndArticleNumberById(@Param("id")Long id);
}
