package hu.exclusive.crm.report;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;

import hu.exclusive.utils.ObjectUtils;

public class POIUtil {

	public static final int CELL_CAF_XLS_NAME = 0;
	public static final int CELL_CAF_XLS_TAX = 1;
	public static final int CELL_CAF_XLS_START = 2;
	public static final int CELL_CAF_XLS_LIMIT = 3;
	public static final int CELL_CAF_XLS_GROUP = 6;
	public static final int CELL_CAF_XLS_AREA = 5;
	public static final Locale huHU = new Locale("hu", "HU");
	public static final NumberFormat NF = NumberFormat.getNumberInstance(huHU);
	public static final DateFormatSymbols HUDATE = new DateFormatSymbols(huHU);
	public static final int CELL_CAF_XLS_CATFROM = 7;

	public static String getRowCell(org.apache.poi.ss.usermodel.Sheet sheet, int row, int cell) {
		Cell c = getRowCellCell(sheet, row, cell);
		if (c != null) {
			// van cella
			if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				return c.getStringCellValue() != null ? c.getStringCellValue().trim() : null;
			} else if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return String.valueOf(c.getNumericCellValue());
			} else if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
				// hmmm
				new IllegalArgumentException(
						asCell(row, cell) + " nor string or num but [" + c.getCellType() + "] value: " + c.toString())
								.printStackTrace();
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
				String cval = c.getStringCellValue();
				try {
					// System.out.println("get num from string " +
					// c.getStringCellValue());
					d = StringUtils.isNotEmpty(cval) && cval.trim().length() > 0 ? new Double(cval.trim()) : null;
				} catch (Exception e) {
					new IllegalArgumentException(
							"row:" + row + " cell:" + cell + "[" + asCell(cell) + "] value:'" + cval + "'", e)
									.printStackTrace();
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

	public static String asCell(int columnNumber) {
		// cell.getSheet().getRow(0).getCell(currentcellIndex)
		// .getRichStringCellValue().toString()
		return CellReference.convertNumToColString(columnNumber);
	}

	public static int asCell(String columnName) {
		if (columnName != null)
			return CellReference.convertColStringToIndex(columnName);
		return -1;
	}

	public static String asCell(int rowIndex, int colIndex) {
		return asCell(colIndex) + ":" + (rowIndex + 1);
	}

	public static void main(String[] args) {
		System.out.println(asCell(777));
		System.out.println(asCell(-1));
		System.out.println(asCell("777"));
		System.out.println(asCell(null));

	}
}
