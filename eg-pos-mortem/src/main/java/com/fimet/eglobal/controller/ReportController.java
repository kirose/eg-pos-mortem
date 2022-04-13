package com.fimet.eglobal.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fimet.eglobal.model.Response;
import com.fimet.eglobal.reports.ReportResponse;
import com.fimet.eglobal.service.ReportService;
import com.fimet.utils.DateUtils;


	
@RestController
@RequestMapping("/report")
public class ReportController {
	
	private static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired private ReportService reportService;
	
	@GetMapping("/create")
	public ResponseEntity<?> create(
			@RequestParam String name,
			@RequestParam String idMatcher,
			@RequestParam String idValidations) {
		try {
			logger.info("report for range name:{}, matcher:{}, validations:{}", name, idMatcher, idValidations);
			long t1 = System.currentTimeMillis();
			ReportResponse response = reportService.create(name, idMatcher, idValidations);
			long t2 = System.currentTimeMillis();
			response.setStartExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t1)));
			response.setEndExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t2)));
			logger.info("report completed at time:{}ms", (t2-t1));
			return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<Response>(Response.newError("Internal error "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
