package com.zyz.blog.dao.po;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class User {

	private Long id;
	private String account;
	private Integer admin;
	private String avatar;
	private Long createDate;
	private Integer deleted;
	private String email;
	private Long lastLogin;
	private String mobilePhoneNumber;
	private String nickname;
	private String password;
	private String salt;
	private String status;
	/**
	 * 版本号
	 * */
	private Integer objectVersion;
}
