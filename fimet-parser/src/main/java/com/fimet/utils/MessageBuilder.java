package com.fimet.utils;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.Message;
import com.fimet.parser.IAdapter;

public class MessageBuilder {
	static IAdapterManager adapterManager = Manager.getManager(IAdapterManager.class);
	static IParserManager parserManager = Manager.getManager(IParserManager.class);
	IMessage message;
	public MessageBuilder() {
		this.message = new Message();
	}
	public MessageBuilder(Message message) {
		this.message = message;
	}
	public MessageBuilder property(String name, Object value) {
		message.setProperty(name, value);
		return this;
	}
	public MessageBuilder mti(String mti) {
		message.setProperty(Message.MTI, mti);
		return this;
	}
	public MessageBuilder header(String header) {
		message.setProperty(Message.HEADER, header);
		return this;
	}
	public MessageBuilder parser(String parser) {
		message.setParser(parserManager.getParser(parser));
		return this;
	}
	public MessageBuilder parser(IParser parser) {
		message.setParser(parser);
		return this;
	}
	public MessageBuilder adapter(int idAdapter) {
		message.setProperty(Message.ADAPTER, adapterManager.getAdapter(idAdapter));
		return this;
	}
	public MessageBuilder adapter(String adapter) {
		message.setProperty(Message.ADAPTER, adapterManager.getAdapter(adapter));
		return this;
	}
	public MessageBuilder adapter(IAdapter adapter) {
		message.setProperty(Message.ADAPTER, adapter);
		return this;
	}
	public MessageBuilder value(int idField, String value) {
		message.setValue(idField, value);
		return this;
	}
	public MessageBuilder value(String idField, String value) {
		message.setValue(idField, value);
		return this;
	}
	public MessageBuilder remove(int idField) {
		message.remove(idField);
		message.remove(idField);
		return this;
	}
	public MessageBuilder remove(String idField) {
		message.remove(idField);
		return this;
	}
	public IMessage build() {
		return message;
	}
}
