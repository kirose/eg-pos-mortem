package com.fimet.dao;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fimet.dao.IFieldGroupDAO;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.EFieldGroupXml;
import com.fimet.xml.EFieldGroupsXml;

@Component
public class FieldGroupXmlDAO implements IFieldGroupDAO<EFieldGroupXml> {
	private File file;
	public FieldGroupXmlDAO(@Value("${field.groups.path:model/fieldGroups.xml}") String path) {
		file = new File(path).getAbsoluteFile();
	}

	@Override
	public EFieldGroupXml findByName(String name) {
		if (file.exists()) {
			EFieldGroupsXml groupsXml = XmlUtils.fromFile(file, EFieldGroupsXml.class);
			List<EFieldGroupXml> groups = groupsXml.getGroups();
			for (EFieldGroupXml e : groups) {
				if (name.equals(e.getName())) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	public List<EFieldGroupXml> findAll() {
		if (file.exists()) {
			EFieldGroupsXml groupsXml = XmlUtils.fromFile(file, EFieldGroupsXml.class);
			List<EFieldGroupXml> groups = groupsXml.getGroups();
			return groups;
		}
		return new java.util.ArrayList<EFieldGroupXml>();
	}
	@PostConstruct
	@Override
	public void start() {
	}

	@Override
	public void reload() {
	}

	@Override
	public EFieldGroupXml insert(EFieldGroupXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public EFieldGroupXml update(EFieldGroupXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public EFieldGroupXml delete(EFieldGroupXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
