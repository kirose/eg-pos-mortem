package com.fimet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

import com.fimet.AbstractManager;
import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.dao.IFieldGroupDAO;
import com.fimet.event.ParserEvent;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldGroup;
import com.fimet.parser.IFieldGroup;
import com.fimet.parser.field.*;
import com.fimet.parser.field.mx.*;
import com.fimet.parser.field.tpv.*;
import com.fimet.parser.field.visa.*;
import com.fimet.parser.field.mc.*;
import com.fimet.parser.field.amex.*;
@Component
public class FieldGroupManager extends AbstractManager implements IFieldGroupManager {
	private static Logger logger = LoggerFactory.getLogger(FieldGroupManager.class);
	@Autowired private IEventManager eventManager;
	@Autowired private IFieldGroupDAO<? extends IEFieldGroup> dao;
	@Value("${field.group.autoload}")
	private boolean autoload;
	private Map<String, FieldGroup> mapNameGroup;
	public FieldGroupManager() {
		mapNameGroup = new HashMap<String, FieldGroup>();
	}
	@PostConstruct
	public void start() {
		logger.info("field.group.autoload:{}", autoload);
		reload();
	}
	public void reload() {
		mapNameGroup.clear();
		loadGroups();
		loadFields();
	}
	private void loadFields() {
		if (autoload) {
			FieldGroup curr = null;
			try {
				Set<FieldGroup> roots = getRootGroups();
				for (FieldGroup g : roots) {
						curr = g;
						logger.info("FieldGroup loaded :{}",g);
						g.reload();
				}
			} catch (Exception e) {
				throw new FimetException("LoadFieldGroup Exception "+curr,e);
			}
		}
	}
	private void loadGroups() {
		List<? extends IEFieldGroup> allGroups = dao.findAll();
		for (IEFieldGroup g : allGroups) {
			FieldGroup fg = new FieldGroup(g);
			mapNameGroup.put(g.getName(), fg);
		}
		for (IEFieldGroup g : allGroups) {
			if (g.getParent() != null) {
				FieldGroup parent = mapNameGroup.get(g.getParent());
				FieldGroup child = mapNameGroup.get(g.getName());
				child.setParent(parent);
				parent.addChild(child);
			}
		}
	}
	private Set<FieldGroup> getRootGroups() {
		Set<FieldGroup> roots = new HashSet<>();
		for (Entry<String, FieldGroup> e : mapNameGroup.entrySet()) {
			if (e.getValue().getParent() == null) {
				roots.add(e.getValue());
			}
		}
		return roots;
	}
	@Override
	public FieldGroup getGroup(String name) {
		if (mapNameGroup.containsKey(name)) {
			return mapNameGroup.get(name);
		} else {
			throw new FimetException("Unkown field group "+name);
		}
	}
	@Override
	public IFieldGroup remove(String name) {
		if (mapNameGroup.containsKey(name)) {
			FieldGroup group = mapNameGroup.remove(name);
			eventManager.fireEvent(ParserEvent.FIELDGROUP_REMOVED, this, group);
			return group;
		} else {
			return null;
		}
	}
	@Override
	public IFieldGroup reload(String name) {
		if (mapNameGroup.containsKey(name)) {
			FieldGroup group = mapNameGroup.get(name);
			group.reload();
			eventManager.fireEvent(ParserEvent.FIELDGROUP_REMOVED, this, group);
			eventManager.fireEvent(ParserEvent.FIELDGROUP_LOADED, this, group);
			return group;
		} else {
			IEFieldGroup group = dao.findByName(name);
			if (group != null) {
				FieldGroup fg = new FieldGroup(group);
				if (group.getParent()!=null) {
					FieldGroup parent = getGroup(group.getParent());
					fg.setParent(parent);
				}
				mapNameGroup.put(fg.getName(), fg);
				eventManager.fireEvent(ParserEvent.FIELDGROUP_LOADED, this, group);
				return fg;
			} else {
				logger.warn("Unkown Field Group "+name);
				return null;
			}
		}
	}
	@Override
	public Class<?>[] getParserClasses() {
		return new Class<?>[] {
			FixedFieldParser.class,
			VarFieldParser.class,
			LTrimVarFieldParser.class,
			LTrimFixedFieldParser.class,
			RTrimVarFieldParser.class,
			RTrimFixedFieldParser.class,
			NatTagFixedFieldParser.class,
			NatTagsVarFieldParser.class,
			NatTagVarFieldParser.class,
			NatTokenR1VarFieldParser.class,
			NatTokenEZVarFieldParser.class,
			NatTokensVarFieldParser.class,
			NatTokenVarFieldParser.class,
			TpvTag55VarFieldParser.class,
			TpvTag56VarFieldParser.class,
			TpvTags55VarFieldParser.class,
			TpvTags56VarFieldParser.class,
			TpvTokenEZVarFieldParser.class,
			TpvTokenQKVarFieldParser.class,
			TpvTokensVarFieldParser.class,
			TpvTokenVarFieldParser.class,
			Visa126VarFieldParser.class,
			Visa62VarFieldParser.class,
			Visa63VarFieldParser.class,
			VisaDatasetsVarFieldParser.class,
			VisaDatasetVarFieldParser.class,
			VisaTagVarFieldParser.class,
			MC48VarFieldParser.class,
			MCTagsVarFieldParser.class,
			MCTagVarFieldParser.class,
			AmexTagsVarFieldParser.class,
		};
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
