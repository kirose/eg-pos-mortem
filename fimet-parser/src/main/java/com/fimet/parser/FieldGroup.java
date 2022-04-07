package com.fimet.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fimet.Manager;
import com.fimet.dao.FieldFormatXmlDAO;
import com.fimet.dao.IFieldFormatDAO;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class FieldGroup implements IFieldGroup {
	static final String mode = Manager.getProperty("field.group.mode", "lazy");
	static Comparator<IFieldParser> comparatorIdOrder = (IFieldParser f1, IFieldParser f2)->{
		return f1.getIdOrder().compareTo(f2.getIdOrder());
	}; 
	private Map<String, IFieldParser> fields;
	private List<IFieldParser> roots;
	private String name;
	private FieldGroup parent;
	private List<FieldGroup> children;
	private IFieldLoader loader;
	@SuppressWarnings("unchecked")
	private IFieldFormatDAO<? extends IEFieldFormat> dao = Manager.getManager(IFieldFormatDAO.class, FieldFormatXmlDAO.class);
	public FieldGroup(IEFieldGroup egroup) {
		fields = new HashMap<String, IFieldParser>();
		this.name = egroup.getName();
		loader = "eager".equalsIgnoreCase(mode) ? new Eager() : new Lazy();
		roots = new ArrayList<>();
	}
	@Override
	public byte[] parse(int idField, IMessage message, IReader reader) {
		return getFieldParser(String.valueOf(idField)).parse(reader, message);
	}
	@Override
	public byte[] parse(String idField, IMessage message, IReader reader) {
		return getFieldParser(idField).parse(reader, message);
	}
	@Override
	public void format(String idField, IMessage message, IWriter writer) {
		getFieldParser(idField).format(writer, message);
	}
	@Override
	public void format(int idField, IMessage message, IWriter writer) {
		getFieldParser(String.valueOf(idField)).format(writer, message);
	}
	@Override
	public short[] getAddress(String idField) {
		IFieldParser field = getFieldParser(idField);
		return field!=null?field.getAddress():null;
	}
	public void setParent(FieldGroup parent) {
		this.parent = parent;
	}
	public FieldGroup getParent() {
		return parent;
	}
	public void addChild(FieldGroup child) {
		if (children == null)
			children = new ArrayList<>();
		children.add(child);
	}
	public IFieldParser getFieldParser(String idField) {
		return loader.getFieldParser(idField);
	}
	IFieldParser loadFieldParser(IEFieldFormat f) {
		try {
			Class<?> classParser = Class.forName(f.getClassParser());
			IFieldParser fieldParser = (IFieldParser) classParser.getConstructor(FieldGroup.class, IEFieldFormat.class).newInstance(this, f);
			fields.put(f.getIdField(), fieldParser);
			return fieldParser;
		} catch (Exception e) {
			throw new ParserException("Invalid field parser for group "+name+": "+f.getClassParser(),e);
		}
	}
	Map<String, IFieldParser> getFields(){
		return fields;
	}
	@Override
	public List<IFieldParser> getRoots() {
		if (!roots.isEmpty())
			return roots;
		loader.load();
		for (Map.Entry<String, IFieldParser> e : fields.entrySet()) {
			if (e.getValue().isRoot()) {
				roots.add(e.getValue());
			}
		}
		Collections.sort(roots, comparatorIdOrder);
		return roots;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return name;
	}
	public void load() {
		loader.load();
	}
	public void reload() {
		loader.reload();
	}
	private interface IFieldLoader {
		/**
		 * Return the Field identified by idField
		 * @param idField
		 * @return
		 */
		IFieldParser getFieldParser(String idField);
		/**
		 * Load parent then own fields
		 */
		void load();
		/**
		 * Load own then children fields
		 */
		void reload();
	}
	private class Eager implements IFieldLoader {
		@Override
		public IFieldParser getFieldParser(String idField) {
			return fields.get(idField);
		}
		private void loadFields() {
			fields.clear();
			List<? extends IEFieldFormat> formats = dao.findByGroup(name);// Own Fields
			if (formats!=null) {
				for (IEFieldFormat f : formats) {
					loadFieldParser(f);
				}
			}
			if (parent != null) {// Inherit Fields
				for (Entry<String, IFieldParser> e : parent.getFields().entrySet()) {
					if (!fields.containsKey(e.getKey())) {
						fields.put(e.getKey(), e.getValue());
					}
				}
			}
		}
		@Override
		public void reload() {
			loadFields();
			if (children!=null) {
				for (FieldGroup g : children) {
					g.loader.reload();
				}
			}
		}
		@Override
		public void load() {}
	}
	private class Lazy implements IFieldLoader {
		@Override
		public IFieldParser getFieldParser(String idField) {
			if (fields.containsKey(idField)) {
				return fields.get(idField);
			}
			if (fields.isEmpty()) {
				load();
			}
			if (fields.containsKey(idField)) {
				return fields.get(idField);
			}
			throw new ParserException("Unkown field "+idField+" for group "+name);
		}
		@Override
		public void load() {
			if (fields.isEmpty()) {
				if (parent!=null&&parent.fields.isEmpty()) {
					parent.loader.load();
				}
				loadFields();
			}
		}
		private void loadFields() {
			fields.clear();
			List<? extends IEFieldFormat> formats = dao.findByGroup(name);// Own Fields
			for (IEFieldFormat f : formats) {
				loadFieldParser(f);
			}
			if (parent != null) {// Inherit Fields
				for (Entry<String, IFieldParser> e : parent.getFields().entrySet()) {
					if (!fields.containsKey(e.getKey())) {
						fields.put(e.getKey(), e.getValue());
					}
				}
			}
		}
		@Override
		public void reload() {
			if (!fields.isEmpty()) {
				loadFields();
			}
			if (children!=null) {
				for (FieldGroup g : children) {
					g.loader.reload();
				}
			}
		}
	}
	@Override
	public List<IFieldGroup> getChildren() {
		return CollectionUtils.cast(children, IFieldGroup.class);
	}
}
