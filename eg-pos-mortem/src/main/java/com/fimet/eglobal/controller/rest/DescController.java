package com.fimet.eglobal.controller.rest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fimet.eglobal.desc.DescResponse;
import com.fimet.eglobal.model.Response;
import com.fimet.eglobal.service.DescService;
import com.fimet.utils.DateUtils;


	
@RestController
@RequestMapping("/desc")
public class DescController {
	
	private static Logger logger = LoggerFactory.getLogger(DescController.class);
	
	@Autowired private DescService descService;
	
	@GetMapping("/analyze")
	public ResponseEntity<?> analyze(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
		try {
			logger.info("analyze for range start:{}, end:{}", start, end);
			long t1 = System.currentTimeMillis();
			DescResponse response = descService.analyze(start, end);
			long t2 = System.currentTimeMillis();
			response.setStartExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t1)));
			response.setEndExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t2)));
			logger.info("analyze completed at time:{}ms", (t2-t1));
			return new ResponseEntity<DescResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<Response>(Response.newError("Internal error "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
