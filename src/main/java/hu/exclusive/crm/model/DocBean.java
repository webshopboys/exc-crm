package hu.exclusive.crm.model;

import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.dao.model.StaffNote;

import java.util.Date;

public class DocBean implements Comparable<DocBean> {

    public static final String KEY_DR = "dr_";
    public static final String KEY_CONTRACT = "contract_";
    public static final String KEY_NOTE = "note_";
    public static final String KEY_ATTACHMENT = "attach_";

    private DrDoc drDoc;
    private StaffNote staffNote;
    private ContractDoc contractDoc;
    private Attachment attachment;

    public DocBean(ContractDoc contractDoc, DrDoc drDoc, StaffNote staffNote, Attachment attachment) {
        this.drDoc = drDoc;
        this.staffNote = staffNote;
        this.contractDoc = contractDoc;
        this.attachment = attachment;
    }

    public DocBean() {
    }

    /**
     * A tipus és az egyedi id együtt.
     * 
     * @return
     */
    public String getDocumentKey() {
        return drDoc != null ? KEY_DR + drDoc.getIdDrdoc() : contractDoc != null ? KEY_CONTRACT + contractDoc.getIdContractdoc()
                : staffNote != null ? KEY_NOTE + staffNote.getIdStaffnote() : KEY_ATTACHMENT
                        + (attachment != null ? attachment.getIdAttachment() : null);
    }

    public DrDoc getDrDoc() {
        return drDoc;
    }

    public void setDrDoc(DrDoc drDoc) {
        this.drDoc = drDoc;
    }

    public StaffNote getStaffNote() {
        return staffNote;
    }

    public void setStaffNote(StaffNote staffNote) {
        this.staffNote = staffNote;
    }

    public ContractDoc getContractDoc() {
        return contractDoc;
    }

    public void setContractDoc(ContractDoc contractDoc) {
        this.contractDoc = contractDoc;
    }

    public String getIconPath() {
        return drDoc != null ? "doc_dr_128.png" : contractDoc != null ? "doc_contract_128.png"
                : staffNote != null ? "doc_note_128.png" : "doc_attachment_128.png";
    }

    public String getTitle() {
        return drDoc != null ? drDoc.toString() : contractDoc != null ? contractDoc.toString() : staffNote != null ? staffNote
                .toString() : "" + attachment;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public int compareTo(DocBean o) {
        if (o != null) {
            Date d1 = getContractDoc() != null ? getContractDoc().getDocumentCreated() : getDrDoc() != null ? getDrDoc()
                    .getDocumentCreated() : getStaffNote() != null ? getStaffNote().getNoteCreated()
                    : getAttachment() != null ? getAttachment().getDocumentCreated() : null;

            Date d2 = o.getContractDoc() != null ? o.getContractDoc().getDocumentCreated() : o.getDrDoc() != null ? o.getDrDoc()
                    .getDocumentCreated() : o.getStaffNote() != null ? o.getStaffNote().getNoteCreated()
                    : o.getAttachment() != null ? o.getAttachment().getDocumentCreated() : null;

            if (d1 != null && d2 != null) {
                return d1.compareTo(d2);
            }
        }
        return 0;
    }

}
