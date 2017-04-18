package com.lee.privatecustom.utils;

import android.util.Log;

import java.security.MessageDigest;

public class MD5Util {
	public static String getMD5(String str) {
		StringBuffer sb = new StringBuffer();

		try{
			//获得摘要对象
			MessageDigest md = MessageDigest.getInstance("md5");
			//转换str--->md5
			md.update(str.getBytes());
			byte[] bytes = md.digest();
			//如下直接转换是可以的，但是可读性太差，不推荐
			String string = new String(bytes);
			Log.d("TAG", "不经过额外处理的MD5字符串--->"+string);
			for (byte b : bytes) {
				//把每一个byte数据做一下“格式化”
				// 1111 & 1010--->1010
				String temp = Integer.toHexString(b & 0xFF);
				if(temp.length()==1){
					sb.append("0");
				}
				sb.append(temp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}
}
