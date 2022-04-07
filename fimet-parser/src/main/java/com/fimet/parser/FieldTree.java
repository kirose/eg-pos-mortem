package com.fimet.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fimet.parser.MessageFields.Node;
import com.fimet.parser.MessageFields.Parent;


public class FieldTree {
	MessageFields fields;
	FieldTree(MessageFields fields){
		this.fields = fields; 
	}
	private List<Field> createList() {
		List<Field> roots = new ArrayList<>();
		Node node = fields.head;
		Field field;
		while ((node = node.next) != null) {
			field = new Field(node.idField, node.bytes);
			roots.add(field);
			if (node.hasChildren()) {
				addChildren(field, node.idField, ((Parent)node).head);
			}
		}
		return roots;
	}
	private Map<String,Field> createMap() {
		Map<String,Field> roots = new LinkedHashMap<>();
		Node node = fields.head;
		Field field;
		while ((node = node.next) != null) {
			field = new Field(node.idField, node.bytes);
			roots.put(field.getIdField(),field);
			if (node.hasChildren()) {
				addChildren(field, node.idField, ((Parent)node).head);
			}
		}
		return roots;
	}
	private void addChildren(Field parent, String idParent, Node head) {
		Node node = head;
		Field child;
		while ((node = node.next) != null) {
			child = new Field(node.idField, node.bytes);
			parent.add(child);
			if (node.hasChildren()) {
				addChildren(child, node.idField, ((Parent)node).head);
			}
		}
	}
	
	public  Map<String,Field> getRootsAsMap(){
		return createMap();
	}
	public List<Field> getRootsAsList(){
		return createList();
	}
}
