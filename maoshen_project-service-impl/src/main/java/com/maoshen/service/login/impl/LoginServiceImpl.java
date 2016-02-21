package com.maoshen.service.login.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.maoshen.dao.account.AccountDao;
import com.maoshen.dao.account.entity.Account;
import com.maoshen.service.cookie.CookieService;
import com.maoshen.service.login.LoginService;
import com.maoshen.service.login.vo.UserInfo;

@Service("loginServiceImpl")
public class LoginServiceImpl implements LoginService {
	private static final Logger LOGGER = Logger.getLogger(LoginServiceImpl.class);

	@Autowired
	private AccountDao accountDao;

	@Autowired
	@Qualifier("cookieServiceImpl")
	private CookieService cookieService;

	@Override
	public UserInfo checkUserNameAndPwd(String userName, String password) {
		UserInfo userInfo = null;
		if (StringUtils.isAnyBlank(userName) || StringUtils.isAnyBlank(password)) {
			return null;
		}
		Account account = accountDao.selectByUserNameAndPwd(userName, password);
		if (account != null && account.getId() > 0) {
			userInfo = new UserInfo();
			userInfo.setUserId(account.getId());
			userInfo.setUserName(account.getUserName());
		}
		return userInfo;
	}

	@Override
	public boolean isValidateUserNameAndPassword(String userName, String password) {
		boolean result = false;
		if (StringUtils.isAnyBlank(userName) || StringUtils.isAnyBlank(password)) {
			LOGGER.warn("userName or password valid is fail");
			return result;
		}
		result = true;
		return result;
	}

	@Override
	public boolean isValidateKaptcha(String kaptcha) {
		boolean result = false;
		if (StringUtils.isAnyBlank(kaptcha) || kaptcha.length() != 5) {
			LOGGER.warn("kaptcha valid is fail");
			return result;
		}
		result = true;
		return result;
	}
}
