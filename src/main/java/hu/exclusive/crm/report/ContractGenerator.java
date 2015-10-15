package hu.exclusive.crm.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.NotSupportedException;
import javax.xml.bind.JAXBElement;

import org.docx4j.model.fields.docproperty.DocPropertyResolver;
import org.docx4j.openpackaging.contenttype.CTOverride;
import org.docx4j.openpackaging.contenttype.ContentTypeManager;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import hu.exclusive.crm.report.ReportParameterProvider.DOCFIELDS_NAMES;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.StaffDetail;
import hu.exclusive.utils.ObjectUtils;

/**
 * A staff és sablon alapján előállítja és letöltésre dobja a szerződést.
 * Támogatott doc típusok:
 * <ul>
 * <li>*.docx / *.dotx (docx4j)
 * <li>*.doc (Apache POI) *
 * <li>*.odt (Apache ODF Toolkit)
 * </ul>
 * 
 * Minhárom esetben a [[fieldkey]] alakú szövegeket cseréli le, melyek nem
 * valódi mezők, hanem textek!
 * 
 * @author Petnehazi
 *
 */
public class ContractGenerator {

	private ContractDoc template;
	private StaffDetail staff;

	DocPropertyResolver docPropertyResolver;

	public StringBuilder report = new StringBuilder();
	private ReportParameterProvider pp = new ReportParameterProvider();

	public ContractGenerator(ContractDoc template, StaffDetail staff) {

		this.template = template;
		this.staff = staff;
	}

	private final static Pattern paramPatern = Pattern.compile("(?i)(\\$\\{([\\w\\.]+)\\})");

	private Map<String, String> initialParameters(Map<String, String> replacement) {
		if (this.template != null) {
			if (replacement == null)
				replacement = new HashMap<String, String>();
			// inicializalas a bean-bol
			for (DOCFIELDS_NAMES field : DOCFIELDS_NAMES.values()) {
				if (!replacement.containsKey(field.name())) {
					replacement.put(field.name(), getStaffField(field));
				}
			}
		}
		return replacement;
	}

	public byte[] processByExtension() throws Exception {

		if (this.template != null) {

			Map<String, String> replacement = initialParameters(null);
			WordprocessingMLPackage doc = null;
			if (template.getDocumentUrl().endsWith(".docx") || template.getDocumentUrl().endsWith(".dotx")) {
				doc = processDOCX(replacement);
			} else if (template.getDocumentUrl().endsWith(".doc")) {
				doc = processDOC(replacement);
			} else if (template.getDocumentUrl().endsWith(".odt")) {
				doc = processODT(replacement);
			} else {
				report.append(
						"Nem támogatott fájl típus: " + ObjectUtils.getFileName(template.getDocumentUrl()) + "\n");
				throw new IllegalArgumentException(
						"Nem támogatott fájl típus: " + ObjectUtils.getFileName(template.getDocumentUrl()));
			}

			if (doc != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				doc.save(out);
				return out.toByteArray();
			}

		} else {
			report.append("Nincs sablon rekord!\n");
			throw new IllegalArgumentException("Nincs sablon rekord!");
		}

		return new byte[0];
	}

	public void processByExtension(Map<String, String> replacement, OutputStream out) throws Exception {

		if (this.template != null) {

			replacement = initialParameters(replacement);

			if (template.getDocumentUrl().endsWith(".docx") || template.getDocumentUrl().endsWith(".dotx")) {
				processDOCX(replacement);
			} else if (template.getDocumentUrl().endsWith(".doc")) {
				processDOC(replacement);
			} else if (template.getDocumentUrl().endsWith(".odt")) {
				processODT(replacement);
			} else {
				report.append(
						"Nem támogatott fájl típus: " + ObjectUtils.getFileName(template.getDocumentUrl()) + "\n");
				throw new IllegalArgumentException(
						"Nem támogatott fájl típus: " + ObjectUtils.getFileName(template.getDocumentUrl()));
			}

		} else {
			report.append("Nincs sablon rekord!\n");
			throw new IllegalArgumentException("Nincs sablon rekord!");
		}
	}

	public WordprocessingMLPackage processDOCX(final Map<String, String> replacement) throws Exception {
		String fileName = ObjectUtils.getFileName(template.getDocumentUrl());

		report.append("processDOCX...\n");
		InputStream fileSource = new ByteArrayInputStream(template.getDocumentBin());
		WordprocessingMLPackage doc = openDoc(fileSource, fileName);
		replace(doc, replacement);
		return doc;
	}

	public WordprocessingMLPackage processDOC(final Map<String, String> replacement) throws Exception {
		throw new NotSupportedException("Nincs még DOC kezelés.");
	}

	public WordprocessingMLPackage processODT(final Map<String, String> replacement) throws Exception {
		throw new NotSupportedException("Nincs még ODT kezelés.");
	}

	WordprocessingMLPackage openDoc(String path) throws Exception {
		return openDoc(new File(path));
	}

	WordprocessingMLPackage openDoc(File docFile) throws Exception {
		return openDoc(new FileInputStream(docFile), docFile.getName());
	}

	private WordprocessingMLPackage openDoc(InputStream fileSource, String fileName) throws Exception {

		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fileSource);

		// Replace dotx content type with docx
		ContentTypeManager ctm = wordMLPackage.getContentTypeManager();

		// Get <Override PartName="/word/document.xml"
		// ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.template.main+xml"/>
		CTOverride override = ctm.getOverrideContentType().get(new URI("/word/document.xml")); // note
																								// this
																								// assumption

		if (fileName.endsWith("dotm")) // // macro enabled?
		{
			override.setContentType(
					org.docx4j.openpackaging.contenttype.ContentTypes.WORDPROCESSINGML_DOCUMENT_MACROENABLED);
		} else {
			override.setContentType(org.docx4j.openpackaging.contenttype.ContentTypes.WORDPROCESSINGML_DOCUMENT);
		}

		return wordMLPackage;

	}

	//
	// private void writeDocxToStream(WordprocessingMLPackage doc, OutputStream
	// out) throws IOException, Docx4JException {
	// doc.save(out);
	// }
	//
	// private void writeDocxToStream(WordprocessingMLPackage doc, File file)
	// throws IOException, Docx4JException {
	// doc.save(file);
	// }

	private void replace(WordprocessingMLPackage doc, Map<String, String> values) {
		List<Object> texts = getAllElementFromObject(doc.getMainDocumentPart(), Text.class, "${");
		replace(texts, values);
	}

	private void replace(List<Object> texts, Map<String, String> values) {

		report.append("Map:" + values + "\n");

		for (int i = 0; i < texts.size(); i++) {
			Object text = texts.get(i);
			Text textElement = (Text) text;
			report.append("Text: " + textElement.getValue() + "\n");

			// textElement.setSpace("preserve"); // needed?
			textElement.setValue(replaceParams(textElement.getValue(), values));

			report.append(" value: " + textElement.getValue() + "\n");
		}

	}

	private String replaceParams(String text, Map<String, String> values) {

		if (text == null)
			return "";

		if (values == null || values.isEmpty())
			return text;

		Matcher m = paramPatern.matcher(text);

		if (!m.find())
			return text;

		// report.append(" A textre okés a regexp\n");

		StringBuffer sb = new StringBuffer();
		String param, replacement;

		do {
			param = m.group(2);
			// report.append(" Param:" + param + "\n");
			if (param != null) {
				replacement = getParamValue(param, values, text);
				m.appendReplacement(sb, replacement);
			} else
				m.appendReplacement(sb, "");
		} while (m.find());

		m.appendTail(sb);
		return sb.toString();
	}

	private String getParamValue(String key, Map<String, String> values, String text) {
		// report.append(" getParamValue " + key + "\n");
		if (values.containsKey(key)) {
			return values.get(key) == null ? "" : values.get(key);
		}

		return "<" + key + ">";
	}

	private List<Object> getAllElementFromObject(Object obj, Class<?> toSearch, String prefix) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		if (obj.getClass().equals(toSearch)) {

			if (prefix != null) {
				if (((Text) obj).getValue() != null && ((Text) obj).getValue().contains(prefix)) {
					// report.append("Text: " + ((Text) obj).getValue() +
					// "\n\n");
					result.add(obj);
				}
			} else {
				// report.append("Text obj: " + obj + "\n\n");
				result.add(obj);
			}
		} else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			// report.append("Text children rekurzio.\n");
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch, prefix));
			}

		}
		return result;
	}

	private String getStaffField(DOCFIELDS_NAMES field) {
		if (staff != null)
			switch (field) {
			case TELJESNEV:
				return staff.getFullName();
			case SZULETESINEV:
				return staff.getBirthName();
			case SZULETESIDATUM:
				return ObjectUtils.getDate(staff.getBirthDate());
			case SZULETESIHELY:
				return staff.getBirthPlace();
			case ANYJANEVE:
				return staff.getMotherName();
			case SZIGSZAM:
				return staff.getSidSerial();
			case TAJSZAM:
				return staff.getTajSerial();
			case ADOSZAM:
				return staff.getTaxSerial();
			case ALLANDOCIM:
				return staff.getResidentAddress();
			case TARTOZKODASIHELY:
				return staff.getHomeAddress();
			case BANKSZAMLASZAM:
				return staff.getAccountNumber();
			case MUNKALTATONEV:
				return pp.getCompanyName(staff);
			case MUNKALTATOCIM:
				return pp.getCompanyAddress(staff);
			case MUNKALTATOADOSZAM:
				return pp.getCompanyTax(staff);
			case MUNKALTATOCEGSZAM:
				return pp.getCompanySerial(staff);
			case MUNKALTATOKEPVISELO:
				return pp.getCompanyRepresentative(staff);
			case KEZDEV:
				return pp.getStartYear(staff);
			case KEZDHONAP:
				return pp.getStartMonth(staff);
			case KEZDNAP:
				return pp.getStartDay(staff);
			case TELEPHELYEK:
				return pp.getPlaces(staff);
			case PROBANAPOK:
				return pp.getTrialPeriod(staff);
			case BRUTTOSZAM:
				return pp.getSalaryNum(staff);
			case BRUTTOSZOVEG:
				return pp.getSalaryText(staff);
			case BERTIPUS:
				return pp.getSalaryType(staff);
			case ORABER:
				return pp.getHourPay(staff);
			case NAPIBER:
				return pp.getDailyPay(staff);
			case HAVIBER:
				return pp.getMonthlyPay(staff);
			case HATAROZOTTIGEV:
				return pp.getEndYear(staff);
			case HATAROZOTTIGHONAP:
				return pp.getEndMonth(staff);
			case HATAROZOTTIGNAP:
				return pp.getEndDay(staff);
			case MUNKAIDEJE:
				return pp.getPartialJobType(staff);
			case MUNKAKOROK:
				return pp.getJobtitles(staff);
			case MAINAP:
				return ObjectUtils.getDateTime(new Date());
			case DATUMEV:
				return ReportParameterProvider.YEAR.format(new Date());
			case DATUMHONAP:
				return ReportParameterProvider.MONTHNAMES.format(new Date());
			case DATUMNAP:
				return ReportParameterProvider.DAY.format(new Date());

			default:
				return "";
			}
		return "";
	}
}
