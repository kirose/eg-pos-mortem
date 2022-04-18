package com.fimet.eglobal.json;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Classifiers;
import com.fimet.eglobal.utils.JsonUtils;
import com.fimet.parser.IParser;

public class ConnectionAdapter extends TypeAdapter<Connection>{
	private static Logger logger = LoggerFactory.getLogger(ConnectionAdapter.class);
	private static IParserManager parserManager = Manager.getContext().getBean(IParserManager.class);
	private TypeAdapter<Connection> delegateAdapter;
	private Map<String,Classifier> classifiers;
	public ConnectionAdapter(TypeAdapter<Connection> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		try {
			classifiers = JsonUtils.fromFile(new File("resources/classifiers.json"), Classifiers.class).getClassifiers();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public Connection read(JsonReader in) throws IOException {
		in.beginObject();
		String name;
		String value;
		Connection connection = new Connection();
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("type".equals(name)) {
				value = in.nextString();
				connection.setType(Connection.Type.valueOf(value.toUpperCase()));
			} else if ("alias".equals(name)) {
				value = in.nextString();
				connection.setAlias(value);
			} else if ("sanitizeHex".equals(name)) {
				connection.setSanitizeHex(in.nextBoolean());
			} else if ("parser".equals(name)) {
				value = in.nextString();
				IParser parser = parserManager.getParser(value);
				connection.setParser(parser);
			} else if ("classifier".equals(name)) {
				value = in.nextString();
				Classifier classifier = classifiers.get(value);
				connection.setClassifier(classifier);
			} else {
				logger.error("Unknow property {} for connection {}", name, in);
				throw new MalformedJsonException("End of json exception "+in);
			}
		}
		in.endObject();
		return connection;
	}
	@Override
	public void write(JsonWriter out, Connection value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
