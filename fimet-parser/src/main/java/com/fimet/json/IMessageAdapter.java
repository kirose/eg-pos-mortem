package com.fimet.json;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.Field;
import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.parser.ParserException;
import com.fimet.utils.ByteBuilder;

public class IMessageAdapter extends TypeAdapter<Message>{
	private static Logger logger = LoggerFactory.getLogger(IMessageAdapter.class);
	private static IParserManager parserManager = Manager.getContext().getBean(IParserManager.class);
	private static IAdapterManager adapterManager = Manager.getContext().getBean(IAdapterManager.class);
	@Override
	public Message read(JsonReader in) throws IOException {
		Message msg = new Message();
		in.beginObject();
		String name;
		 
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("parser".equals(name)) {
				msg.setProperty(name, parserManager.getParser(in.nextString()));
			} else if ("adapter".equals(name)) {
				msg.setProperty(name, adapterManager.getAdapter(in.nextString()));
			} else if ("fields".equals(name)) {
				if (msg.getParser() == null) {
					throw new ParserException("message.parser must be declared before message.fields");
				}
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
		if (msg.getParser() == null) {
			throw new ParserException("message.parser is required");
		}
		return msg;
	}
	private void readFields(JsonReader in, IMessage message) throws IOException{
		if (in.hasNext() && in.peek() == JsonToken.NULL) {
			in.nextNull();
		}
		in.beginObject();
		boolean hasChildren = false;
		while (in.hasNext()) {
			readField(in, null, message);
		}
		in.endObject();
		if (hasChildren) {
			
		}
	}
	private boolean readField(JsonReader in, String idParent, IMessage message) throws IOException {
		if (!in.hasNext()) {
			return false;
		}
		String key = in.nextName();
		String idField;
		if (in.hasNext()) {
			idField = idParent != null ? (idParent+"."+key) : key;
			if (in.peek() == JsonToken.STRING) {
				String value = in.nextString();
				message.setValue(idField, value);
				return false;
			} else if (in.peek() == JsonToken.BEGIN_OBJECT) {
				message.setValue(idField, (byte[])null);
				in.beginObject();
				while (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
					readField(in, idField, message);
				}
				formatParent(idParent, message);
				in.endObject();
				return true;
			} else {
				throw new IllegalStateException("Expected a string or Object but was " + in.peek() + in);
			}
		} else {
			throw new MalformedJsonException("Expected a string or Object" + in);
		}
	}
	private void formatParent(String idField, IMessage message) {
		try {
			ByteBuilder writer = new ByteBuilder();
			message.getParser().getFieldGroup().format(idField, message, writer);
			message.setValue(idField, writer.getBytes());
		} catch (Exception e) {
			logger.warn("Cannot format field "+idField+": "+e.getMessage(),e);
		}
	}
	@Override
	public void write(JsonWriter out, Message msg) throws IOException {
		if (msg != null) {
			out.beginObject();
			for (Entry<String, Object> e : msg.getProperties().entrySet()) {
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
	private void writeFields(JsonWriter out, Message message) throws IOException{
		List<Field> roots = message.getRootsAsList();
		if (roots!=null&&!roots.isEmpty()) {
	 		out.name("fields");
	 		out.beginObject();
	 		for (Field field : roots) {
	 			formatField(out, field);
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
		} else if (field.getBytes()!=null) {
			out.name(field.getKey());
			out.value(field.getValue());
		}
	}
}
