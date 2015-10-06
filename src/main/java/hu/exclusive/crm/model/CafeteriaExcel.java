package hu.exclusive.crm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import hu.exclusive.crm.report.POIUtil;
import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.StaffCafeteria;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.utils.ObjectUtils;

public class CafeteriaExcel {

	private String taxSerial;
	private Date startDate;
	private BigDecimal yearlyLimit;
	private StaffCafeteria staff;
	private Workgroup workgroup;
	private List<Cafeteria> excelMonthlyCafes;
	private int year;

	public String getTaxSerial() {
		return taxSerial;
	}

	public void setTaxSerial(String taxSerial) {
		this.taxSerial = taxSerial;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getYearlyLimit() {
		return yearlyLimit;
	}

	public void setYearlyLimit(BigDecimal yearlyLimit) {
		this.yearlyLimit = yearlyLimit;
	}

	public Workgroup getWorkgroup() {
		return workgroup;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
		// if (getStaff().getWorkgroup() != null) {
		// System.out.println(getStaff().getFullName() + " wg " + workgroup
		// + "\n" + getStaff().getWorkgroup());
		// }
	}

	public List<Cafeteria> getExcelMonthlyCafes() {
		return excelMonthlyCafes;
	}

	public void setExcelMonthlyCafes(List<Cafeteria> monthlyCafes) {
		this.excelMonthlyCafes = monthlyCafes;
	}

	public StaffCafeteria getStaff() {
		return staff;
	}

	public void setStaff(StaffCafeteria staff) {
		this.staff = staff;
	}

	public Cafeteria getMonthlyCafeteria(int month, PCafeteriaCategory cat) {
		if (excelMonthlyCafes == null) {
			excelMonthlyCafes = new ArrayList<>();
		}
		for (Cafeteria cafeteria : excelMonthlyCafes) {
			if (cafeteria.getMonthKey() == month && cafeteria.getCafeCategory() != null
					&& cafeteria.getCafeCategory().getIdCafeteriaCat() != null
					&& cafeteria.getCafeCategory().getIdCafeteriaCat() == cat.getIdCafeteriaCat()) {
				return cafeteria;
			}
		}
		return null;
	}

	public Cafeteria getSetMonthlyCafeteria(int month, PCafeteriaCategory cat) {
		Cafeteria cafeteria = getMonthlyCafeteria(month, cat);
		if (cafeteria == null) {
			cafeteria = new Cafeteria();
			excelMonthlyCafes.add(cafeteria);

			cafeteria.setIdStaff(getStaff().getIdStaff());
			cafeteria.setMonthKey(month);
			// cafeteria.setYearKey(yearKey);
			cafeteria.setCafeCategory(cat);
		}
		return cafeteria;
	}

	/**
	 * A kitoltott vagy mindket datum.
	 * 
	 * @return
	 */
	public String getComparedStartDate() {
		int x = ObjectUtils.compareDays(staff.getEmployStart(), startDate);

		return (x == 0 || x == -2) ? ObjectUtils.getDate(startDate)
				: x == -1 ? ObjectUtils.getDate(staff.getEmployStart())
						: (ObjectUtils.getDate(startDate) + " (" + ObjectUtils.getDate(staff.getEmployStart()) + ")");

	}

	public String getDateCSS() {
		return isEmptyStart() ? "nullBaseData" : isDifferentStart() ? "differentBaseData" : "";
	}

	public boolean isEmptyStart() {
		return startDate == null && staff.getEmployStart() == null;
	}

	public boolean isDifferentStart() {
		return ObjectUtils.compareDays(staff.getEmployStart(), startDate) > 0;
	}

	/**
	 * A kitoltott vagy mindket adoszam.
	 * 
	 * @return
	 */
	public String getComparedTaxNumber() {
		if (StringUtils.isNotEmpty(taxSerial)) {
			if (StringUtils.isNotEmpty(staff.getTaxSerial()) && !taxSerial.equals(staff.getTaxSerial()))
				return taxSerial + " (" + staff.getTaxSerial() + ")";
			return taxSerial;
		}
		return staff.getTaxSerial();
	}

	public boolean isEmptyTax() {
		String s = getComparedTaxNumber();
		return StringUtils.isEmpty(s);
	}

	public boolean isDifferentTax() {
		String s = getComparedTaxNumber();
		return StringUtils.isNotEmpty(s) && (s.contains(" ("));
	}

	public String getTaxCSS() {
		return isEmptyTax() ? "nullBaseData" : isDifferentTax() ? "differentBaseData" : "";
	}

	/**
	 * A kitoltott vagy mindket csoport.
	 * 
	 * @return
	 */
	public String getComparedWorkGroup() {
		Workgroup excelgroup = workgroup;
		Workgroup staffgroup = staff.getWorkgroup();

		if (excelgroup != null) {
			if (staffgroup != null && excelgroup.getIdWorkgroup() != staffgroup.getIdWorkgroup())
				return excelgroup.getCompanyName() + " (" + staffgroup.getCompanyName() + ")";
			return excelgroup.getCompanyName();
		}
		return staffgroup == null ? null : staffgroup.getCompanyName();
	}

	public boolean isEmptyGroup() {
		String s = getComparedWorkGroup();
		return StringUtils.isEmpty(s);
	}

	public boolean isDifferentGroup() {
		String s = getComparedWorkGroup();
		return StringUtils.isNotEmpty(s) && (s.contains(" ("));
	}

	public String getGroupCSS() {
		return isEmptyGroup() ? "nullBaseData" : isDifferentGroup() ? "differentBaseData" : "";
	}

	public boolean isProblematic() {
		return isEmptyTax() || isDifferentTax() || isEmptyStart() || isDifferentStart() || isEmptyGroup()
				|| isDifferentGroup();
	}

	public String getComparedLimit() {
		String newLimit = null;
		String oldLimit = null;
		if (yearlyLimit != null) {
			newLimit = POIUtil.NF.format(yearlyLimit);
		}
		if (getStaff().getYearlyLimit(-1) != null) {
			oldLimit = POIUtil.NF.format(getStaff().getYearlyLimit(-1));
		}
		return newLimit == null ? oldLimit : oldLimit == null ? newLimit : newLimit + " (" + oldLimit + ")";
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
