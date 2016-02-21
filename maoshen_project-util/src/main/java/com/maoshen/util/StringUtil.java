package com.maoshen.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashSet;



public class StringUtil {

    /**
     * 取得hash值，可能返回负数
     * 备注:使用{@link HashAlgorithmUtil}.getDJBHash代替此方法
     * 
     * @param str
     * @return
     */
    @Deprecated
    public static long getDJBHash(String str) {
        long hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 5) + hash + str.charAt(i);
        }
        return hash;
    }

    /**
     * 字符串编码函数。
     * 
     * @param str
     * @param srcCode
     * @param targetCode
     * @return
     */
    public static String encodeStr(String str, String targetCode) {
        try {
            if (str == null) {
                return null;
            }
            byte[] bytesStr = str.getBytes();
            return new String(bytesStr, targetCode);
        } catch (Exception ex) {
            return str;
        }
    }

    /**
     * 字符串编码函数。
     * 
     * @param str
     * @param srcCode
     * @param targetCode
     * @return
     */
    public static String encodeStr(String str, String srcCode, String targetCode) {
        try {
            if (str == null) {
                return null;
            }

            byte[] bytesStr = str.getBytes(srcCode);
            return new String(bytesStr, targetCode);
        } catch (Exception ex) {
            return str;
        }
    }

    /**
     * 判断字符串是否为空字符。
     * 
     * @param value
     * @return
     */
    public static boolean isBlank(String value) {
        boolean ret = false;
        if (value != null && value.equals("")) {
            ret = true;
        }
        return ret;
    }

    /**
     * 判断字符串是否为null。
     * 
     * @param value
     * @return
     */
    public static boolean isNull(String value) {
        return value == null ? true : false;
    }

    /**
     * 判断字符串是否为空字符串或者null。
     * 
     * @param value
     * @return
     */
    public static boolean isNullOrBlank(String value) {
        return isNull(value) || isBlank(value);
    }

    /**
     * 截取字符串前面部分字符,后面加省略号.
     * 
     * @param str
     * @param length
     * @return
     */
    public static String trimWords(String str, int length) {
        String wordStr = str;

        if (wordStr == null) {
            return "";
        }
        if (wordStr.length() <= length) {
            return wordStr;
        }

        wordStr = wordStr.substring(0, length);
        wordStr += "...";
        return wordStr;
    }

    /**
     * 编码带有中文名称Url。
     * 
     * @param url url中的中文
     * @return
     */
    public static String encodeUrl(String url) {
        return encodeUrl(url, "gbk");
    }

    /**
     * 编码带有中文名称Url。
     * 
     * @param url url中的中文
     * @param targetCode 目标字符
     * @return
     */
    public static String encodeUrl(String url, String targetCode) {
        String encodeUrl = "";
        if (StringUtil.isNullOrBlank(url)) {
            return "";
        }
        // 编码并转换空格
        try {
            encodeUrl = URLEncoder.encode(url, targetCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeUrl;
    }

    /**
     * 用指定的分隔符合并数组为单个字符串
     * 
     * @param array 要合并的数组
     * @param separator 分隔符
     * @return
     */
    public static String joinArray(Object[] array, String separator) {
        if (array == null) {
            return "";
        }
        int startIndex = 0;
        int endIndex = array.length;
        int bufSize = endIndex - startIndex;
        if (bufSize <= 0) {
            return "";
        }

        bufSize *= (array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1;
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 过滤内容中的标点符号、空白字符（包括全角空格）
     * 
     * @param content 被过滤内容
     * @return 过滤后内容
     */
    public static String filterPunctuation(String content) {
        if (StringUtil.isNullOrBlank(content)) {
            return content;
        }
        // 过滤标点符号：!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
        // 过滤空白字符：[ \t\n\x0B\f\r] 和 　全角空格
        return content.replaceAll("\\p{Punct}|\\p{Space}|　", "");
    }

    /**
     * get camel style string，eg. last_action_time -> lastActionTime
     * 
     * @param str
     * @return
     */
    public static String getCamelCaseString(String str) {
        if (str == null) {
            return "";
        }
        str = str.toLowerCase();
        StringBuilder result = new StringBuilder(str.length());
        boolean toCapitalize = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                if (toCapitalize) {
                    result.append(Character.toUpperCase(c));
                    toCapitalize = false;
                } else {
                    result.append(c);
                }
            } else {
                toCapitalize = true;
            }
        }
        return result.toString();
    }
    
    /**
     * 字符串折分
     * @param str 原字符
     * @param split 折分标志
     * @return 折分的字符数组
     */
	public static String[] split(String str,String split){
		if(str!=null){
			String[] splitStr = str.split(split);
			if(splitStr!=null){
				for(int i=0;i<splitStr.length;i++){
					splitStr[i] = splitStr[i].trim();
				}
			}
			return splitStr;
		}
		return null;
	}

	
	/**
	 * 
	 * @Description: 用于以指定字符替换原始文件中的字符
	 * @author Richard.Chen
	 * @Email  richard.chen@vipshop.com
	 * @Date 2014年8月7日 下午2:50:57
	 * @param orgStr 原始文件
	 * @param replaceStr  替换的字符
	 * @param start  替换原始字符中的开始位置 （最小长度1） 从1开始 包含start位
	 * @param end	 替换原始字符串中的结束位置 （最大长度orgStr.length） 包含第end位
	 * @return
	 */
	public static String replaceMashChar(String orgStr,char replaceChar,int start,int end){
		//空字符返回""
		if(StringUtil.isNullOrBlank(orgStr)){
			return "";
		}
		
		//如果start大于end 返回原始字符串
		if(start>end){
			return orgStr;
		}
		
		//修正数据，如果start小于1，那么设为1
		if(start<1){
			start=1;
		}
		
		if(start>orgStr.length()){
		    start=orgStr.length()+1;
		}
		
		//如果end大于orgStr.length那么修正为orgStr.length
		if(end>orgStr.length()){
			end=orgStr.length();
		}
				
		String newString="";
		char[] str=orgStr.toCharArray();
		//替换的字符在start与end之间
			for(int i=start-1;i<end;i++){
				str[i]=replaceChar;
			}
			newString=new String(str);
		
			return newString;
		
	}
		
	
	/**
	 * 
	 * @Description: 过滤重复的字符例如a,a,b,c,d,d,e   filterReduplication(str,",")返回a,b,c,d,e
	 * @author Richard.Chen
	 * @Email  richard.chen@vipshop.com
	 * @Date 2014年11月10日 下午6:31:10
	 * @param orgStr  字符串
	 * @param filter  分隔符，会按原样格式过滤后输出
	 * @return
	 */
	public static String filterReduplication(String orgStr,String filter){
		//空字符返回""
		if(StringUtil.isNullOrBlank(orgStr)){
			return "";
		}
		
		if(StringUtil.isNullOrBlank(filter)){
			return orgStr;
		}
		
		String result="";
		String[] strArray=orgStr.split(filter);
		LinkedHashSet<String>  filterStr=new LinkedHashSet<String>();
		
		//保存在唯一的set中去掉重复数据
		for(String s:strArray){
			filterStr.add(s);
		}	
		
		//按原顺序读出
		for(String s:filterStr){			
			if ( "".equals(result) ) {
				result = result + s;
            } else {
            	result = result + filter +  s;
            } 
		}		
	
		return result;
	}
		

	
}
