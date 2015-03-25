package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the k_staff_workplace database table.
 * 
 */
@Entity
@Table(name = "K_STAFF_WORKPLACE")
public class StaffWorkplaceK implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StaffWorkplacePK id;

    public StaffWorkplaceK() {
    }

    public StaffWorkplacePK getId() {
        return this.id;
    }

    public void setId(StaffWorkplacePK id) {
        this.id = id;
    }

}