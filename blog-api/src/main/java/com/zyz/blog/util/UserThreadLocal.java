package com.zyz.blog.util;

import com.zyz.blog.dao.po.User;

/**
 * @author zyz
 * @version 1.0
 */
public class UserThreadLocal {

	private UserThreadLocal(){

	}

	private static final ThreadLocal<User> LOCAL=new ThreadLocal<>();

	public static void put(User user){
		LOCAL.set(user);
	}

	public static User get(){
		return LOCAL.get();
	}

	public static void remove(){
		LOCAL.remove();
	}
}
