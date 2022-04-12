package com.fimet.eglobal.json;

import java.lang.reflect.Type;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rules.Group;
import com.fimet.eglobal.rules.Rule;
import com.fimet.eglobal.rules.Rules;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Classifiers;
import com.fimet.json.JMessageAdapterFactory;
import com.fimet.parser.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class AdapterFactory extends JMessageAdapterFactory {
	
	public static final AdapterFactory INSTANCE = new AdapterFactory();
	
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(AdapterFactory.INSTANCE)
			.create();
	public static final Gson GSON_PRETTY = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(AdapterFactory.INSTANCE)
			.create();
	
	private static final Type connectionType = new TypeToken<Connection>() {}.getType();
	private static final Type operativeType = new TypeToken<Classification>() {}.getType();
	private static final Type operativeGroupType = new TypeToken<Classifier>() {}.getType();
	private static final Type parserType = new TypeToken<IParser>() {}.getType();
	private static final Type rulesType = new TypeToken<Rules>() {}.getType();
	private static final Type groupType = new TypeToken<Group>() {}.getType();
	private static final Type ruleType = new TypeToken<Rule>() {}.getType();
	private static final Type classifiersType = new TypeToken<Classifiers>() {}.getType();
	
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(operativeGroupType)) {
			return (TypeAdapter<T>)new ClassifierAdapter((TypeAdapter<Classifier>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(operativeType)) {
			return (TypeAdapter<T>)new ClassificationAdapter((TypeAdapter<Classification>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(connectionType)) {
			return (TypeAdapter<T>)new ConnectionAdapter((TypeAdapter<Connection>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(rulesType)) {
			return (TypeAdapter<T>)new RulesAdapter((TypeAdapter<Rules>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(groupType)) {
			return (TypeAdapter<T>)new GroupAdapter((TypeAdapter<Group>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(ruleType)) {
			return (TypeAdapter<T>)new RuleAdapter((TypeAdapter<Rule>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(classifiersType)) {
			return (TypeAdapter<T>)new ClassifiersAdapter((TypeAdapter<Classifiers>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(parserType)) {
			return (TypeAdapter<T>)new IParserAdapter();
		} else {
			return super.create(gson, type);
		}
	}
}

