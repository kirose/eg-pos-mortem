package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rules.Validations;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Classifiers;
import com.fimet.eglobal.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

@Service
public class ConfigService {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	private Map<String,Classifier> classifiers;
	private Map<String, Connection> connections;
	@Value("${eglobal.desc.queue.size:20}")
	private int descQueueSize;
	@Value("${eglobal.desc.cache.size:100}")
	private int descCacheSize;
	@Value("${eglobal.rawcom.cache.size:100}")
	private int rawcomCacheSize;
	@Value("${eglobal.rawcom.request.timeout:9000}")
	private int rawcomRequestTimeout;
	private File rawcomOutputFolder;
	private File rawcomInputFolder;

	private File descInputFolder;
	private File descOutputFolder;

	private File matchOutputFolder;

	private Validations validations;

	private File reportOutputFolder;
	private File resources;
	
	public ConfigService(
			@Value("${eglobal.path.resources:resources}") String resourcesPath,
			@Value("${eglobal.path.rawcom.output:analyzed}") String rawcomOutputPath,
			@Value("${eglobal.path.rawcom.input:rawcom}") String rawcomInputPath,
			@Value("${eglobal.path.desc.output:analyzed}") String descOutputPath,
			@Value("${eglobal.path.desc.input:desc}") String descInputPath,
			@Value("${eglobal.path.match.output:analyzed}") String matchOutputPath,
			@Value("${eglobal.path.reports.output:reports}") String reportOutputPath
			) {
		resources = new File(resourcesPath);
		matchOutputFolder  = new File(matchOutputPath);
		rawcomInputFolder  = new File(rawcomInputPath);
		rawcomOutputFolder = new File(rawcomOutputPath);
		descInputFolder  = new File(descInputPath);
		descOutputFolder = new File(descOutputPath);
		reportOutputFolder = new File(reportOutputPath);
	}
	@PostConstruct
	private void start() throws IOException {
		classifiers = JsonUtils.fromFile(new File(resources,"classifiers.json"), Classifiers.class).getClassifiers();
		logger.info("classifiers loaded:{}",(classifiers!=null?classifiers.size():0));
		connections = JsonUtils.fromFile(new File(resources,"connections.json"), new TypeToken<Map<String, Connection>>() {}.getType());
		logger.info("connections loaded:{}",(connections!=null?connections.size():0));
		validations  = JsonUtils.fromFile(new File(resources,"validations.json"), Validations.class);
		logger.info("validations loaded:{}",(validations!=null&&validations.getGroups()!=null?validations.getGroups().size():0));
	}
	public Map<String, Classifier> getClassifiers() {
		return classifiers;
	}
	public Map<String, Connection> getConnections() {
		return connections;
	}
	public int getRawcomCacheSize() {
		return rawcomCacheSize;
	}
	public File getRawcomOutputFolder() {
		return rawcomOutputFolder;
	}
	public File getRawcomInputFolder() {
		return rawcomInputFolder;
	}
	public File getDescInputFolder() {
		return descInputFolder;
	}
	public File getDescOutputFolder() {
		return descOutputFolder;
	}
	public int getDescCacheSize() {
		return descCacheSize;
	}
	public int getRawcomRequestTimeout() {
		return rawcomRequestTimeout;
	}
	public File getMatchOutputFolder() {
		return matchOutputFolder;
	}
	public Validations getValidations() {
		return validations;
	}
	public int getDescQueueSize() {
		return descQueueSize;
	}
	public File getReportOutputFolder() {
		return reportOutputFolder;
	}
}
