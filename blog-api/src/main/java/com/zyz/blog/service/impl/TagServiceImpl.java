package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blog.dao.mapper.TagMapper;
import com.zyz.blog.dao.po.Tag;
import com.zyz.blog.service.TagService;
import com.zyz.blog.vo.Result;
import com.zyz.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class TagServiceImpl implements TagService {


	@Autowired
	private TagMapper tagMapper;

	@Override
	public List<TagVo> findTagByArticleId(Long articleId) {
		List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
		return copyToTagVoList(tags);
	}


	@Override
	public Result hots(int limit) {
		//先找到最热标签的Id集合
		List<Long> tagIds = tagMapper.findHotsTagIds(limit);
		if (CollectionUtils.isEmpty(tagIds)){
			return Result.success(Collections.emptyList());
		}
		//更加最热标签id集合找出标签详细内容
		List<Tag> tags=tagMapper.findTagsByIds(tagIds);
		return Result.success(tags);
	}

	@Override
	public Result findAll() {
		LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.select(Tag::getId,Tag::getTagName);
		List<Tag> tags = tagMapper.selectList(queryWrapper);
		return Result.success(copyToTagVoList(tags));
	}

	@Override
	public Result findAllDetail() {
		List<TagVo> tags = tagMapper.findAllTagAndArticleNumber();
		return Result.success(tags);
	}

	@Override
	public Result findTagsDetailById(Long id) {
		TagVo tag = tagMapper.findTagAndArticleNumberById(id);
		return Result.success(tag);
	}

	private List<TagVo> copyToTagVoList(List<Tag> tags) {
		List<TagVo> tagVos = new ArrayList<>();
		for (Tag tag : tags) {
			tagVos.add(copyToTagVo(tag));

		}
		return tagVos;

	}

	private TagVo copyToTagVo(Tag tag) {

		TagVo tagVo = new TagVo();
		BeanUtils.copyProperties(tag, tagVo);
		tagVo.setId(String.valueOf(tag.getId()));
		return tagVo;
	}

}
