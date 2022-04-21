package com.fimet.eglobal.validator;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import com.fimet.eglobal.reports.ReportType;
import com.fimet.eglobal.rules.Validations;
import com.fimet.eglobal.service.ConfigService;
import com.fimet.eglobal.store.Store;
import com.fimet.eglobal.store.StoreException;

public class ValidatorReq implements Closeable{

	private String id;
	private Store store;
	private ReportType type;
	private Validations validations;
	public ValidatorReq(ConfigService cfg, String id, ReportType type) throws StoreException {
		super();
		this.type = type;
		this.id = id;
		this.validations = cfg.getValidations(type);
		File data = new File(cfg.getRawcomOutputFolder(), "Validations-"+id+".txt");
		File index = new File(cfg.getRawcomOutputFolder(), "Validations-index-"+id+".txt");
		this.store = new Store(index, data);
	}
	public ReportType getType() {
		return type;
	}
	public String getId() {
		return id;
	}
	public Store getStore() {
		return store;
	}
	public Validations getValidations() {
		return validations;
	}
	@Override
	public void close() throws IOException {
		store.close();
	}
}
