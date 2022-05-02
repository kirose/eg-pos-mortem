package com.fimet.eglobal.json;

import java.io.IOException;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.fimet.eglobal.rules.Equals;
import com.fimet.eglobal.rules.Exists;
import com.fimet.eglobal.rules.IValueOperator;
import com.fimet.eglobal.rules.NotEquals;
import com.fimet.eglobal.rules.NotExists;
import com.fimet.eglobal.rules.Rule;
import com.fimet.eglobal.rules.SubstringOperator;
import com.fimet.eglobal.rules.ValueOperator;

public class RuleAdapter extends TypeAdapter<Rule>{
	private TypeAdapter<Rule> delegateAdapter;
	public RuleAdapter(TypeAdapter<Rule> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
	}
	@Override
	public Rule read(JsonReader in) throws IOException {
		in.beginObject();
		String name = in.nextName();
		Rule rule = new Rule();
		if ("equals".equals(name)) {
			in.beginArray();
			IValueOperator left = parseArg(in);
			IValueOperator right = parseArg(in);
			in.endArray();
			rule.setOperator(new Equals(left, right));
		} else if ("notEquals".equals(name)) {
			in.beginArray();
			IValueOperator left = parseArg(in);
			IValueOperator right = parseArg(in);
			in.endArray();
			rule.setOperator(new NotEquals(left, right));			
		} else if ("exists".equals(name)) {
			if (in.peek() == JsonToken.BEGIN_ARRAY)
				in.beginArray();
			IValueOperator arg = parseArg(in);
			if (in.peek() == JsonToken.END_ARRAY)
				in.endArray();
			rule.setOperator(new Exists(arg));
		} else if ("notExists".equals(name)) {
			if (in.peek() == JsonToken.BEGIN_ARRAY)
				in.beginArray();
			IValueOperator arg = parseArg(in);
			if (in.peek() == JsonToken.END_ARRAY)
				in.endArray();
			rule.setOperator(new NotExists(arg));
		} else {
			throw new MalformedJsonException("Unknow funtion "+name+" at "+in);
		}
		in.endObject();
		return rule;
	}
	private IValueOperator parseArg(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.STRING) {
			return new ValueOperator(in.nextString());
		} else if (in.peek() == JsonToken.BEGIN_OBJECT) {
			in.beginObject();
			String name = in.nextName();
			IValueOperator op;
			if ("substring".equals(name)) {
				String jpath = in.nextString();
				if (!"start".equals(in.nextName())) {
					throw new MalformedJsonException("Expected start proprty at "+in);
				}
				int start = in.nextInt();
				int end = -1;
				if (in.peek() == JsonToken.NAME) {
					if (!"end".equals(in.nextName())) {
						throw new MalformedJsonException("Expected end proprty at "+in);
					}
					end = in.nextInt();
				}
				op = new SubstringOperator(jpath, start, end);
			} else {
				throw new MalformedJsonException("Unknow funtion at "+in);
			}
			in.endObject();
			return op;
		} else  {
			throw new MalformedJsonException("Unknow funtion at "+in);
		}
	}
	@Override
	public void write(JsonWriter out, Rule value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
