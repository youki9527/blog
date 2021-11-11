package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blog.dao.mapper.ArticleBodyMapper;
import com.zyz.blog.dao.po.ArticleBody;
import com.zyz.blog.service.ArticleBodyService;
import com.zyz.blog.vo.ArticleBodyVo;
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
		//前端只需要content字段的信息
		LambdaQueryWrapper<ArticleBody> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.select(ArticleBody::getContent).
				eq(ArticleBody::getId,id).
				last("limit 1");
		ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
		articleBodyVo.setContent(articleBody.getContent());
		return articleBodyVo;
	}
}
