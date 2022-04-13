package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.reports.IReport;
import com.fimet.eglobal.reports.ReportMpuBBVA;
import com.fimet.eglobal.reports.ReportResponse;
import com.fimet.eglobal.store.DataReader;
import com.fimet.eglobal.store.Index;
import com.fimet.eglobal.store.IndexReader;

@Service
public class ReportService {
	private static Logger logger = LoggerFactory.getLogger(RawcomService.class);
	
	@Autowired private ConfigService config;
	
	public ReportService() {}
	@PostConstruct
	private void start() throws IOException {
	}
	
	public ReportResponse create(String reportName, String idMatcher, String idValidations) throws IOException {
		logger.debug("Analiyze matcher:{}, validations:{}", idMatcher, idValidations);
		IReport report = newReport(reportName);
		
		IndexReader idxRdrMtch  = new IndexReader(new File(config.getRawcomOutputFolder(), "Match-index-"+idMatcher+".txt"));
		DataReader dtaRdrMtch   = new DataReader(new File(config.getRawcomOutputFolder(), "Match-data-"+idMatcher+".txt"));
		IndexReader idxRdrVal = new IndexReader(new File(config.getDescOutputFolder(), "RuleVal-index-"+idValidations+".txt"));
		DataReader dtaRdrVal  = new DataReader(new File(config.getDescOutputFolder(), "RuleVal-data-"+idValidations+".txt"));
		Index idxMtch;
		Index idxVal;
		
		while (idxRdrMtch.hasNext() && idxRdrVal.hasNext()) {
			idxMtch = idxRdrMtch.next();
			idxVal = idxRdrVal.next();
			String jsonMtch = dtaRdrMtch.read(idxMtch);
			String jsonVal = dtaRdrVal.read(idxVal);
			report.add(jsonMtch, jsonVal);// add row to reprot
		}
		report.close();
		String name = report.getName();
		idxRdrMtch.close();
		dtaRdrMtch.close();
		idxRdrVal.close();
		dtaRdrVal.close();
		return new ReportResponse(name);
	}
	private IReport newReport(String reportName) {
		if ("MPU".equals(reportName)) {
			return new ReportMpuBBVA(config);
		}
		return null;
	}
}
