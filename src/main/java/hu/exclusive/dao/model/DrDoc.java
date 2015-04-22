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
 * The persistent class for the t_drdoc database table.
 * 
 */
@Entity
@Table(name = "T_DRDOC")
@NamedQueries({
        @NamedQuery(name = "DrDoc.findAll", query = "SELECT d FROM DrDoc d"),
        @NamedQuery(name = "DrDoc.findForStaff", query = "SELECT c FROM DrDoc c WHERE c.idStaff = :idStaff ORDER BY c.documentCreated"),
        @NamedQuery(name = "DrDoc.find", query = "SELECT c FROM DrDoc c WHERE c.idDrdoc = :idDoc") })
public class DrDoc extends EntityCommons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_drdoc")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idDrdoc;

    @Lob
    @Column(name = "document_bin")
    private byte[] documentBin;

    @Column(name = "document_created")
    private Timestamp documentCreated;

    @Column(name = "document_expire")
    private Timestamp documentExpire;

    @Column(name = "document_note")
    private String documentNote;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "id_staff")
    private Integer idStaff;

    public DrDoc() {
    }

    @Override
    public Integer getId() {
        return getIdDrdoc();
    }

    public Integer getIdDrdoc() {
        return this.idDrdoc;
    }

    public void setIdDrdoc(Integer idDrdoc) {
        this.idDrdoc = idDrdoc;
    }

    public byte[] getDocumentBin() {
        return this.documentBin;
    }

    public void setDocumentBin(byte[] documentBin) {
        this.documentBin = documentBin;
    }

    public Timestamp getDocumentCreated() {
        return this.documentCreated;
    }

    public void setDocumentCreated(Timestamp documentCreated) {
        this.documentCreated = documentCreated;
    }

    public Timestamp getDocumentExpire() {
        return this.documentExpire;
    }

    public void setDocumentExpire(Timestamp documentExpire) {
        this.documentExpire = documentExpire;
    }

    public String getDocumentNote() {
        return this.documentNote;
    }

    public void setDocumentNote(String documentNote) {
        this.documentNote = documentNote;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUrl() {
        return this.documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public Integer getIdStaff() {
        return this.idStaff;
    }

    public void setIdStaff(Integer idStaff) {
        this.idStaff = idStaff;
    }

    @Override
    public String toString() {
        return "Orvosi dokumentum neve '" + getFileName(documentUrl) + "', létrehozva '" + getDateTime(documentCreated)
                + "' időpontban, lejár '" + getDateTime(documentExpire) + "' időpontban."
                + (isEmpty(documentNote) ? "" : "Megjegyzés: '" + documentNote + "'");
    }
}