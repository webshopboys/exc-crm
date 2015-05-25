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
import javax.persistence.Table;

/**
 * The persistent class for the T_STAFF database table.
 * 
 */
@Entity
@Table(name = "T_STAFF")
@NamedQueries({ @NamedQuery(name = "StaffDetail.findStaff", query = "SELECT s FROM StaffDetail s WHERE s.idStaff = :idStaff") })
public class StaffDetail extends StaffBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "K_STAFF_JOBTITLE", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_jobtitle", nullable = true))
    private List<Jobtitle> jobtitles;

    // bi-directional many-to-many association to Workgroup
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinTable(name = "K_STAFF_WORKGROUP", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_workgroup", nullable = true))
    private Workgroup workgroup;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "K_STAFF_WORKPLACE", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_workplace", nullable = true))
    private List<Workplace> workplaces;

    // bi-directional many-to-one association to StaffAlarm
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinTable(name = "K_STAFF_ALARM", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_alarm", nullable = true))
    private Alarm alarm;

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public List<Jobtitle> getJobtitles() {
        return jobtitles;
    }

    public String getJobtitlesAsString() {
        StringBuilder sb = new StringBuilder();
        if (jobtitles != null) {
            for (Jobtitle jobtitle : jobtitles) {
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(jobtitle.getJobtitle()).append(";");
            }
        }
        return sb.toString();
    }

    public void setJobtitles(List<Jobtitle> jobtitles) {
        this.jobtitles = jobtitles;
    }

    public Workgroup getWorkgroup() {
        return this.workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }

    public List<Workplace> getWorkplaces() {
        return this.workplaces;
    }

    public void setWorkplaces(List<Workplace> workplace) {
        this.workplaces = workplace;
    }

    public String getWorkplacesAsString() {
        StringBuilder sb = new StringBuilder();
        if (workplaces != null) {
            for (Workplace wp : workplaces) {
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(wp.getWorkplaceName()).append(";");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "StaffDetail [jobtitles=" + jobtitles + ", workgroup=" + workgroup + ", workplace=" + workplaces + ", alarm="
                + alarm + super.toString() + "]";
    }

}