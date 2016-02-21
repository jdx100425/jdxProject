package com.maoshen.util.http;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.maoshen.util.StringUtil;

public class CookieUtil {

	/**
	 * 
	 * @Description: (解析MEMCACHE中COOKIE信息 时候目前的存储格式 如果格式变化 需要修改 )
	 * @author junfu.dai
	 * @Email junfu.dai@vipshop.com
	 * @Date 2014年3月20日 上午11:09:04
	 * @param str
	 * @return
	 */
	public static Map<String, Object> parseMemSsoStr(String str) {
		if (StringUtil.isNullOrBlank(str)) {
			return null;
		}
		Map<String, Object> cookies = new HashMap<String, Object>();
		String[] ss = str.split(";");
		for (int i = 0; i < ss.length; i++) {
			String s = ss[i];
			int firstEqualIndex = s.indexOf("|");
			if (firstEqualIndex > 0) {
				String key = s.substring(0, firstEqualIndex).trim();
				String value = s.substring(firstEqualIndex + 1, s.length());
				if ("mail".equalsIgnoreCase(key)) {
					// 第三方登录的用户名中存在冒号
					// mail|s:19:"netease:82144995742"
					String email = "";
					if (!StringUtil.isNullOrBlank(value)) {
						int m1 = value.indexOf(":");
						int m2 = value.indexOf(":", m1 + 1);
						// 读取数据的长度
						int length = Integer.valueOf(value.substring(m1 + 1, m2));
						email = value.substring(m2 + 2, m2 + 2 + length);
					}
					cookies.put(key, email);
				} else {
					cookies.put(key, getValue(value));
				}
			}

		}
		return cookies;
	}

	private static String getValue(String tmpValue) {
		// TODO Auto-generated method userIdTmp
		String str = "";
		if (!StringUtil.isNullOrBlank(tmpValue)) {
			tmpValue = tmpValue.replace("\"", "");
			int startKey = tmpValue.lastIndexOf(":");
			if (-1 != startKey) {
				str = tmpValue.substring(startKey + 1);
			}
		}
		return str;
	}

	public static Cookie[] parseCookieStr(String str) {
		if (StringUtil.isNullOrBlank(str)) {
			return null;
		}
		String[] ss = str.split(";");
		Cookie[] cookies = new Cookie[ss.length];
		for (int i = 0; i < ss.length; i++) {
			String s = ss[i];
			int firstEqualIndex = s.indexOf("=");
			if (firstEqualIndex > 0) {
				String key = s.substring(0, firstEqualIndex).trim();
				String value = s.substring(firstEqualIndex + 1, s.length());
				Cookie c = new Cookie(key, value);
				cookies[i] = c;
			}

		}
		return cookies;
	}

	public static String getCookie(HttpServletRequest request) {
		String str = request.getHeader("Cookie");
		if (StringUtil.isNullOrBlank(str)) {
			str = request.getHeader("cookie");
		}
		if (StringUtil.isNullOrBlank(str)) {
			return null;
		}
		return str;
	}
	
	
	/**
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName) {
		if (cookieName == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		Cookie sCookie;
		String cValue;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				sCookie = cookies[i];
				if (cookieName.equals(sCookie.getName())) {
					cValue = sCookie.getValue();
					return cValue == null ? null : cValue;
				}
			}
		}
		return null;

	}


	public static Cookie[] parseCookieStr(HttpServletRequest request) {
		String str = getCookie(request);
		Cookie[] cookies = null;
		if (!StringUtil.isNullOrBlank(str)) {
			String[] ss = str.split(";");
			cookies = new Cookie[ss.length];
			for (int i = 0; i < ss.length; i++) {
				String s = ss[i];
				int firstEqualIndex = s.indexOf("=");
				if (firstEqualIndex > 0) {
					String key = s.substring(0, firstEqualIndex).trim();
					String value = s.substring(firstEqualIndex + 1, s.length());
					Cookie c = new Cookie(key, value);
					cookies[i] = c;
				}

			}
		}
		return cookies;
	}

	public static String getCookieValue(Cookie[] cookies, String cookieName) {
		if (cookies == null) {
			return "";
		}

		String value = null;
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName())) {
				value = cookie.getValue();
				value = unescape(value);
				break;
			}
		}

		if (value == null) {
			return "";
		}

		return value;
	}

	/**
	 * 根据cookie name 获得其值 cookie格式为:cookieName=w=xxx&id=xxx
	 * 
	 * @param cookies
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(Cookie[] cookies, String cookieName, String key) {
		if (cookies == null) {
			return "";
		}

		String cookieStr = null;
		String value = "";
		try {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					cookieStr = cookie.getValue();
					cookieStr = unescape(cookieStr);
					break;
				}
			}

			if (cookieStr == null || "".equals(cookieStr)) {
				return "";
			}

			String validKey = key + "=";

			// 对cookieStr进行解析
			StringTokenizer st = new StringTokenizer(cookieStr, "&");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.indexOf(validKey) != -1) {
					value = token.substring(token.indexOf("=") + 1, token.length());

					value = unescape(value);
					break;
				}

			}
		} catch (Exception ex) {
			value = "";
		}

		return value;
	}

	/**
	 * 实现对应的JavaScript escape函数．
	 * 
	 * @param src
	 *            要编码的字符串。
	 * @return
	 */
	public static String escape(String src) {
		if (src == null) {
			return null;
		}

		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (j < 256) { // 1字节
				// 字母或者数字
				if (Character.isLetterOrDigit(j)) {
					tmp.append(j);
				} else {
					tmp.append("%u00");

					// 0.5个字节
					if (j < 16) {
						tmp.append("0");
					}

					tmp.append(Integer.toString(j, 16));
				}
			} else {
				tmp.append("%u");

				// 1.5个字节
				if (j < 0x1000) {
					tmp.append("0");
				}

				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * 实现对应的JavaScript unescape函数．
	 * 
	 * @param src
	 *            要解码的字符串。
	 * @return
	 */
	public static String unescape(String src) {
		if (src == null) {
			return null;
		}

		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}

		return tmp.toString();
	}
}
