package com.fimet.json;


import com.fimet.parser.IMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JMessageAdapterFactory implements TypeAdapterFactory {
	
	public static final JMessageAdapterFactory INSTANCE = new JMessageAdapterFactory();
	
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(JMessageAdapterFactory.INSTANCE)
			.create();
	public static final Gson GSON_PRETTY = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(JMessageAdapterFactory.INSTANCE)
			.create();
	
	private static final Type iMessageType = new TypeToken<IMessage>() {}.getType();
	private static final Type messageMapType = new TypeToken<JMessageMap>() {}.getType();
	private static final Type messageTreeType = new TypeToken<JMessageTree>() {}.getType();
	
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(messageMapType)) {
			return (TypeAdapter<T>)new JMessageMapAdapter();
		} else if (type.getType().equals(messageTreeType)) {
			return (TypeAdapter<T>)new JMessageTreeAdapter();
		} else if (type.getType().equals(iMessageType)) {
			return (TypeAdapter<T>)new IMessageAdapter();
		} else {
			return gson.getDelegateAdapter(this, type);
		}
	}
	public static <T>T fromJson(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}
	public static <T> T fromJson(String json, Type typeOfT) {
		return GSON.fromJson(json, typeOfT);
	}
	public static String toJson(Object object) {
		return GSON.toJson(object);
	}
	public static String toPrettyJson(Object object) {
		return GSON_PRETTY.toJson(object);
	}
	public static Map<String,Object> parseJsonAsMap(String json){
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		return GSON_PRETTY.fromJson(json, type);
	}
}
