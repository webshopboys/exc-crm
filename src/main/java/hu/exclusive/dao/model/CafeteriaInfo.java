package hu.exclusive.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the t_cafeteria_info database table.
 * 
 */
@Entity
@Table(name = "T_CAFETERIA_INFO")
@NamedQuery(name = "CafeteriaInfo.findAll", query = "SELECT c FROM CafeteriaInfo c")
public class CafeteriaInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_caf_info")
	private Integer idCafInfo;

	@Column(name = "id_staff")
	private Integer idStaff;

	private String info1;

	private String info2;

	private Timestamp updated;

	private String updater;

	@Column(name = "year_key")
	private int yearKey;

	@Column(name = "year_limit")
	private BigDecimal yearLimit;

	public CafeteriaInfo() {
	}

	public Integer getIdCafInfo() {
		return this.idCafInfo;
	}

	public void setIdCafInfo(Integer idCafInfo) {
		this.idCafInfo = idCafInfo;
	}

	public String getInfo1() {
		return this.info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getInfo2() {
		return this.info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getUpdater() {
		return this.updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public int getYearKey() {
		return this.yearKey;
	}

	public void setYearKey(int yearKey) {
		this.yearKey = yearKey;
	}

	public BigDecimal getYearLimit() {
		return this.yearLimit;
	}

	public void setYearLimit(BigDecimal yearLimit) {
		this.yearLimit = yearLimit;
	}

	public Integer getIdStaff() {
		return idStaff;
	}

	public void setIdStaff(Integer idStaff) {
		this.idStaff = idStaff;
	}

	@Override
	public String toString() {
		return "CafeteriaInfo [idCafInfo=" + idCafInfo + ", idStaff=" + idStaff + ", info1=" + info1 + ", info2="
				+ info2 + ", updated=" + updated + ", updater=" + updater + ", yearKey=" + yearKey + ", yearLimit="
				+ yearLimit + "]";
	}

}