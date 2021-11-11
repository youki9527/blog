package com.zyz.blogadmin.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class Tag {

	@TableId(type = IdType.AUTO)
	private Long id;
	private String avatar;
	private String tagName;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;
}
