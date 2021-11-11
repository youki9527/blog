package com.zyz.blog.vo.Params;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class PageParams {

	private Integer currentPage;

	private Integer pageSize;

	private Long categoryId;

	private Long tagId;

	private String year;

	private String month;

	public String getMonth(){
		if (this.month!=null&&this.month.length()==1){
			return "0"+this.month;
		}
		return this.month;
	}

}
