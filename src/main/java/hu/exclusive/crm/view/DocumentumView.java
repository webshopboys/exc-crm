package hu.exclusive.crm.view;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.crm.service.AttachmentGenerator;
import hu.exclusive.crm.service.AttachmentService;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.DaoFilter.RELATION;
import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.dao.model.StaffNote;
import hu.exclusive.utils.ObjectUtils;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "documentumView")
@ViewScoped
public class DocumentumView implements Serializable {

    private static final long serialVersionUID = -1774648789104774066L;
    @Autowired
    AttachmentService service;

    private AttachmentGenerator generator;

    @PostConstruct
    public void init() {
        generator = new AttachmentGenerator();
    }

    public List<ContractDoc> getContractTemplates() {
        DaoFilter filter = new DaoFilter();
        filter.addFilter("idStaff", RELATION.ISNULL, null);
        return service.getContractDocs(filter);
    }

    public StreamedContent getContractStream(int contractId) throws Exception {
        return getDocumentStream(DocBean.KEY_CONTRACT + contractId);
    }

    public StreamedContent getDocumentStream(final String documentKey) throws Exception {
        try {

            final String docType = ObjectUtils.getTagX_String(documentKey, 0) + "_";
            final Integer docid = ObjectUtils.getTagX_Integer(documentKey, 1);

            // System.out.println("getDocumentStream " + documentKey + " " + docType + " " + docid);

            generator.setContent(documentKey.getBytes()).setAttachmentName(documentKey)
                    .setMimeType(AttachmentGenerator.MIME_TEXT);

            if (docType.equals(DocBean.KEY_DR)) {

                DaoFilter filter = new DaoFilter("DrDoc.find", "idDoc", RELATION.NAMED_QUERY, docid);
                DrDoc doc = service.getDrDoc(filter);
                if (doc != null)
                    generator.setContent(doc.getDocumentBin()).setMimeType(AttachmentGenerator.MIME_DOCX)
                            .setAttachmentPath(ObjectUtils.getFilePath(doc.getDocumentUrl()))
                            .setAttachmentName(ObjectUtils.getFileName(doc.getDocumentUrl()));
                else
                    throw new ServiceException("Nem található szerződés (" + docid + ")");

            } else if (docType.equals(DocBean.KEY_CONTRACT)) {

                DaoFilter filter = new DaoFilter("ContractDoc.find", "idDoc", RELATION.NAMED_QUERY, docid);
                ContractDoc doc = service.getContractDoc(filter);
                if (doc != null)
                    generator.setContent(doc.getDocumentBin()).setMimeType(AttachmentGenerator.MIME_DOCX)
                            .setAttachmentPath(ObjectUtils.getFilePath(doc.getDocumentUrl()))
                            .setAttachmentName(ObjectUtils.getFileName(doc.getDocumentUrl()));
                else
                    throw new ServiceException("Nem található orvosi (" + docid + ")");

            } else if (docType.equals(DocBean.KEY_NOTE)) {

                DaoFilter filter = new DaoFilter("StaffNote.find", "idDoc", RELATION.NAMED_QUERY, docid);
                StaffNote doc = service.getStaffNote(filter);
                if (doc != null)
                    generator.setContent(doc.getNote().getBytes()).setMimeType(AttachmentGenerator.MIME_TEXT)
                            .setAttachmentName("note-" + ObjectUtils.toString(doc.getNoteCreated()));
                else
                    throw new ServiceException("Nem található feljegyzés (" + docid + ")");

            } else if (docType.equals(DocBean.KEY_ATTACHMENT)) {

                DaoFilter filter = new DaoFilter("Attachment.find", "idDoc", RELATION.NAMED_QUERY, docid);
                Attachment doc = service.getAttachment(filter);
                if (doc != null)
                    generator.setContent(doc.getDocumentBin()).setMimeType(AttachmentGenerator.MIME_DOCX)
                            .setAttachmentPath(ObjectUtils.getFilePath(doc.getDocumentUrl()))
                            .setAttachmentName(ObjectUtils.getFileName(doc.getDocumentUrl()));
                else
                    throw new ServiceException("Nem található melléklet (" + docid + ")");

            } else {
                throw new ServiceException("Ismeretlen dokumentum típus (" + documentKey + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hiba", "A letöltés nem sikerült: "
                    + e.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return generator.generateStream();
    }
}