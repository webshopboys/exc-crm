package hu.exclusive.crm.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.dao.model.StaffCafeteria;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.utils.ObjectUtils;

public class CafeteriaExcel {

	private String taxSerial;
	private Date startDate;
	private BigDecimal yearlyLimit;
	private StaffCafeteria staff;
	private Workgroup workgroup;
	private List<Cafeteria> monthlyCafes;

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
	}

	public List<Cafeteria> getMonthlyCafes() {
		return monthlyCafes;
	}

	public void setMonthlyCafes(List<Cafeteria> monthlyCafes) {
		this.monthlyCafes = monthlyCafes;
	}

	public StaffCafeteria getStaff() {
		return staff;
	}

	public void setStaff(StaffCafeteria staff) {
		this.staff = staff;
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
		return ObjectUtils.compareDays(staff.getEmployStart(), startDate) > 0 ? "differentBaseData" : "";
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

	public String getTaxCSS() {
		String s = getComparedTaxNumber();
		if (s == null)
			return "nullBaseData";
		if (s.contains(" ("))
			return "differentBaseData";
		return "";
	}

	/**
	 * A kitoltott vagy mindket csoport.
	 * 
	 * @return
	 */
	public String getComparedWorkGroup() {
		if (workgroup != null) {
			if (staff.getWorkgroup() != null && workgroup.getIdWorkgroup() != workgroup.getIdWorkgroup())
				return workgroup.getGroupName() + " (" + staff.getWorkgroup().getGroupName() + ")";
			return workgroup.getGroupName();
		}
		return staff.getWorkgroup() == null ? null : staff.getWorkgroup().getGroupName();
	}

	public String getGroupCSS() {
		String s = getComparedWorkGroup();
		if (s == null)
			return "nullBaseData";
		if (s.contains(" ("))
			return "differentBaseData";
		return "";
	}
}
