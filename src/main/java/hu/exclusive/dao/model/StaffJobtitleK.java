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
 * The persistent class for the k_staff_jobtitle database table.
 * 
 */
@Entity
@Table(name = "K_STAFF_JOBTITLE")
public class StaffJobtitleK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_staffjob")
    private Integer idStaffjob;

    private Timestamp created;

    @Column(name = "id_jobtitle")
    private int idJobtitle;

    @Column(name = "id_staff")
    private int idStaff;

    public Integer getIdStaffjob() {
        return this.idStaffjob;
    }

    public void setIdStaffjob(Integer idStaffjob) {
        this.idStaffjob = idStaffjob;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getIdJobtitle() {
        return this.idJobtitle;
    }

    public void setIdJobtitle(int idJobtitle) {
        this.idJobtitle = idJobtitle;
    }

    public int getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

}