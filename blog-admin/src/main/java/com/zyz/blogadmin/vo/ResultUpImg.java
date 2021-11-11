package com.zyz.blogadmin.vo;

import lombok.Data;

/**
 * @author zyz
 * @version 1.0
 * 文章上传结果封装
 */
@Data
public class ResultUpImg {

	private Integer success;
	private String message;
	private String  url;

	public ResultUpImg(int success, String message) {
		this.success = success;
		this.message = message;
	}

	public ResultUpImg(int success, String message, String url) {
		this.success = success;
		this.message = message;
		this.url = url;
	}

	public static ResultUpImg success(String url){
		return new ResultUpImg(1,"success",url);


	}

	public static ResultUpImg fail(String message){
		return new ResultUpImg(0,message);
	}

}
