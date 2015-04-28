package hu.exclusive.dao.model;

import hu.exclusive.utils.PageUtils;

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
 * The persistent class for the t_attachment database table.
 * 
 */
@Entity
@Table(name = "T_ATTACHMENT")
@NamedQueries({
        @NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a"),
        @NamedQuery(name = "Attachment.findForStaff", query = "SELECT c FROM Attachment c WHERE c.idStaff = :idStaff ORDER BY c.documentCreated"),
        @NamedQuery(name = "Attachment.find", query = "SELECT c FROM Attachment c WHERE c.idAttachment = :idDoc") })
public class Attachment extends EntityCommons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_attachment")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAttachment;

    @Lob
    @Column(name = "document_bin")
    private byte[] documentBin;

    @Column(name = "document_created")
    private Timestamp documentCreated;

    @Column(name = "document_note")
    private String documentNote;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "id_staff")
    private Integer idStaff;

    public Attachment() {
    }

    @Override
    public Integer getId() {
        return getIdAttachment();
    }

    public Integer getIdAttachment() {
        return this.idAttachment;
    }

    public void setIdAttachment(Integer idAttachment) {
        this.idAttachment = idAttachment;
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

    public String getDocumentNote() {
        return this.documentNote;
    }

    public void setDocumentNote(String documentNote) {
        this.documentNote = documentNote;
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
        return "Melléklet fájl neve '" + getFileName(documentUrl) + "', feltöltve '" + getDateTime(documentCreated)
                + "' időpontban. " + (isEmpty(documentNote) ? "" : "Megjegyzés: '" + documentNote + "'");
    }

    public String getFileInfo() {
        return "Melléklet fájl, ["
                + getDateTime(documentCreated)
                + "] "
                + PageUtils.BR_UNESCAPE
                + (isEmpty(documentNote) ? "" : "'" + (documentNote.length() > 30 ? documentNote.substring(0, 30) : documentNote)
                        + "...'");
    }

}