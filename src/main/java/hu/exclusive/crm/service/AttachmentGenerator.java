package hu.exclusive.crm.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import hu.exclusive.utils.ObjectUtils;

public class AttachmentGenerator implements Serializable {

	private static final long serialVersionUID = 4665732209090471772L;
	public static final String MIME_PDF = "application/pdf";
	public static final String MIME_PNG = "image/png";
	public static final String MIME_JPG = "image/jpg";
	public static final String MIME_TEXT = "text/plain";

	// http://www.iana.org/assignments/media-types
	public static final String MIME_DOC = "application/vnd.ms-word.document.macroEnabled.12";
	public static final String MIME_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml";
	public static final String MIME_XLS = "application/vnd.ms-excel";

	public static final String PLAYER_PDF = "pdf";
	public static final String PLAYER_FLASH = "flash";
	public static final String PLAYER_MOV = "quicktime";
	public static final String PLAYER_MP3 = "quicktime";

	private byte[] content;

	private String mimeType;
	private String attachmentName;
	private String attachmentPath;

	public StreamedContent generateStream() throws Exception {

		if (content == null) {
			System.err.println("!!!!AttachmentGenerator.generateStream() is null!!!");
			return new DefaultStreamedContent(new ByteArrayInputStream(new byte[0]));
		} else {

			InputStream stream = new ByteArrayInputStream(content);
			// if (ObjectUtils.isZippedStream(stream)) {
			// System.err.println("!!!!AttachmentGenerator.generateStream()
			// zipped bytes OK !!!");
			// // stream = new ByteArrayInputStream(ObjectUtils.unzip(content));
			// // a docx alapbol zip, így egyelőre nem kezelünk
			// // zippelést
			// } else {
			// System.err.println("!!!!AttachmentGenerator.generateStream()
			// bytes OK !!!");
			// }

			System.err.println("!!!!AttachmentGenerator.generateStream() bytes[" + stream.available() + "] OK !!!");
			return new DefaultStreamedContent(stream, mimeType, attachmentName);
		}

	};

	public StreamedContent getAttachment(Integer id) throws Exception {
		setContent(("" + id).getBytes());
		return generateStream();
	};

	public boolean isImage() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMedia() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isObject() {
		// TODO Auto-generated method stub
		return false;
	}

	public static String calulateMimeType(String filename) {
		if (!ObjectUtils.isEmpty(filename)) {
			if (filename.toLowerCase().endsWith(".doc"))
				return MIME_DOC;
			if (filename.toLowerCase().endsWith(".docx"))
				return MIME_DOCX;
			if (filename.toLowerCase().endsWith(".xls"))
				return MIME_XLS;
			if (filename.toLowerCase().endsWith(".xlsx"))
				return MIME_XLSX;
			if (filename.toLowerCase().endsWith(".pdf"))
				return MIME_PDF;
			if (filename.toLowerCase().endsWith(".jpg"))
				return MIME_JPG;
			if (filename.toLowerCase().endsWith(".jpeg"))
				return MIME_JPG;
			if (filename.toLowerCase().endsWith(".png"))
				return MIME_PNG;
		}
		return MIME_TEXT;
	}

	public byte[] getContent() {
		return content;
	}

	public AttachmentGenerator setContent(byte[] content) {
		this.content = content;
		return this;
	}

	public String getMimeType() {
		return mimeType;
	}

	public AttachmentGenerator setMimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public AttachmentGenerator setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
		return this;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public AttachmentGenerator setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
		return this;
	}

	public static final String ROW_INDEX = "row-index";
	public static final String COL_INDEX = "col-index";

	public void createExcelBIN(String sheetName, List<String> header, List<Map<String, Object>> rows,
			OutputStream outStream) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);

		CellStyle wordwrap = workbook.createCellStyle();
		wordwrap.setWrapText(true);

		try {

			HSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < header.size(); i++) {
				headerRow.createCell(i).setCellValue(header.get(i));
				headerRow.getCell(i).setCellStyle(wordwrap);
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

					int c = inputRow.containsKey(COL_INDEX) ? (Integer) inputRow.get(ROW_INDEX) : j;

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
	}

	public void createExcelXML(String sheetName, List<String> header, List<Map<String, Object>> rows,
			OutputStream outStream) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(sheetName);

		CellStyle wordwrap = workbook.createCellStyle();
		wordwrap.setWrapText(true);

		try {

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < header.size(); i++) {
				headerRow.createCell(i).setCellValue(header.get(i));
				headerRow.getCell(i).setCellStyle(wordwrap);
			}
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> inputRow = rows.get(i);
				int rownum = i + 1;
				if (inputRow.containsKey(ROW_INDEX)) {
					rownum = (Integer) inputRow.get(ROW_INDEX);
				}
				XSSFRow row = sheet.createRow(rownum);
				for (int j = 0; j < header.size(); j++) {

					String name = header.get(j);
					Object value = inputRow.get(name);

					int c = inputRow.containsKey(COL_INDEX) ? (Integer) inputRow.get(ROW_INDEX) : j;

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
	}

}
