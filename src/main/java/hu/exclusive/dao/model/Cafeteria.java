package hu.exclusive.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The persistent class for the t_cafeteria database table.
 * 
 */
@Entity
@Table(name = "T_CAFETERIA")
@NamedQuery(name = "Cafeteria.findAll", query = "SELECT c FROM Cafeteria c")
public class Cafeteria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cafeteria")
	private Integer idCafeteria;

	private BigDecimal amount;

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@PrimaryKeyJoinColumn
	private PCafeteriaCategory cafeCategory;

	@Column(name = "id_staff")
	private Integer idStaff;

	@Column(name = "month_key")
	private int monthKey;

	private Timestamp updated;

	private String updater;

	@Column(name = "year_key")
	private int yearKey;

	public Cafeteria() {
	}

	public Integer getIdCafeteria() {
		return this.idCafeteria;
	}

	public void setIdCafeteria(Integer idCafeteria) {
		this.idCafeteria = idCafeteria;
	}

	public PCafeteriaCategory setCafeCategory() {
		return cafeCategory;
	}

	public void setCafeCategory(PCafeteriaCategory cafeteria) {
		this.cafeCategory = cafeteria;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getIdStaff() {
		return this.idStaff;
	}

	public void setIdStaff(Integer idStaff) {
		this.idStaff = idStaff;
	}

	public int getMonthKey() {
		return this.monthKey;
	}

	public void setMonthKey(int monthKey) {
		this.monthKey = monthKey;
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

}