package com.fimet.json;

import java.io.IOException;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import com.fimet.parser.Field;

public class JMessageTreeAdapter extends TypeAdapter<JMessageTree>{
	private static Logger logger = LoggerFactory.getLogger(JMessageTreeAdapter.class);
	@Override
	public JMessageTree read(JsonReader in) throws IOException {
		JMessageTree msg = new JMessageTree();
		in.beginObject();
		String name;
		 
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("fields".equals(name)) {
				try {
					readFields(in, msg);
				} catch (Exception e) {
					logger.warn("Error Parsing message",e);
				}
			} else {
				msg.setProperty(name, in.nextString());
			}
		}
		in.endObject();
		return msg;
	}
	private void readFields(JsonReader in, JMessageTree message) throws IOException{
		if (in.hasNext() && in.peek() == JsonToken.NULL) {
			in.nextNull();
		}
		in.beginObject();
		while (in.hasNext()) {
			readField(in, null, message);
		}
		in.endObject();
	}
	private void readField(JsonReader in, Field parent, JMessageTree message) throws IOException {
		if (!in.hasNext()) {
			return;
		}
		String key = in.nextName();
		String idField;
		if (in.hasNext()) {
			idField = parent != null ? (parent+"."+key) : key;
			if (in.peek() == JsonToken.STRING) {
				String value = in.nextString();
				if(parent==null) {
					message.add(new Field(idField, value));
				} else {
					parent.add(new Field(idField, value));
				}
			} else if (in.peek() == JsonToken.BEGIN_OBJECT) {
				in.beginObject();
				if (parent==null) {
					 message.add(parent = new Field(idField));
				} else {
					parent.add(parent = new Field(idField));
				}
				while (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
					readField(in, parent, message);
				}
				in.endObject();
			} else {
				throw new IllegalStateException("Expected a string or Object but was " + in.peek() + in);
			}
		} else {
			throw new MalformedJsonException("Expected a string or Object" + in);
		}
	}
	@Override
	public void write(JsonWriter out, JMessageTree msg) throws IOException {
		if (msg != null) {
			out.beginObject();
			for (Entry<String, String> e : msg.getProperties().entrySet()) {
				if (e.getValue() instanceof String) {
					 out.name(e.getKey()).value(e.getValue().toString());
				 }
			}
			writeFields(out, msg);
			out.endObject();
		} else {
			out.nullValue();
		}
	}
	private void writeFields(JsonWriter out, JMessageTree message) throws IOException{
		if (message.getFields()!=null&&!message.getFields().isEmpty()) {
			out.name("fields");
			out.beginObject();
			for (Entry<String, Field> e : message.getFields().entrySet()) {
				formatField(out, e.getValue());
			}
			out.endObject();
		}
	}
	private void formatField(JsonWriter out, Field field) throws IOException {
		if (field.hasChildren()) {
			out.name(field.getKey());
			out.beginObject();
			for (Field f : field.getChildren()) {
				formatField(out, f);
			}
			out.endObject();
		} else if (field.getValue()!=null){
			out.name(field.getKey());
			out.value(field.getValue());
		}		
	}
}
