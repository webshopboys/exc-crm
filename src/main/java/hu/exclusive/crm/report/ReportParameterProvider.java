package hu.exclusive.crm.report;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.exclusive.dao.model.StaffDetail;

/**
 * A paraméterek megokosításához használt wrapper.
 * 
 * @author user
 *
 */
@Component
public class ReportParameterProvider {

	@Autowired
	transient hu.exclusive.crm.service.ParametersService parametersService;
	public final static SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
	public final static SimpleDateFormat MONTHNAMES = new SimpleDateFormat("MMMMM", new Locale("hu", "HU"));
	public final static SimpleDateFormat DAY = new SimpleDateFormat("dd");
	public final static String PLH = "..............";

	public static enum DOCFIELDS_NAMES {
		TELJESNEV, SZULETESINEV, SZULETESIDATUM, ALLANDOCIM, SZULETESIHELY, ANYJANEVE, SZIGSZAM, TAJSZAM, ADOSZAM, BANKSZAMLASZAM, TARTOZKODASIHELY,

		MUNKALTATONEV, MUNKALTATOCIM, MUNKALTATOADOSZAM, MUNKALTATOCEGSZAM, MUNKALTATOKEPVISELO, KEZDHONAP, KEZDEV, KEZDNAP, TELEPHELYEK,

		PROBANAPOK, BRUTTOSZAM, BRUTTOSZOVEG, BERTIPUS, HAVIBER, NAPIBER, ORABER,

		HATAROZOTTIGEV, HATAROZOTTIGHONAP, HATAROZOTTIGNAP, MUNKAIDEJE, MUNKAKOROK, MAINAP, DATUMEV, DATUMHONAP, DATUMNAP;

		public String getLabel() {
			switch (this) {
			case TELJESNEV:
				return "Teljes név";
			case SZULETESINEV:
				return "Születése név";
			case SZULETESIDATUM:
				return "Születései dátum";
			case SZULETESIHELY:
				return "Születési hely";
			case ANYJANEVE:
				return "Anyja neve";
			case SZIGSZAM:
				return "Okirat száma";
			case TAJSZAM:
				return "TAJ szám";
			case ADOSZAM:
				return "Adószám";
			case ALLANDOCIM:
				return "Állandó cím";
			case TARTOZKODASIHELY:
				return "Tartozkodási hely";
			case BANKSZAMLASZAM:
				return "Banszámlára";
			case MUNKALTATONEV:
				return "Munkáltató cég neve";
			case MUNKALTATOCIM:
				return "Munkáltató címe";
			case MUNKALTATOADOSZAM:
				return "Munkáltató adószáma";
			case MUNKALTATOCEGSZAM:
				return "Munkáltató cégszáma";
			case MUNKALTATOKEPVISELO:
				return "Munkáltató képviselője";
			case KEZDEV:
				return "Kezdés éve";
			case KEZDHONAP:
				return "Kezdés hónapja";
			case KEZDNAP:
				return "Kezdés napja";
			case TELEPHELYEK:
				return "Telephelyek";
			case PROBANAPOK:
				return "Próbanapok száma";
			case BRUTTOSZAM:
				return "Bruttó bér";
			case BRUTTOSZOVEG:
				return "Bruttó bér szövegesen";
			case BERTIPUS:
				return "Havibér vagy órabér";
			case NAPIBER:
				return "Napibér összege";
			case HAVIBER:
				return "Havibér összege";
			case ORABER:
				return "Órabér összege";

			case HATAROZOTTIGEV:
				return "Határozott munkaviszony végének éve";
			case HATAROZOTTIGHONAP:
				return "Határozott munkaviszony végének hónapja";
			case HATAROZOTTIGNAP:
				return "Határozott munkaviszony végének napja";
			case MUNKAIDEJE:
				return "Munkaideje teljes vagy részmunkaidő";
			case MUNKAKOROK:
				return "Munkakörei";
			case MAINAP:
				return "Aktuális dátum";
			case DATUMEV:
				return "Aktuális év";
			case DATUMHONAP:
				return "Aktuális hónap";
			case DATUMNAP:
				return "Mai nap";
			default:
				return "-";
			}
		}

		public static String getInfo() {
			StringBuilder sb = new StringBuilder();
			for (DOCFIELDS_NAMES field : DOCFIELDS_NAMES.values()) {
				String s = "${" + field.name() + "} (" + field.getLabel() + "); ";
				sb.append(s);
			}

			return sb.toString();
		}
	};

	public String getValue(String barePlaceholder) {
		return "<<" + barePlaceholder + ">>";
	}

	public String getCompanyName(StaffDetail staff) {
		return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getCompanyName();
	}

	public String getCompanyAddress(StaffDetail staff) {
		return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getAddress();
	}

	public String getCompanyTax(StaffDetail staff) {
		return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getTaxNumber();
	}

	public String getCompanySerial(StaffDetail staff) {
		return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getCompanyNumber();
	}

	public String getCompanyRepresentative(StaffDetail staff) {
		return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getRepresentative();
	}

	public String getStartYear(StaffDetail staff) {
		return staff.getEmployStart() == null ? PLH : YEAR.format(staff.getEmployStart());
	}

	public String getStartMonth(StaffDetail staff) {
		return staff.getEmployStart() == null ? PLH : MONTHNAMES.format(staff.getEmployStart());
	}

	public String getStartDay(StaffDetail staff) {
		return staff.getEmployStart() == null ? PLH : DAY.format(staff.getEmployStart());
	}

	public String getPlaces(StaffDetail staff) {
		if (staff.getWorkplaces() == null || staff.getWorkplaces().isEmpty())
			return PLH;
		String titles = staff.getWorkplacesAsString().trim();
		if (titles.endsWith(";"))
			titles = titles.substring(0, titles.length() - 1);
		return "'" + titles + "'";

	}

	public String getSalaryNum(StaffDetail staff) {
		return staff.getBaseSalary() == null || staff.getBaseSalary().intValue() == 0 ? PLH
				: String.valueOf(staff.getBaseSalary().longValue());
	}

	public String getMonthlyPay(StaffDetail staff) {
		if ("havibér".equals(getSalaryType(staff)))
			return getSalaryNum(staff);
		return PLH;
	}

	public String getDailyPay(StaffDetail staff) {
		if ("napibér".equals(getSalaryType(staff)))
			return getSalaryNum(staff);
		return PLH;
	}

	public String getHourPay(StaffDetail staff) {
		if ("órabér".equals(getSalaryType(staff)))
			return getSalaryNum(staff);
		return PLH;
	}

	public String getSalaryText(StaffDetail staff) {
		return staff.getBaseSalary() == null || staff.getBaseSalary().intValue() == 0 ? PLH
				: HungarianNumbers.convert(staff.getBaseSalary().longValue());
	}

	public String getSalaryType(StaffDetail staff) {
		return staff.getSalaryType() == null ? PLH : staff.getSalaryType().toLowerCase();
	}

	public String getEndYear(StaffDetail staff) {
		return staff.getEmployFinish() == null ? PLH : YEAR.format(staff.getEmployFinish());
	}

	public String getEndMonth(StaffDetail staff) {
		return staff.getEmployFinish() == null ? PLH : MONTHNAMES.format(staff.getEmployFinish());
	}

	public String getEndDay(StaffDetail staff) {
		return staff.getEmployFinish() == null ? PLH : DAY.format(staff.getEmployFinish());
	}

	public String getPartialJobType(StaffDetail staff) {
		return staff.getEmployType() == null ? PLH : staff.getEmployType().toLowerCase();
	}

	public String getJobtitles(StaffDetail staff) {
		if (staff.getJobtitles() == null || staff.getJobtitles().isEmpty())
			return PLH;
		String titles = staff.getJobtitlesAsString().trim();
		if (titles.endsWith(";"))
			titles = titles.substring(0, titles.length() - 1);
		return titles.toLowerCase();
	}

	public String getTrialPeriod(StaffDetail staff) {
		return staff.getTrialPeriod() == null || staff.getTrialPeriod().intValue() == 0 ? PLH
				: String.valueOf(staff.getTrialPeriod().intValue());
	}

}
