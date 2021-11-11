package com.zyz.blogadmin.vo.params;

import lombok.Data;



/**
 * @author zyz
 * @version 1.0
 * 注册新增用户参数与
 */
@Data
public class RegisterUserParams {

	private String account;
	private String password;
	private String nickname;

}
