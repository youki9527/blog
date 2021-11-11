package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.auth.AdminUser;
import com.zyz.blogadmin.dao.mapper.*;
import com.zyz.blogadmin.dao.po.*;
import com.zyz.blogadmin.service.ArticleBodyService;
import com.zyz.blogadmin.service.ArticleService;
import com.zyz.blogadmin.service.TagService;
import com.zyz.blogadmin.util.MyDateUtils;
import com.zyz.blogadmin.vo.*;
import com.zyz.blogadmin.vo.params.ArticlePageParams;
import com.zyz.blogadmin.vo.params.ArticleParams;
import com.zyz.blogadmin.vo.params.UpdateWeightParams;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private ArticleBodyMapper articleBodyMapper;

	@Autowired
	private ArticleBodyService articleBodyService;

	@Autowired
	private ArticleTagMapper articleTagMapper;

	@Autowired
	private CommentMapper commentMapper;


	@Autowired
	private SysAdminMapper sysAdminMapper;

	@Autowired
	private TagService tagService;

	@Override
	public Result listArticle(ArticlePageParams pageParam) {
		Page<Article> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
		//分类没有选择处理
		Long categoryId = Long.valueOf(0);
		if (pageParam.getCategoryId().equals("")) {
			categoryId = null;
		} else {
			categoryId = Long.valueOf(pageParam.getCategoryId());
		}
		//没有开始结束时间处理
		Long startDate = Long.valueOf(0);
		Long endDate = Long.valueOf(0);
		if (pageParam.getCreateStartDate().equals("")) {
			startDate = null;
			endDate = null;
		} else {
			startDate = MyDateUtils.DateStringToLong(pageParam.getCreateStartDate());
			endDate = MyDateUtils.DateStringToLong(pageParam.getCreateEndDate());
		}
		IPage<Article> articleIPage = articleMapper.listArticle(page,
				pageParam.getTitle(),
				categoryId,
				startDate,
				endDate);
		List<Article> articles = articleIPage.getRecords();
		long total = articleIPage.getTotal();
		PageResult<ArticleVo> objectPageResult = new PageResult<>();
		objectPageResult.setList(copyToArticleVoList(articles, false, true, true, true));
		objectPageResult.setTotal(total);
		return Result.success(objectPageResult);
	}


	@Override
	@Transactional
	public Result addArticle(ArticleParams articleParams) {
		//获取当前登录用户管理员信息
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AdminUser principal = (AdminUser) authentication.getPrincipal();
		Long id = principal.getId();
		//插入文章表
		Article article = new Article();
		article.setAuthorId(id);
		article.setViewCounts(0);
		article.setCommentCounts(0);
		article.setWeight(articleParams.getWeight());
		article.setIsPublished(1);
		article.setTitle(articleParams.getTitle());
		article.setSummary(articleParams.getSummary());
		article.setCategoryId(Long.parseLong(articleParams.getCategory().getId()));
		/**
		 * 在linux部署环境需要加8个小时
		 * */
		article.setCreateDate(System.currentTimeMillis()+8*60*60*1000);
		articleMapper.insert(article);
		//文章标签表
		List<TagVo> tags = articleParams.getTags();
		if (tags != null) {
			for (TagVo tag : tags) {
				ArticleTag articleTag = new ArticleTag();
				//文章id -在插入文章表后会生成Id返回到 article对象中
				articleTag.setArticleId(article.getId());
				//标签Id
				articleTag.setTagId(Long.parseLong(tag.getId()));
				articleTagMapper.insert(articleTag);
			}
		}
		//body表
		ArticleBody articleBody = new ArticleBody();
		articleBody.setArticleId(article.getId());
		articleBody.setContent(articleParams.getBody().getContent());
		articleBody.setContentHtml(articleParams.getBody().getContentHtml());
		articleBody.setObjectVersion(1);
		articleBodyMapper.insert(articleBody);
		//更新文章表(bodyId字段)
		Article article1 = new Article();
		article1.setBodyId(articleBody.getId());
		article1.setId(article.getId());
		articleMapper.updateById(article1);
		Map<String, String> map = new HashMap<>();
		map.put("id", article.getId().toString());
		return Result.success(map);
	}


	@Override
	@Transactional
	public Result deleteArticle(Long id) {
		//删除文章表信息
		articleMapper.deleteById(id);
		//删除文章内容表信息
		LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				eq(ArticleBody::getArticleId, id).
				last("limit 1");
		articleBodyMapper.delete(queryWrapper);
		//删除评论表信息
		LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		commentLambdaQueryWrapper.
				eq(Comment::getArticleId, id);
		commentMapper.delete(commentLambdaQueryWrapper);
		//删除标签文章表
		LambdaQueryWrapper<ArticleTag> queryWrapperArticleTag = new LambdaQueryWrapper<>();
		queryWrapperArticleTag.
				eq(ArticleTag::getArticleId, id);
		articleTagMapper.delete(queryWrapperArticleTag);
		return Result.success(null);
	}

	@Override
	@Transactional
	public Result UpdateWeight(UpdateWeightParams updateWeightParams) {
		if (updateWeightParams == null || Strings.isBlank(updateWeightParams.getId())) {
			return Result.fail(301, "参数不合法");
		}
		//查出版本号
		Long id = Long.valueOf(updateWeightParams.getId());
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(Article::getObjectVersion).
				eq(Article::getId, id).
				last("limit 1");
		Article article = articleMapper.selectOne(queryWrapper);
		//更新
		Article newArticle = new Article();
		newArticle.setObjectVersion(article.getObjectVersion() + 1);
		newArticle.setWeight(updateWeightParams.getWeight());
		LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(Article::getId, id);
		int update = articleMapper.update(newArticle, updateWrapper);
		if (update > 0) {
			return Result.success(null);
		}
		return Result.fail(301, "系统异常");
	}

	@Override
	public Result articleDetail(Long id) {
		//查询
		Long articleId=Long.valueOf(id);
		LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.
				select(Article::getId,
						Article::getTitle,
						Article::getSummary,
						Article::getCategoryId,
						Article::getBodyId,
						Article::getWeight).
				eq(Article::getId,articleId).
				last("limit 1");
		Article article = articleMapper.selectOne(queryWrapper);
		return Result.success(copyToArticleVo(article,true,true,true,false));
	}

	@Override
	@Transactional
	public Result updateArticle(ArticleParams articleParams) {
		//查出版本号
		LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.select(Article::getObjectVersion).
				    eq(Article::getId, articleParams.getId()).
				    last("limit 1");
		Article oldArticle = articleMapper.selectOne(queryWrapper);
		//更新文章表
		LambdaUpdateWrapper<Article> updateWrapper=new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(Article::getId, articleParams.getId()).
				eq(Article::getObjectVersion,oldArticle.getObjectVersion());
		Article newArticle = new Article();
		newArticle.setTitle(articleParams.getTitle());
		newArticle.setWeight(articleParams.getWeight());
		newArticle.setSummary(articleParams.getSummary());
		newArticle.setCategoryId(Long.valueOf(articleParams.getCategory().getId()));
		/**
		 * 在linux部署环境需要加8个小时
		 * */
		newArticle.setUpdateDate(System.currentTimeMillis()+8*60*60*1000);
		newArticle.setObjectVersion(oldArticle.getObjectVersion()+1);
		articleMapper.update(newArticle,updateWrapper);
		//更新标签表
		LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper=new LambdaQueryWrapper<>();
		articleTagLambdaQueryWrapper.
				eq(ArticleTag::getArticleId, articleParams.getId());
		articleTagMapper.delete(articleTagLambdaQueryWrapper);
		List<TagVo> tags = articleParams.getTags();
		if (tags != null) {
			for (TagVo tag : tags) {
				ArticleTag articleTag = new ArticleTag();
				//文章id
				articleTag.setArticleId(articleParams.getId());
				//标签Id
				articleTag.setTagId(Long.parseLong(tag.getId()));
				articleTagMapper.insert(articleTag);
			}
		}
		//更新文章体表
		LambdaUpdateWrapper<ArticleBody> articleBodyLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
		articleBodyLambdaUpdateWrapper.
				eq(ArticleBody::getArticleId, articleParams.getId());
		ArticleBody newArticleBody = new ArticleBody();
		newArticleBody.setContent(articleParams.getBody().getContent());
		newArticleBody.setContentHtml(articleParams.getBody().getContentHtml());
		articleBodyMapper.update(newArticleBody,articleBodyLambdaUpdateWrapper);
		return Result.success(null);
	}

	private List<ArticleVo> copyToArticleVoList(List<Article> articles, Boolean isBody, Boolean isCategory, Boolean isTags, Boolean isAuthor) {
		List<ArticleVo> articleVos = new ArrayList<>();
		for (Article article : articles) {
			articleVos.add(copyToArticleVo(article, isBody, isCategory, isTags, isAuthor));
		}
		return articleVos;
	}

	private ArticleVo copyToArticleVo(Article article, Boolean isBody, Boolean isCategory, Boolean isTags, Boolean isAuthor) {

		ArticleVo articleVo = new ArticleVo();
		BeanUtils.copyProperties(article, articleVo);
		//id
		articleVo.setId(String.valueOf(article.getId()));
		//创建时间
		if (article.getCreateDate()!=null){
			articleVo.setCreateDate(MyDateUtils.LongToDateString(article.getCreateDate()));
		}
		//修改时间
		if (article.getUpdateDate() != null) {
			articleVo.setUpdateDate(MyDateUtils.LongToDateString(article.getUpdateDate()));
		}
		//标签ids
		if (isTags) {
			//只需要（TagVoid就行了
			Long articleId = article.getId();
			articleVo.setTags(tagService.findTagByArticleId(articleId));
		}
		//分类id
		if (isCategory) {
			//分类(只需要分类id就可以了)
			CategoryVo categoryVo = new CategoryVo();
			categoryVo.setId(String.valueOf(article.getCategoryId()));
			articleVo.setCategory(categoryVo);
		}
		//作者账号
		if (isAuthor) {
			LambdaQueryWrapper<SysAdmin> queryWrapperAuthor = new LambdaQueryWrapper<>();
			queryWrapperAuthor.
					select(SysAdmin::getUsername).
					eq(SysAdmin::getId, article.getAuthorId()).
					last("limit 1");
			SysAdmin sysAdmin = sysAdminMapper.selectOne(queryWrapperAuthor);
			if (sysAdmin == null) {
				SysAdminVo sysAdminVo = new SysAdminVo();
				sysAdminVo.setUsername("该管理员已删除");
				articleVo.setAuthor(sysAdminVo);
			} else {
				SysAdminVo sysAdminVo = new SysAdminVo();
				sysAdminVo.setUsername(sysAdmin.getUsername());
				articleVo.setAuthor(sysAdminVo);
			}
		}
		//博客内容主体
		if (isBody) {
			Long bodyId = article.getBodyId();
			articleVo.setBody(articleBodyService.findArticleBodyById(bodyId));
		}
		return articleVo;
	}

}
