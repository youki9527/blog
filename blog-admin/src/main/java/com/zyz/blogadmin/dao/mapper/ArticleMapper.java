package com.zyz.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.dao.po.Article;

/**
 * @author zyz
 * @version 1.0
 */
public interface ArticleMapper extends BaseMapper<Article> {

	IPage<Article> listArticle(Page<Article> page,
							   String title,
							   Long categoryId,
							   Long createStartDate,
							   Long createEndDate);

}
