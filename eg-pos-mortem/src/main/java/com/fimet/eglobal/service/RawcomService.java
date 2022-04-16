package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rawcom.Rawcom;
import com.fimet.eglobal.rawcom.RawcomReader;
import com.fimet.eglobal.rawcom.RawcomRequest;
import com.fimet.eglobal.rawcom.RawcomResponse;
import com.fimet.eglobal.rawcom.RawcomType;
import com.fimet.eglobal.rawcom.SortedList;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;
import com.fimet.parser.Field;
import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.utils.DateUtils;
import com.fimet.utils.JsonUtils;
import com.google.gson.JsonObject;

@Service
public class RawcomService {
	private static Logger logger = LoggerFactory.getLogger(RawcomService.class);
	
	@Autowired private ConfigService config;
	
	public RawcomService() {}
	@PostConstruct
	private void start() throws IOException {
	}
	private List<File> findRawcomFiles(Date start, Date end) {
		List<File> files = new ArrayList<File>();
		String yyMMdd = DateUtils.formatyyMMdd(start)+".";
		File folder = config.getRawcomInputFolder();
		File[] allFiles = folder.listFiles();
		for (File file : allFiles) {
			if (file.getName().contains(yyMMdd)) {
				files.add(file);
			}
		}
		return files;
	}
 	public RawcomResponse analyze(Date start, Date end) throws IOException, StoreException {
 		logger.info("Analyze start:{}, end:{}", start, end);
 		List<File> files = findRawcomFiles(start, end);
 		if (files == null || files.isEmpty()) {
 			return new RawcomResponse();
 		}
 		RawcomRequest req = new RawcomRequest(start, end, files, config);
 		long t1 = System.currentTimeMillis();
 		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
 		String id = fmt.format(start)+"-"+fmt.format(end);
		File data = new File(config.getRawcomOutputFolder(), "Rawcom-"+id+".txt");
		File index = new File(config.getRawcomOutputFolder(), "Rawcom-index-"+id+".txt");
		req.setFileData(data);
		req.setFileIndex(index);
		Store store = new Store(req.getFileIndex(), req.getFileData());
		req.setStore(store);
		analyze(req);
		long t2 = System.currentTimeMillis();
		RawcomResponse res = new RawcomResponse(id);
		res.setStartExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t1)));
		res.setEndExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t2)));
		return res;
	}
	private void analyze(RawcomRequest req) throws StoreException, IOException {
		req.prepareReaders();
		req.populateCache();
		createRawcomGroups(req);
		closeReaders(req);
		req.getStore().close();
	}
	public void createRawcomGroups(RawcomRequest req) throws StoreException {
		SortedList<Rawcom> cache = req.getCache().getSortedList();
		Rawcom curr = null;
		Iterator<Rawcom> it;
		while (!cache.isEmpty()) {
			it = cache.iterator();
			while (it.hasNext()) {
				curr = it.next();
				if (curr.getDirection() == 'R'
					&& curr.getType().isRequest()) {
					break;
				}
				logger.debug("Skipped rawcom:{}", curr);
				cache.remove(curr);
			}
			if (curr!=null) {
				List<Rawcom> parts = findParts(cache, curr);
				cache.removeAll(parts);
				save(req.getStore(), parts);
			}
			if (cache.size() < config.getRawcomCacheSize()/2) {
				req.populateCache();
			}
		}
	}
	private List<Rawcom> findParts(SortedList<Rawcom> cache, Rawcom req) {
		Rawcom res = findResponse(cache, req);
		List<Rawcom> parts = new ArrayList<>();
		parts.add(req);
		Date max;
		if (res!=null) {
			max = res.getTime();
		} else {
			max = new Date(req.getTime().getTime()+config.getRawcomRequestTimeout());
		}
		Iterator<Rawcom> it = cache.iterator();
		Rawcom test;
		while (it.hasNext()) {
			test = it.next();
			if (test.getTime().after(max)) {
				break;
			}
			if (test != req && test != res && isPart(req, test)) {
				parts.add(test);
			}
		}
		if (res!=null) {
			parts.add(res);
		}
		return parts;
	}
	private Rawcom findResponse(SortedList<Rawcom> cache, Rawcom req) {
		Iterator<Rawcom> it = cache.iterator();
		Date max = new Date(req.getTime().getTime()+config.getRawcomRequestTimeout());
		Rawcom res;
		while (it.hasNext()) {
			res = it.next();
			if (res.getTime().after(max)) {
				break;
			}
			if (
				res.getDirection() == 'W'
				&& res.getPan().equals(req.getPan())
				&& req.getDispatcher().equals(res.getDispatcher())
				&& req.getIap().equals(res.getIap())
				&& req.getMti()+10 == res.getMti()
			) {
				return res;
			}
		}
		return null;
	}
	private void save(Store store, List<Rawcom> parts) throws StoreException {
		Rawcom first = parts.get(0);
		String reqTime = DateUtils.formathhmmssSSS(first.getTime());
		String resTime = null;
		Rawcom last = null;
		
		if (parts.size() > 1) {
			last = parts.get(parts.size()-1);
			if (last.getType().isResponse() && first.getDispatcher().equals(last.getDispatcher())) {
				resTime = DateUtils.formathhmmssSSS(last.getTime());
			} else {
				last = null;
			}
		}
		long key = hashCode(first);
		JsonObject root = new JsonObject();
		root.addProperty("key", key);
		root.addProperty("reqTime", reqTime);
		root.addProperty("resTime", resTime);
		
		Map<String, JsonObject> map = new HashMap<String, JsonObject>();
		for (Rawcom r : parts) {
			Connection connection = config.getConnections().get(r.getIap());
			JsonObject alias;
			if (map.containsKey(connection.getAlias())) {
				alias = map.get(connection.getAlias());
			} else {
				alias = new JsonObject();
				alias.addProperty("classifier", connection.getClassifier().getName());
				map.put(connection.getAlias(), alias);
				root.add(connection.getAlias(), alias);
			}
			JsonObject msg = new JsonObject();
			msg.addProperty("time", DateUtils.formathhmmssSSS(r.getTime()));
			msg.addProperty("iap", r.getIap());
			msg.addProperty("pan", r.getPan());
			msg.addProperty("dispatcher", String.format("%05d", r.getDispatcher()));
			createMessageJsonObject(msg, r);
			alias.add((r.getType().isRequest() ? "req" : "res"), msg);
		}
		String json = JsonUtils.toJson(root);
		logger.debug("Rawcom group size:{}, time:{}-{}, json:{}",parts.size(), reqTime, resTime, json);
		store.save(key, json);
	}
	private JsonObject createMessageJsonObject(JsonObject root, Rawcom r) {
		Message msg = (Message)r.getMessage();
		root.addProperty("mti", (String)msg.getProperty(IMessage.MTI));
		root.addProperty("header", (String)msg.getProperty(IMessage.HEADER));
		List<Field> roots = msg.getRootsAsList();
		for (Field field : roots) {
			createField(root, field);
		}
		return root;
	}
	private void createField(JsonObject parent, Field field) {
		if (field.hasChildren()) {
			JsonObject value = new JsonObject();
			for (Field child : field.getChildren()) {
				createField(value, child);
			}
			parent.add(field.getKey(), value);
		} else {
			parent.addProperty(field.getKey(), field.getValue());
		}
	}
	private boolean isPart(Rawcom req, Rawcom test) {
		if (!req.getPan().equals(test.getPan())) {
			return false;
		}
		if (!equalsFieldValue(req, test, "4")) {
			return false;
		}
		if (req.getType() == RawcomType.AUTHORIZATION_REQUEST 
			&& !((test.getType() == RawcomType.AUTHORIZATION_REQUEST
				|| test.getType() == RawcomType.AUTHORIZATION_RESPONSE)
		)) {
			return false;
		}
		if (req.getType() == RawcomType.REVERSAL_REQUEST 
			&& !((test.getType() == RawcomType.REVERSAL_REQUEST
				|| test.getType() == RawcomType.REVERSAL_RESPONSE)
		)) {
			return false;
		}
		if (req.getType() == RawcomType.NETWORK_REQUEST 
			&& !((test.getType() == RawcomType.NETWORK_REQUEST
				|| test.getType() == RawcomType.NETWORK_RESPONSE)
		)) {
			return false;
		}
		return true;
	}
	private boolean equalsFieldValue(Rawcom r1, Rawcom r2, String id) {
		String v1 = r1.getMessage().getValue(id);
		String v2 = r2.getMessage().getValue(id);
		if (v1 == null) {
			if (v2 == null) {
				return true; 
			} else {
				return false;
			}
		}
		return v1.equals(v2);
	}
	private void closeReaders(RawcomRequest req) {
		Map<String, RawcomReader> map = req.getReaders();
		for (Map.Entry<String, RawcomReader> e : map.entrySet()) {
			e.getValue().close();
		}
	}
	private long hashCode(Rawcom req) {
		IMessage msg = req.getMessage();
		String mti = (String)msg.getProperty(IMessage.MTI);
		String pan = req.getPan();
		String amount = msg.getValue("4");
		String stan = msg.getValue("11");
		String rrn = msg.getValue("37");
		final int prime = 31;
		long key = 1;
		key = prime * key + ((mti == null) ? 0 : mti.hashCode());
		key = prime * key + ((pan == null) ? 0 : pan.hashCode());
		key = prime * key + ((amount == null) ? 0 : amount.hashCode());
		key = prime * key + ((stan == null) ? 0 : stan.hashCode());
		key = prime * key + ((rrn == null) ? 0 : rrn.hashCode());
		return key;
	}
}
