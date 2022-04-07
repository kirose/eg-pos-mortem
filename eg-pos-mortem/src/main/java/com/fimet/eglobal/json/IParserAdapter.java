package com.fimet.eglobal.json;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.IParser;

public class IParserAdapter extends TypeAdapter<IParser>{
	private static IParserManager parserManager = Manager.getContext().getBean(IParserManager.class);
	public IParserAdapter() {
	}
	@Override
	public IParser read(JsonReader in) throws IOException {
		String name = in.nextString();
		IParser parser = parserManager.getParser(name);
		return parser;
	}
	@Override
	public void write(JsonWriter out, IParser value) throws IOException {
		out.value(value.getName());
	}
}
