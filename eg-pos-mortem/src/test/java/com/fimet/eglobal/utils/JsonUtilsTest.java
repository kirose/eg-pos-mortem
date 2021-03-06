package com.fimet.eglobal.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Classifiers;
import com.google.gson.reflect.TypeToken;

@SpringBootTest
public class JsonUtilsTest {
	@Test
	public void operativesTest() {
		try {
			Map<String, Classifier> classifiers = JsonUtils.fromFile(new File("resources/classifiers.json"), Classifiers.class).getClassifiers();
			assertNotNull(classifiers);
		} catch (Exception e) {
			fail(e);
		}
	}
	@Test
	public void connectionsTest() {
		try {
			Map<String, Connection> connections = JsonUtils.fromFile(new File("resources/connections.json"), new TypeToken<Map<String, Connection>>() {}.getType());
			assertNotNull(connections);
		} catch (Exception e) {
			fail(e);
		}
	}
}
