package com.fimet.eglobal.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fimet.eglobal.reports.IReport;
import com.fimet.eglobal.reports.ReportType;
import com.fimet.eglobal.reports.ReportMpul;
import com.fimet.eglobal.reports.ReportResponse;
import com.fimet.eglobal.store.DataReader;
import com.fimet.eglobal.store.Index;
import com.fimet.eglobal.store.IndexReader;
import com.fimet.eglobal.utils.JsonUtils;
import com.fimet.eglobal.validator.ValidatorReq;
import com.fimet.utils.FileUtils;
import com.jayway.jsonpath.DocumentContext;

@Service
public class ReportService {
	private static Logger logger = LoggerFactory.getLogger(RawcomService.class);

	@Autowired private ApplicationContext context;
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
		ValidatorReq valReq = null;
		try {
			report = newReport(reportName, id);

			idxRdrMtch  = new IndexReader(new File(config.getRawcomOutputFolder(), "Match-index-"+id+".txt"));
			dtaRdrMtch   = new DataReader(new File(config.getRawcomOutputFolder(), "Match-"+id+".txt"));

			Index idxMtch;

			valReq = new ValidatorReq(config, id, ReportType.MPUL);

			while (idxRdrMtch.hasNext()) {

				idxMtch = idxRdrMtch.next();

				String jsonMtch = dtaRdrMtch.read(idxMtch);
				DocumentContext mtch = JsonUtils.jaywayParse(jsonMtch);

				try {
					report.add(mtch);
				} catch (Exception e) {
					logger.error("Error adding transation:{} to report:{}",idxMtch.getKey(),report.getName(),e);
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
			FileUtils.close(valReq);
		}
	}
	private IReport newReport(String reportName, String id) throws IOException {
		ReportType type = ReportType.valueOf(reportName.toUpperCase());
		switch (type) {
		case MPUL:
			return context.getBean(ReportMpul.class, id);
		default:
			return null;
		}
	}
}
