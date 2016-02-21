package com.maoshen.service.kaptcha.impl;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.kaptcha.Producer;
import com.maoshen.service.kaptcha.KaptchaService;
import com.maoshen.service.redis.RedisService;
import com.maoshen.util.StringUtil;

@Service("kaptchaServiceImpl")
public class KaptchaServiceImpl implements KaptchaService {
	private static final Logger LOGGER = Logger.getLogger(KaptchaServiceImpl.class);
	@Autowired
	private RedisService redisService;

	private Producer captchaProducer = null;

	@Autowired
	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}

	@Override
	public BufferedImage getKaptcha(String userId, String type, String tokenKey) throws Exception {
		BufferedImage bi = null;
		if (StringUtil.isNullOrBlank(type) || StringUtil.isNullOrBlank(tokenKey)) {
			return bi;
		}

		StringBuffer key = new StringBuffer();
		if (!StringUtil.isNullOrBlank(userId)) {
			key.append(userId);
		} else {
			key.append(type);
		}
		key.append("_").append(type).append("_").append(tokenKey);
		try {
			String capText = captchaProducer.createText();
			redisService.insertByValue(key.toString(), capText, 1800, TimeUnit.SECONDS);
			bi = captchaProducer.createImage(capText);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return bi;
	}

	@Override
	public boolean checkKaptcha(String userId, String type, String kaptcha, String tokenKey) throws Exception {
		boolean result = false;
		if (StringUtil.isNullOrBlank(type) || StringUtil.isNullOrBlank(tokenKey) || StringUtil.isNullOrBlank(kaptcha)) {
			return result;
		}
		StringBuffer key = new StringBuffer();
		if (!StringUtil.isNullOrBlank(userId)) {
			key.append(userId);
		} else {
			key.append(type);
		}
		key.append("_").append(type).append("_").append(tokenKey);
		try {
			Object o = redisService.getByValue(key.toString());
			if (null == o) {
				return result;
			}
			if (kaptcha.equals(o)) {
				result = true;
			}
			redisService.remove(key.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
}
