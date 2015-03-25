package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the T_STAFF database table.
 * 
 */
@Entity
@Table(name = "T_STAFF")
public class StaffDetail extends StaffBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "K_STAFF_JOBTITLE", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_jobtitle", nullable = false))
    private List<Jobtitle> Jobtitles;

    // bi-directional many-to-many association to Workgroup
    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "K_STAFF_WORKGROUP", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_workgroup", nullable = false))
    private Workgroup workgroup;

    // bi-directional many-to-many association to Workplace
    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "K_STAFF_WORKPLACE", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_workplace", nullable = false))
    private Workplace workplace;

    // bi-directional many-to-one association to StaffAlarm
    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "K_STAFF_ALARM", joinColumns = @JoinColumn(name = "id_staff", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_alarm", nullable = false))
    private Alarm alarm;

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public List<Jobtitle> getJobtitles() {
        return Jobtitles;
    }

    public String getJobtitlesAsString() {
        StringBuilder sb = new StringBuilder();
        if (Jobtitles != null) {
            for (Jobtitle jobtitle : Jobtitles) {
                sb.append(jobtitle.getJobtitle()).append(";\n");
            }
        }
        return sb.toString();
    }

    public void setJobtitles(List<Jobtitle> jobtitles) {
        Jobtitles = jobtitles;
    }

    public Workgroup getWorkgroup() {
        return this.workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }

    public Workplace getWorkplace() {
        return this.workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

}