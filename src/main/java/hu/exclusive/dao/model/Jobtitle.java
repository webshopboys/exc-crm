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
 * The persistent class for the T_JOBTITLE database table.
 * 
 */
@Entity
@Table(name = "T_JOBTITLE")
@NamedQueries({
        @NamedQuery(name = "Jobtitle.findAll", query = "SELECT j FROM Jobtitle j ORDER BY j.jobtitle"),
        @NamedQuery(name = "Jobtitle.findForStaff", query = "SELECT j FROM Jobtitle j  WHERE j.idJobtitle in (SELECT k.idJobtitle FROM StaffJobtitleK k WHERE k.idStaff = :idStaff) ORDER BY j.jobtitle") })
public class Jobtitle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_jobtitle", unique = true, nullable = false)
    private int idJobtitle;

    @Column(nullable = false, length = 100)
    private String jobtitle;

    public Jobtitle() {
    }

    public int getIdJobtitle() {
        return this.idJobtitle;
    }

    public void setIdJobtitle(int idJobtitle) {
        this.idJobtitle = idJobtitle;
    }

    public String getJobtitle() {
        return this.jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    @Override
    public String toString() {
        return "Jobtitle [idJobtitle=" + idJobtitle + ", jobtitle=" + jobtitle + "]";
    }

}
