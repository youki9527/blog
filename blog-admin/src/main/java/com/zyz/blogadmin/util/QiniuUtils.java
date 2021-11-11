package com.zyz.blogadmin.util;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zyz
 * @version 1.0
 *
 * 七牛云服务器（图片）
 */
@Component
public class QiniuUtils {


	public static final String url = "http://qyaiup71f.hn-bkt.clouddn.com/";

	@Value("${qiniu.accessKey}")
	private String accessKey;
	@Value("${qiniu.accessSecretKey}")
	private String accessSecretKey ;

	/**
	 * 上传文件
	 * */
	public boolean upload(MultipartFile file, String filname) {

		//构造一个带指定 Region 对象的配置类
		Configuration cfg = new Configuration(Region.huanan());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// //...生成上传凭证，然后准备上传
		// String accessKey = "your access key";
		// String secretKey = "your secret key";
		String bucket = "zyzservice";
		// //默认不指定key的情况下，以文件内容的hash值作为文件名
		// String key = null;
		try {
			byte[] uploadBytes = file.getBytes();
			Auth auth = Auth.create(accessKey, accessSecretKey);
			String upToken = auth.uploadToken(bucket);
			Response response = uploadManager.put(uploadBytes, filname, upToken);
			//解析上传成功的结果
			DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
