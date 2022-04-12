package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.eglobal.rules.Group;
import com.fimet.eglobal.rules.Rule;

public class GroupAdapter extends TypeAdapter<Group>{
	private TypeAdapter<Group> delegateAdapter;
	private TypeAdapter<Rule> ruleAdapter;
	public GroupAdapter(TypeAdapter<Group> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		this.ruleAdapter = AdapterFactory.GSON.getAdapter(Rule.class);
	}
	@Override
	public Group read(JsonReader in) throws IOException {
		in.beginObject();
		Group group = new Group();
		List<Rule> rules = new ArrayList<Rule>();
		String name;
		Rule rule;
 		while (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
			name = in.nextName();
			rule = ruleAdapter.read(in);
			rule.setName(name);
			rules.add(rule);
		}
		if (!rules.isEmpty()) {
			group.setRules(rules);
		}
		in.endObject();
		return group;
	}

	@Override
	public void write(JsonWriter out, Group value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
