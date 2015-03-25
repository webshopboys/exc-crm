package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the k_staff_workplace database table.
 * 
 */
@Embeddable
public class StaffWorkplacePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "id_staff", insertable = false, updatable = false)
    private int idStaff;

    @Column(name = "id_workplace", insertable = false, updatable = false)
    private int idWorkplace;

    public StaffWorkplacePK() {
    }

    public int getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

    public int getIdWorkplace() {
        return this.idWorkplace;
    }

    public void setIdWorkplace(int idWorkplace) {
        this.idWorkplace = idWorkplace;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StaffWorkplacePK)) {
            return false;
        }
        StaffWorkplacePK castOther = (StaffWorkplacePK) other;
        return (this.idStaff == castOther.idStaff) && (this.idWorkplace == castOther.idWorkplace);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.idStaff;
        hash = hash * prime + this.idWorkplace;

        return hash;
    }
}