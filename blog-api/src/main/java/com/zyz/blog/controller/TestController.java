package com.zyz.blog.controller;

import com.zyz.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author zyz
 * @version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@GetMapping
	public Result test(){
		// 获取所有的key
		Set<String> keys = redisTemplate.keys("TOKEN_*");
		for (String key : keys) {
			System.out.println(key);
			// 获取key对应值
			Object value = redisTemplate.opsForValue().get(key);
			// if (value.equals(cardId)) {
			// 	redisTemplate.delete(key);
			// }
		}
		return Result.success(keys);
	}
}
