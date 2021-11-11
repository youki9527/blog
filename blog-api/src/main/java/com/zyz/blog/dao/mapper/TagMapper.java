package com.zyz.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyz.blog.dao.po.Tag;
import com.zyz.blog.vo.TagVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */

public interface TagMapper extends BaseMapper<Tag> {


	List<Tag> findTagsByArticleId(Long articleId);

	List<Long> findHotsTagIds(int limit);

	List<Tag> findTagsByIds(List<Long> tagIds);

	/**
	 * 查询所有标签详细信息和该标签的文章总数
	 * */
	List<TagVo> findAllTagAndArticleNumber();

	/**
	 * 根据查询标签详细信息和该标签的文章总数
	 * */
	TagVo findTagAndArticleNumberById(@Param("id") Long id);
}
