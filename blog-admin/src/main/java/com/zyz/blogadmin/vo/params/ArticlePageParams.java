package com.zyz.blogadmin.vo.params;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class ArticlePageParams {
	private Integer currentPage;
	private Integer pageSize;
	private String title;
	private String categoryId;
	private String createStartDate;
	private String createEndDate;
}
