package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.reports.IReport;
import com.fimet.eglobal.reports.ReportMpul;
import com.fimet.eglobal.reports.ReportResponse;
import com.fimet.eglobal.store.DataReader;
import com.fimet.eglobal.store.Index;
import com.fimet.eglobal.store.IndexReader;
import com.fimet.utils.FileUtils;

@Service
public class ReportService {
	private static Logger logger = LoggerFactory.getLogger(RawcomService.class);
	
	@Autowired private ConfigService config;
	
	public ReportService() {}
	@PostConstruct
	private void start() throws IOException {}
	
	public ReportResponse create(String reportName, String id) throws Exception {
		logger.debug("Analiyze id:{}", id);
		IndexReader idxRdrMtch = null;
		DataReader dtaRdrMtch = null;
		IndexReader idxRdrVal = null;
		DataReader dtaRdrVal = null;
		IReport report = null;
		try {
			report = newReport(reportName, id);
			
			idxRdrMtch  = new IndexReader(new File(config.getRawcomOutputFolder(), "Match-index-"+id+".txt"));
			dtaRdrMtch   = new DataReader(new File(config.getRawcomOutputFolder(), "Match-"+id+".txt"));
			idxRdrVal = new IndexReader(new File(config.getDescOutputFolder(), "Validations-index-"+id+".txt"));
			dtaRdrVal  = new DataReader(new File(config.getDescOutputFolder(), "Validations-"+id+".txt"));
			
			Index idxMtch;
			Index idxVal;
			
			while (idxRdrMtch.hasNext() && idxRdrVal.hasNext()) {
				idxMtch = idxRdrMtch.next();
				idxVal = idxRdrVal.next();
				String jsonMtch = dtaRdrMtch.read(idxMtch);
				String jsonVal = dtaRdrVal.read(idxVal);
				try {
					report.add(jsonMtch, jsonVal);
				} catch (Exception e) {
					logger.error("Error adding transation:{} to report:{}",idxMtch.getKey(),report.getName());
				}
			}
			return new ReportResponse(report.getName());
		} catch (Exception e){
			logger.error("Report exception",e);
			throw e;
		} finally {
			FileUtils.close(report);
			FileUtils.close(idxRdrMtch);
			FileUtils.close(dtaRdrMtch);
			FileUtils.close(idxRdrVal);
			FileUtils.close(dtaRdrVal);
		}
	}
	private IReport newReport(String reportName, String id) throws IOException {
		if ("MPUL".equals(reportName)) {
			return new ReportMpul(config, id);
		}
		return null;
	}
}
