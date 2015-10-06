package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the p_cafeteria_cat database table.
 * 
 */
@Entity
@Table(name = "P_CAFETERIA_CAT")

@NamedQueries({ @NamedQuery(name = "PCafeteriaCategory.findAll", query = "SELECT p FROM PCafeteriaCategory p"),
		@NamedQuery(name = "PCafeteriaCategory.findByName", query = "SELECT p FROM PCafeteriaCategory p WHERE upper(p.categoryName) like :name"),
		@NamedQuery(name = "PCafeteriaCategory.findByKey", query = "SELECT p FROM PCafeteriaCategory p WHERE upper(p.categoryKeys) like :name"), })
public class PCafeteriaCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cafeteria_cat")
	private Integer idCafeteriaCat;

	@Column(name = "category_info")
	private String categoryInfo;

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "category_keys")
	private String categoryKeys;

	private boolean enabled;

	@Column(name = "monthly_limit")
	private Integer monthlyLimit;

	@Column(name = "yearly_limit")
	private Integer yearlyLimit;

	public PCafeteriaCategory() {
	}

	public Integer getIdCafeteriaCat() {
		return this.idCafeteriaCat;
	}

	public void setIdCafeteriaCat(Integer idCafeteriaCat) {
		this.idCafeteriaCat = idCafeteriaCat;
	}

	public String getCategoryInfo() {
		return this.categoryInfo;
	}

	public void setCategoryInfo(String categoryInfo) {
		this.categoryInfo = categoryInfo;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(Integer monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public Integer getYearlyLimit() {
		return yearlyLimit;
	}

	public void setYearlyLimit(Integer yearlyLimit) {
		this.yearlyLimit = yearlyLimit;
	}

	public String getCategoryKeys() {
		return categoryKeys;
	}

	public void setCategoryKeys(String categoryKeys) {
		this.categoryKeys = categoryKeys;
	}

}