package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the T_WORKPLACE database table.
 * 
 */
@Entity
@Table(name = "P_WORKPLACE")
@NamedQueries({
        @NamedQuery(name = "Workplace.findAll", query = "SELECT w FROM Workplace w ORDER BY w.workplaceName"),
        @NamedQuery(name = "Workplace.findById", query = "SELECT w FROM Workplace w WHERE w.idWorkplace = :idWorkplace"),
        @NamedQuery(name = "Workplace.findForGroup", query = "SELECT w FROM Workplace w WHERE w.workgroup.idWorkgroup = :idWorkgroup ORDER BY w.workplaceName"),
        @NamedQuery(name = "Workplace.findForStaff", query = "SELECT w FROM Workplace w WHERE w.idWorkplace in (SELECT k.id.idWorkplace FROM StaffWorkplaceK k WHERE k.id.idStaff = :idStaff) ORDER BY w.workplaceName") })
public class Workplace extends EntityCommons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_workplace", unique = true, nullable = false)
    private Integer idWorkplace;

    @Column(name = "e_mail", length = 200)
    private String eMail;

    @Column(name = "open_friday", length = 45)
    private String openFriday;

    @Column(name = "open_monday", length = 45)
    private String openMonday;

    @Column(name = "open_saturday", length = 45)
    private String openSaturday;

    @Column(name = "open_sunday", length = 45)
    private String openSunday;

    @Column(name = "open_thursday", length = 45)
    private String openThursday;

    @Column(name = "open_tuesday", length = 45)
    private String openTuesday;

    @Column(name = "open_wednesday", length = 45)
    private String openWednesday;

    @Column(length = 45)
    private String telephone;

    @Column(name = "workplace_name", nullable = false, length = 200)
    private String workplaceName;

    @Transient
    private Integer idWorkgroup;

    // bi-directional many-to-one association to Workgroup
    @ManyToOne
    @JoinColumn(name = "id_workgroup", nullable = false)
    private Workgroup workgroup;

    public Workplace() {
    }

    @Override
    public Integer getId() {
        return getIdWorkplace();
    }

    public Integer getIdWorkplace() {
        return this.idWorkplace;
    }

    public void setIdWorkplace(Integer idWorkplace) {
        this.idWorkplace = idWorkplace;
    }

    public String getEMail() {
        return this.eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getOpenFriday() {
        return this.openFriday;
    }

    public void setOpenFriday(String openFriday) {
        this.openFriday = openFriday;
    }

    public String getOpenMonday() {
        return this.openMonday;
    }

    public void setOpenMonday(String openMonday) {
        this.openMonday = openMonday;
    }

    public String getOpenSaturday() {
        return this.openSaturday;
    }

    public void setOpenSaturday(String openSaturday) {
        this.openSaturday = openSaturday;
    }

    public String getOpenSunday() {
        return this.openSunday;
    }

    public void setOpenSunday(String openSunday) {
        this.openSunday = openSunday;
    }

    public String getOpenThursday() {
        return this.openThursday;
    }

    public void setOpenThursday(String openThursday) {
        this.openThursday = openThursday;
    }

    public String getOpenTuesday() {
        return this.openTuesday;
    }

    public void setOpenTuesday(String openTuesday) {
        this.openTuesday = openTuesday;
    }

    public String getOpenWednesday() {
        return this.openWednesday;
    }

    public void setOpenWednesday(String openWednesday) {
        this.openWednesday = openWednesday;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWorkplaceName() {
        return this.workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    //
    // public List<Staff> getStaffs() {
    // return this.Staffs;
    // }
    //
    // public void setStaffs(List<Staff> Staffs) {
    // this.Staffs = Staffs;
    // }

    // public Alarm getAlarm() {
    // return Alarm;
    // }
    //
    // public void setAlarm(Alarm alarm) {
    // Alarm = alarm;
    // }

    public Workgroup getWorkgroup() {
        return this.workgroup;
    }

    public void setWorkgroup(Workgroup Workgroup) {
        this.workgroup = Workgroup;
    }

    public Integer getIdWorkgroup() {
        return idWorkgroup;
    }

    public void setIdWorkgroup(Integer idWorkgroup) {
        this.idWorkgroup = idWorkgroup;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Workplace) {
            return this.idWorkplace == ((Workplace) obj).idWorkplace;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Workplace.class.hashCode() + this.idWorkplace;
    }

    @Override
    public String toString() {
        return "Workplace [idWorkplace=" + idWorkplace + ", workplaceName=" + workplaceName + "]";
    }

}