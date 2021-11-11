package com.zyz.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyz.blog.dao.mapper.UserMapper;
import com.zyz.blog.dao.po.User;
import com.zyz.blog.service.LoginRegistLogoutService;
import com.zyz.blog.service.UserService;
import com.zyz.blog.util.MdFive;
import com.zyz.blog.util.MyDateUtils;
import com.zyz.blog.vo.LoginUserVo;

import com.zyz.blog.vo.Params.PasswordParams;
import com.zyz.blog.vo.Params.UserParams;
import com.zyz.blog.vo.Result;
import com.zyz.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private LoginRegistLogoutService loginRegistLogoutService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public User findUserById(Long id) {
		LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.
				select(User::getNickname, User::getAvatar).
				eq(User::getId, id).last("limit 1");
		User user = userMapper.selectOne(lambdaQueryWrapper);
		if (user == null) {
			User user1 = new User();
			user1.setNickname("用户已删除");
			return user1;
		}
		return user;
	}

	@Override
	public User findUser(String account, String password) {
		Integer delete = 0;
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				eq(User::getAccount, account).
				eq(User::getPassword, password).
				eq(User::getDeleted, delete).
				select(User::getId,
						User::getAccount,
						User::getAvatar,
						User::getNickname,
						User::getStatus,
						User::getDeleted).
				last("limit 1");
		return userMapper.selectOne(queryWrapper);
	}


	@Override
	public Result findUserByToken(String token) {

		User user = loginRegistLogoutService.checkToken(token);
		if (user == null) {
			Result.fail(305, "token校验失败");
		}
		LoginUserVo loginUserVo = new LoginUserVo();
		loginUserVo.setId(String.valueOf(user.getId()));
		loginUserVo.setNickname(user.getNickname());
		loginUserVo.setAccount(user.getAccount());
		loginUserVo.setAvatar(user.getAvatar());
		return Result.success(loginUserVo);
	}

	@Override
	public User findUserByAccount(String account) {

		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				eq(User::getAccount, account).
				last("limit 1");
		return userMapper.selectOne(queryWrapper);

	}

	@Override
	public void save(User user) {
		userMapper.insert(user);
	}

	/**
	 * 游客的昵称和头像
	 */
	@Override
	public UserVo findUserVoById(Long authorId) {
		//查询游客昵称 头像 是否注销
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(User::getAvatar, User::getNickname, User::getDeleted).
				eq(User::getId, authorId).
				last("limit 1");
		User sysUser = userMapper.selectOne(queryWrapper);
		if (sysUser == null) {
			UserVo userVo = new UserVo();
			userVo.setAvatar("/static/img/user/user_delete.png");
			userVo.setNickname("用户已删除");
			return userVo;
		}
		if (sysUser.getDeleted() == 1) {
			UserVo userVo = new UserVo();
			userVo.setAvatar("/static/img/user/user_delete.png");
			userVo.setNickname("用户已注销");
			return userVo;
		}
		UserVo userVo = new UserVo();
		userVo.setAvatar(sysUser.getAvatar());
		userVo.setNickname(sysUser.getNickname());
		return userVo;
	}

	@Override
	public Result findUserDetailByToken(String token) {
		User user = loginRegistLogoutService.checkToken(token);
		if (user == null) {
			Result.fail(305, "token校验失败");
		}
		//从数据库中查询详细信息
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				eq(User::getId, user.getId()).
				select(User::getId,
						User::getAccount,
						User::getAvatar,
						User::getNickname,
						User::getStatus,
						User::getEmail,
						User::getMobilePhoneNumber,
						User::getLastLogin,
						User::getCreateDate).
				last("limit 1");
		User newUser = userMapper.selectOne(queryWrapper);
		UserVo userVo = copyToUserVo(newUser);
		return Result.success(userVo);
	}

	@Override
	@Transactional
	public Result updateUserDetailByToken(String token, UserParams userParams) {
		User user = loginRegistLogoutService.checkToken(token);
		//查询版本号
		LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.
				eq(User::getId, user.getId()).
				select(User::getObjectVersion).
				last("limit 1");
		User oldUser = userMapper.selectOne(lambdaQueryWrapper);
		//更新条件
		LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.
				eq(User::getId, user.getId()).
				eq(User::getObjectVersion, oldUser.getObjectVersion());
		//更新字段
		User newUser = new User();
		if (StringUtils.isNotEmpty(userParams.getMobilePhoneNumber())){
			newUser.setMobilePhoneNumber(userParams.getMobilePhoneNumber());
		}
	    if (StringUtils.isNotEmpty(userParams.getMobilePhoneNumber())){
			newUser.setEmail(userParams.getEmail());
		}
		if (StringUtils.isNotEmpty(userParams.getNickname())){
			newUser.setNickname(userParams.getNickname());
		}
		if (StringUtils.isNotEmpty(userParams.getAvatar())){
			newUser.setAvatar(userParams.getAvatar());
		}
		int update = userMapper.update(newUser, lambdaUpdateWrapper);
		//删除redis游客信息的缓存信息
		//1.游客详细信息
		// String params =String.valueOf(token);
		// params = DigestUtils.md5Hex(params);
		// String key = "currentUser::UserController::currentUserDetail::" + params;
		// redisTemplate.delete(key);
		// //2.游客信息
		// String key2 = "currentUser::UserController::currentUser::" + params;
		// redisTemplate.delete(key2);
		return Result.success(null);
	}

	@Override
	public int updateDeleteValueById(Long id) {
		//查出版本号
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(User::getObjectVersion).
				eq(User::getId, id).
				last("limit 1");
		User user = userMapper.selectOne(queryWrapper);
		//更新删除字段为0
		LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(User::getObjectVersion,user.getObjectVersion()).
				eq(User::getId,id).
				last("limit 1");
		User newUser = new User();
		//设置删除字段为1
		newUser.setDeleted(1);
		//版本号加1
		newUser.setObjectVersion(user.getObjectVersion()+1);
		int update = userMapper.update(newUser, updateWrapper);
		return update;

	}

	@Override
	@Transactional
	public Result updatePassword(String token, PasswordParams passwordParams) {

		//1.校验参数是否合法
		if (passwordParams==null||passwordParams.getOldPassword().equals(passwordParams.getNewPassword())){
			return Result.fail(301,"参数不合法或新旧密码一致");
		}
		//2.校验token
		User user = loginRegistLogoutService.checkToken(token);
		if (user==null){
			return Result.fail(302,"token不合法或者过期");
		}
		//3.查出版本号和旧密码和账号（用户加密）
		LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper.
				select(User::getObjectVersion,User::getAccount,User::getPassword).
				eq(User::getId,user.getId()).
				last("limit 1");
		User oldUser= userMapper.selectOne(queryWrapper);
		 //旧密码不正确
		 if (!oldUser.getPassword().equals(MdFive.encrypt(passwordParams.getOldPassword(),oldUser.getAccount()))){
		 	return Result.fail(303,"旧密码输入不正确");
		}
		//4.更新
		User newUser = new User();
		newUser.setObjectVersion(oldUser.getObjectVersion()+1);
		newUser.setPassword(MdFive.encrypt(passwordParams.getNewPassword(),oldUser.getAccount()));
		LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper();
		updateWrapper.
					  eq(User::getId,user.getId()).
					  eq(User::getObjectVersion,oldUser.getObjectVersion());
		int update = userMapper.update(newUser, updateWrapper);
		if (update>0){
			return Result.success(null);
		}
		return Result.fail(304,"系统异常");
	}


	private UserVo copyToUserVo(User user) {
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		userVo.setId(String.valueOf(user.getId()));
		userVo.setCreateDate(MyDateUtils.LongToDateString(user.getCreateDate()));
		userVo.setLastLogin(MyDateUtils.LongToDateString(user.getLastLogin()));
		return userVo;
	}
}
