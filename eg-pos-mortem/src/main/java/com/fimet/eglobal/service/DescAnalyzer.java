package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fimet.ParserManager;
import com.fimet.eglobal.desc.Desc;
import com.fimet.eglobal.desc.DescReader;
import com.fimet.eglobal.desc.DescRequest;
import com.fimet.eglobal.desc.DescResponse;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;
import com.fimet.parser.Field;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.utils.DateUtils;
import com.fimet.utils.JsonUtils;
import com.google.gson.JsonObject;

@Service
public class DescAnalyzer {
	private static Logger logger = LoggerFactory.getLogger(DescAnalyzer.class);
	private File outputFolder;
	@Value("${eglobal.desc.cache.size}")
	private int cacheSize;
	@Value("${eglobal.path.desc.input}")
	private String inputPath;
	@Autowired private ConfigService configService;
	@Autowired private ParserManager parserManager;
	@Value("${eglobal.desc.parsers}")
	private List<String> parsersNames;
	private Map<String, IParser> parsers;
	public DescAnalyzer(
			@Value("${eglobal.path.desc.output}") String outputPath
	) throws IOException {
		logger.info("OutputPath:{}",outputPath);
		logger.info("CacheSize:{}",cacheSize);
		logger.info("Parsers:{}",parsersNames);
		outputFolder = new File(outputPath);
		parsers = new HashMap<String, IParser>();
	}
	@PostConstruct
	private void start() throws IOException {
		for (String name : parsersNames) {
			IParser parser = parserManager.getParser(name);
			this.parsers.put(name, parser);
		}
	}
 	public DescResponse analyze(Date start, Date end) throws IOException, StoreException {
 		logger.info("Analyze start:{}, end:{}", start, end);
		String mmddyy = new SimpleDateFormat("MMddyy").format(start);
		File fileBase = new File(inputPath, "desc"+mmddyy+".pos.LR.1.desa");
		File fileAdditional = new File(inputPath, "desc"+mmddyy+".pos.LR.1.AD.desa");
 		DescRequest req = new DescRequest(start, end, fileBase, fileAdditional, cacheSize);
 		long t1 = System.currentTimeMillis();
 		String id = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		File data = new File(this.outputFolder, "Desc-data-"+id+".txt");
		File index = new File(this.outputFolder, "Desc-index-"+id+".txt");
		req.setFileData(data);
		req.setFileIndex(index);
		Store store = new Store(req.getFileIndex(), req.getFileData());
		req.setStore(store);
		analyze(req);
		long t2 = System.currentTimeMillis();
		DescResponse res = new DescResponse(id);
		res.setStartExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t1)));
		res.setEndExecution(DateUtils.formatyyyyMMddhhmmssSSS(new Date(t2)));
		return res;
	}
	public void analyze(DescRequest req) throws StoreException, IOException {
		req.prepareReaders();
		req.populateCache();
		createDescGroups(req);
		req.closeReaders();
		req.getStore().close();
	}
	public void createDescGroups(DescRequest req) throws StoreException {
		DescReader reader = req.getReaderBase();
		Desc desc;
		Map<Long, List<String>> cache = req.getAdditionals();
		IMessage msg;
		while (reader.hasNext() && reader.peek().getTime().before(req.getEnd())) {
			desc = reader.next();
			msg = parsers.get("Desc00").parseMessage(desc.getLine().getBytes());
			desc.setMessage(msg);
			if (cache.containsKey(desc.getSequence())) {
				List<String> additionals = cache.get(desc.getSequence());
				List<IMessage> additionalsParsed = new ArrayList<IMessage>();
				for (String add : additionals) {
					String id = add.substring(18, 20);
					IParser parser;
					if ("08".equals(id)) {
						parser = parsers.get("Desc"+id+add.charAt(24));
					} else if ("32".equals(id)) {
						parser = parsers.get("Desc"+id+add.substring(24,27));
					} else {
						parser = parsers.get("Desc"+id);
					}
					msg = parser.parseMessage(add.getBytes());
					additionalsParsed.add(msg);
				}
				desc.setAdditionals(additionalsParsed);
			}
			classify(desc);
			save(req.getStore(), desc);
		}
	}
	private void classify(Desc desc) {
		
	}
	private void save(Store store, Desc desc) throws StoreException {
		String startTime = DateUtils.formatyyyyMMddhhmmssSSS(desc.getTime());
		long key = hashCode(desc);
		JsonObject root = new JsonObject();
		root.addProperty("key", key);
		root.addProperty("time", startTime);
		JsonObject desc00 = createMessageJsonObject(desc.getMessage());
		root.add("desc00", desc00);
		if (desc.getAdditionals()!=null) {
			for (IMessage msg : desc.getAdditionals()) {
				JsonObject descNN = createMessageJsonObject(msg);
				root.add("desc"+msg.getValue("id").substring(1, 3), descNN);
			}
		}
		String json = JsonUtils.toJson(root);
		logger.debug("Desc group at time:{}, json:{}",startTime, json);
		store.store(key, json);
	}
	private JsonObject createMessageJsonObject(IMessage msg) {
		JsonObject root = new JsonObject();
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
	private long hashCode(Desc req) {
		IMessage msg = req.getMessage();
		String mti = msg.getValue("mti");
		String pan = msg.getValue("track2.pan").trim();
		String amount = msg.getValue("amount");
		String stan = msg.getValue("stan");
		String rrn = msg.getValue("rrn");
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
