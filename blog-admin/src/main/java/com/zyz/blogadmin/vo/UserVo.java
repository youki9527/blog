package com.zyz.blogadmin.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class UserVo {

	private String id;

	private String account;

	private Integer admin;

	private String avatar;

	private String  createDate;

	private String deleted;

	private String email;

	private String lastLogin;

	private String mobilePhoneNumber;

	private String nickname;

	private String password;

	private String status;
}
