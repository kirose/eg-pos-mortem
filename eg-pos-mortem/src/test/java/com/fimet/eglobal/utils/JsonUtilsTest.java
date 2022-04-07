package com.fimet.eglobal.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.model.OperativeGroup;
import com.google.gson.reflect.TypeToken;

@SpringBootTest
public class JsonUtilsTest {
	@Test
	public void operativesTest() {
		try {
			Map<String,OperativeGroup> operatives = JsonUtils.fromResource("operatives.json", new TypeToken<Map<String,OperativeGroup>>() {}.getType());
			assertNotNull(operatives);
		} catch (Exception e) {
			fail(e);
		}
	}
	@Test
	public void connectionsTest() {
		try {
			Map<String, Connection> connections = JsonUtils.fromResource("connections.json", new TypeToken<Map<String, Connection>>() {}.getType());
			assertNotNull(connections);
		} catch (Exception e) {
			fail(e);
		}
	}
}
