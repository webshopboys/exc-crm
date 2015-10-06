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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import hu.exclusive.crm.report.POIUtil;

/**
 * The persistent class for the t_cafeteria database table.
 * 
 */
@Entity
@Table(name = "T_CAFETERIA")
@NamedQueries({ @NamedQuery(name = "Cafeteria.findAll", query = "SELECT c FROM Cafeteria c"),
		@NamedQuery(name = "Cafeteria.findByDate", query = "SELECT c FROM Cafeteria c WHERE c.yearKey = :year AND c.monthKey = :month"),
		@NamedQuery(name = "Cafeteria.deleteStaffYearMonth", query = "DELETE FROM Cafeteria c WHERE c.idStaff = :staff AND c.yearKey = :year AND c.monthKey = :month"),
		@NamedQuery(name = "Cafeteria.deleteStaffYearMonthCategory", query = "DELETE FROM Cafeteria c WHERE c.idStaff = :staff AND c.yearKey = :year AND c.monthKey = :month AND (c.cafeCategory.idCafeteriaCat = null OR c.cafeCategory.idCafeteriaCat = :category)") })
public class Cafeteria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cafeteria")
	private Integer idCafeteria;

	private BigDecimal amount;

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@PrimaryKeyJoinColumn
	private PCafeteriaCategory cafeCategory;

	@Column(name = "id_staff")
	private Integer idStaff;

	@Column(name = "id_cafeteria_cat")
	private int idCategory;

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

	public PCafeteriaCategory getCafeCategory() {
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

	public int getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
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

	public String monthName() {
		if (monthKey > 0) {
			return POIUtil.HUDATE.getMonths()[monthKey - 1];
		}
		return null;
	}

	@Override
	public String toString() {
		return "Cafeteria [yearKey=" + yearKey + ", monthKey=" + monthKey + ", cafeCategory=" + cafeCategory
				+ ", amount=" + amount + ", idStaff=" + idStaff + ", idCafeteria=" + idCafeteria + "]";
	}

	public String display() {
		if (yearKey > 0 && monthKey > 0) {
			return yearKey + " " + POIUtil.HUDATE.getMonths()[monthKey - 1] + ": "
					+ (cafeCategory == null ? "<???>" : cafeCategory.getCategoryName()) + " "
					+ (amount == null ? "<??.??>" : POIUtil.NF.format(amount)) + " Ft";
		}
		return null;
	}

}