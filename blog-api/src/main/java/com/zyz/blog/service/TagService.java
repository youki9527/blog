package com.zyz.blog.service;

import com.zyz.blog.vo.Result;
import com.zyz.blog.vo.TagVo;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface TagService {

	/**
	 *根据文章Id获取文章标签
	 * */
	List<TagVo> findTagByArticleId(Long articleId);

	/**
	 * 获取最热标签
	 * */
	Result hots(int limit);

	/**
	 * 查找所有标签的id和name
	 * */
	Result findAll();

	/**
	 * 查找所有标签的所有信息和对应标签的文章数量
	 * */
	Result findAllDetail();

	/**
	 * 根据id查找标签的所有信息和该标签的文章数量
	 * */
	Result findTagsDetailById(Long id);
}
