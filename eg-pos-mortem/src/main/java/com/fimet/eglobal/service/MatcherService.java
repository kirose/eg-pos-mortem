package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
import com.fimet.eglobal.matcher.MatcherException;
import com.fimet.eglobal.matcher.MatcherResponse;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rawcom.RawcomResponse;
import com.fimet.eglobal.store.DataReader;
import com.fimet.eglobal.store.Index;
import com.fimet.eglobal.store.IndexReader;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;
import com.fimet.eglobal.utils.JsonUtils;
import com.jayway.jsonpath.DocumentContext;

@Service
public class MatcherService {
	
	private static Logger logger = LoggerFactory.getLogger(MatcherService.class);
	private static final Pattern DESC_TIME_PATTERN = Pattern.compile("(\"descTime\":\"[^\"]+\",)");
	
	@Autowired private RawcomService rawcomService;
	@Autowired private DescService descService;
	@Autowired private ConfigService config;
	
	public MatcherResponse analyze(Date start, Date end) throws IOException, StoreException, MatcherException {
		RawcomResponse rawRes = rawcomService.analyze(start, end);
		DescResponse descRes = descService.analyze(start, end);
		
		return analyze(rawRes, descRes);
	}
	public MatcherResponse analyze(RawcomResponse rawRes, DescResponse descRes) throws IOException, StoreException, MatcherException {
		logger.debug("Analiyze rawcom:{}, desc:{}", rawRes.getId(), descRes.getId());
		IndexReader idxRdrRaw  = new IndexReader(new File(config.getRawcomOutputFolder(), "Rawcom-index-"+rawRes.getId()+".txt"));
		DataReader dtaRdrRaw   = new DataReader(new File(config.getRawcomOutputFolder(), "Rawcom-"+rawRes.getId()+".txt"));
		IndexReader idxRdrDesc = new IndexReader(new File(config.getDescOutputFolder(), "Desc-index-"+descRes.getId()+".txt"));
		DataReader dtaRdrDesc  = new DataReader(new File(config.getDescOutputFolder(), "Desc-"+descRes.getId()+".txt"));
		Index idxRaw;
		Index idxDesc = null;
		boolean matches = false;
 		String id = rawRes.getId();
		File data = new File(config.getRawcomOutputFolder(), "Match-"+id+".txt");
		File index = new File(config.getRawcomOutputFolder(), "Match-index-"+id+".txt");
		Store storeMatch = new Store(index, data);
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
			String jsonDesc = null;
			if (matches) {
				jsonDesc = dtaRdrDesc.read(idxDesc);
			}
			try {
				String jsonMatch = createJsonMatch(idxRaw.getKey(), jsonRaw, jsonDesc);
				DocumentContext json = JsonUtils.jaywayParse(jsonMatch);
				String classifications = classify(json);
				jsonMatch = addKeyAndCls(jsonMatch, idxRaw.getKey(), classifications);
				storeMatch.save(idxRaw.getKey(), jsonMatch);
			} catch (Exception e) {
				logger.error("Matcher processing exception",e);
			}
		}
		idxRdrRaw.close();
		dtaRdrRaw.close();
		idxRdrDesc.close();
		dtaRdrDesc.close();
		storeMatch.close();
		return new MatcherResponse(id);
		
	}
	private String addKeyAndCls(String jsonMatch, Long key, String classifications) {
		String keyPty = "\"key\":"+key+",";
		String keyCls = "\"classifications\":"+classifications+",";
		return "{"+keyPty + keyCls + jsonMatch.substring(1);
	}
	private String classify(DocumentContext json) {
		StringBuilder matches = new StringBuilder("[");
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
				matches.append("\"").append(e.getValue().getName()).append("\",");
			}
		}
		if (matches.length() > 1) {
			matches.delete(matches.length()-1, matches.length());
		}
		matches.append("]");
		return matches.toString();
	}
	private String createJsonMatch(long key, String jsonRaw, String jsonDesc) throws MatcherException {
		jsonRaw = jsonRaw.trim();
		String keyPty = "\"key\":"+key+",";
		jsonRaw = jsonRaw.replace(keyPty, "");
		if (jsonDesc == null) {
			return jsonRaw;
		} else {
			jsonDesc = jsonDesc.trim().replace(keyPty, "");
			Matcher m = DESC_TIME_PATTERN.matcher(jsonDesc);
			String descTime;
			if (m.find()) {
				descTime = m.group(1);
				jsonDesc = jsonDesc.replace(descTime, "");
			} else {
				throw new MatcherException("Uknow property descTime");
			}
			return "{"
					+ descTime
					+ jsonRaw.substring(1, jsonRaw.length()-1)
					+ ","+jsonDesc.substring(1, jsonDesc.length()-1)
				+ "}";			
		}
	}
}
