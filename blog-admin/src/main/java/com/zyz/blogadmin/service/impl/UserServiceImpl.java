package com.zyz.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyz.blogadmin.constants.UserConstant;
import com.zyz.blogadmin.dao.mapper.UserMapper;

import com.zyz.blogadmin.dao.po.User;
import com.zyz.blogadmin.service.UserService;
import com.zyz.blogadmin.util.MdFive;
import com.zyz.blogadmin.util.MyDateUtils;
import com.zyz.blogadmin.vo.PageResult;
import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.UserVo;
import com.zyz.blogadmin.vo.params.PageParam;

import com.zyz.blogadmin.vo.params.RegisterUserParams;
import com.zyz.blogadmin.vo.params.UpdateUserStatusParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public Result listUser(PageParam pageParam) {
		Page<User> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.select(
				User::getId,
				User::getAccount,
				User::getNickname,
				User::getAvatar,
				User::getCreateDate,
				User::getLastLogin,
				User::getStatus,
				User::getDeleted,
				User::getMobilePhoneNumber,
				User::getEmail);
		if (StringUtils.isNotBlank(pageParam.getQueryString())) {
			queryWrapper.eq(User::getAccount, pageParam.getQueryString());
		}
		Page<User> sysUserPage = userMapper.selectPage(page, queryWrapper);
		PageResult<UserVo> pageResult = new PageResult<>();
		//?????????userVo???list ??????????????????Long?????????ID???Long?????????????????????
		pageResult.setList(copyList(sysUserPage.getRecords()));
		pageResult.setTotal(sysUserPage.getTotal());
		return Result.success(pageResult);
	}

	@Override
	@Transactional
	public Result addUser(RegisterUserParams registerUserParams) {
		//1.????????????????????????
		if (StringUtils.isBlank(registerUserParams.getAccount())
				|| StringUtils.isBlank(registerUserParams.getPassword())
				|| StringUtils.isBlank(registerUserParams.getNickname())) {
			return Result.fail(301, "???????????????");
		}
		//????????????????????????????????????
		User user = this.findUserByAccount(registerUserParams.getAccount());
		if (user != null) {
			return Result.fail(302, "??????????????????");
		}
		User newUser = new User();
		newUser.setAccount(registerUserParams.getAccount());
		newUser.setNickname(registerUserParams.getNickname());
		newUser.setPassword(MdFive.encrypt(registerUserParams.getPassword(), registerUserParams.getAccount()));
		newUser.setCreateDate(System.currentTimeMillis());
		newUser.setAvatar("/templates/img/user/default_user_avatar.png");
		newUser.setAdmin(1);
		newUser.setDeleted(0);
		newUser.setSalt(registerUserParams.getAccount());
		newUser.setStatus(UserConstant.NORMAL_STATE);
		//??????????????????
		int save = this.save(newUser);
		if (save > 0) {
			return Result.success(null);
		} else {
			return Result.fail(303, "????????????");
		}

	}

	@Override
	@Transactional
	public Result updateUser(UserVo userVo) {

		//??????????????????
		Long id = Long.valueOf(userVo.getId());
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(User::getObjectVersion).
				eq(User::getId, id).
				last("limit 1");
		User user = userMapper.selectOne(queryWrapper);
		if (user == null) {
			return Result.fail(301, "???????????????");
		}
		//????????????
		User newUser = copyToSysUser(userVo);
		newUser.setObjectVersion(user.getObjectVersion() + 1);
		LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(User::getId, id).
				eq(User::getObjectVersion, user.getObjectVersion());
		int update = userMapper.update(newUser, updateWrapper);
		if (update > 0) {
			return Result.success(null);
		} else {
			return Result.fail(301, "????????????");
		}

	}

	@Override
	@Transactional
	public Result deleteUser(Long id) {
		int i = userMapper.deleteById(id);
		if (i > 0) {
			return Result.success(null);
		}
		return Result.fail(501, "????????????");

	}

	@Override
	public Result deleteUserList(List<Long> ids) {
		userMapper.deleteBatchIds(ids);
		return Result.success(ids);
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
	public int save(User newUser) {
		return userMapper.insert(newUser);

	}



	@Override
	@Transactional
	public Result writeOffUser(Long id) {
		//????????????????????????
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(User::getObjectVersion).
				eq(User::getId, id).
				last("limit 1");
		User user = userMapper.selectOne(queryWrapper);
		if (user == null) {
			return Result.fail(301, "???????????????");
		}
		User newUser = new User();
		newUser.setObjectVersion(user.getObjectVersion() + 1);
		newUser.setDeleted(1);
		LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(User::getId, id).
				eq(User::getObjectVersion, user.getObjectVersion());
		int update = userMapper.update(newUser, updateWrapper);
		if (update > 0) {
			return Result.success(null);
		}
		return Result.fail(302, "????????????");
	}

	@Override
	public Result UpdateStatus(UpdateUserStatusParams updateUserStatusParams) {

		if (updateUserStatusParams == null ||
				StringUtils.isBlank(updateUserStatusParams.getId()) ||
				StringUtils.isBlank(updateUserStatusParams.getStatus())) {
			return Result.fail(301, "???????????????");
		}
		//???????????????
		Long id = Long.valueOf(updateUserStatusParams.getId());
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.
				select(User::getObjectVersion).
				eq(User::getId, id).
				last("limit 1");
		User user = userMapper.selectOne(queryWrapper);
		if (user == null) {
			return Result.fail(302, "???????????????");
		}
		User newUser = new User();
		newUser.setStatus(updateUserStatusParams.getStatus());
		newUser.setObjectVersion(user.getObjectVersion() + 1);
		LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.
				eq(User::getId, id).
				eq(User::getObjectVersion, user.getObjectVersion());
		int update = userMapper.update(newUser, updateWrapper);
		if (update > 0) {
			return Result.success(null);
		}
		return Result.fail(303, "????????????");

	}

	private List<UserVo> copyList(List<User> records) {
		List<UserVo> userVos = new ArrayList<>();
		for (User record : records) {
			userVos.add(copyToSysUserVo(record));
		}
		return userVos;
	}

	private UserVo copyToSysUserVo(User user) {

		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		userVo.setId(user.getId().toString());
		if (user.getCreateDate() != null) {
			userVo.setCreateDate(MyDateUtils.LongToDateString(user.getCreateDate()));
		}
		if (user.getLastLogin() != null) {
			userVo.setLastLogin(MyDateUtils.LongToDateString(user.getLastLogin()));
		}
		if (user.getDeleted() == 0) {
			userVo.setDeleted("?????????");
		} else {
			userVo.setDeleted("?????????");
		}
		return userVo;
	}

	private User copyToSysUser(UserVo userVo) {
		User user = new User();
		BeanUtils.copyProperties(userVo, user);
		user.setId(Long.valueOf(userVo.getId()));
		if (StringUtils.isNotBlank(userVo.getCreateDate())){
			user.setCreateDate(MyDateUtils.DateStringToLong(userVo.getCreateDate()));
		}
		if(StringUtils.isNotBlank(userVo.getLastLogin())){
			user.setLastLogin(MyDateUtils.DateStringToLong(userVo.getLastLogin()));
		}
		// if (userVo.getDeleted().equals(UserConstant.NO_CLOSED)) {
		// 	user.setDeleted(0);
		// }
		return user;
	}
}
