package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.po.Tag;
import com.zyz.blogadmin.vo.TagVo;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface TagMapper extends BaseMapper<Tag> {

	List<Tag> findTagsByArticleId(Long articleId);

	IPage<TagVo> listTag(Page<TagVo> page, String tagName);

}
