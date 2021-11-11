package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.dao.po.Tag;
import com.zyz.blogadmin.service.TagService;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.PageParam;
import com.zyz.blogadmin.vo.params.TagPageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("tag")
public class TagController {

	@Autowired
	private TagService tagService;

	@PostMapping("categoryList")
	@LogAnnotation(module="标签管理",operator="获取标签列表")
	public Result listTag(@RequestBody TagPageParams pageParam) {
		return tagService.listTag(pageParam);
	}

	@PostMapping("add")
	@LogAnnotation(module="标签管理",operator="添加标签列表")
	public Result addTag(@RequestBody Tag tag) {
		return tagService.addTag(tag);
	}

	@PostMapping("update")
	@LogAnnotation(module="标签管理",operator="更新标签")
	public Result updateTag(@RequestBody Tag tag) {
		return tagService.updateTag(tag);
	}

	@GetMapping("delete/{id}")
	@LogAnnotation(module="标签管理",operator="删除标签")
	public Result deleteTag(@PathVariable("id") Long id) {
		return tagService.deleteTag(id);
	}

	/**
	 * 查询所有标签的id和name
	 * */
	@GetMapping("getAllIdAndName")
	@LogAnnotation(module="标签管理",operator="删除标签")
	public Result tags() {
		return tagService.findAll();
	}
}
