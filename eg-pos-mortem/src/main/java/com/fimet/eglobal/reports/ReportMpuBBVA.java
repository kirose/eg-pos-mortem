package com.fimet.eglobal.reports;

import java.io.File;

import com.fimet.eglobal.service.ConfigService;
import com.fimet.eglobal.utils.JsonUtils;
import com.fimet.utils.FileUtils;
import com.jayway.jsonpath.DocumentContext;

public class ReportMpuBBVA implements IReport {
	private String name;
	private File file;
	public ReportMpuBBVA(ConfigService cfg) {
		this.name = "MPU.txt";
		this.file = new File(cfg.getReportOutput(), name);
	}
	@Override
	public void add(String jsonMatch, String jsonValidate) {
		DocumentContext json = JsonUtils.jaywayParse(jsonMatch);
		String iap = json.read("$.acq.req.iap");
		String mti = json.read("$.acq.req.mti");
		FileUtils.appendContents(file, iap+","+mti+"\n");
	}
	@Override
	public void close() {
		
	}
	@Override
	public String getName() {
		return name;
	}
}
