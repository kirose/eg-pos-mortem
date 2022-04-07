package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.eglobal.model.Operative;
import com.fimet.eglobal.model.OperativeGroup;

public class OperativeGroupAdapter extends TypeAdapter<OperativeGroup>{
	private TypeAdapter<OperativeGroup> delegateAdapter;
	private TypeAdapter<Operative> operativeAdapter;
	public OperativeGroupAdapter(TypeAdapter<OperativeGroup> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		this.operativeAdapter = AdapterFactory.GSON.getAdapter(Operative.class);
	}
	@Override
	public OperativeGroup read(JsonReader in) throws IOException {
		in.beginObject();
		String name;
		OperativeGroup group = new OperativeGroup();
		Map<String, Operative> operatives = new HashMap<String, Operative>();
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			Operative o = operativeAdapter.read(in);
			o.setName(name);
			operatives.put(name, o);
		}
		in.endObject();
		if (!operatives.isEmpty()) {
			group.setOperatives(operatives);
		}
		return group;
	}
	@Override
	public void write(JsonWriter out, OperativeGroup value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
