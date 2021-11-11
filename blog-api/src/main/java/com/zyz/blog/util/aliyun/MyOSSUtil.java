package com.zyz.blog.util.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.zyz.blog.config.OSSConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * @author zyz
 * @version 1.0
 */
@Component
public class MyOSSUtil {


	/**
	 * oss配置
	 * */
	@Autowired
	private OSSConfigure config;

	//OSS上传文件
	public String uploadFile(MultipartFile file){
		OSS ossClient = null;
		//文件名称
		String fileName = file.getOriginalFilename();
		String folder = contentType(fileName.substring(fileName.lastIndexOf(".") + 1));
		//拼接对象名
		String objectName = folder + UUID.randomUUID() + fileName;
		//如果没有下面这两行代码，访问url将不会预览而是直接下载下来
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/jpg");
		try {
			//创建OSS客户端
			ossClient = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
			//上传
			ossClient.putObject(config.getBucketName(), objectName, new ByteArrayInputStream(file.getBytes()), metadata);
			//获取url
			String[] url = getUrl(objectName).split("\\?");
			return url[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error";
	}

	//获得URL连接
	public String getUrl(String objectName){
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		//生成URL
		OSS ossClient = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
		URL url = ossClient.generatePresignedUrl(config.getBucketName(), objectName, expiration);
		if (url != null){
			return url.toString();
		}
		return null;
	}

	//根据文件类型分配文件夹
	private static String contentType(String fileType){
		fileType = fileType.toLowerCase();
		String folder = "";
		switch (fileType){
			case "bmp":
			case "gif":
			case "png":
			case "jpg":
			case "jpeg": folder = "images/";
				break;

			case "html":
			case "txt":
			case "xml": folder = "text/";
				break;

			case "map3": folder = "map3/";
				break;

			case "vsd":
			case "ppt":
			case "pptx":
			case "doc":
			case "docx": folder = "application/";
				break;
			case "mp4": folder = "video/";
				break;
		}
		return folder;
	}

}
