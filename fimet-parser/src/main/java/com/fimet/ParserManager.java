package com.fimet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fimet.dao.IParserDAO;
import com.fimet.event.ParserEvent;
import com.fimet.AbstractManager;
import com.fimet.IParserManager;
import com.fimet.parser.AbstractMessageBitmapParser;
import com.fimet.parser.IEParser;
import com.fimet.parser.IParser;
import com.fimet.parser.ParserException;
import com.fimet.parser.mx.AmexParser;
import com.fimet.parser.mx.DiscoverParser;
import com.fimet.parser.mx.LayoutParser;
import com.fimet.parser.mx.JcbParser;
import com.fimet.parser.mx.MasterCardParser;
import com.fimet.parser.mx.NationalParser;
import com.fimet.parser.mx.TpvParser;
import com.fimet.parser.mx.VisaParser;

@Component
public class ParserManager extends AbstractManager implements IParserManager {
	private static Logger logger = LoggerFactory.getLogger(ParserManager.class);
	@Autowired private IEventManager eventManager;
	@Autowired private IParserDAO<? extends IEParser> dao;
	@Value("${parsers.autoload:false}")
	private boolean autoload;
	Map<String,IParser> mapNameParsers = new HashMap<>();
	public ParserManager() {
		super();
	}
	@PostConstruct
	public void start() {
		logger.info("parsers.autoload:{}", autoload);
		if (autoload) {
			getParsers();
		}
	}
	@Override
	public void reload() {
		mapNameParsers.clear();
	}
	@Override
	public IParser getParser(String name) {
		if (mapNameParsers.containsKey(name)) {
			return mapNameParsers.get(name);
		} else {
			IEParser entity = dao.findByName(name);
			if (entity!=null) {
				IParser parser = newParser(entity);
				mapNameParsers.put(name, parser);
				logger.info("Parser loaded {}",parser);
				eventManager.fireEvent(ParserEvent.PARSER_LOADED, this, parser);
				return parser;
			} else {
				return null;
			}
		}
	}
	@Override
	public IParser getParser(IEParser entity) {
		if (entity.getName() != null) {
			if (mapNameParsers.containsKey(entity.getName())) {
				return mapNameParsers.get(entity.getName());
			} else {
				IParser parser = newParser(entity);
				logger.info("Parser loaded {}",entity.getName());
				mapNameParsers.put(entity.getName(), parser);
				eventManager.fireEvent(ParserEvent.PARSER_LOADED, this, parser);
				return parser;
			}
		} else {
			throw new ParserException("Parser and name is null, invalid entity "+entity);
		}
	}
	@SuppressWarnings("unchecked")
	private IParser newParser(IEParser entity) {
		try {
			Class<? extends AbstractMessageBitmapParser> parserClass = (Class<? extends AbstractMessageBitmapParser>) Class.forName(entity.getParserClass());
			Constructor<? extends AbstractMessageBitmapParser> constructor = parserClass.getConstructor(com.fimet.parser.IEParser.class);
			return constructor.newInstance(entity);
		} catch (ClassNotFoundException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (NoSuchMethodException e) {
			throw new ParserException("No found public constructor with "+IEParser.class.getName()+" as argument in class: " + entity.getParserClass(),e);
		} catch (SecurityException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (IllegalArgumentException | InvocationTargetException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		}
	}

	@Override
	public List<IParser> getParsers() {
		List<? extends IEParser> entities = dao.findAll();
		if (entities != null) {
			List<IParser> ps = new ArrayList<>();
			for (IEParser e : entities) {
				if (mapNameParsers.containsKey(e.getName())) {
					ps.add(mapNameParsers.get(e.getName()));
				} else {
					ps.add(getParser(e));
				}
			}
			return ps;
		} else {
			return null;
		}
	}
	@Override
	public Class<?>[] getParserClasses() {
		return new Class<?>[] {
			NationalParser.class,
			VisaParser.class,
			MasterCardParser.class,
			TpvParser.class,
			DiscoverParser.class,
			AmexParser.class,
			JcbParser.class,
			LayoutParser.class
		};
	}
	@Override
	public IParser reload(String parser) {
		remove(parser);
		return getParser(parser);
	}
	@Override
	public IParser remove(String parser) {
		if (mapNameParsers.containsKey(parser)) {
			IParser p = mapNameParsers.remove(parser);
			eventManager.fireEvent(ParserEvent.PARSER_REMOVED, this, p);
			return p;
		} else {
			return null;
		}
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
