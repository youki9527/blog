package com.zyz.blog.vo.Params;

import com.zyz.blog.vo.CategoryVo;
import com.zyz.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class ArticleParms {

	private Long id;

	private ArticleBodyParms body;

	private CategoryVo category;

	private String summary;

	private List<TagVo> tags;

	private String title;
}
