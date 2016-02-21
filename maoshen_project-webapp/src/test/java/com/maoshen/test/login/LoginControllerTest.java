package com.maoshen.test.login;

import static org.hamcrest.MatcherAssert.assertThat;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;

import com.maoshen.controller.login.LoginController;
import com.maoshen.controller.login.vo.LoginUserInfoVO;
import com.maoshen.service.cookie.CookieService;
import com.maoshen.service.kaptcha.KaptchaService;
import com.maoshen.service.login.LoginService;
import com.maoshen.service.login.vo.UserInfo;
import com.maoshen.test.base.BaseTest;

@RunWith(JMockit.class)
public class LoginControllerTest extends BaseTest {
	@Tested
	private LoginController loginController;

	@Injectable
	private CookieService cookieService;

	@Injectable
	private LoginService loginService;

	@Injectable
	private KaptchaService kaptchaService;

	@Test
	public void testIsLogin() throws Exception {
		final UserInfo userInfo = new UserInfo();
		userInfo.setUserId(8888L);
		userInfo.setUserName("9999");
		new Expectations() {
			{
				cookieService.getUserInfoFromRedis(anyString, anyString);
				result = userInfo;
			}
		};
		MockHttpServletRequest request = new MockHttpServletRequest();

		loginController.isLogin(request, null, "", "");
		LoginUserInfoVO loginUserInfoVO = new LoginUserInfoVO();
		loginUserInfoVO.setUserInfo(userInfo);

		assertThat(loginUserInfoVO.getUserInfo().getUserId(), Matchers.equalTo(8888L));
	}
}
