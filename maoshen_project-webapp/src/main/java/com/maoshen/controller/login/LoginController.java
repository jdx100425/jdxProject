package com.maoshen.controller.login;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maoshen.base.BaseController;
import com.maoshen.controller.login.vo.LoginUserInfoVO;
import com.maoshen.controller.login.vo.LoginVO;
import com.maoshen.errorcode.ErrorCode;
import com.maoshen.response.ResponseResult;
import com.maoshen.service.cookie.CookieService;
import com.maoshen.service.kaptcha.KaptchaService;
import com.maoshen.service.login.LoginService;
import com.maoshen.service.login.vo.UserInfo;

/**
 * 
 * @Description:
 * @author Daxian.jiang
 * @Email Daxian.jiang@vipshop.com
 * @Date 2015年7月14日 上午11:28:11
 * @Version V1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	private static final Logger LOGGER = Logger.getLogger(LoginController.class);

	@Autowired
	@Qualifier("loginServiceImpl")
	private LoginService loginService;

	@Autowired
	@Qualifier("cookieServiceImpl")
	private CookieService cookieService;

	@Autowired
	@Qualifier("kaptchaServiceImpl")
	private KaptchaService kaptchaService;

	// @Autowired
	// @Qualifier("jedisCluster")
	// private JedisCluster jedisCluster;

	/**
	 * 
	 * @Description: 登录首页入口
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月24日 上午9:37:35
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = { RequestMethod.POST, RequestMethod.GET })
	public String index(HttpServletRequest request, Model model, String src) {
		LOGGER.info("进入登录页首页");
		UserInfo userInfo = cookieService.getUserInfoFromRedis(request.getCookies());

		// 检测是否已经登录，若不为空，重定向到首页
		if (userInfo != null && userInfo.getUserId() > 0) {
			return redirectAdminPage();
		}

		model.addAttribute("tokenKey", UUID.randomUUID().toString());
		return "/login/index";
	}

	/**
	 * 
	 * @Description:验证用户名和密码是否有效
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年7月14日 上午11:51:56
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/submit", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseResult<LoginVO> submit(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String tokenKey, @RequestParam String userName, @RequestParam String password,
			@RequestParam String kaptcha, String src) throws Exception {
		LOGGER.info("登录提交");
		LoginVO loginVO = new LoginVO();
		UserInfo userInfo = cookieService.getUserInfoFromRedis(request.getCookies());
		// 是否已经登录
		if (userInfo != null && userInfo.getUserId() > 0) {
			return new ResponseResult<LoginVO>(loginVO, ErrorCode.LOGIN_EXIST);
		}

		// 验证验证码格式
		if (!loginService.isValidateKaptcha(kaptcha)) {
			return new ResponseResult<LoginVO>(loginVO, ErrorCode.LOGIN_KAPTCHA_FORMAT_FAIL);
		}

		// 验证用户名和密码格式
		if (!loginService.isValidateUserNameAndPassword(userName, password)) {
			return new ResponseResult<LoginVO>(loginVO, ErrorCode.LOGIN_USERNAME_PASSWORD_FORMAT_FAIL);
		}

		boolean kaptchaResult = kaptchaService.checkKaptcha(null, KaptchaService.KAPTCHA_TYPE_LOGIN, kaptcha, tokenKey);
		if (!kaptchaResult) {
			return new ResponseResult<LoginVO>(loginVO, ErrorCode.LOGIN_KAPTCHA_CHECK_FAIL);
		}

		// 验证用户名和密码是否正确
		userInfo = loginService.checkUserNameAndPwd(userName, password);
		if (userInfo == null) {
			return new ResponseResult<LoginVO>(loginVO, ErrorCode.LOGIN_USERNAME_PASSWORD_CHECK_FAIL);
		}

		// 创建COOKIE并写到redis
		cookieService.setInfoToRedis(userInfo, response);

		loginVO.setResult(true);
		return new ResponseResult<LoginVO>(loginVO);
	}

	/**
	 * 
	 * @Description: 检测是否登录
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月24日 上午9:38:15
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = { RequestMethod.POST })
	public ResponseResult<LoginUserInfoVO> isLogin(HttpServletRequest request, Model model, String cookieValue,
			String userIdValue) {
		LOGGER.info("验证是否已经登录");
		UserInfo userInfo = cookieService.getUserInfoFromRedis(cookieValue, userIdValue);
		LoginUserInfoVO loginUserInfoVO = new LoginUserInfoVO();
		loginUserInfoVO.setUserInfo(userInfo);
		return new ResponseResult<LoginUserInfoVO>(loginUserInfoVO);
	}

}
