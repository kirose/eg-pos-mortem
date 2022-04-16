package com.fimet.eglobal.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.eglobal.JPaths;
import com.fimet.eglobal.classification.IRule;
import com.fimet.eglobal.model.Classification;
import com.fimet.eglobal.model.Classifier;
import com.fimet.eglobal.model.Validation;
import com.fimet.eglobal.model.Validations;
import com.fimet.eglobal.service.ConfigService;
import com.fimet.eglobal.utils.JsonUtils;
import com.fimet.utils.FileUtils;
import com.jayway.jsonpath.DocumentContext;

public class ReportMpul implements IReport {

	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);

	private String name;
	private File file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private Map<Integer, XSSFRow> rows = new HashMap<Integer, XSSFRow>();
	private Map<String, Integer> mapCells;
	private ConfigService cfg;
	public ReportMpul(ConfigService cfg, String id) throws IOException {
		this.cfg = cfg;
		mapCells = new HashMap<String, Integer>();
		mapCells.put(JPaths.ACQ_REQ_IAP, 7);
		mapCells.put(JPaths.ISS_REQ_IAP, 8);
		this.name = "MPUL-"+id;
		this.file = new File(cfg.getReportOutputFolder(), name+".xlsx");
		FileUtils.copy(new File("templates/MPUL-Template.xlsx"), file);
		init();
	}
	private void init() throws IOException {
		this.workbook = new XSSFWorkbook(new FileInputStream(file));
		this.sheet = workbook.getSheetAt(1);
		int startRow = 17;
		XSSFCell cell;
		XSSFRow row;
		for (int i = startRow;;i++) {
			row = sheet.getRow(i);
			if (row == null) {
				break;
			} else {
				cell = row.getCell(2);
				Integer id = getIntValue(cell);
				if (id == null) {
					break;
				}
				rows.put(id, row);
			}
		}
	}
	private Integer getIntValue(XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		CellType type = cell.getCellType();
		if (type == CellType.STRING) {
			return Integer.valueOf(cell.getStringCellValue());
		} else if (type == CellType.NUMERIC) {
			return (int)cell.getNumericCellValue();
		} else {
			return null;
		}
	}
	@Override
	public void add(String stringJsonMatch, String stringJsonValidations) {
		DocumentContext json = JsonUtils.jaywayParse(stringJsonMatch);

		String rrn = json.read("$.acq.req.37");
		XSSFRow row = findRow(rrn);
		XSSFCell cell;
		if (row == null) {
			logger.warn("Invalid transaction with rrn:{}",rrn);
			return;
		} 
		
		Validations vals = JsonUtils.fromJson(stringJsonValidations, Validations.class);
		
		setCol9(row, vals);// Tipo de transaccion
		setCol10(row, json, vals);// Condiciones Acq
		setCol20(row, json);// PAN
		setCol21(row, json);// Fuyo al emisor?
		setCol22(row, json);// Aprovada?
		setCol26(row, json);// Se genero desc?
		setCol29(row, vals);// Estatus

		String value;
		for (Map.Entry<String, Integer> e : mapCells.entrySet()) {
			value = json.read(e.getKey());
			if (value != null) {
				cell = getOrCreateCell(row, e.getValue());
				cell.setCellValue(value);
			}
		}
	}
	private void setCol26(XSSFRow row, DocumentContext json) {
		Object desc = json.read(JPaths.DESC0);
		XSSFCell cell = getOrCreateCell(row, 22);
		cell.setCellValue(desc!=null?"SI":"NO");
	}
	private void setCol22(XSSFRow row, DocumentContext json) {
		String code = json.read(JPaths.ACQ_RES_39);
		XSSFCell cell = getOrCreateCell(row, 22);
		cell.setCellValue("00".equals(code)?"SI":"NO");
	}
	private void setCol21(XSSFRow row, DocumentContext json) {
		Object req = json.read(JPaths.ISS_REQ);
		XSSFCell cell = getOrCreateCell(row, 21);
		cell.setCellValue(req != null?"SI":"NO");
	}
	private void setCol20(XSSFRow row, DocumentContext json) {
		String value = json.read(JPaths.ACQ_REQ_PAN);
		XSSFCell cell = getOrCreateCell(row, 20);
		cell.setCellValue(value);
	}
	private void setCol10(XSSFRow row, DocumentContext json, Validations vals) {
		Classifier classifier = cfg.getClassifiers().get(vals.getClassifier());
		List<String> classifications = vals.getClassifications();
		Map<String, Classification> map = classifier.getClassifications();
		StringBuilder s = new StringBuilder();
		for (String classification : classifications) {
			Classification c = map.get(classification);
			s.append(c.getName()).append(":");
			for (IRule rule : c.getRules()) {
				s.append(rule.toString()).append(",");
			}
			if (s.length() > 0) {
				s.delete(s.length()-1, s.length());
			}
			s.append("\n");
		}
		if (s.length() > 0) {
			s.delete(s.length()-1, s.length());
		}
		XSSFCell cell = getOrCreateCell(row, 10);
		cell.setCellValue(s.toString());		
	}
	private void setCol9(XSSFRow row, Validations vals) {
		String value = vals.getClassifications().stream().collect(Collectors.joining(","));
		XSSFCell cell = getOrCreateCell(row, 9);
		cell.setCellValue(value);		
	}
	private void setCol29(XSSFRow row, Validations vals) {
		XSSFCell cell = getOrCreateCell(row, 29);
		String status = getStatus(vals);
		cell.setCellValue(status);		
	}
	private String getStatus(Validations vals) {
		for (Validation v : vals.getValidations()) {
			if (!v.isCorrect()) {
				return "FALLIDO";
			}
		}
		return "EXITOSO";
	}
	public XSSFCell getOrCreateCell(XSSFRow row, int index) {
		XSSFCell cell = row.getCell(index);
		if (cell == null) {
			cell = row.createCell(index);
		}
		return cell;
	}
	private XSSFRow findRow(String rrn) {
		if (rrn != null && rrn.matches("\\d+")) {
			Integer id = Integer.parseInt(rrn);
			return rows.get(id);
		} else {
			return null;
		}
	}
	@Override
	public void close() {
		try {
			mapCells.clear();
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
