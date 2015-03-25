package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the t_documents database table.
 * 
 */
@Entity
@Table(name = "T_DOCUMENTS")
@NamedQuery(name = "Document.findAll", query = "SELECT d FROM StaffDocument d")
public class StaffDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_CONTRACT = "Munkaszerződés";
    public static final String TYPE_CONTRACT_MODIFICATION = "Munkaszerződés módosítás";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_document")
    private Integer idDocument;

    @Lob
    @Column(name = "document_bin")
    private byte[] documentBin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "document_created")
    private Date documentCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "document_expire")
    private Date documentExpire;

    @Lob
    @Column(name = "document_note")
    private String documentNote;

    @Column(name = "document_subtype")
    private String documentSubtype;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "id_staff")
    private Integer idStaff;

    public StaffDocument() {
    }

    public Integer getIdDocument() {
        return this.idDocument;
    }

    public void setIdDocument(Integer idDocument) {
        this.idDocument = idDocument;
    }

    public byte[] getDocumentBin() {
        return this.documentBin;
    }

    public void setDocumentBin(byte[] documentBin) {
        this.documentBin = documentBin;
    }

    public Date getDocumentCreated() {
        return this.documentCreated;
    }

    public void setDocumentCreated(Date documentCreated) {
        this.documentCreated = documentCreated;
    }

    public Date getDocumentExpire() {
        return this.documentExpire;
    }

    public void setDocumentExpire(Date documentExpire) {
        this.documentExpire = documentExpire;
    }

    public String getDocumentNote() {
        return this.documentNote;
    }

    public void setDocumentNote(String documentNote) {
        this.documentNote = documentNote;
    }

    public String getDocumentSubtype() {
        return this.documentSubtype;
    }

    public void setDocumentSubtype(String documentSubtype) {
        this.documentSubtype = documentSubtype;
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

}