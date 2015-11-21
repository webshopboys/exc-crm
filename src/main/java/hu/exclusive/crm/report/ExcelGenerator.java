package hu.exclusive.crm.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelGenerator {

	public static Workbook getWorkBook(byte[] stream)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		java.io.ByteArrayInputStream in = new ByteArrayInputStream(stream);

		Workbook wb = WorkbookFactory.create(in);
		return wb;
	}

	public static Workbook getWorkBook(String path)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		java.io.InputStream in = new FileInputStream(new File(path));

		Workbook wb = WorkbookFactory.create(in);
		return wb;
	}

	public static Workbook getWorkBook(java.io.InputStream in)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook wb = WorkbookFactory.create(in);
		return wb;
	}

	protected File uploadBaseDir;

	public static final String ROW_INDEX = "row-index";
	public static final String COL_INDEX = "col-index";

	private static Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

	public boolean create(String sheetName, List<String> header, List<Map<String, Object>> rows, OutputStream outStream)
			throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);

		CellStyle wordwrap = workbook.createCellStyle();
		wordwrap.setWrapText(true);

		try {

			HSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < header.size(); i++) {
				headerRow.createCell(i).setCellValue(header.get(i));
			}
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> inputRow = rows.get(i);
				int rownum = i + 1;
				if (inputRow.containsKey(ROW_INDEX)) {
					rownum = (Integer) inputRow.get(ROW_INDEX);
				}
				HSSFRow row = sheet.createRow(rownum);
				for (int j = 0; j < header.size(); j++) {

					String name = header.get(j);
					Object value = inputRow.get(name);

					int c = inputRow.containsKey(COL_INDEX) ? j : (Integer) inputRow.get(ROW_INDEX);

					if (value != null) {
						if (value instanceof java.lang.String) {
							row.createCell(c).setCellValue((String) value);
						} else if (value instanceof java.lang.Number) {
							row.createCell(c).setCellValue(((Number) value).doubleValue());
						} else if (value instanceof java.util.Date) {
							row.createCell(c).setCellValue((Date) value);
						} else if (value instanceof java.util.Calendar) {
							row.createCell(c).setCellValue((Calendar) value);
						} else if (value instanceof java.lang.Boolean) {
							row.createCell(c).setCellValue((Boolean) value);
						} else {
							row.createCell(c).setCellValue(value.toString());
						}
						row.getCell(c).setCellStyle(wordwrap);
					}
				}
			}

			for (int i = 0; i < header.size(); i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(outStream);

		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

	public boolean createWithErrorList(List<String> header, List<Map<String, Object>> rows, List<String> errorList,
			OutputStream outStream) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Munka1");

		HSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < header.size(); i++) {
			headerRow.createCell(i).setCellValue(header.get(i));
		}
		for (int i = 0; i < rows.size(); i++) {
			Map<String, Object> inputRow = rows.get(i);
			int rownum = i + 1;
			if (inputRow.containsKey("xls_row")) {
				rownum = (Integer) inputRow.get("xls_row");
			}
			HSSFRow row = sheet.createRow(rownum);
			for (int j = 0; j < header.size(); j++) {
				String name = header.get(j);
				Object value = inputRow.get(name);

				if (value != null) {
					if (value instanceof java.lang.String) {
						row.createCell(j).setCellValue((String) value);
					} else if (value instanceof java.lang.Number) {
						row.createCell(j).setCellValue(((Number) value).doubleValue());
					} else if (value instanceof java.util.Date) {
						row.createCell(j).setCellValue((Date) value);
					} else if (value instanceof java.util.Calendar) {
						row.createCell(j).setCellValue((Calendar) value);
					} else if (value instanceof java.lang.Boolean) {
						row.createCell(j).setCellValue((Boolean) value);
					} else {
						row.createCell(j).setCellValue(value.toString());
					}
				}
			}
		}
		int lastCellIndex = 0;
		if (sheet.getRow(0) != null) {
			lastCellIndex = sheet.getRow(0).getLastCellNum();
		}
		for (int i = 0; i < errorList.size(); i++) {
			String error = errorList.get(i);
			int errorRow = i;
			Row row = sheet.getRow(errorRow);
			if (row == null)
				row = sheet.createRow(errorRow);

			int targetCell = Math.max(row.getLastCellNum(), lastCellIndex);
			row.createCell(targetCell).setCellValue(error);
		}

		try {
			workbook.write(outStream);
		} catch (IOException e) {
			log.error("Write error.", e);
			return false;
		}
		return true;
	}

	public boolean errorPrinter(List<String> errors, int sheetIndex, String sheetName, String fileName,
			OutputStream outStream) {

		File xls = new File(fileName);

		if (xls.exists() == false) {
			log.error(uploadBaseDir + " / " + fileName + " not exists");
			return false;
		}

		try {

			Workbook wb = WorkbookFactory.create(xls);
			Sheet sheet = wb.getSheetAt(sheetIndex);
			// sheet.setName(sheetName);

			int lastCellIndex = 0;
			if (sheet.getRow(0) != null) {
				lastCellIndex = sheet.getRow(0).getLastCellNum();
			}
			// log.debug("State errors: " + state.getErrors() +
			// "\nlastCellIndex:" + lastCellIndex);

			for (int i = 0; i < errors.size(); i++) {
				String error = errors.get(i);
				int errorRow = i;// (int) Math.max(error.getXlsRow() - 1, 0);
				Row row = sheet.getRow(errorRow);
				if (row == null)
					row = sheet.createRow(errorRow);

				// int targetCell=Math.max(row.getLastCellNum(),lastCellIndex);
				// row.createCell(targetCell).setCellValue(error.getErrorValue());
				row.createCell(lastCellIndex).setCellValue(error);
			}
			wb.write(outStream);

		} catch (OpenXML4JException e) {
			log.error(e.getMessage(), e);
			return false;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}

		return true;

	}

	public boolean createFromSampleFile(String path, List<String> header, List<Map<String, Object>> rows,
			OutputStream outStream) {
		File xls = new File(path);
		if (xls.exists() == false)
			return false;
		try {
			Workbook wb = WorkbookFactory.create(xls);
			log.debug("Workbook: {}", wb);
			Sheet sheet = wb.getSheetAt(0);

			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> inputRow = rows.get(i);
				int rownum = i + 1;
				if (inputRow.containsKey("xls_row")) {
					rownum = (Integer) inputRow.get("xls_row");
				}
				Row row = sheet.createRow(rownum);
				for (int j = 0; j < header.size(); j++) {
					String name = header.get(j);
					Object value = inputRow.get(name);

					if (value != null) {
						if (value instanceof java.lang.String) {
							row.createCell(j).setCellValue((String) value);
						} else if (value instanceof java.lang.Number) {
							row.createCell(j).setCellValue(((Number) value).doubleValue());
						} else if (value instanceof java.util.Date) {
							row.createCell(j).setCellValue((Date) value);
						} else if (value instanceof java.util.Calendar) {
							row.createCell(j).setCellValue((Calendar) value);
						} else if (value instanceof java.lang.Boolean) {
							row.createCell(j).setCellValue((Boolean) value);
						} else {
							row.createCell(j).setCellValue(value.toString());
						}
					}
				}
			}
			wb.write(outStream);
		} catch (InvalidFormatException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;

	}

	public File getUploadBaseDir() {
		return uploadBaseDir;
	}

	public void setUploadBaseDir(File uploadBaseDir) {
		this.uploadBaseDir = uploadBaseDir;
	}
}
