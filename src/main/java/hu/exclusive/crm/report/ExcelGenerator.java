package hu.exclusive.crm.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelGenerator {

	public static Workbook getWorkBook(byte[] stream)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		java.io.ByteArrayInputStream in = new ByteArrayInputStream(stream);

		Workbook wb = WorkbookFactory.create(in);
		return wb;
	}

}
