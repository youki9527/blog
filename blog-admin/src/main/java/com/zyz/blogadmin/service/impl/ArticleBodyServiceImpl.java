package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blogadmin.dao.mapper.ArticleBodyMapper;
import com.zyz.blogadmin.dao.po.ArticleBody;
import com.zyz.blogadmin.service.ArticleBodyService;
import com.zyz.blogadmin.vo.ArticleBodyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {

	@Autowired
	private ArticleBodyMapper articleBodyMapper;

	@Override
	public ArticleBodyVo findArticleBodyById(Long id) {
		ArticleBodyVo articleBodyVo = new ArticleBodyVo();
		LambdaQueryWrapper<ArticleBody> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.select(ArticleBody::getContent,ArticleBody::getContentHtml).
				     eq(ArticleBody::getId,id).
				     last("limit 1");
		ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
		articleBodyVo.setContent(articleBody.getContent());
		return articleBodyVo;
	}
}
