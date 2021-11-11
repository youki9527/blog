package com.zyz.blog.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class Category {

	@TableId(type = IdType.AUTO)
	private Long id;
	private String avatar;
	private String categoryName;
	private String description;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;

}
