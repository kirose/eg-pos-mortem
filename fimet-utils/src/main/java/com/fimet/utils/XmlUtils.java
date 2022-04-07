package com.fimet.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public final class XmlUtils {
	private XmlUtils() {}
	public static <T> T fromFile(File file, Class<T> clazz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);              
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    Object unmarshal = jaxbUnmarshaller.unmarshal(file);
		    return clazz.cast(unmarshal);
		} catch (Exception e) {
			throw new RuntimeException("Error parsing xml "+file,e);
		}
	}
	public static <T> T fromPath(String path, Class<T> clazz) {
		return fromFile(new File(path), clazz);
	}
	public static void writeXml(Object instance, File out) {
		try {
			JAXBContext context = JAXBContext.newInstance(instance.getClass());  
		    Marshaller marshaller = context.createMarshaller();  
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
		    marshaller.marshal(instance, new FileOutputStream(out));
		} catch (Exception e) {
			throw new RuntimeException("Error formating xml "+out,e);
		}
	}
	public static <T> String toXml(Object instance, Class<T> clazz) {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);  
		    Marshaller marshaller = context.createMarshaller();  
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    StringWriter sw = new StringWriter();
		    marshaller.marshal(instance, sw);
		    return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error formating xml "+instance,e);
		}
	}

}
