package com.zyz.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyz.blog.constants.UserConstant;
import com.zyz.blog.dao.mapper.UserMapper;
import com.zyz.blog.dao.po.User;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.service.UserService;
import com.zyz.blog.service.ThreadService;
import com.zyz.blog.util.JWTUtils;
import com.zyz.blog.util.MdFive;
import com.zyz.blog.vo.Params.LoginParams;
import com.zyz.blog.vo.Params.RegisterParms;
import com.zyz.blog.vo.Result;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class LoginRegistLogoutServiceImpl implements LoginRegistLogoutService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ThreadService threadService;

	/**
	 * 加上事务回滚
	 */
	@Override
	@Transactional
	public Result register(RegisterParms registerParms) {

		String account = registerParms.getAccount();
		String password = registerParms.getPassword();
		String nickname = registerParms.getNickname();
		//检查参数是否合法
		if (StringUtils.isBlank(account)
				|| StringUtils.isBlank(password)
				|| StringUtils.isBlank(nickname)) {
			return Result.fail(204, "参数不合法");
		}
		//检查账号是否已经注册过了
		User sysUser = userService.findUserByAccount(account);
		if (sysUser != null) {
			return Result.fail(206, "账号已经存在");
		}
		User user = new User();
		user.setAccount(account);
		user.setNickname(nickname);
		user.setPassword(MdFive.encrypt(password, account));
		user.setCreateDate(System.currentTimeMillis());
		user.setAvatar("/static/img/user/default_user_avatar.png");
		user.setAdmin(1);
		user.setDeleted(0);
		user.setSalt(account);
		user.setStatus(UserConstant.NORMAL_STATE);
		//保存到数据库
		userService.save(user);
		//将用户信息存在redis（k:token,v:json(user))
		String token = JWTUtils.createToken(user.getId());
		redisTemplate.opsForValue().set("TOKEN_" +user.getId()+"::" +token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
		return Result.success(token);
	}

	@Override
	@Transactional
	public Result login(LoginParams loginParams) {

		/**
		 * 1.检查参数是否合法
		 * 2.校验用户名或者密码
		 * 3.登录成功生成token存入redis和返回token
		 * */
		String account = loginParams.getAccount();
		String password = loginParams.getPassword();
		//1.检查参数合法性
		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
			return Result.fail(300, "参数不合法");
		}
		//2.查找游客
		password = MdFive.encrypt(password, account);
		User user = userService.findUser(account, password);
		if (user == null) {
			return Result.fail(301, "用户名不存在或用户已注销或者密码错误");
		}
		//3.登录成功生成token存入redis和返回token
		String token = JWTUtils.createToken(user.getId());
		redisTemplate.opsForValue().set("TOKEN_"  +token, JSON.toJSONString(user), 1, TimeUnit.DAYS);
		//4.更新最后一次登录时间
		/**
		 * 更新最后一次登录时间 在线程池中进行
		 * */
		threadService.updateLastLoginByAccount(account, userMapper);
		return Result.success(token);
	}

	@Override
	// @Transactional
	public User checkToken(String token) {
		//1.参数校验
		if (StringUtils.isBlank(token)) {
			return null;
		}
		//2.解析token
		Map<String, Object> res = JWTUtils.checkToken(token);
		if (res == null) {
			return null;
		}
		//3.在redis中查找看是否过期
		String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
		if (StringUtils.isBlank(userJson)) {
			return null;
		}
		//4.没有过期 查看是否需要更新保存的用户信息(用户自己和后台管理员可能更新用户的信息)
		User user = JSON.parseObject(userJson, User.class);
		LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.
				eq(User::getId,user.getId()).
				select(User::getId,
						User::getAccount,
						User::getAvatar,
						User::getNickname,
						User::getStatus,
						User::getDeleted).
				last("limit 1");
		User newUser = userMapper.selectOne(queryWrapper);
		//后台将用户删除
		if (newUser==null){
			redisTemplate.delete("TOKEN_" + token);
			return null;
		}
		//后台或者用户自己将账号注销
		if (newUser.getDeleted()==1){
			redisTemplate.delete("TOKEN_" + token);
			return null;
		}
		//用户未注销和删除
		if (user.equals(newUser)) {
			return user;
		} else {
			redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(newUser), 1, TimeUnit.DAYS);
			return newUser;
		}

	}

	@Override
	@Transactional
	public Result logout(String token) {
		redisTemplate.delete("TOKEN_" + token);
		return Result.success(null);
	}

	@Override
	public Result deleteAccount(String token) {
		User user = this.checkToken(token);
		if (user==null){
			return Result.fail(301,"注销失败");
		}
		int i = userService.updateDeleteValueById(user.getId());
		if (i>0){
			Boolean delete = redisTemplate.delete("TOKEN_" + token);
			return Result.success(null);
		}else {
			return Result.fail(302,"注销失败");
		}
	}

}
