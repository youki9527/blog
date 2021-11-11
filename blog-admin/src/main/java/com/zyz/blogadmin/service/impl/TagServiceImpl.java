package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.mapper.TagMapper;
import com.zyz.blogadmin.dao.po.Comment;
import com.zyz.blogadmin.dao.po.Tag;
import com.zyz.blogadmin.service.TagService;
import com.zyz.blogadmin.vo.CommentVo;
import com.zyz.blogadmin.vo.PageResult;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.TagVo;
import com.zyz.blogadmin.vo.params.PageParam;
import com.zyz.blogadmin.vo.params.TagPageParams;
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
public class TagServiceImpl implements TagService {

	@Autowired
	private TagMapper tagMapper;

	@Override
	public Result listTag(TagPageParams pageParam) {
		Page<TagVo> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
		IPage<TagVo> tagIPage = tagMapper.listTag(page, pageParam.getTagName());
		List<TagVo> tags = tagIPage.getRecords();
		long total =  tagIPage.getTotal();
		PageResult<TagVo> objectPageResult = new PageResult<>();
		objectPageResult.setList(tags);
		objectPageResult.setTotal(total);
		return Result.success(objectPageResult);
	}

	@Override
	@Transactional
	public Result addTag(Tag tag) {
		if (tag==null||tag.getTagName()==null){
			return Result.fail(301,"参数不能为null");
		}
		//查看是否已经存在相同的标签名
		LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				    select(Tag::getTagName).
					eq(Tag::getTagName, tag.getTagName()).
				    last("limit 1");
		Tag tag1 = tagMapper.selectOne(queryWrapper);
		if (tag1!=null){
			return Result.fail(302,"已经存在相同的标签");
		}
		int insert = tagMapper.insert(tag);
		if (insert>0){
			return Result.success(null);
		}
		else {
			return Result.fail(303,"插入失败");
		}

	}

	@Override
	@Transactional
	public Result updateTag(Tag tag) {

		if (tag==null||tag.getTagName()==null){
			return Result.fail(301,"参数不能为null");
		}
		// //查看是否已经存在
		// LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
		// queryWrapper.
		// 		select(Tag::getTagName).
		// 		eq(Tag::getTagName, tag.getTagName()).
		// 		last("limit 1");
		// Tag tag1 = tagMapper.selectOne(queryWrapper);
		// if (tag1!=null){
		// 	return Result.fail(302,"已经存在相同的标签");
		// }
		//1.修改前查出版本号
		LambdaQueryWrapper<Tag> queryWrapperVersion=new LambdaQueryWrapper<>();
		queryWrapperVersion.
				select(Tag::getObjectVersion).
				eq(Tag::getId,tag.getId()).
				last("limit 1");
		Tag oldTagVersionTag = tagMapper.selectOne(queryWrapperVersion);
		//2.更新条件
		LambdaUpdateWrapper<Tag> updateWrapper=new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(Tag::getId,tag.getId()).
				eq(Tag::getObjectVersion,oldTagVersionTag.getObjectVersion());
		//更新子端版本号+1
		tag.setObjectVersion(oldTagVersionTag.getObjectVersion()+1);
		//更新
		int update = tagMapper.update(tag, updateWrapper);
		if (update>0){
			return Result.success(null);
		}else {
			return Result.fail(303,"更新失败");
		}

	}

	@Override
	@Transactional
	public Result deleteTag(Long id) {
		tagMapper.deleteById(id);
		return Result.success(null);
	}


	@Override
	public Result findAll() {
		LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(Tag::getId, Tag::getTagName);
		List<Tag> tags = tagMapper.selectList(queryWrapper);
		return Result.success(copyToTagVoList(tags));
	}

	@Override
	public List<TagVo> findTagByArticleId(Long articleId) {
		List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
		return copyToTagVoList(tags);
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
