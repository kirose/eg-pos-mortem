package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.model.Operative;
import com.fimet.eglobal.model.OperativeGroup;
import com.fimet.eglobal.utils.JsonUtils;
import com.fimet.parser.IParser;

public class ConnectionAdapter extends TypeAdapter<Connection>{
	private static Logger logger = LoggerFactory.getLogger(ConnectionAdapter.class);
	private static IParserManager parserManager = Manager.getContext().getBean(IParserManager.class);
	private TypeAdapter<Connection> delegateAdapter;
	private Map<String,OperativeGroup> operatives;
	public ConnectionAdapter(TypeAdapter<Connection> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		try {
			operatives = JsonUtils.fromResource("operatives.json", new TypeToken<Map<String,OperativeGroup>>() {}.getType());
		} catch (IOException e) {
			logger.error("Error parsing operatives.json", e);
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
			} else if ("operatives".equals(name)) {
				in.beginArray();
				List<Operative> operatives = new ArrayList<Operative>();
		 		while (in.hasNext() && in.peek() != JsonToken.END_ARRAY) {
					value = in.nextString();
					String[] split = value.split("\\.");
					String groupName = split[0];
					String operativeName = split[1];
					OperativeGroup group = this.operatives.get(groupName);
					if (group==null) {
						logger.warn("Unknow operative group:{}",group);
						continue;
					}
					Operative operative = group.getOperatives().get(operativeName);
					if (operative==null) {
						logger.warn("Unknow operative:{}",value);
						continue;
					}
					operatives.add(operative);
		 		}
		 		connection.setOperatives(operatives);
				in.endArray();
			} else {
				logger.error("Uknow property {} for connection {}", name, in);
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
