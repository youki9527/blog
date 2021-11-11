package com.zyz.blogadmin.service;

import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.ArticlePageParams;
import com.zyz.blogadmin.vo.params.ArticleParams;
import com.zyz.blogadmin.vo.params.UpdateWeightParams;

/**
 * @author zyz
 * @version 1.0
 */
public interface ArticleService {

	Result listArticle(ArticlePageParams pageParam);

	Result addArticle(ArticleParams article);



	Result deleteArticle(Long id);

	Result UpdateWeight(UpdateWeightParams updateWeightParams);

	/**
	 * 根据id查询文章
	 * */
	Result articleDetail(Long id);

	Result updateArticle(ArticleParams articleParams);
}
