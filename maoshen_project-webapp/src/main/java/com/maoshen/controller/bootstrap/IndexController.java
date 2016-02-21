package com.maoshen.controller.bootstrap;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/bootstrap")
public class IndexController {
	private static final Logger LOGGER = Logger.getLogger(IndexController.class);

	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public String index(HttpServletRequest request, Model model, String src) {
		LOGGER.info("进入bootstrap首页");
		return "/bootstrap/index";
	}
}
