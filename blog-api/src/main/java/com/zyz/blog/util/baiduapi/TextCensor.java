package com.zyz.blog.util.baiduapi;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author zyz
 * @version 1.0
 */
@Component
public class TextCensor {
	/**
	 * 重要提示代码中所需工具类
	 * FileUtil,Base64Util,HttpUtil,GsonUtils请从
	 * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
	 * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
	 * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
	 * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
	 * 下载
	 */

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public  String TextCensor(String text) {

		//从缓存中获取内容审核token
		String redisKey="contentCheckAuth";
		String redisValue = redisTemplate.opsForValue().get(redisKey);
		//缓存中没有
		if (StringUtils.isEmpty(redisValue)){
			//请求获取内容审核Token
			String contentCheckAuth = BaiDuAccessToken.getAuth("S6QrG1QyEoX1cC4WevLBZVb0", "wTsplHwN1RxujPjU7sRzPx9929jO1ry8");
			redisKey=contentCheckAuth;
			//存入缓存
			redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(contentCheckAuth), Duration.ofDays(30));
		}
		// 请求文本审核url
		String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
		try {
			String param = "text=" +text;
			String accessToken = redisValue;
			String result = HttpUtil.post(url, accessToken, param);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
