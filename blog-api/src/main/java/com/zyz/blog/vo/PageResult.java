package com.zyz.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class PageResult<T> {

	private List<T> list;

	private Long total;
}
