package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.fimet.eglobal.model.Operative;
import com.fimet.eglobal.rules.Equals;
import com.fimet.eglobal.rules.EqualsIn;
import com.fimet.eglobal.rules.Exists;
import com.fimet.eglobal.rules.IRule;
import com.fimet.eglobal.rules.Matches;

public class OperativeAdapter extends TypeAdapter<Operative>{
	private static Logger logger = LoggerFactory.getLogger(OperativeAdapter.class);
	private TypeAdapter<Operative> delegateAdapter;
	public OperativeAdapter(TypeAdapter<Operative> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
	}
	@Override
	public Operative read(JsonReader in) throws IOException {
		in.beginObject();
		String name;
		String value;
		Operative operative = new Operative();
		List<IRule> rules = new ArrayList<IRule>();
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if (in.hasNext()) {
				if (in.peek() == JsonToken.STRING) {
					rules.add(new Equals(name, in.nextString()));
				} else if (in.peek() == JsonToken.BEGIN_OBJECT) {
					in.beginObject();
					String key = in.nextName();
					if ("equals".equalsIgnoreCase(key)) {
						value = in.nextString();
						rules.add(new Equals(name, value));
					} else if ("matches".equals(key)) {
						value = in.nextString();
						rules.add(new Matches(name, value));
					} else if ("exists".equals(key)) {
						if (in.peek() == JsonToken.NUMBER) {
							in.nextDouble();
						} else if (in.peek() == JsonToken.BOOLEAN) {
							in.nextBoolean();
						} else {
							in.nextString();
						}
						rules.add(new Exists(name));
					} else {
						logger.error("Parsing json exception {}",in);
						throw new MalformedJsonException("Unexpected key: "+key +" for "+ in);
					}
					in.endObject();
				} else if (in.peek() == JsonToken.BEGIN_ARRAY) {
					in.beginArray();
					List<String> values = new ArrayList<>();
					while (in.hasNext() && in.peek() != JsonToken.END_ARRAY) {
						values.add(in.nextString());
					}
					rules.add(new EqualsIn(name, values));
					in.endArray();
				}
			} else {
				logger.error("End of json exception {}",in);
				throw new MalformedJsonException("End of json exception "+in);
			}
		}
		in.endObject();
		if (!rules.isEmpty()) {
			operative.setRules(rules);
		}
		return operative;
	}
	@Override
	public void write(JsonWriter out, Operative value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
