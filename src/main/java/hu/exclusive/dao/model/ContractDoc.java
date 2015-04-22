package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the t_contractdoc database table.
 * 
 */
@Entity
@Table(name = "T_CONTRACTDOC")
@NamedQueries({
        @NamedQuery(name = "ContractDoc.findAll", query = "SELECT c FROM ContractDoc c"),
        @NamedQuery(name = "ContractDoc.findForStaff", query = "SELECT c FROM ContractDoc c WHERE c.idStaff = :idStaff ORDER BY c.documentCreated"),
        @NamedQuery(name = "ContractDoc.find", query = "SELECT c FROM ContractDoc c WHERE c.idContractdoc = :idDoc") })
public class ContractDoc extends EntityCommons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_contractdoc")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idContractdoc;

    @Lob
    @Column(name = "document_bin")
    private byte[] documentBin;

    @Column(name = "document_created", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentCreated;

    @Column(name = "document_expire")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentExpire;

    @Column(name = "document_note")
    private String documentNote;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "id_staff")
    private Integer idStaff;

    public ContractDoc() {
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {

    }

    @Override
    public Integer getId() {
        return getIdContractdoc();
    }

    public Integer getIdContractdoc() {
        return this.idContractdoc;
    }

    public void setIdContractdoc(Integer idContractdoc) {
        this.idContractdoc = idContractdoc;
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
        return "Szerződés neve '" + getDocumentType() + "', fájl neve '" + getFileName(documentUrl) + "', létrehozva "
                + getDateTime(documentCreated) + " időpontban."
                + (isEmpty(documentNote) ? "" : "Megjegyzés: '" + documentNote + "'");
    }

}