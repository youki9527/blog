package com.zyz.blogadmin.service;

import com.zyz.blogadmin.dao.po.Tag;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.TagVo;
import com.zyz.blogadmin.vo.params.PageParam;
import com.zyz.blogadmin.vo.params.TagPageParams;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface TagService {

	Result listTag(TagPageParams pageParam);

	Result addTag(Tag tag);

	Result updateTag(Tag tag);

	Result deleteTag(Long id);

	Result findAll();

	/**
	 * 只需要id就行了
	 * */
	List<TagVo> findTagByArticleId(Long articleId);
}
