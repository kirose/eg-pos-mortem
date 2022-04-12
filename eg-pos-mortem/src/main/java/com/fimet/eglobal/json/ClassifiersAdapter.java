package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Classifiers;

public class ClassifiersAdapter extends TypeAdapter<Classifiers>{
	private TypeAdapter<Classifiers> delegateAdapter;
	private TypeAdapter<Classifier> ruleAdapter;
	public ClassifiersAdapter(TypeAdapter<Classifiers> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		this.ruleAdapter = AdapterFactory.GSON.getAdapter(Classifier.class);
	}
	@Override
	public Classifiers read(JsonReader in) throws IOException {
		in.beginObject();
		Classifiers group = new Classifiers();
		Map<String, Classifier> classifiers = new HashMap<String, Classifier>();
		String name;
		Classifier classifier;
 		while (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
			name = in.nextName();
			classifier = ruleAdapter.read(in);
			classifier.setName(name);
			classifiers.put(name, classifier);
		}
		if (!classifiers.isEmpty()) {
			group.setClassifiers(classifiers);
		}
		in.endObject();
		return group;
	}

	@Override
	public void write(JsonWriter out, Classifiers value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
