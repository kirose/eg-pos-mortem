package com.fimet.eglobal.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fimet.eglobal.JPaths;
import com.fimet.eglobal.classification.IRule;
import com.fimet.eglobal.dao.TransactionLogDAO;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.TransactionLog;
import com.fimet.eglobal.service.ConfigService;
import com.fimet.eglobal.service.ValidatorService;
import com.fimet.eglobal.validator.Validation;
import com.fimet.utils.FileUtils;
import com.fimet.utils.StringUtils;
import com.jayway.jsonpath.DocumentContext;

@Scope("prototype")
@Component
public class ReportMpul implements IReport {

	private static Logger logger = LoggerFactory.getLogger(ReportMpul.class);

	@Autowired private ConfigService cfg;
	@Autowired private ValidatorService validatorService;
	@Autowired private TransactionLogDAO transactionLogDAO;
	public static final SimpleDateFormat yyyyMMddHHmmssSS_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private String name;
	private String date;
	private File file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheetMpul;
	private Map<String, Integer> mapRows = new HashMap<String, Integer>();
	public ReportMpul(String id) throws IOException {
		this.name = "MPUL-"+id;
		this.date = id.split("\\-")[0].substring(0, 8);
		this.date = date.substring(0, 4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	}
	@PostConstruct
	public void init() throws IOException {
		this.file = new File(cfg.getReportOutputFolder(), name+".xlsx");
		FileUtils.copy(new File(cfg.getTemplatesFolder(), "MPUL-Template.xlsx"), file);
		this.workbook = new XSSFWorkbook(new FileInputStream(file));
		this.sheetMpul = workbook.getSheetAt(1);
		initMapRows();		
	}
	private void initMapRows() throws IOException {
		int startRow = 17;
		XSSFCell cell;
		XSSFRow row;
		for (int i = startRow;;i++) {
			row = sheetMpul.getRow(i);
			if (row == null) {
				break;
			} else {
				cell = row.getCell(2);
				String id = getStrValue(cell);
				if (id == null) {
					break;
				}
				id = StringUtils.leftPad(id, 12, '0');
				mapRows.put(id, Integer.valueOf(i));
			}
		}
	}
	private String getStrValue(XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		CellType type = cell.getCellType();
		if (type == CellType.STRING) {
			return cell.getStringCellValue().trim();
		} else if (type == CellType.NUMERIC) {
			return ""+((long)cell.getNumericCellValue());
		} else {
			return null;
		}
	}
	@Override
	public void add(DocumentContext json) {

		String rrn = json.read("$.acq.req.37");
		XSSFRow row = findRow(rrn);
		if (row == null) {
			logger.warn("Skiped transaction with rrn:{}",rrn);
			return;
		} 
		logger.info("MPUL transaccion with rrn:{}",rrn);
		TransactionLog trn = findTransactionLog(json, rrn);
		setColNombreAdq(row, 7, json);
		//setColNombreEmi(row, 8, json);
		setColTipoTransacion(row, 9, json);// Tipo de transaccion
		setColCondicionesReqAcq(row, 10, json);// Condiciones Acq
		setColTimestamp(row, 11, trn);// Timestamp
		setColPAN(row, 12, json);// PAN
		evalAndSet(row, 13, json);// Condiciones Req Emi
		evalAndSet(row, 15, json);// Condiciones Res Emi
		evalAndSet(row, 17, json);// Condiciones Res Acq
		evalAndSet(row, 19, json);// Condiciones RC Req
		evalAndSet(row, 21, json);// Considciones RC Res
		evalAndSet(row, 23, json);// Condiciones Desc
		evalAndSet(row, 25, json);// Condiciones especiales
		setColFluyoEmi(row, 28, json);// Fuyo al emisor?
		setColFluyoRC(row, 30, json);// Fuyo al RiskCenter?
		setColAprovadaEmi(row, 32, json);// Aprovada?
		setColAprovadaAdq(row, 34, json);// Aprovada?
		setColGuardadaBD(row, 36, trn);// Se gurado en el transaction log?
		setColGuardadaDesc(row, 38, json);// Se genero desc?
	}
//	private void setColNombreEmi(XSSFRow row, int col, DocumentContext json) {
//		String value = json.read(JPaths.ISS_REQ_IAP);
//		XSSFCell cell = getOrCreateCell(row, col);
//		cell.setCellValue(value);
//	}
	private void setColNombreAdq(XSSFRow row, int col, DocumentContext json) {
		String value = json.read(JPaths.ACQ_REQ_IAP);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(value);
	}
	private void evalAndSet(XSSFRow row, int col, DocumentContext json) {
		XSSFCell cellRule = getOrCreateCell(row, col);
		String value = cellRule.getStringCellValue();
		XSSFCell cellVals = getOrCreateCell(row, col+1);
		if (StringUtils.isEmpty(value)) {
			cellVals.setCellValue("N/A");
		} else {
			List<Validation> vals = validatorService.validateBlockRules(json, value);
			value = vals.stream().map(v->formatValidation(v)).collect(Collectors.joining("\n"));
			cellVals.setCellValue(value);
		}
	}
	private String formatValidation(Validation v) {
		if (v.isCorrect()) {
			return "CORRECTO";
		} else {
			return "INCORRECTO:" + String.format(v.getRule(), (Object[])v.getArgs());
		}	
	}
	private void setColGuardadaBD(XSSFRow row, int col, TransactionLog trn) {
		XSSFCell cell = getOrCreateCell(row, col);
		if (trn!=null) {
			cell.setCellValue("SI");
		} else {
			cell.setCellValue("NO");
		}
	}
	private TransactionLog findTransactionLog(DocumentContext json, String rrn) {
		try {
			String end = date + " " + (String)json.read(JPaths.ACQ_REQ_TIME);// Timestamp < Rawcom.time
			Date date = yyyyMMddHHmmssSS_FMT.parse(end);
			String start = yyyyMMddHHmmssSS_FMT.format(new Date(date.getTime()-cfg.getRawcomTrnLogOffset()));// Timestamp > Rawcom.time - Delta
			logger.info("transactionLogDAO.findByRrnAndRangeTime({},{},{})",rrn, start, end);
			return transactionLogDAO.findByRrnAndRangeTime(rrn, start, end);
		} catch (Exception e) {
			logger.error("Error fetching transactionlog record",e);
			return null;
		}
	}
	private void setColAprovadaEmi(XSSFRow row, int col, DocumentContext json) {
		String value = json.read(JPaths.ISS_RES_39);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue("00".equals(value)?"SI":"NO");
	}
	private void setColTimestamp(XSSFRow row, int col, TransactionLog trn) {
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(trn!=null?yyyyMMddHHmmssSS_FMT.format(trn.getTimestamp()):"");
	}
	private void setColGuardadaDesc(XSSFRow row, int col, DocumentContext json) {
		Object desc = json.read(JPaths.DESC0);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(desc!=null?"SI":"NO");
	}
	private void setColAprovadaAdq(XSSFRow row, int col, DocumentContext json) {
		String code = json.read(JPaths.ACQ_RES_39);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue("00".equals(code)?"SI":"NO");
	}
	private void setColFluyoRC(XSSFRow row, int col, DocumentContext json) {
		Object value = json.read(JPaths.RC_REQ);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(value!=null?"SI":"NO");
	}
	private void setColFluyoEmi(XSSFRow row, int col, DocumentContext json) {
		Object req = json.read(JPaths.ISS_REQ);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(req != null?"SI":"NO");
	}
	private void setColPAN(XSSFRow row, int col, DocumentContext json) {
		String value = json.read(JPaths.ACQ_REQ_PAN);
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(value);
	}
	private void setColCondicionesReqAcq(XSSFRow row, int col, DocumentContext json) {
		String classifierName = json.read(JPaths.ACQ_CLASSIFIER);
		Classifier classifier = cfg.getClassifiers().get(classifierName);
		List<String> classifications = json.read(JPaths.CLASSIFICATIONS);
		Map<String, Classification> map = classifier.getClassifications();
		StringBuilder s = new StringBuilder();
		for (String classification : classifications) {
			Classification c = map.get(classification);
			s.append(c.getName()).append(":");
			for (IRule rule : c.getRules()) {
				s.append(rule.toString()).append(" y ");
			}
			if (s.length() > 0) {
				s.delete(s.length()-3, s.length());
			}
			s.append("\n");
		}
		if (s.length() > 0) {
			s.delete(s.length()-1, s.length());
		}
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(s.toString());		
	}
	private void setColTipoTransacion(XSSFRow row, int col, DocumentContext json) {
		List<String> cls = json.read(JPaths.CLASSIFICATIONS);
		String value = cls.stream().collect(Collectors.joining(","));
		XSSFCell cell = getOrCreateCell(row, col);
		cell.setCellValue(value);
	}
	public XSSFCell getOrCreateCell(XSSFRow row, int index) {
		XSSFCell cell = row.getCell(index);
		if (cell == null) {
			cell = row.createCell(index);
		}
		return cell;
	}
	private XSSFRow findRow(String rrn) {
		if (rrn != null && mapRows.containsKey(rrn)) {
			return sheetMpul.getRow(mapRows.get(rrn));
		}
		return null;
	}
	@Override
	public void close() {
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (IOException e) {
			logger.error("Error saving MUL",e);
		}
	}
	@Override
	public String getName() {
		return name;
	}
}
