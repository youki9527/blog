package com.zyz.blog.vo.Params;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class PasswordParams {
	private String oldPassword;
	private String newPassword;
}
