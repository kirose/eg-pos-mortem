package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rules.Rules;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Classifiers;
import com.fimet.eglobal.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

@Service
public class ConfigService {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	private Map<String,Classifier> classifiers;
	private Map<String, Connection> connections;
	@Value("${eglobal.desc.queue.size}")
	private int descQueueSize;
	
	@Value("${eglobal.desc.cache.size}")
	private int descCacheSize;
	
	@Value("${eglobal.rawcom.files}")
	private List<String> rawcomFiles;
	@Value("${eglobal.rawcom.cache.size}")
	private int rawcomCacheSize;
	@Value("${eglobal.rawcom.request.timeout}")
	private int rawcomRequestTimeout;
	private File rawcomOutputFolder;
	private File rawcomInputFolder;

	private File descInputFolder;
	private File descOutputFolder;

	private File matchOutputFolder;

	private Rules rules;
	//private HashMap<String, IParser> parsers;
	
	public ConfigService(
			@Value("${eglobal.path.match.output}") String matchOutputPath,
			@Value("${eglobal.path.rawcom.output}") String rawcomOutputPath,
			@Value("${eglobal.path.rawcom.input}") String rawcomInputPath,
			@Value("${eglobal.path.desc.output}") String descOutputPath,
			@Value("${eglobal.path.desc.input}") String descInputPath
			) {
		matchOutputFolder  = new File(matchOutputPath);
		rawcomInputFolder  = new File(rawcomInputPath);
		rawcomOutputFolder = new File(rawcomOutputPath);
		descInputFolder  = new File(descInputPath);
		descOutputFolder = new File(descOutputPath);
	}
	@PostConstruct
	private void start() throws IOException {
		classifiers = JsonUtils.fromResource("classifiers.json", Classifiers.class).getClassifiers();
		logger.info("classifiers:{}",classifiers);
		connections = JsonUtils.fromResource("connections.json", new TypeToken<Map<String, Connection>>() {}.getType());
		logger.info("connections:{}",connections);
		rules  = JsonUtils.fromResource("rules.json", Rules.class);
		logger.info("rules:{}",rules);
	}
	public Map<String, Classifier> getClassifiers() {
		return classifiers;
	}
	public Map<String, Connection> getConnections() {
		return connections;
	}
	public List<String> getRawcomFiles() {
		return rawcomFiles;
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
	public Rules getRules() {
		return rules;
	}
	public int getDescQueueSize() {
		return descQueueSize;
	}
}
