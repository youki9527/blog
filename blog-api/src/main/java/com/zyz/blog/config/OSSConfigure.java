package com.zyz.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * @author zyz
 * @version 1.0
 *
 * 对象存储配置
 */
@Component
@ConfigurationProperties(prefix = "oss")
public class OSSConfigure {

	private String endPoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;

	@Override
	public String toString() {
		return "OSSConfigure{" +
				"endPoint='" + endPoint + '\'' +
				", accessKeyId='" + accessKeyId + '\'' +
				", accessKeySecret='" + accessKeySecret + '\'' +
				", bucketName='" + bucketName + '\'' +
				'}';
	}


	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}


}
