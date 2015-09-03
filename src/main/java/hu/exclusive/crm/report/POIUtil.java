package hu.exclusive.crm.report;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import hu.exclusive.utils.ObjectUtils;

public class POIUtil {

	public static final int CELL_CAF_XLS_NAME = 0;
	public static final int CELL_CAF_XLS_TAX = 1;
	public static final int CELL_CAF_XLS_START = 2;
	public static final int CELL_CAF_XLS_LIMIT = 3;
	public static final int CELL_CAF_XLS_GROUP = 6;
	public static final int CELL_CAF_XLS_AREA = 5;

	public static String getRowCell(org.apache.poi.ss.usermodel.Sheet sheet, int row, int cell) {
		Cell c = getRowCellCell(sheet, row, cell);
		if (c != null) {
			// van cella
			if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				return c.getStringCellValue() != null ? c.getStringCellValue().trim() : null;
			} else if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return String.valueOf(c.getNumericCellValue());
			} else {

			}
		}
		return null;
	}

	public static BigDecimal getRowCellNumber(org.apache.poi.ss.usermodel.Sheet sheet, int row, int cell) {
		Double d = null;
		Cell c = getRowCellCell(sheet, row, cell);
		if (c != null) {
			// van cella
			if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				try {
					// System.out.println("get num from string " +
					// c.getStringCellValue());
					d = StringUtils.isNotEmpty(c.getStringCellValue()) ? new Double(c.getStringCellValue().trim())
							: null;
				} catch (Exception e) {
					new IllegalArgumentException("row:" + row + " cell:" + cell + " value:" + c, e).printStackTrace();
				}
			} else if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				d = c.getNumericCellValue();
			} else {

			}
		}
		return d == null ? null : new BigDecimal(d);
	}

	public static Cell getRowCellCell(org.apache.poi.ss.usermodel.Sheet sheet, int row, int cell) {
		Double d = null;
		if (sheet.getLastRowNum() > row && sheet.getRow(row) != null && sheet.getRow(row).getLastCellNum() > cell) {
			return sheet.getRow(row).getCell(cell);
		}
		return null;
	}

	public static Date getRowCellDate(org.apache.poi.ss.usermodel.Sheet sheet, int row, int cell) {
		Cell c = getRowCellCell(sheet, row, cell);
		if (c != null) {
			if (HSSFDateUtil.isCellDateFormatted(c)) {
				return HSSFDateUtil.getJavaDate(c.getNumericCellValue());
			} else if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				try {
					return ObjectUtils.SDF_YYYYMMDD.parse(c.getStringCellValue().replace('-', '.'));
				} catch (Exception e) {
				}
			}
		}
		return null;
	}
}
