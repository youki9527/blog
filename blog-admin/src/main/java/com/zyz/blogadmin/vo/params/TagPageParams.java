package com.zyz.blogadmin.vo.params;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class TagPageParams {
	private Integer currentPage;
	private Integer pageSize;
	private String tagName;

}
