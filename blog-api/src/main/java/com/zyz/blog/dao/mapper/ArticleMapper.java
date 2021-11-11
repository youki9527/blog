package com.zyz.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blog.vo.ArchivesVo;
import com.zyz.blog.dao.po.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface ArticleMapper extends BaseMapper<Article> {


	/**
	 * 查出文章年、月的文章数
	 * */
	List<ArchivesVo> listArchives();

	/**
	 * 分页根据文章分类、标签、年月动态查询文章列表
	 * */
	IPage<Article> listArticle(Page<Article> page,
							   Long categoryId,
							   Long tagId,
							   String year,
							   String month);

	/**
	 * 更新文章评论数量
	 * */
	Integer updateArticleCommentCountById(@Param("id") long articleId,@Param("objectVersion") Integer objectVersion);
}
