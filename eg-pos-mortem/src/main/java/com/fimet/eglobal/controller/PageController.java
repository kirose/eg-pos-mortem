package com.fimet.eglobal.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController implements ErrorController  {
	private static Logger logger = LoggerFactory.getLogger(PageController.class);
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	@RequestMapping("/analysis")
	public String analysis() {
		return "analysis";
	}
	@RequestMapping("/reports")
	public String reports() {
		return "reports";
	}
	@RequestMapping("/example")
	public String example() {
		return "example";
	}
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if(statusCode == HttpStatus.NOT_FOUND.value()) {
				logger.error("Not found {}",request.getContextPath(),request);
				//return "error-404";
				return "error";
			}
			else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				logger.error("Internal Error",request);
				//return "error-500";
				return "error";
			}
		}
		return "error";
	}
}
