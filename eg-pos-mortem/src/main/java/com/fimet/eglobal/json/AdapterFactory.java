package com.fimet.eglobal.json;

import java.lang.reflect.Type;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.model.Operative;
import com.fimet.eglobal.model.OperativeGroup;
import com.fimet.json.JMessageAdapterFactory;
import com.fimet.parser.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class AdapterFactory extends JMessageAdapterFactory {
	
	public static final AdapterFactory INSTANCE = new AdapterFactory();
	
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(AdapterFactory.INSTANCE)
			.create();
	public static final Gson GSON_PRETTY = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(AdapterFactory.INSTANCE)
			.create();
	
	private static final Type connectionType = new TypeToken<Connection>() {}.getType();
	private static final Type operativeType = new TypeToken<Operative>() {}.getType();
	private static final Type operativeGroupType = new TypeToken<OperativeGroup>() {}.getType();
	private static final Type parserType = new TypeToken<IParser>() {}.getType();
	
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(operativeGroupType)) {
			return (TypeAdapter<T>)new OperativeGroupAdapter((TypeAdapter<OperativeGroup>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(operativeType)) {
			return (TypeAdapter<T>)new OperativeAdapter((TypeAdapter<Operative>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(connectionType)) {
			return (TypeAdapter<T>)new ConnectionAdapter((TypeAdapter<Connection>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(parserType)) {
			return (TypeAdapter<T>)new IParserAdapter();
		} else {
			return super.create(gson, type);
		}
	}
}

