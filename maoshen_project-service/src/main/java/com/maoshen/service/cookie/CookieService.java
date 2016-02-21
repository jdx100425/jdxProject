package com.maoshen.service.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.maoshen.service.login.vo.UserInfo;

public interface CookieService {
	public static final String COOKIE_TOKEN = "COOKIE_TOKEN";

	public static final String COOKIE_USER_ID = "COOKIE_USER_ID";

	// public static final String COOKIE_USER_NAME = "COOKIE_USER_NAME";

	public static final String COOKIE_PATH = "/";

	public static final String COOKIE_DOMAIN = ".maoshen.com";

	public static final int COOKIE_TIMES = 1800;

	/**
	 * 
	 * @Description: 把COOKIE登录信息保存到redis
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月16日 下午6:33:02
	 * @param userInfo
	 */
	public void setInfoToRedis(UserInfo userInfo, HttpServletResponse response);

	/**
	 * 
	 * @Description: 从redis获取登录信息
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月16日 下午6:39:55
	 * @param cookie
	 * @return
	 */
	public UserInfo getUserInfoFromRedis(Cookie[] Cookies);

	/**
	 * 
	 * @Description: 从redis获取登录信息
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月16日 下午6:39:55
	 * @param cookie
	 * @return
	 */
	public UserInfo getUserInfoFromRedis(String cookieValue, String userIdValue);
}
