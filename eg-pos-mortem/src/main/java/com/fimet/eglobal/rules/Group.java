package com.fimet.eglobal.rules;

import java.util.List;

import com.fimet.eglobal.JPaths;
import com.fimet.eglobal.model.Classification;
import com.jayway.jsonpath.DocumentContext;

public class Group {
	private String acquirerPattern;
	private String issuerPattern;
	private String classificationPattern;
	private List<IBooleanOperator> optionals;
	private List<Rule> rules;
	public Group() {
		super();
	}
	public boolean matches(List<Classification> classifications, DocumentContext json) {
		return matchesAcq(json) && matchesIss(json)
				&& matchesClassification(classifications)
				&& marchesOptionals(json);
	}
	private boolean marchesOptionals(DocumentContext json) {
		if (optionals!=null) {
			for (IBooleanOperator rule : optionals) {
				if (!rule.eval(json).getValue()) {
					return false;
				}
			}
			return true;
		}
		return true;
	}
	private boolean matchesClassification(List<Classification> classifications) {
		if ("*".equals(classificationPattern)) {
			return true;
		} else {
			for (Classification c : classifications) {
				if (c.getName().matches(classificationPattern)) {
					return true;
				}
			}
			return false;
		}
	}
	private boolean matchesIss(DocumentContext json) {
		if ("*".equals(issuerPattern)) {
			return true;
		} else {
			String issuer = json.read(JPaths.ISS_CLASSIFIER);
			return issuer!=null && issuer.matches(issuerPattern);
		}
	}
	private boolean matchesAcq(DocumentContext json) {
		if ("*".equals(acquirerPattern)) {
			return true;
		} else {
			String acquirer = json.read(JPaths.ACQ_CLASSIFIER);
			return acquirer!=null && acquirer.matches(acquirerPattern);
		}
	}
	public void setAcquirerPattern(String acquirerPattern) {
		this.acquirerPattern = acquirerPattern;
	}
	public void setIssuerPattern(String issuerPattern) {
		this.issuerPattern = issuerPattern;
	}
	public void setClassificationPattern(String classificationPattern) {
		this.classificationPattern = classificationPattern;
	}
	public void setOptionals(List<IBooleanOperator> optionals) {
		this.optionals = optionals;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public String getAcquirerPattern() {
		return acquirerPattern;
	}
	public String getIssuerPattern() {
		return issuerPattern;
	}
	public String getClassificationPattern() {
		return classificationPattern;
	}
	public List<IBooleanOperator> getOptionals() {
		return optionals;
	}
	public List<Rule> getRules() {
		return rules;
	}
	@Override
	public String toString() {
		return "[" + acquirerPattern + "][" + issuerPattern+ "][" + classificationPattern + "]"+(optionals!=null?("[" + optionals + "]"):"")+":" + rules;
	}
	public static void main(String[] args) {
		String pattern = "^(?!(Echo|Signon)$).*$";//"Interred|Compra";
		String classifiaction = "Compra";
		if (classifiaction.matches(pattern)) {
			System.out.println("Matches");
		}
	}
}
