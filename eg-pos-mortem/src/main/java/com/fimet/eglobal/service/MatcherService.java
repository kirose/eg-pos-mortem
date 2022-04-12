package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.JPaths;
import com.fimet.eglobal.classification.IRule;
import com.fimet.eglobal.desc.DescResponse;
import com.fimet.eglobal.matcher.Match;
import com.fimet.eglobal.matcher.MatcherException;
import com.fimet.eglobal.matcher.MatcherResponse;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rawcom.RawcomResponse;
import com.fimet.eglobal.rules.Group;
import com.fimet.eglobal.rules.Result;
import com.fimet.eglobal.rules.Rule;
import com.fimet.eglobal.store.DataReader;
import com.fimet.eglobal.store.Index;
import com.fimet.eglobal.store.IndexReader;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;
import com.fimet.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;

@Service
public class MatcherService {
	
	private static Logger logger = LoggerFactory.getLogger(MatcherService.class);
	private static final Configuration configuration = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
	private static final Pattern DESC_TIME_PATTERN = Pattern.compile("(\"descTime\":\"[^\"]+\",)");
	
	@Autowired private RawcomService rawcomService;
	@Autowired private DescService descService;
	@Autowired private ConfigService config;
	private ParseContext parser = JsonPath.using(configuration);
	
	public MatcherResponse analyze(Date start, Date end) throws IOException, StoreException, MatcherException {
		RawcomResponse rawRes = rawcomService.analyze(start, end);
		DescResponse descRes = descService.analyze(start, end);
		
		return analyze(rawRes, descRes);
	}
	public MatcherResponse analyze(RawcomResponse rawRes, DescResponse descRes) throws IOException, StoreException, MatcherException {
		logger.debug("Analiyze rawcom:{}, desc:{}", rawRes.getId(), descRes.getId());
		IndexReader idxRdrRaw  = new IndexReader(new File(config.getRawcomOutputFolder(), "Rawcom-index-"+rawRes.getId()+".txt"));
		DataReader dtaRdrRaw   = new DataReader(new File(config.getRawcomOutputFolder(), "Rawcom-data-"+rawRes.getId()+".txt"));
		IndexReader idxRdrDesc = new IndexReader(new File(config.getDescOutputFolder(), "Desc-index-"+descRes.getId()+".txt"));
		DataReader dtaRdrDesc  = new DataReader(new File(config.getDescOutputFolder(), "Desc-data-"+descRes.getId()+".txt"));
		Index idxRaw;
		Index idxDesc = null;
		boolean matches = false;
 		String id = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		File data = new File(config.getRawcomOutputFolder(), "Match-data-"+id+".txt");
		File index = new File(config.getRawcomOutputFolder(), "Match-index-"+id+".txt");
		Store storeMatch = new Store(index, data);
		data = new File(config.getRawcomOutputFolder(), "RuleVal-data-"+id+".txt");
		index = new File(config.getRawcomOutputFolder(), "RuleVal-index-"+id+".txt");
		Store storeRule = new Store(index, data);
		while (idxRdrRaw.hasNext()) {
			idxRaw = idxRdrRaw.next();
			matches = false;
			while (idxRdrDesc.hasNext()) {
				idxDesc = idxRdrDesc.next();
				if (idxDesc.getKey() == idxRaw.getKey()) {
					matches = true;
					break;
				}
			}
			String jsonRaw = dtaRdrRaw.read(idxRaw);
			Match match = new Match(jsonRaw);
			String jsonDesc = null;
			if (matches) {
				jsonDesc = dtaRdrDesc.read(idxDesc);
				match.setDesc(jsonDesc);
			}
			String jsonMatch = createJsonMatch(idxRaw.getKey(), jsonRaw, jsonDesc);
			storeMatch.save(idxRaw.getKey(), jsonMatch);
			DocumentContext json = parser.parse(jsonMatch);
			List<Classification> classifications = classify(json);
			JsonObject result = new JsonObject();
			result.addProperty("key", idxRaw.getKey());
			addClassifications(result, classifications);
			validate(result, classifications, json);
			save(storeRule, idxRaw.getKey(), result);
		}
		idxRdrRaw.close();
		dtaRdrRaw.close();
		idxRdrDesc.close();
		dtaRdrDesc.close();
		storeRule.close();
		storeMatch.close();
		return new MatcherResponse(id);
		
	}
	private void addClassifications(JsonObject json, List<Classification> classifications) {
		JsonArray items = new JsonArray();
		for (Classification c : classifications) {
			items.add(c.getName());
		}
		json.add("classifications", items);		
	}

	private String createJsonMatch(long key, String jsonRaw, String jsonDesc) throws MatcherException {
		jsonRaw = jsonRaw.trim();
		if (jsonDesc == null) {
			return jsonRaw;
		} else {
			String keyProperty = "\"key\":"+key+",";
			jsonDesc = jsonDesc.trim().replace(keyProperty, "");
			jsonRaw = jsonRaw.trim().replace(keyProperty, "");
			Matcher m = DESC_TIME_PATTERN.matcher(jsonDesc);
			String descTime;
			if (m.find()) {
				descTime = m.group(1);
				jsonDesc = jsonDesc.replace(descTime, "");
			} else {
				throw new MatcherException("Uknow property descTime");
			}
			return "{"
					 +keyProperty
					+ descTime
					+ jsonRaw.substring(1, jsonRaw.length()-1)
					+ ","+jsonDesc.substring(1, jsonDesc.length()-1)
				+ "}";			
		}
	}
	private List<Classification> classify(DocumentContext json) {
		List<Classification> matches = new ArrayList<>();
		String iap = json.read(JPaths.ACQ_REQ_IAP);
		Connection connection = config.getConnections().get(iap);
		Classifier classifier = connection.getClassifier();
		Map<String, Classification> classifications = classifier.getClassifications();
		for (Entry<String, Classification> e : classifications.entrySet()) {
			boolean matchesOperative = true;
			for (IRule r : e.getValue().getRules()) {
				if (!r.eval(json)) {
					matchesOperative = false;
					break;
				}
			}
			if (matchesOperative) {
				matches.add(e.getValue());
			}
		}
		return matches;
	}
	public JsonObject validate(JsonObject root, List<Classification> classifications, DocumentContext json) {
		JsonArray results = new JsonArray();
		root.add("validations", results);
		for (Group group : config.getRules().getGroups()) {
			if (group.matches(classifications, json)) {
				for (Rule rule : group.getRules()) {
					JsonObject result = evaluateRule(rule, json);
					if (result!=null) {
						results.add(result);
					}
				}
			}
		}
		return root;
	}
	private JsonObject evaluateRule(Rule rule, DocumentContext json) {
		try {
			Result eval = rule.eval(json);
			JsonObject result = new JsonObject();
			result.addProperty("name", rule.getName());
			result.addProperty("rule", rule.getOperator().toString());
			if (eval.getArguments()!=null) {
				JsonArray args = new JsonArray();
				for (String arg : eval.getArguments()) {
					args.add(arg);
				}
				result.add("args", args);
			}
			result.addProperty("correct", eval.getValue());
			return result;
		} catch (Exception e) {
			logger.error("Exception in rule:{}, json:",rule,json);
		}
		return null;
	}
	private void save(Store store, long key, JsonObject json) throws StoreException {
		String text = JsonUtils.toJson(json);
		store.save(key, text);
	}
}
