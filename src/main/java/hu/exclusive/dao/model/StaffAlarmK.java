package hu.exclusive.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the k_staff_alarm database table.
 * 
 */
@Entity
@Table(name = "K_STAFF_ALARM")
public class StaffAlarmK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_staffalarm")
    private int idStaffalarm;

    private Timestamp created;

    @Column(name = "id_alarm")
    private int idAlarm;

    @Column(name = "id_staff")
    private int idStaff;

    public StaffAlarmK() {
    }

    public int getIdStaffalarm() {
        return this.idStaffalarm;
    }

    public void setIdStaffalarm(int idStaffalarm) {
        this.idStaffalarm = idStaffalarm;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getIdAlarm() {
        return this.idAlarm;
    }

    public void setIdAlarm(int idAlarm) {
        this.idAlarm = idAlarm;
    }

    public int getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

}