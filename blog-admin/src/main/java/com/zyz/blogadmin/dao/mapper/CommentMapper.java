package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.po.Article;
import com.zyz.blogadmin.dao.po.Comment;

/**
 * @author zyz
 * @version 1.0
 */
public interface CommentMapper extends BaseMapper<Comment> {

	IPage<Comment> listComment(Page<Comment> page,
							   String articleTitle,
							   String authorAccount);


}
