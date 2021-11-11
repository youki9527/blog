package com.zyz.blog.controller;

import com.zyz.blog.common.log.LogAnnotation;
import com.zyz.blog.util.aliyun.MyOSSUtil;
import com.zyz.blog.util.qiniu.QiniuUtils;
import com.zyz.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author zyz
 * @version 1.0
 * 文件上传
 */
@RestController
@RequestMapping("upload")
public class UploadController {

	@Autowired
	private MyOSSUtil ossUtil;

	@PostMapping
	@LogAnnotation(module = "文件上传", operator="图片上传")
	public Result uploadAli(@RequestParam("image") MultipartFile file) {
		String s = ossUtil.uploadFile(file);
		if (s.equals("error")){
			return Result.fail(200001,"上传失败服务器异常");
		}
		else {
			System.out.println(s);
			return Result.success(s);
		}
	}

}
