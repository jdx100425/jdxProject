package com.maoshen.controller.kaptcha;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maoshen.service.kaptcha.KaptchaService;

@Controller
@RequestMapping("/kaptcha")
public class KaptchaController {
	private static final Logger LOGGER = Logger.getLogger(KaptchaController.class);

	@Autowired
	private KaptchaService kaptchaService;

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
	@RequestMapping(value = "/getKaptcha", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String tokenKey) throws Exception {

		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// create the text for the image
		// String capText = captchaProducer.createText();
		// store the text in the session
		// request.getSession().setAttribute(KaptchaConstance.KAPTCHA_SESSION_KEY, capText);
		// create the image with the text
		// BufferedImage bi = captchaProducer.createImage(capText);

		/*************/
		BufferedImage bi = kaptchaService.getKaptcha(null, KaptchaService.KAPTCHA_TYPE_LOGIN, tokenKey);
		/*************/

		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}
}
