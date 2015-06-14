package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the p_cafeteria_limit database table.
 * 
 */
@Entity
@Table(name = "p_cafeteria_limit")
@NamedQuery(name = "PCafeteriaLimit.findAll", query = "SELECT p FROM PCafeteriaLimit p")
public class PCafeteriaLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cafeteria_limit")
    private Integer idCafeteriaLimit;

    @Column(name = "limit_label")
    private String limitLabel;

    @Column(name = "minimum_years")
    private int minimumYears;

    @Column(name = "yearly_limit")
    private int yearlyLimit;

    public PCafeteriaLimit() {
    }

    public Integer getIdCafeteriaLimit() {
        return this.idCafeteriaLimit;
    }

    public void setIdCafeteriaLimit(Integer idCafeteriaLimit) {
        this.idCafeteriaLimit = idCafeteriaLimit;
    }

    public String getLimitLabel() {
        return this.limitLabel;
    }

    public void setLimitLabel(String limitLabel) {
        this.limitLabel = limitLabel;
    }

    public int getMinimumYears() {
        return this.minimumYears;
    }

    public void setMinimumYears(int minimumYears) {
        this.minimumYears = minimumYears;
    }

    public int getYearlyLimit() {
        return this.yearlyLimit;
    }

    public void setYearlyLimit(int yearlyLimit) {
        this.yearlyLimit = yearlyLimit;
    }

}