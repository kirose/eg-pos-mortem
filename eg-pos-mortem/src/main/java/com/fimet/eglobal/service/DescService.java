package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fimet.ParserManager;
import com.fimet.eglobal.desc.Desc;
import com.fimet.eglobal.desc.DescReader;
import com.fimet.eglobal.desc.DescRequest;
import com.fimet.eglobal.desc.DescResponse;
import com.fimet.eglobal.rawcom.SortedList;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;
import com.fimet.parser.Field;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.utils.DateUtils;
import com.fimet.utils.FileUtils;
import com.fimet.utils.JsonUtils;
import com.google.gson.JsonObject;

@Service
public class DescService {
	private static Logger logger = LoggerFactory.getLogger(DescService.class);
	@Autowired private ConfigService configService;
	@Autowired private ParserManager parserManager;
	public DescService() {
	}
	@PostConstruct
	private void start() throws IOException {
	}
 	public DescResponse analyze(Date start, Date end) throws IOException, StoreException {
 		logger.info("Analyze start:{}, end:{}", start, end);
 		
		String mmddyy = new SimpleDateFormat("MMddyy").format(start);
		File fileBase = new File(configService.getDescInputFolder(), "desc"+mmddyy+".pos.LR.1.desa");
		if (!fileBase.exists()) {
			fileBase = new File(configService.getDescInputFolder(), "desc"+mmddyy+".pos.LR.1.txt");
		}
		File fileAdditional = new File(configService.getDescInputFolder(), "desc"+mmddyy+".pos.LR.1.AD.desa");
		if (!fileAdditional.exists()) {
			fileAdditional = new File(configService.getDescInputFolder(), "desc"+mmddyy+".pos.LR.1.AD.txt");
		}
 		DescRequest req = new DescRequest(start, end, fileBase, fileAdditional, configService.getDescCacheSize());
 		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
 		String id = fmt.format(start)+"-"+fmt.format(end);
		File data = new File(configService.getRawcomOutputFolder(), "Desc-"+id+".txt");
		File index = new File(configService.getRawcomOutputFolder(), "Desc-index-"+id+".txt");
		req.setFileData(data);
		req.setFileIndex(index);
		Store store = new Store(req.getFileIndex(), req.getFileData());
		req.setStore(store);
		analyze(req);
		
		DescResponse res = new DescResponse(id);
		
		return res;
	}
	private void analyze(DescRequest req) throws StoreException, IOException {
		req.prepareReaders();
		req.populateCache();
		matchDescAndAdditionals(req);
		closeReaders(req);
		req.getStore().close();
	}
	private void closeReaders(DescRequest req) {
		FileUtils.close(req.getReaderBase());
		FileUtils.close(req.getReaderAdditional());
	}
	public void matchDescAndAdditionals(DescRequest req) throws StoreException, NumberFormatException, IOException {
		DescReader reader = req.getReaderBase();
		if (reader==null) {
			return;
		}
		Desc desc;
		Map<Long, List<String>> cache = req.getAdditionalsCache();
		IMessage msg;
		SortedList<Desc> descQueue = req.getDescQueue();
		while (reader.hasNext() && reader.peek().getTime().before(req.getEnd())) {
			desc = reader.next();
			msg = parserManager.getParser("Desc00").parseMessage(desc.getLine().getBytes());
			desc.setMessage(msg);
			if (cache.containsKey(desc.getSequence())) {
				List<String> additionals = cache.get(desc.getSequence());
				List<IMessage> additionalsParsed = new ArrayList<IMessage>();
				for (String add : additionals) {
					String id = add.substring(18, 20);
					IParser parser;
					if ("08".equals(id)) {
						parser = parserManager.getParser("Desc"+id+add.charAt(24));
					} else if ("32".equals(id)) {
						parser = parserManager.getParser("Desc"+id+add.substring(24,27));
					} else {
						parser = parserManager.getParser("Desc"+id);
					}
					msg = parser.parseMessage(add.getBytes());
					additionalsParsed.add(msg);
				}
				desc.setAdditionals(additionalsParsed);
			}
			req.populateCache();
			descQueue.add(desc);
			dispatchDescQueue(descQueue, req.getStore());
		}
		while (!descQueue.isEmpty()) {
			save(req.getStore(), descQueue.removeFirst());
		}
	}
	private void dispatchDescQueue(SortedList<Desc> queue, Store store) throws StoreException {
		if (queue.size() >= configService.getDescQueueSize()) {
			int limit = configService.getDescQueueSize() / 2;
			while (queue.size() > limit) {
				Desc desc = queue.removeFirst();
				save(store, desc);
			}
		}
	}
	private void save(Store store, Desc desc) throws StoreException {
		String descTime = DateUtils.formathhmmssSSS(desc.getTime());
		long key = hashCode(desc);
		JsonObject root = new JsonObject();
		root.addProperty("key", key);
		root.addProperty("descTime", descTime);
		JsonObject desc0= createMessageJsonObject(desc.getMessage());
		root.add("desc0", desc0);
		if (desc.getAdditionals()!=null) {
			for (IMessage msg : desc.getAdditionals()) {
				JsonObject descN = createMessageJsonObject(msg);
				root.add("desc"+Integer.parseInt(msg.getValue("id").substring(1, 3)), descN);
			}
		}
		String json = JsonUtils.toJson(root);
		logger.debug("Desc group at time:{}, json:{}",descTime, json);
		store.save(key, json);
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
		String amount = msg.getValue("amnt");
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
