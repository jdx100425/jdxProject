package com.maoshen.util;

import java.text.DecimalFormat;

/**
 * 提供一些跟数字相关的方法。
 */
public class NumberUtil {
	
	/**
	 * 将字节转化为合适的单位输出保留两位有效数字
	 * @param long
	 * @return String
	 */
	public static String convertByteToGMKB(long bytes){
		
		String gmkb = null;
		DecimalFormat df = new DecimalFormat("0.##");
		if(bytes<1024){
			gmkb =  String.valueOf(bytes)+"B";
		}else if(bytes>=1024&&bytes<1024*1024){
			double kb = (double)bytes/1024; 
			gmkb = df.format(kb)+"K";
		}else if(bytes>=1024*1024&&bytes<1024*1024*1024){			
			double mb = (double)bytes/1024/1024;
			gmkb = df.format(mb)+"M";
		}else if(bytes>=1024*1024*1024){
			double gb = (double)bytes/1024/1024/1024;
			gmkb = df.format(gb)+"G";
		}
		return gmkb;
	}

	/**
	 * 将数据以千为单位格式花输出
	 * @param long
	 * @return String
	 */
	public static String formatLong(long dataLong){
		
        DecimalFormat df = new DecimalFormat("###,###");
		return df.format(dataLong);
		
	}		
}
