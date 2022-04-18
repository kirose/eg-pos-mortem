package com.fimet.eglobal.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


import com.fimet.eglobal.json.AdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;

public final class JsonUtils {
	private static final Configuration configuration = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
	private static ParseContext parser = JsonPath.using(configuration);
	private JsonUtils() {
	}
	public static <T>T fromFile(File file, Type type) throws IOException{
		String json = new String(Files.readAllBytes(file.toPath()));
		T instance = AdapterFactory.GSON.fromJson(json, type);
		return instance;
	}
	public static <T>T fromFile(File file, Class<T> clazz) throws IOException{
		String json = new String(Files.readAllBytes(file.toPath()));
		T instance = clazz.cast(AdapterFactory.GSON.fromJson(json, clazz));
		return instance;
	}
	public static <T>T fromResource(String name, Class<T> clazz) throws IOException{
		URL resource = JsonUtils.class.getResource("../../../../"+name);
		if (resource==null)
			throw new FileNotFoundException(name);
		String path = resource.getPath();
		path = path.replaceFirst("^/(.:/)", "$1");
		String json = new String(Files.readAllBytes(Paths.get(path)));
		T instance = clazz.cast(AdapterFactory.GSON.fromJson(json, clazz));
		return instance;
	}
	public static <T>T fromResource(String name, Type type) throws IOException {
		URL resource = JsonUtils.class.getResource("../../../../"+name);
		if (resource==null)
			throw new FileNotFoundException(name);
		String path = resource.getPath();
		path = path.replaceFirst("^/(.:/)", "$1");
		String json = new String(Files.readAllBytes(Paths.get(path)));
		T instance = AdapterFactory.GSON.fromJson(json, type);
		return instance;
	}
	public static <T>T fromJson(String json, Class<T> clazz) {
		return AdapterFactory.GSON.fromJson(json, clazz);
	}
	public static <T> T fromJson(String json, Type typeOfT) {
		return AdapterFactory.GSON.fromJson(json, typeOfT);
	}
	public static String toJson(Object object) {
		return AdapterFactory.GSON.toJson(object);
	}
	public static String toPrettyJson(Object object) {
		return AdapterFactory.GSON_PRETTY.toJson(object);
	}
	public static Map<String,Object> parseJsonAsMap(String json){
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		return AdapterFactory.GSON_PRETTY.fromJson(json, type);
	}
	public static DocumentContext jaywayParse(String json){		
		return parser.parse(json);
	}
}
