package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the k_staff_workgroup database table.
 * 
 */
@Entity
@Table(name = "K_STAFF_WORKGROUP")
public class StaffWorkgroupK implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StaffWorkgroupPK id;

    public StaffWorkgroupK() {
    }

    public StaffWorkgroupPK getId() {
        return this.id;
    }

    public void setId(StaffWorkgroupPK id) {
        this.id = id;
    }

}