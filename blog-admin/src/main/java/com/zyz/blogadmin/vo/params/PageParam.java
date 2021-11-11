package com.zyz.blogadmin.vo.params;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class PageParam {

	private Integer currentPage;

	private Integer pageSize;

	private String queryString;
}
