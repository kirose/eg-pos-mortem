package com.fimet.eglobal.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.fimet.eglobal.rules.Equals;
import com.fimet.eglobal.rules.Group;
import com.fimet.eglobal.rules.IBooleanOperator;
import com.fimet.eglobal.rules.Rules;
import com.fimet.eglobal.rules.ValueOperator;
import com.fimet.utils.StringUtils;

public class RulesAdapter extends TypeAdapter<Rules>{
	private static final Pattern FILTER_PATTERN = Pattern.compile("\\[([^\\]]+)\\]\\[([^\\]]+)\\]\\[([^\\]]+)\\](\\[[^\\]]+\\])*");
	private TypeAdapter<Rules> delegateAdapter;
	private TypeAdapter<Group> groupAdapter;
	public RulesAdapter(TypeAdapter<Rules> delegateAdapter) {
		this.delegateAdapter = delegateAdapter;
		this.groupAdapter = AdapterFactory.GSON.getAdapter(Group.class);
	}
	@Override
	public Rules read(JsonReader in) throws IOException {
		in.beginObject();
		Rules rules = new Rules();
		List<Group> groups = new ArrayList<Group>();
		String pattern;
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			pattern = in.nextName();
			Group group = groupAdapter.read(in);
			setPattern(group, pattern);
			groups.add(group);
		}
		in.endObject();
		if (!groups.isEmpty()) {
			rules.setGroups(groups);
		}
		return rules;
	}
	private void setPattern(Group group, String pattern) throws IOException {
		Matcher m = FILTER_PATTERN.matcher(pattern);
		if (m.find()) {
			group.setAcquirerPattern(m.group(1));
			group.setIssuerPattern(m.group(2));
			group.setClassificationPattern(m.group(3));
			String optional = m.group(4);
			if (!StringUtils.isEmpty(optional)) {
				List<IBooleanOperator> optionals = new ArrayList<IBooleanOperator>();
				String[] optinals = optional.substring(1, optional.length()-1).split("\\]\\[");
				for (String filter : optinals) {
					if (filter.indexOf('=')!=-1) {
						String[] parts = filter.split("=");
						optionals.add(new Equals(new ValueOperator(parts[0]), new ValueOperator(parts[1])));
					} else if (filter.indexOf('~')!=-1) {
						String[] parts = filter.split("~");
						optionals.add(new Equals(new ValueOperator(parts[0]), new ValueOperator(parts[1])));
					} else {
						throw new MalformedJsonException("Invalid filter:"+filter+" for rule "+pattern);
					}
				}
				group.setOptionals(optionals);
			}
		} else {
			throw new MalformedJsonException("Invalid group pattern "+pattern);
		}
	} 
	@Override
	public void write(JsonWriter out, Rules value) throws IOException {
		delegateAdapter.write(out, value);
	}
}
