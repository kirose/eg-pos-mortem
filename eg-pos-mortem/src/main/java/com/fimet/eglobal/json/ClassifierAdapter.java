package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;

public class ClassifierAdapter extends TypeAdapter<Classifier>{
	private TypeAdapter<Classifier> delegateAdapter;
	private TypeAdapter<Classification> classificationAdapter;
	public ClassifierAdapter(TypeAdapter<Classifier> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		this.classificationAdapter = AdapterFactory.GSON.getAdapter(Classification.class);
	}
	@Override
	public Classifier read(JsonReader in) throws IOException {
		in.beginObject();
		String name;
		Classifier group = new Classifier();
		Map<String, Classification> classifications = new HashMap<String, Classification>();
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			Classification o = classificationAdapter.read(in);
			o.setName(name);
			classifications.put(name, o);
		}
		in.endObject();
		if (!classifications.isEmpty()) {
			group.setClassifications(classifications);
		}
		return group;
	}
	@Override
	public void write(JsonWriter out, Classifier value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
