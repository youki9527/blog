package com.zyz.blog.controller;


import com.alibaba.fastjson.JSONObject;
import com.zyz.blog.util.baiduapi.ApiCheckImgResult;
import com.zyz.blog.util.baiduapi.ApiCheckTextResult;
import com.zyz.blog.util.baiduapi.ImgCensor;
import com.zyz.blog.util.baiduapi.TextCensor;
import com.zyz.blog.vo.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zyz
 * @version 1.0
 * 审核
 */
@RestController
@RequestMapping("/check")
public class CheckController {

	@Autowired
	private TextCensor textCensor;
	@Autowired
	private ImgCensor imgCensor;

	/**
	 * 文本审核
	 */
	@PostMapping("/text")
	public Result checkText(@RequestBody String text) {
		String res = textCensor.TextCensor(text);
		//JSON字符串转换成Javabean对象
		ApiCheckTextResult apiCheckTextResult = JSONObject.parseObject(res, ApiCheckTextResult.class);
		return Result.success(apiCheckTextResult);
	}

	/**
	 * 图像审核
	 * */
	@PostMapping("/img")
	public Result checkImg(@RequestBody MultipartFile image) {
		String res = imgCensor.ImgCensor(image);
		ApiCheckImgResult apiCheckImgResult = JSONObject.parseObject(res, ApiCheckImgResult.class);
		return Result.success(apiCheckImgResult);
	}


}
