package com.zyz.blogadmin.controller;

import com.zyz.blogadmin.common.log.LogAnnotation;
import com.zyz.blogadmin.util.QiniuUtils;
import com.zyz.blogadmin.util.aliyun.MyOSSUtil;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.ResultUpImg;
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
 */
@RestController
@RequestMapping("upload")
public class UploadController {

	@Autowired
	private QiniuUtils qiniuUtils;

	@Autowired
	private MyOSSUtil ossUtil;

	/**
	 * 博客图片上传
	 **/
	@RequestMapping("/articleimg")
	public ResultUpImg uploadArticleImg(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file) {
		String s = ossUtil.uploadFile(file);
		if (s.equals("error")){
			return ResultUpImg.fail("失败");
		}else {
			return ResultUpImg.success(s);
		}
	}

	/**
	 * 标签和分类图片上传
	 */
	@PostMapping("/categorytagimg")
	// @LogAnnotation(module = "文件上传", operator="图片上传")
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
