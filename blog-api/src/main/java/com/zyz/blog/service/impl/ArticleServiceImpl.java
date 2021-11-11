package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blog.dao.po.*;
import com.zyz.blog.vo.*;
import com.zyz.blog.dao.mapper.ArticleBodyMapper;
import com.zyz.blog.dao.mapper.ArticleMapper;
import com.zyz.blog.dao.mapper.ArticleTagMapper;
import com.zyz.blog.service.*;
import com.zyz.blog.util.MyDateUtils;
import com.zyz.blog.util.UserThreadLocal;
import com.zyz.blog.vo.Params.ArticleParms;
import com.zyz.blog.vo.Params.PageParams;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	private ArticleTagMapper articleTagMapper;

	@Autowired
	private ArticleBodyMapper articleBodyMapper;

	@Autowired
	private TagService tagService;

	@Autowired
	private UserService userService;

	@Autowired
	private SysAdminService sysAdminService;

	@Autowired
	private ArticleBodyService articleBodyService;

	@Autowired
	private ThreadService threadService;

	@Autowired
	private CategoryService categoryService;

	@Override
	public Result listArticle(PageParams pageParams) {
		Page<Article> page = new Page<>(pageParams.getCurrentPage(), pageParams.getPageSize());
		IPage<Article> articleIPage = articleMapper.listArticle(page,
				pageParams.getCategoryId(),
				pageParams.getTagId(),
				pageParams.getYear(),
				pageParams.getMonth());
		List<Article> records = articleIPage.getRecords();
		PageResult<ArticleVo> articlePageResult = new PageResult<>();
		articlePageResult.setList(copyList(records, true, true, false, true));
		articlePageResult.setTotal(articleIPage.getTotal());
		return Result.success(articlePageResult);

	}

	@Override
	public Result hotArticle(int limit) {
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(Article::getId, Article::getTitle).
				orderByDesc(Article::getViewCounts).
				last("limit " + limit);
		List<Article> articles = articleMapper.selectList(queryWrapper);
		return Result.success(copyList(articles, false, false, false, false));
	}

	@Override
	public Result newArticle(int limit) {
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(Article::getId, Article::getTitle).
				orderByDesc(Article::getCreateDate).
				last("limit " + limit);
		List<Article> articles = articleMapper.selectList(queryWrapper);
		return Result.success(copyList(articles, false, false, false, false));

	}

	@Override
	public Result listArchives() {
		List<ArchivesVo> articles = articleMapper.listArchives();
		return Result.success(articles);
	}

	@Override
	public Result findArticleById(Long id) {

		Article article = articleMapper.selectById(id);
		ArticleVo ArticleVo = copy(article, true, true, true, true);
		/**
		 * 更新阅读数量 在线程池中进行
		 * */
		threadService.updateArticleViewCount(articleMapper, article);
		return Result.success(ArticleVo);
	}

	@Override
	@Transactional
	public Result publish(ArticleParms articleParms) {
		//获取当前用户信息
		User user = UserThreadLocal.get();
		Article article = new Article();
		//插入文章表
		article.setAuthorId(user.getId());
		article.setWeight(Article.Article_Common);
		article.setViewCounts(0);
		article.setCommentCounts(0);
		article.setTitle(articleParms.getTitle());
		article.setSummary(articleParms.getSummary());
		article.setCategoryId(Long.parseLong(articleParms.getCategory().getId()));
		article.setCreateDate(System.currentTimeMillis());
		article.setUpdateDate(System.currentTimeMillis());
		articleMapper.insert(article);
		//文章标签表
		List<TagVo> tags = articleParms.getTags();
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
		articleBody.setContent(articleParms.getBody().getContent());
		articleBody.setContentHtml(articleParms.getBody().getContentHtml());
		articleBodyMapper.insert(articleBody);
		//更新文章表 将bodyid
		article.setBodyId(articleBody.getId());
		articleMapper.updateById(article);
		Map<String, String> map = new HashMap<>();
		map.put("id", article.getId().toString());
		return Result.success(map);
	}

	/**
	 * 将Articlelist转换为ArticleVolist
	 */
	private List<ArticleVo> copyList(List<Article> articles, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
		List<ArticleVo> articleVos = new ArrayList<>();
		for (Article article : articles) {
			articleVos.add(copy(article, isTag, isAuthor, isBody, isCategory));
		}
		return articleVos;
	}

	/**
	 * 将相同属性的 Article转换为ArticleVo
	 */
	private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
		ArticleVo articleVo = new ArticleVo();
		BeanUtils.copyProperties(article, articleVo);
		articleVo.setId(String.valueOf(article.getId()));
		if (article.getUpdateDate() != null) {
			articleVo.setCreateDate(MyDateUtils.LongToDateString(article.getUpdateDate()));
		}
		if (article.getCreateDate() != null) {
			articleVo.setCreateDate(MyDateUtils.LongToDateString(article.getCreateDate()));
		}
		if (isTag) {
			Long articleId = article.getId();
			articleVo.setTags(tagService.findTagByArticleId(articleId));
		}
		if (isCategory) {
			Long categoryId = article.getCategoryId();
			articleVo.setCategory(categoryService.findCategoryById(categoryId));
		}
		if (isAuthor) {
			Long authorId = article.getAuthorId();
			SysAdmin author = sysAdminService.findSysAuthorByID(authorId);
			if (author == null) {
				SysAdminVo sysAdminVo = new SysAdminVo();
				sysAdminVo.setNickname("用户已删除");
				articleVo.setAuthor(sysAdminVo);
			} else {
				//构建作者头像和昵称信息
				SysAdminVo authorVo = new SysAdminVo();
				authorVo.setNickname(author.getNickname());
				authorVo.setAvatar(author.getAvatar());
				articleVo.setAuthor(authorVo);
			}
		}
		if (isBody) {
			Long bodyId = article.getBodyId();
			articleVo.setBody(articleBodyService.findArticleBodyById(bodyId));
		}

		return articleVo;
	}
}
