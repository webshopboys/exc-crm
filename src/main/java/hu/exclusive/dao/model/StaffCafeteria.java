package hu.exclusive.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * The persistent class for the T_STAFF + T_CAFETERIA + T_CAFETERIA_INFO
 * database tables.
 * 
 */
@Entity
@Table(name = "T_STAFF")
@NamedQueries({
		@NamedQuery(name = "StaffCafeteria.findStaff", query = "SELECT s FROM StaffCafeteria s WHERE s.idStaff = :idStaff"),
		@NamedQuery(name = "StaffCafeteria.getStaffByName", query = "SELECT s FROM StaffCafeteria s WHERE s.status = 'Aktív' AND upper(s.fullName) like :personName "),
		@NamedQuery(name = "StaffCafeteria.getStaffByNameTax", query = "SELECT s FROM StaffCafeteria s WHERE s.status = 'Aktív' AND upper(s.fullName) like :personName AND (s.taxSerial IS NULL OR s.taxSerial = :tax) ") })
public class StaffCafeteria extends StaffBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public StaffCafeteria() {
	}

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_staff", referencedColumnName = "id_staff")
	@OrderBy("year_key ASC, month_key ASC")
	private List<Cafeteria> monthlyCafes;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_staff", referencedColumnName = "id_staff")
	private List<CafeteriaInfo> infos;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinTable(name = "K_STAFF_WORKGROUP", joinColumns = @JoinColumn(name = "id_staff", nullable = false) , inverseJoinColumns = @JoinColumn(name = "id_workgroup", nullable = true) )
	private Workgroup workgroup;

	public List<Cafeteria> getMonthlyCafes() {
		return monthlyCafes;
	}

	public void setMonthlyCafes(List<Cafeteria> monthlyCafes) {
		this.monthlyCafes = monthlyCafes;
	}

	public Workgroup getWorkgroup() {
		return this.workgroup;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	/**
	 * Az adott éves keret a listaban, ami a ev filterhez tartozo, vagy az
	 * aktualis.
	 * 
	 * @return
	 */
	public BigDecimal getYearlyLimit(int year) {
		return getYearlyInfo(year).getYearLimit();
	}

	public CafeteriaInfo getYearlyInfo() {
		return getYearlyInfo(-1);
	}

	/**
	 * Az aktualis eves info rekord. Ebbe kerülnek a modosithato reszek es az
	 * eves keret. Az adott eves info rekord.
	 */
	public CafeteriaInfo getYearlyInfo(int year) {
		CafeteriaInfo yearlyInfo = null;
		if (year == -1)
			year = Calendar.getInstance().get(Calendar.YEAR);

		for (CafeteriaInfo info : getInfos()) {
			if (year == info.getYearKey()) {
				yearlyInfo = info;
				break;
			}
		}
		if (yearlyInfo == null) {
			yearlyInfo = new CafeteriaInfo();
			getInfos().add(yearlyInfo);
			yearlyInfo.setYearKey(Calendar.getInstance().get(Calendar.YEAR));
		}

		return yearlyInfo;
	}

	public List<CafeteriaInfo> getInfos() {
		if (infos == null)
			infos = new ArrayList<CafeteriaInfo>();
		return infos;
	}

	public void setInfos(List<CafeteriaInfo> infos) {
		this.infos = infos;
	}

}