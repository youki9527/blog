package com.zyz.blog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zyz
 * @version 1.0
 */
public class MyDateUtils {


	public static String LongToDateString(Long timeLong) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date= new Date(timeLong);
		String dateString = sdf.format(date);
		return dateString;
	}

	public static long DateStringToLong(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt= sdf.parse(dateString);
			System.out.println("dt="+dt);
			return dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static void main(String[] args){

		long l = 1630335230932L;
		System.out.println(l);
		String s = LongToDateString(l);
		System.out.println(s);

		long l1 = DateStringToLong(s);
		System.out.println(l1);
		System.out.println(LongToDateString(l1));


	}

}
