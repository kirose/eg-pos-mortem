package com.fimet.eglobal.controller.rest;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fimet.eglobal.reports.ReportResponse;
import com.fimet.eglobal.service.ConfigService;
import com.fimet.eglobal.service.ReportService;
import com.fimet.utils.DateUtils;
import com.fimet.utils.FileUtils;


	
@RestController
@RequestMapping("/report")
public class ReportController {
	
	private static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired private ConfigService config;
	@Autowired private ReportService reportService;
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll() {
		try {
			logger.debug("getAll");
			String[] list = config.getReportOutputFolder().list();
			if (list == null || list.length == 0) {
				return new ResponseEntity<List<String>>(new ArrayList<String>(0), HttpStatus.OK);
			}
			return new ResponseEntity<List<String>>(Arrays.asList(list), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam String name){
		try {
			logger.debug("delete:{}",name);
			FileUtils.deleteFiles(new File(config.getReportOutputFolder(),name));
			return new ResponseEntity<String>(String.format("%s eliminado",name), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/deleteAll")
	public ResponseEntity<?> deleteAll(){
		try {
			logger.debug("deleteAll");
			int length = config.getReportOutputFolder().list().length;
			FileUtils.deleteFiles(config.getReportOutputFolder());
			return new ResponseEntity<String>(String.format("%s archivos eliminados",length), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/create")
	public ResponseEntity<?> create(
			@RequestParam String name,
			@RequestParam String id) {
		try {
			logger.info("report for range name:{}, id:{}", name, id);
			long t1 = System.currentTimeMillis();
			ReportResponse response = reportService.create(name, id);
			long t2 = System.currentTimeMillis();
			response.setStartExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t1)));
			response.setEndExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t2)));
			logger.info("report completed at time:{}ms", (t2-t1));
			return new ResponseEntity<ReportResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Internal Error",e);
			return new ResponseEntity<String>("Internal error "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/download/{name:.+}")
	public ResponseEntity<Resource> download(@PathVariable String name){
		Resource resource = null;
		try {
			Path path = new File(config.getReportOutputFolder(), name).toPath();
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			logger.error("Error resolviendo resource "+name,e);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+resource.getFilename()+"\"");
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
}
