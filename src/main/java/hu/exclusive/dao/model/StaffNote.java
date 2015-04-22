package hu.exclusive.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the t_staffnote database table.
 * 
 */
@Entity
@Table(name = "T_STAFFNOTE")
@NamedQueries({
        @NamedQuery(name = "StaffNote.findAll", query = "SELECT s FROM StaffNote s"),
        @NamedQuery(name = "StaffNote.findForStaff", query = "SELECT c FROM StaffNote c WHERE c.idStaff = :idStaff ORDER BY c.noteCreated"),
        @NamedQuery(name = "StaffNote.find", query = "SELECT c FROM StaffNote c WHERE c.idStaffnote = :idDoc") })
public class StaffNote extends EntityCommons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_staffnote")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idStaffnote;

    @Column(name = "id_staff")
    private Integer idStaff;

    @Lob
    private String note;

    @Column(name = "note_created")
    private Timestamp noteCreated;

    public StaffNote() {
    }

    @Override
    public Integer getId() {
        return getIdStaff();
    }

    public Integer getIdStaffnote() {
        return this.idStaffnote;
    }

    public void setIdStaffnote(Integer idStaffnote) {
        this.idStaffnote = idStaffnote;
    }

    public Integer getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(Integer idStaff) {
        this.idStaff = idStaff;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getNoteCreated() {
        return this.noteCreated;
    }

    public void setNoteCreated(Timestamp noteCreated) {
        this.noteCreated = noteCreated;
    }

    @Override
    public String toString() {
        return "Megjegyzés, értékelés , létrehozva '" + getDateTime(noteCreated) + "' időpontban. Tartalma: ''"
                + (note != null ? note.length() > 200 ? note.substring(0, 200) : note : "[üres]") + "'";
    }

}