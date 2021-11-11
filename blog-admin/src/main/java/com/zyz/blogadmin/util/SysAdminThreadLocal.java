package com.zyz.blogadmin.util;

import com.zyz.blogadmin.dao.po.SysAdmin;

/**
 * @author zyz
 * @version 1.0
 */
public class SysAdminThreadLocal {

	private SysAdminThreadLocal(){

	}

	private static final ThreadLocal<SysAdmin> LOCAL=new ThreadLocal<>();

	public static void put(SysAdmin sysAdmin){
		LOCAL.set(sysAdmin);
	}

	public static SysAdmin get(){
		return LOCAL.get();
	}

	public static void remove(){
		LOCAL.remove();
	}
}
