package com.fimet.eglobal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.JPaths;
import com.fimet.eglobal.classification.IRule;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rules.Equals;
import com.fimet.eglobal.rules.Exists;
import com.fimet.eglobal.rules.Group;
import com.fimet.eglobal.rules.IBooleanOperator;
import com.fimet.eglobal.rules.IValueOperator;
import com.fimet.eglobal.rules.NotEquals;
import com.fimet.eglobal.rules.NotExists;
import com.fimet.eglobal.rules.Result;
import com.fimet.eglobal.rules.Rule;
import com.fimet.eglobal.rules.SubstringOperator;
import com.fimet.eglobal.rules.ValueOperator;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;
import com.fimet.eglobal.utils.JsonUtils;
import com.fimet.eglobal.validator.Validation;
import com.fimet.eglobal.validator.Validations;
import com.fimet.eglobal.validator.ValidatorReq;
import com.fimet.utils.StringUtils;
import com.jayway.jsonpath.DocumentContext;

@Service
public class ValidatorService {

	private static Logger logger = LoggerFactory.getLogger(RawcomService.class);
	
	private static final Pattern ARGUMENT_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");
	private static final String ADDRESS_PATTERN = "[A-Za-z0-9]+(\\.[A-Za-z0-9\\,\\(\\) ]+)+";
	private static final Pattern FUNCTION_PATTERN = Pattern.compile("(.*)\\.([A-Za-z0-9]+)\\((.*)\\)");
	private Map<Integer, IBooleanOperator> cache = new HashMap<Integer, IBooleanOperator>();
	
	@Autowired private ConfigService config;

	public Validations validate(long key, ValidatorReq req, DocumentContext json) {
		try {
			Validations validations = new Validations();
			validations.setKey(key);
			String classifier = json.read(JPaths.ACQ_CLASSIFIER);
			validations.setClassifier(classifier);
			List<Classification> classifications = classify(json);
			List<String> collect = classifications.stream().map(v->v.getName()).collect(Collectors.toList());
			validations.setClassifications(collect);
			if (req.getValidations() != null) {
				return validations;
			}
	
			validate(validations, classifications, json);
			
			save(req.getStore(), key, validations);
		} catch (Exception e) {
			logger.error("Matcher processing exception",e);
		}
		return null;
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
	public void validate(Validations validations, List<Classification> classifications, DocumentContext json) {
		List<Validation> vals = new ArrayList<Validation>();
		for (Group group : config.getValidations().getGroups()) {
			if (group.matches(classifications, json)) {
				for (Rule rule : group.getRules()) {
					Validation result = evaluateRule(rule, json);
					if (result!=null) {
						vals.add(result);
					}
				}
			}
		}
		if (!vals.isEmpty())
			validations.setValidations(vals);
	}
	private Validation evaluateRule(Rule rule, DocumentContext json) {
		try {
			Result eval = rule.eval(json);
			Validation result = new Validation();
			result.setName(rule.getName());
			result.setRule(rule.getOperator().toString());
			if (eval.getArguments()!=null) {
				result.setArgs(eval.getArguments());
			}
			result.setCorrect(eval.getValue());
			return result;
		} catch (Exception e) {
			logger.error("Exception in evaluateRule:{}, json:{}",rule,json, e);
		}
		return null;
	}
	private void save(Store store, long key, Validations json) throws StoreException {
		String text = JsonUtils.toJson(json);
		store.save(key, text);
	}
	/**
	 * 
	 * @param json
	 * @param blockRules:"[igual][acq.res.39][00]"
	 * @return
	 */
	public List<Validation> validateBlockRules(DocumentContext json, String blockRules) {
		List<IBooleanOperator> rules = parseBlockRules(blockRules);
		List<Validation> results = new ArrayList<Validation>();
		for (IBooleanOperator rule : rules) {
			results.add(evaluateOperator(rule, json));
		}
		return results;
	}
	private Validation evaluateOperator(IBooleanOperator rule, DocumentContext json) {
		try {
			Result eval = rule.eval(json);
			Validation result = new Validation();
			result.setRule(rule.toString());
			if (eval.getArguments()!=null) {
				result.setArgs(eval.getArguments());
			}
			result.setCorrect(eval.getValue());
			return result;
		} catch (Exception e) {
			logger.error("Exception in evaluateOperator:{}, json:",rule, json, e);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<IBooleanOperator> parseBlockRules(String blockRules) {
		if (StringUtils.isEmpty(blockRules)) {
			return Collections.EMPTY_LIST;
		}
		String[] rules = blockRules.split("\n");
		String functionName;
		List<IBooleanOperator> list = new ArrayList<>();
		for (String strRule : rules) {
			strRule = strRule.trim();
			int hash = strRule.hashCode();
			try {
			if (!cache.containsKey(hash)) {
				Matcher m = ARGUMENT_PATTERN.matcher(strRule);
				functionName = parseName(m);
				IBooleanOperator rule = parse(functionName, m);
				cache.put(hash, rule);
			}
			list.add(cache.get(hash));
			} catch (Exception e) {
				logger.error("Parse rule exception", e);
			}
		}
		return list;
	}
	public String parseName(Matcher m) {
		if (m.find()) {
			return m.group(1);
		} else {
			throw new RuntimeException("Invalid rule:"+m.group());
		}		
	}
	public IBooleanOperator parse(String name, Matcher m) {
		if ("igual".equalsIgnoreCase(name)) {
			IValueOperator arg1 = parseArgument(m);
			IValueOperator arg2 = parseArgument(m);
			return new Equals(arg1, arg2);
		} else if ("diferente".equalsIgnoreCase(name)) {
			IValueOperator arg1 = parseArgument(m);
			IValueOperator arg2 = parseArgument(m);
			return new NotEquals(arg1, arg2);
		} else if ("existe".equalsIgnoreCase(name)) {
			IValueOperator arg1 = parseArgument(m);
			return new Exists(arg1);
		} else if ("noexiste".equalsIgnoreCase(name)) {
			IValueOperator arg1 = parseArgument(m);
			return new NotExists(arg1);
		} else {
			throw new RuntimeException("Invalid argument or no present: "+m.group());
		}
	}
	private IValueOperator parseArgument(Matcher m) {
		if (m.find()) {
			String arg = m.group(1);
			if (arg.matches(ADDRESS_PATTERN)) {
				arg = "$."+arg;
				if (arg.endsWith(")")) {
					m = FUNCTION_PATTERN.matcher(arg);
					if (m.find()) {
						arg = m.group(1);
						String fn = m.group(2);
						String args = m.group(3);
						return parseOperator(arg, fn, args);
					}
				}
			}
			return new ValueOperator(arg);
		} else {
			throw new RuntimeException("Invalid argument or no present: "+m.group());
		}
	}
	private IValueOperator parseOperator(String jpath, String name, String args) {
		if ("substring".equalsIgnoreCase(name)) {
			String[] parts = args.split(",");
			int start = Integer.parseInt(parts[0]);
			if (parts.length == 1) {
				return new SubstringOperator(jpath, start);
			} else if (parts.length == 2) {
				int end = Integer.parseInt(parts[1]);
				return new SubstringOperator(jpath, start, end);
			} else {
				throw new RuntimeException("Invalid number of arguments in substring: "+jpath+","+args);
			}
		} else {
			throw new RuntimeException("Invalid operatioin on jpath : "+jpath+"."+name+""+args);
		}
	}
}
