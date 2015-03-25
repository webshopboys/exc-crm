package hu.exclusive.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the t_staff database table.
 * 
 */
@MappedSuperclass
@Table(name = "T_STAFF")
public class StaffBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_STATUS = "Aktív";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_staff")
    private Integer idStaff;

    @Column(name = "base_salary")
    private BigDecimal baseSalary;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "birth_name")
    private String birthName;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "employ_finish")
    private Timestamp employFinish;

    @Temporal(TemporalType.DATE)
    @Column(name = "employ_period")
    private Date employPeriod;

    @Column(name = "employ_start")
    private Timestamp employStart;

    @Column(name = "employ_time")
    private String employTime;

    @Column(name = "employ_type")
    private String employType;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "home_address")
    private String homeAddress;

    @Column(name = "mother_name")
    private String motherName;

    @Lob
    @Column(name = "private_note")
    private String privateNote;

    @Column(name = "resident_address")
    private String residentAddress;

    @Column(name = "salary_type")
    private String salaryType;

    @Column(name = "sid_serial")
    private String sidSerial;

    private String status;

    @Column(name = "taj_serial")
    private String tajSerial;

    @Column(name = "tax_serial")
    private String taxSerial;

    @Column(name = "trial_period")
    private Integer trialPeriod;

    public StaffBase() {
    }

    public Integer getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(Integer idStaff) {
        this.idStaff = idStaff;
    }

    public BigDecimal getBaseSalary() {
        return this.baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthName() {
        return this.birthName;
    }

    public void setBirthName(String birthName) {
        this.birthName = birthName;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Timestamp getEmployFinish() {
        return this.employFinish;
    }

    public void setEmployFinish(Timestamp employFinish) {
        this.employFinish = employFinish;
    }

    public Date getEmployPeriod() {
        return this.employPeriod;
    }

    public void setEmployPeriod(Date employPeriod) {
        this.employPeriod = employPeriod;
    }

    public Timestamp getEmployStart() {
        return this.employStart;
    }

    public void setEmployStart(Timestamp employStart) {
        this.employStart = employStart;
    }

    public String getEmployTime() {
        return this.employTime;
    }

    public void setEmployTime(String employTime) {
        this.employTime = employTime;
    }

    public String getEmployType() {
        return this.employType;
    }

    public void setEmployType(String employType) {
        this.employType = employType;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHomeAddress() {
        return this.homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getMotherName() {
        return this.motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPrivateNote() {
        return this.privateNote;
    }

    public void setPrivateNote(String privateNote) {
        this.privateNote = privateNote;
    }

    public String getResidentAddress() {
        return this.residentAddress;
    }

    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }

    public String getSalaryType() {
        return this.salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getSidSerial() {
        return this.sidSerial;
    }

    public void setSidSerial(String sidSerial) {
        this.sidSerial = sidSerial;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTajSerial() {
        return this.tajSerial;
    }

    public void setTajSerial(String tajSerial) {
        this.tajSerial = tajSerial;
    }

    public String getTaxSerial() {
        return this.taxSerial;
    }

    public void setTaxSerial(String taxSerial) {
        this.taxSerial = taxSerial;
    }

    public Integer getTrialPeriod() {
        return this.trialPeriod;
    }

    public void setTrialPeriod(Integer trialPeriod) {
        this.trialPeriod = trialPeriod;
    }

}