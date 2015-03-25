package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the k_staff_workgroup database table.
 * 
 */
@Embeddable
public class StaffWorkgroupPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "id_staff", insertable = false, updatable = false)
    private int idStaff;

    @Column(name = "id_workgroup", insertable = false, updatable = false)
    private int idWorkgroup;

    public StaffWorkgroupPK() {
    }

    public int getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

    public int getIdWorkgroup() {
        return this.idWorkgroup;
    }

    public void setIdWorkgroup(int idWorkgroup) {
        this.idWorkgroup = idWorkgroup;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StaffWorkgroupPK)) {
            return false;
        }
        StaffWorkgroupPK castOther = (StaffWorkgroupPK) other;
        return (this.idStaff == castOther.idStaff) && (this.idWorkgroup == castOther.idWorkgroup);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.idStaff;
        hash = hash * prime + this.idWorkgroup;

        return hash;
    }
}