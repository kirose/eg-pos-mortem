package com.fimet.eglobal.controller.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.fimet.eglobal.matcher.MatcherResponse;
import com.fimet.eglobal.model.Matcher;
import com.fimet.eglobal.rawcom.RawcomResponse;
import com.fimet.eglobal.service.ConfigService;
import com.fimet.eglobal.service.DescService;
import com.fimet.eglobal.service.MatcherService;
import com.fimet.eglobal.service.RawcomService;
import com.fimet.utils.DateUtils;
import com.fimet.utils.FileUtils;


	
@RestController
@RequestMapping("/matcher")
public class MatcherController {
	
	private static Logger logger = LoggerFactory.getLogger(MatcherController.class);

	@Autowired private ConfigService config;
	@Autowired private DescService descService;
	@Autowired private MatcherService matcherService;
	@Autowired private RawcomService rawcomService;

	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			logger.debug("getAll");
			File[] list = config.getMatchOutputFolder().listFiles();
			if (list == null || list.length == 0) {
				return new ResponseEntity<List<Matcher>>(new ArrayList<Matcher>(0), HttpStatus.OK);
			}
			List<Matcher> result = new ArrayList<Matcher>(list.length);
			String name;

			for (File file : list) {
				name = file.getName();
				if (name.matches("Match\\-[0-9]+\\-[0-9]+\\.txt$")) {
					String[] parts = name.substring(6,name.length()-4).split("-");
					Date date1 = DateUtils.parseyyyyMMddHHmmss(parts[0]);
					Date date2 = DateUtils.parseyyyyMMddHHmmss(parts[1]);
					String fmt1 = DateUtils.formatyyyyMMdd_hhmmss(date1);
					String fmt2 = DateUtils.formatyyyyMMdd_hhmmss(date2);
					result.add(new Matcher(fmt1,fmt2, ""+file.length()));
				}
			}
			return new ResponseEntity<List<Matcher>>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/deleteAll")
	public ResponseEntity<?> deleteAll(){
		try {
			logger.info("getAll");
			int length = config.getMatchOutputFolder().list().length/8;
			FileUtils.deleteFiles(config.getMatchOutputFolder());
			return new ResponseEntity<String>(String.format("%s archivos eliminados",length), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/analyze")
	public ResponseEntity<?> analyze(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
		try {
			logger.info("analyze for range start:{}, end:{}", start, end);
			long t1 = System.currentTimeMillis();
			RawcomResponse rawcomResponse = rawcomService.analyze(start, end);
			DescResponse descResponse = descService.analyze(start, end);
			MatcherResponse response = matcherService.analyze(rawcomResponse, descResponse);
			long t2 = System.currentTimeMillis();
			response.setStartExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t1)));
			response.setEndExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t2)));
			logger.info("analyze completed at time:{}ms", (t2-t1));
			return new ResponseEntity<MatcherResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
