package com.fimet.eglobal.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.model.OperativeGroup;
import com.fimet.eglobal.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

@Service
public class ConfigService {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	@Value("${eglobal.rawcom.files}")
	private List<String> inputPath;
	private Map<String,OperativeGroup> operatives;
	private Map<String, Connection> connections;

	@PostConstruct
	private void start() throws IOException {
		operatives = JsonUtils.fromResource("operatives.json", new TypeToken<Map<String,OperativeGroup>>() {}.getType());
		logger.info("operatives:{}",operatives);
		connections = JsonUtils.fromResource("connections.json", new TypeToken<Map<String, Connection>>() {}.getType());
		logger.info("connections:{}",connections);
	}

	public List<String> getInputPath() {
		return inputPath;
	}
	public Map<String, OperativeGroup> getOperatives() {
		return operatives;
	}
	public Map<String, Connection> getConnections() {
		return connections;
	}
}
