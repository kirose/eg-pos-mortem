package com.fimet.utils.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public abstract class NumericParser implements INumericParser {

	public static final Map<String, INumericParser> mapNameParser = new HashMap<>();
	public static final INumericParser DEC = new NumericParserDec(0,"Decimal");
	public static final INumericParser DEC_DOUBLE = new NumericParserDecDouble(1,"DecimalDouble");
	public static final INumericParser DEC_HALF = new NumericParserDecHalf(2,"DecimalHalf");
	public static final INumericParser HEX = new NumericParserHex(3,"Hex");
	public static final INumericParser HEX_DOUBLE = new NumericParserHexDouble(4,"HexDouble");
	public static final INumericParser HEX_HALF = new NumericParserHexHalf(5,"HexHalf");
	
	public static INumericParser getParser(String name) {
		return mapNameParser.get(name);
	}
	public static List<INumericParser> getParsers() {
		List<INumericParser> list = new ArrayList<>(mapNameParser.size());
		for (Map.Entry<String, INumericParser> c : mapNameParser.entrySet()) {
			list.add(c.getValue());
		}
		return list;
	}
	private int id;
	private String name;
	NumericParser(int id, String name) {
		this.id = id;
	    this.name = name;
	    mapNameParser.put(name, this);
	}
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	@Override
	public String toString() {
	    return name;
	}
}