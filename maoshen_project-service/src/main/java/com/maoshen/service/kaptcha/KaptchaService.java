package com.maoshen.service.kaptcha;

import java.awt.image.BufferedImage;

public interface KaptchaService {
	public static final String KAPTCHA_TYPE_LOGIN = "KAPTCHA_TYPE_LOGIN";

	/**
	 * 获取图片验证码
	 * @param userId,用户ID
	 * @param type,验证码类型
	 * @param tokenKey，图片验证码token
	 * @return
	 * @throws Exception
	 */
	public BufferedImage getKaptcha(String userId, String type, String tokenKey) throws Exception;

	/**
	 * 校验图片验证码
	 * @param userId,用户ID
	 * @param type,验证码类型
	 * @param kaptcha,要校验的验证码
	 * @param tokenKey，图片验证码token
	 * @return
	 * @throws Exception
	 */
	public boolean checkKaptcha(String userId, String type, String kaptcha, String tokenKey) throws Exception;
}
