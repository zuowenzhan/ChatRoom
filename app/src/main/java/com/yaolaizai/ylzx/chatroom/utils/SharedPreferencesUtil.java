package com.yaolaizai.ylzx.chatroom.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	private static SharedPreferences sp;

	/**
	 * 保存boolean信息
	 */
	public static void saveBoolean(Context context,String key,boolean value){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * 获取boolean信息
	 */
	public static boolean getBoolean(Context context,String key,boolean defValue){

		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		 return sp.getBoolean(key, defValue);
	}
	
	
	/**
	 * 保存String信息
	 */
	public static void saveString(Context context,String key,String value){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * 获取String信息
	 */
	public static String getString(Context context,String key,String defValue){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		 return sp.getString(key, defValue);
	}


}
