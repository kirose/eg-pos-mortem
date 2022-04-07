package com.fimet.eglobal.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fimet.IParserManager;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.utils.FileUtils;

@SpringBootTest
public class ParserTest {
	@Autowired private IParserManager parserManager;
	@Test
	public void tpvRequestTest() {
		try {
			byte[] bytes = FileUtils.readBytesContents(new File("src\\test\\resources\\TPVRequestISO.txt"));
			IParser parser = parserManager.getParser("TPV");
			IMessage msg = parser.parseMessage(bytes);
			assertNotNull(msg);
			System.out.println(msg);
		} catch (Exception e) {
			fail(e);
		}
	}
	@Test
	public void tpvResponseTest() {
		try {
			byte[] bytes = FileUtils.readBytesContents(new File("src\\test\\resources\\TPVResponseISO.txt"));
			IParser parser = parserManager.getParser("TPV");
			IMessage msg = parser.parseMessage(bytes);
			assertNotNull(msg);
			System.out.println(msg);
		} catch (Exception e) {
			fail(e);
		}
	}
	@Test
	public void descTest() {
		try {
			byte[] bytes = FileUtils.readBytesContents(new File("src\\test\\resources\\desc.txt"));
			IParser parser = parserManager.getParser("Desc00");
			IMessage msg = parser.parseMessage(bytes);
			assertNotNull(msg);
			System.out.println(msg);
		} catch (Exception e) {
			fail(e);
		}
	}
}
