package com.fimet.utils;


import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.json.JMessageMap;
import com.fimet.json.JMessageTree;
import com.fimet.json.JMessageAdapterFactory;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.Message;
import com.fimet.parser.Field;
import com.fimet.parser.IAdapter;
import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStringAdapter;

/**
 * 
 * @author Marco A. Salazar
 *
 */
public class MessageUtils {
	private static Logger logger = LoggerFactory.getLogger(MessageUtils.class);
	private MessageUtils() {}

	public static final String REGEXP_JSON = "(?s)^\\s*\\{\\s*\\\"[a-zA-Z]+.*";
	private static IAdapterManager adapterManager = Manager.getManager(IAdapterManager.class);
	private static IParserManager parserManager = Manager.getManager(IParserManager.class);
	public static JMessageMap toMessageJson(IMessage message) {
		String json = toJson(message);
		return JMessageAdapterFactory.fromJson(json, JMessageMap.class);
	}
	public static String toJsonPretty(IMessage message) {
		return JMessageAdapterFactory.toPrettyJson(message);
	}
	public static String toJson(IMessage message) {
		return JMessageAdapterFactory.toJson(message);
	}
	public static JMessageMap fromStringToJMessageMap(String json) {
		return JMessageAdapterFactory.fromJson(json, JMessageMap.class);
	}
	static Message fromJson(JMessageMap json, IParser parser) {
		Message message = new Message();
		for (Entry<String, String> e : json.getProperties().entrySet()) {
			if (IMessage.PARSER.equals(e.getKey())) {
				message.setProperty(e.getKey(), parserManager.getParser(e.getValue()));	
			} else if (IMessage.ADAPTER.equals(e.getKey())) {
				message.setProperty(e.getKey(), adapterManager.getAdapter(e.getValue()));
			} else {
				message.setProperty(e.getKey(), e.getValue());
			}
		}
		if (parser != null) {
			message.setParser(parser);
		}
		for (Entry<String, String> e : json.getFields().entrySet()) {
			if (e.getValue() != null) {
				message.setValue(e.getKey(), e.getValue());
			}
		}
		return message;
	}
	public static Message fromJson(JMessageMap json) {
		return fromJson(json, null);
	}
	public static Message fromJson(String json) {
		JMessageMap messageJson = JMessageAdapterFactory.fromJson(json, JMessageMap.class);
		return fromJson(messageJson);
	}
	public static byte[] format(IMessage msg) {
		byte[] bytes = msg.getParser().formatMessage(msg);
		if (msg.hasProperty(Message.ADAPTER)) {
			IAdapter adapter = (IAdapter)msg.getProperty(Message.ADAPTER);
			if (msg.getProperty(Message.ADAPTER) instanceof IByteArrayAdapter) {
				return ((IByteArrayAdapter)adapter).writeByteArray(bytes);
			} else {
				return bytes;
			}
		} else {
			return msg.getParser().formatMessage(msg);
		}
	}
	public static IMessage fromString(String message, String parser) {
		if (message.matches(REGEXP_JSON)) {
			return fromJson(message);
		}
		return fromString(message, parserManager.getParser(parser));
	}
	public static IMessage fromBytes(byte[] message, String parserName) {
		return fromBytes(message, parserManager.getParser(parserName));
	}
	public static IMessage fromBytes(byte[] message, IParser parser) {
		IByteArrayAdapter adapter = adapterManager.adapterFor(message);
		IMessage parseMessage = parser.parseMessage(adapter.readByteArray(message));
		parseMessage.setProperty(Message.ADAPTER, adapter);
		return parseMessage;
	}
	/**
	 * Parse a sim_queue (Hexadecimal) to a Msg
	 * @param message posible hex, ascii, etc.
	 * @param parser the parser
	 * @return message parsed
	 */
	public static IMessage fromString(String message, IParser parser) {
		IStringAdapter adapter = adapterManager.adapterFor(message);
		IMessage parseMessage = parser.parseMessage(adapter.readString(message));
		parseMessage.setProperty(Message.ADAPTER, adapter);
		return parseMessage;
	}

	public static String getPan(IMessage m) {
		String pan = null;
		if (m.hasField(35)) {// Nacional
			pan = m.getValue(35);
			if (pan.indexOf('=') > 0) {
				pan = pan.substring(0,pan.indexOf('='));
			} else if (pan.indexOf('D') > 0) {
				pan = pan.substring(0,pan.indexOf('D'));
			} else {
				pan = null;
			}
		} else if (m.hasField(2)) {// Visa MasterCard
			pan = m.getValue(2).substring(0,16);
		} else if (m.hasField("63.EZ.10.track2")) {// Int
			pan = m.getValue("63.EZ.10.track2");
			pan = pan.substring(0,pan.indexOf('D'));
		} else if (m.hasField("63.EZ.9.track2")) {// TPV
			pan = m.getValue("63.EZ.9.track2");
			pan = pan.substring(0,pan.indexOf('D'));
		}
		return pan;
	}
	public static void setPan(IMessage m, String pan) {
		if (pan != null && pan.length() > 0) {
			if (m.hasField(35)) {// Nacional
				String track2 = changePanToTrack2(pan, m.getValue(35));
				m.setValue(35,track2);
			} else 
			if (m.hasField(2)) {// Visa MasterCard
				String track2 = changePanToTrack2(pan, m.getValue(2));
				m.setValue(2,track2);
			} else
			if (m.hasField("63.EZ.10.track2")) {// Int
				String track2 = changePanToTrack2(pan, m.getValue("63.EZ.10.track2"));
				m.setValue("63.EZ.10.track2",track2);
			} else 
			if (m.hasField("63.EZ.9.track2")) {// TPV
				String track2 = changePanToTrack2(pan, m.getValue("63.EZ.9.track2"));
				m.setValue("63.EZ.9.track2",track2);
			}
		}
	}
	private static String changePanToTrack2(String pan, String track2) {
		int ln = pan.length();
		if (ln <= 6) {//BIN
			return pan+track2.substring(ln);
		} else {
			String oldPan = extractPanFromTrack2(track2);
			int oldLn = oldPan.length();
			if (oldLn > ln) {
				return StringUtils.rightPad(pan + track2.substring(oldLn), track2.length(), 'F');
			} else if (oldLn < ln) {
				int newLn = ln+track2.length()-oldLn;
				if (newLn<38) {
					return StringUtils.rightPad(pan + track2.substring(oldLn), newLn%2==0?newLn:(newLn+1), 'F');	
				} else {
					logger.warn("Replace "+pan+" in track 2 "+track2+" los info");
					return (pan + track2.substring(oldLn)).substring(0,38);
				}
			} else {
				return pan + track2.substring(ln); 
			}
			//logger.warn(MessageUtils.class,"Bad track 2 for "+track2);
		}
	}
	private static String extractPanFromTrack2(String track2) {
		int index = track2.indexOf('=');
		if (index != -1) {
			return track2.substring(0,index);
		} else if ((index = track2.indexOf('D'))!=-1) {
			return track2.substring(0,index);
		}
		return track2;// Bad Track2, maybe pan
	}
	public static JMessageTree toTree(IMessage message) {
		JMessageTree tree = new JMessageTree();
		if (message.getProperties()!=null && !message.getProperties().isEmpty()) {
			for (Entry<String, Object> e : message.getProperties().entrySet()) {
				if (e.getValue() instanceof String)
				tree.setProperty(e.getKey(), (String)e.getValue());
			}
		}
		Map<String, Field> map = message.getRootsAsMap();
		tree.setFields(map);
		return tree;
	}
}
