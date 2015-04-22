package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the T_WORKGROUP database table.
 * 
 */
@Entity
@Table(name = "T_WORKGROUP")
@NamedQueries({
        @NamedQuery(name = "Workgroup.findAll", query = "SELECT w FROM Workgroup w"),
        @NamedQuery(name = "Workgroup.findForStaff", query = "SELECT w FROM Workgroup w WHERE w.idWorkgroup in (SELECT k.id.idWorkgroup FROM StaffWorkgroupK k WHERE k.id.idStaff = :idStaff) ORDER BY w.groupName") })
public class Workgroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_workgroup", unique = true, nullable = false)
    private int idWorkgroup;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    // a group tobb staff, de egy staffnak egy groupja lehet
    // @OneToMany(mappedBy="workgroup")
    // private List<Staff> Staffs;

    // egy place csak egy groupba tartozhat
    @OneToMany(mappedBy = "Workgroup")
    private List<Workplace> Workplaces;

    public Workgroup() {
    }

    public int getIdWorkgroup() {
        return this.idWorkgroup;
    }

    public void setIdWorkgroup(int idWorkgroup) {
        this.idWorkgroup = idWorkgroup;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // public List<Staff> getStaffs() {
    // return this.Staffs;
    // }
    //
    // public void setStaffs(List<Staff> Staffs) {
    // this.Staffs = Staffs;
    // }

    public List<Workplace> getWorkplaces() {
        return this.Workplaces;
    }

    public void setWorkplaces(List<Workplace> Workplaces) {
        this.Workplaces = Workplaces;
    }

    public Workplace addWorkplace(Workplace Workplace) {
        getWorkplaces().add(Workplace);
        Workplace.setWorkgroup(this);

        return Workplace;
    }

    public Workplace removeWorkplace(Workplace Workplace) {
        getWorkplaces().remove(Workplace);
        Workplace.setWorkgroup(null);

        return Workplace;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Workgroup) {
            return this.idWorkgroup == ((Workgroup) obj).idWorkgroup;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Workgroup.class.hashCode() + this.idWorkgroup;
    }

    @Override
    public String toString() {
        return "Workgroup [idWorkgroup=" + idWorkgroup + ", groupName=" + groupName + "]";
    }

}