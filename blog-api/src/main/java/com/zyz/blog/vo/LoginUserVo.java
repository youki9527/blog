package com.zyz.blog.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class LoginUserVo {


	// @JsonSerialize(using = ToStringSerializer.class)
	private String id;

	private String account;

	private String nickname;

	private String avatar;

}
