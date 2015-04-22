package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the T_ALARM database table.
 * 
 */
@Entity
@Table(name = "T_ALARM")
@NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a")
public class Alarm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_alarm", unique = true, nullable = false)
    private int idAlarm;

    @Column(name = "alarm_code", nullable = false, length = 45)
    private String alarmCode;

    @Column(name = "alarm_name", nullable = false, length = 200)
    private String alarmName;

    @Column(name = "d24_code", nullable = false, length = 45)
    private String d24Code;

    @Column(name = "d24_name", nullable = false, length = 200)
    private String d24Name;

    @Column(name = "d24_partition", length = 100)
    private String d24Partition;

    // bi-directional many-to-one association to StaffAlarm
    // @OneToOne(mappedBy="Alarm")
    // private Staff Staff;

    // bi-directional many-to-one association to Workplace
    // @OneToOne(mappedBy="Alarm")
    // private Workplace Workplace;

    public Alarm() {
    }

    public int getIdAlarm() {
        return this.idAlarm;
    }

    public void setIdAlarm(int idAlarm) {
        this.idAlarm = idAlarm;
    }

    public String getAlarmCode() {
        return this.alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmName() {
        return this.alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getD24Code() {
        return this.d24Code;
    }

    public void setD24Code(String d24Code) {
        this.d24Code = d24Code;
    }

    public String getD24Name() {
        return this.d24Name;
    }

    public void setD24Name(String d24Name) {
        this.d24Name = d24Name;
    }

    public String getD24Partition() {
        return this.d24Partition;
    }

    public void setD24Partition(String d24Partition) {
        this.d24Partition = d24Partition;
    }

    // public Workplace getWorkplace() {
    // return Workplace;
    // }
    //
    // public void setWorkplace(Workplace workplace) {
    // Workplace = workplace;
    // }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Alarm) {
            return this.idAlarm == ((Alarm) obj).idAlarm;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Alarm.class.hashCode() + this.idAlarm;
    }
}