package com.zyz.blog.controller;

import com.zyz.blog.common.cache.Cache;
import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.service.TagService;
import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("tags")
public class TagsController {

	@Autowired
	private TagService tagService;

	@GetMapping("hot")
	@LogAnnotation(module = "标签", operator="最热标签")
	@Cache(expire = 5*60*1000,name = "hotTags")
	public Result hotTags() {
		int limit=6;
		Result result = tagService.hots(limit);
		return result;
	}

	/**
	 * 查询所有标签的id和name
	 * */
	@LogAnnotation(module = "标签", operator="查询所有标签的id和name")
	@Cache(expire = 5*60*1000,name = "tagsIDName")
	@GetMapping
	public Result tagsIDName() {
		return tagService.findAll();
	}

	/**
	 *  查询所有标签的全部信息和对应标签的文章数量
	 * */
	@GetMapping("detail")
	@LogAnnotation(module = "标签", operator="查询所有标签的全部信息")
	@Cache(expire = 5*60*1000,name = "tagsDetail")
	public Result tagsDetail() {
		return tagService.findAllDetail();
	}

	/**
	 *  根据id查询标签的全部信息和该标签的文章数量
	 * */
	@GetMapping("detail/{id}")
	@LogAnnotation(module = "标签", operator="根据id查询标签的全部信息")
	@Cache(expire = 5*60*1000,name = "tagsDetailById")
	public Result tagsDetailById(@PathVariable("id") Long id) {
		return tagService.findTagsDetailById(id);
	}

}
