package com.zyz.blogadmin.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class SysAdmin {

	@TableId(type = IdType.AUTO)
	private Long id;
	private String username;
	private String password;
	private String nickname;
	private String avatar;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;
}
