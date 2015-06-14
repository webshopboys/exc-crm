package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * The persistent class for the T_STAFF database table.
 * 
 */
@Entity
@Table(name = "T_STAFF")
@NamedQueries({ @NamedQuery(name = "StaffCafeteria.findStaff", query = "SELECT s FROM StaffCafeteria s WHERE s.idStaff = :idStaff") })
public class StaffCafeteria extends StaffBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public StaffCafeteria() {
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_staff", referencedColumnName = "id_staff")
    @OrderBy("year_key ASC, month_key ASC")
    private List<Cafeteria> monthlyCafes;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinTable(name = "K_STAFF_WORKGROUP", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_workgroup", nullable = true))
    private Workgroup workgroup;

    public List<Cafeteria> getMonthlyCafes() {
        return monthlyCafes;
    }

    public void setMonthlyCafes(List<Cafeteria> monthlyCafes) {
        this.monthlyCafes = monthlyCafes;
    }

    public Workgroup getWorkgroup() {
        return this.workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }

}