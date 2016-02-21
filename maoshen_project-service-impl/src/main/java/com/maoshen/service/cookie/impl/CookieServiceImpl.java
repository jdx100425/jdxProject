package com.maoshen.service.cookie.impl;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maoshen.service.cookie.CookieService;
import com.maoshen.service.login.vo.UserInfo;
import com.maoshen.service.redis.RedisService;
import com.maoshen.util.JsonUtil;
import com.maoshen.util.encry.MD5Util;
import com.maoshen.util.http.CookieUtil;

@Service("cookieServiceImpl")
public class CookieServiceImpl implements CookieService {
	private static final Logger LOGGER = Logger.getLogger(CookieServiceImpl.class);

	@Autowired
	private RedisService redisService;

	@Override
	public void setInfoToRedis(UserInfo userInfo, HttpServletResponse response) {
		try {
			String userId = Long.toString(userInfo.getUserId());
			String cookieKey = MD5Util.MD5(userId + userId);
			Cookie cookieToken = new Cookie(CookieService.COOKIE_TOKEN, cookieKey);
			Cookie cookieUserId = new Cookie(CookieService.COOKIE_USER_ID, userId);

			// 生命周期
			cookieToken.setMaxAge(CookieService.COOKIE_TIMES);
			cookieUserId.setMaxAge(CookieService.COOKIE_TIMES);

			// 设置哪个域名写cookie
			cookieToken.setPath(COOKIE_PATH);
			cookieUserId.setPath(COOKIE_PATH);

			cookieToken.setDomain(COOKIE_DOMAIN);
			cookieUserId.setDomain(COOKIE_DOMAIN);

			response.addCookie(cookieToken);
			response.addCookie(cookieUserId);

			// 组装userInfo信息到json并插入到redis
			String userInfoJson = JsonUtil.toJson(userInfo);
			redisService.insertByValue(cookieKey, userInfoJson, COOKIE_TIMES, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public UserInfo getUserInfoFromRedis(Cookie[] cookies) {
		UserInfo userInfo = null;
		try {
			if (cookies == null || cookies.length == 0) {
				return null;
			}

			String cookieValue = CookieUtil.getCookieValue(cookies, CookieService.COOKIE_TOKEN);
			String userIdValue = CookieUtil.getCookieValue(cookies, CookieService.COOKIE_USER_ID);

			userInfo = getUserInfo(cookieValue, userIdValue);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return userInfo;
	}

	@Override
	public UserInfo getUserInfoFromRedis(String cookieValue, String userIdValue) {
		UserInfo userInfo = null;
		try {
			userInfo = getUserInfo(cookieValue, userIdValue);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return userInfo;
	}

	private UserInfo getUserInfo(String cookieValue, String userIdValue) throws Exception {
		UserInfo userInfo;
		// 先看COOKIE_TOKEN存不存在
		if (StringUtils.isAnyBlank(cookieValue) || StringUtils.isAnyBlank(userIdValue)) {
			return null;
		}

		// 如COOKIE_TOKEN存在，去redis查看是否还存在，
		Object o = redisService.getByValue(cookieValue);
		if (o == null) {
			return null;
		}

		// 如redis也存在，获取userInfo
		String userInfoJson = (String) o;
		userInfo = JsonUtil.toBean(userInfoJson, UserInfo.class);

		// 检测userId是否一致
		if (!userIdValue.equals(Long.toString(userInfo.getUserId()))) {
			return null;
		}

		// 更新redis时间
		redisService.insertByValue(cookieValue, userInfoJson, COOKIE_TIMES, TimeUnit.SECONDS);
		return userInfo;
	}
}
