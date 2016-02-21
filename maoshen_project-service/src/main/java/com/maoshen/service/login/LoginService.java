/**
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email Daxian.jiang@vipshop.com
 * @Date 2015年9月16日 下午4:58:27
 * @Version V1.0
 */
package com.maoshen.service.login;

import com.maoshen.service.login.vo.UserInfo;

public interface LoginService {
	/**
	 * 
	 * @Description:验证用户名，密码格式
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月17日 上午10:14:09
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean isValidateUserNameAndPassword(String userName, String password);

	/**
	 * 
	 * @Description:，验证验证码
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月30日 下午3:34:16
	 * @param kaptcha
	 * @return
	 */
	public boolean isValidateKaptcha(String kaptcha);

	/**
	 * 
	 * @Description: 验证用户名和密码是否正确
	 * @author Daxian.jiang
	 * @Email Daxian.jiang@vipshop.com
	 * @Date 2015年9月16日 下午6:17:00
	 * @param userName
	 * @param password
	 * @return
	 */
	// @DataSource("slave")
	// @DataSource("master")
	public UserInfo checkUserNameAndPwd(String userName, String password);
}