package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the s_system database table.
 * 
 */
@Entity
@Table(name = "S_SYSTEM")
@NamedQuery(name = "SSystem.findGroupKey", query = "SELECT s FROM SSystem s WHERE s.sysgroup = :sysgroup AND s.syskey = :syskey ORDER BY s.sysorder")
public class SSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sysid;

    private String sysgroup;

    private String syskey;

    private int sysorder = 0;

    private String sysparam;

    public SSystem() {
    }

    public int getSysid() {
        return this.sysid;
    }

    public void setSysid(int sysid) {
        this.sysid = sysid;
    }

    public String getSysgroup() {
        return this.sysgroup;
    }

    public void setSysgroup(String sysgroup) {
        this.sysgroup = sysgroup;
    }

    public String getSyskey() {
        return this.syskey;
    }

    public void setSyskey(String syskey) {
        this.syskey = syskey;
    }

    public int getSysorder() {
        return this.sysorder;
    }

    public void setSysorder(int sysorder) {
        this.sysorder = sysorder;
    }

    public String getSysparam() {
        return this.sysparam;
    }

    public void setSysparam(String sysparam) {
        this.sysparam = sysparam;
    }

}