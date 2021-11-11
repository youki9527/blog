package com.zyz.blogadmin.vo.params;

import com.zyz.blogadmin.vo.CategoryVo;
import com.zyz.blogadmin.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class ArticleParams {

	private Long id;
	/**
	 * 是否置顶
	 * */
    private Integer weight;
	private ArticleBodyParms body;
	private CategoryVo category;
	private String summary;
	private List<TagVo> tags;
	private String title;
}
