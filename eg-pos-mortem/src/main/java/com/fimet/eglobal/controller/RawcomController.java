package com.fimet.eglobal.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fimet.eglobal.model.Response;
import com.fimet.eglobal.rawcom.RawcomResponse;
import com.fimet.eglobal.service.RawcomAnalyzer;


	
@RestController
@RequestMapping("/rawcom")
public class RawcomController {
	
	private static Logger logger = LoggerFactory.getLogger(RawcomController.class);
	
	@Autowired private RawcomAnalyzer rawcomService;
	
	@GetMapping("/get/{pan}")
	public ResponseEntity<?> get(@PathVariable("pan") String pan) {
		try {
			return new ResponseEntity<Response>(Response.newOk(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<Response>(Response.newError("Cannot disconnect "+pan), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/analyze")
	public ResponseEntity<?> analyze(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
		try {
			logger.info("analyze start:{}, end:{}", start, end);
			RawcomResponse response = rawcomService.analyze(start, end);
			return new ResponseEntity<RawcomResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<Response>(Response.newError("Internal error "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
