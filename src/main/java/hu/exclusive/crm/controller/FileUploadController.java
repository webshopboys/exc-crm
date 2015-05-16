package hu.exclusive.crm.controller;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.crm.report.ContractGenerator;
import hu.exclusive.crm.service.AttachmentService;
import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.utils.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.servlet.http.Part;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "fileUploadController")
@ViewScoped
public class FileUploadController extends Commontroller implements Serializable {

    private static final long serialVersionUID = -1774648789104774066L;
    @Autowired
    AttachmentService service;

    private UploadedFile filedrDoc;
    private Part filePart;
    private Integer idDocument;
    private ContractDoc template;
    private Integer idStaff;
    private Date drDocExpired;
    private String contractNote;
    private String doctorNote;
    private String attachmentNote;

    public void uploadContractDoc(FileUploadEvent event) {

        message(event.getFile().getFileName() + " feltöltése...", "");

        try {
            // byte[] bin = serializeFile(event.getFile().getFileName(), event.getFile().getInputstream());
            //
            // ContractDoc doc = new ContractDoc();
            // doc.setDocumentType("Új szerződés sablon");
            // doc.setDocumentNote("Kiválasztással feltöltött fájl. Utólag szerkesztendő!\nA munkatárs elérhető mezői: "
            // + Staff.DOCFIELDS_NAMES.getInfo());
            // doc.setDocumentUrl(event.getFile().getFileName());
            // doc.setDocumentBin(bin);
            // service.saveDocument(doc);

            message("Új szerződés feltöltve.", template.getDocumentType());

        } catch (Exception e) {
            e.printStackTrace();
            error("Feltöltési hiba", null, e);
        }

    }

    public void uploadTemplates(FileUploadEvent event) {

        message(event.getFile().getFileName() + " feltöltése...", "");
        try {
            byte[] bin = ObjectUtils.serializeFile(event.getFile().getFileName(), event.getFile().getInputstream());

            byte[] zipped = bin;// ObjectUtils.zip(bin, event.getFile().getFileName());

            ContractDoc doc = new ContractDoc();
            doc.setDocumentType("Új szerződés sablon");
            doc.setDocumentNote("Kiválasztással feltöltött fájl. Utólag szerkesztendő!\nA munkatárs elérhető mezői: "
                    + ContractGenerator.DOCFIELDS_NAMES.getInfo());
            doc.setDocumentUrl(event.getFile().getFileName());
            doc.setDocumentBin(zipped);
            service.saveDocument(doc);

            message("Új szerződés sablon elmentve.", doc.getDocumentNote());
        } catch (IOException e) {
            e.printStackTrace();
            error("Feltöltési hiba", null, e);
        }

    }

    public void selectTemplate(ContractDoc template) {
        setTemplate(template);
    }

    public void setTemplate(ContractDoc template) {
        System.err.println("set template " + template.getIdContractdoc());
        this.template = template;
    }

    public ContractDoc getTemplate() {
        if (this.template == null)
            this.template = new ContractDoc();
        return this.template;
    }

    public void onTemplateRowSelect(SelectEvent event) {
    }

    public void setFileDrDoc(UploadedFile file) {
        this.filedrDoc = file;
    }

    public UploadedFile getFileDrDoc() {
        return filedrDoc;
    }

    public void saveTemplate() {
        template.setDocumentCreated(new Date());
        System.err.println("saveTemplate " + template);
        service.saveDocument(template);
    }

    public void uploadDrDoc() {
        if (filedrDoc != null) {
            FacesMessage message = new FacesMessage("Succesful", filedrDoc.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public Integer getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Integer idDocument) {
        this.idDocument = idDocument;
    }

    public void setFilePart(Part file) {
        this.filePart = file;
    }

    public Part getFilePart() {
        return filePart;
    }

    public void uploadFileContract(Integer staffId) {
        System.err.println("uploadFileContract " + filePart + " staffId " + staffId);
        if (filePart != null && staffId != null) {
            try {

                ContractDoc doc = new ContractDoc();
                doc.setIdStaff(staffId);
                doc.setDocumentCreated(new Timestamp(new Date().getTime()));
                doc.setDocumentNote(getContractNote());
                doc.setDocumentUrl(getFileNameFromPart(filePart));
                doc.setDocumentBin(getBytesFromPart(filePart));
                doc.setDocumentType("Feltöltött szerződés");
                service.saveDocument(doc);

                message("Sikeres szerződés feltöltés", getFileNameFromPart(filePart) + " feltöltve.");

            } catch (Exception e) {
                e.printStackTrace();
                e = ServiceException.takeReason(e);
                error("Feltöltési hiba", null, e);
            }
        } else {
            error("Hiba", "Nincs kiválasztva a szerződés vagy a munkatárs!");
            throw new AbortProcessingException();
        }
    }

    public void uploadFileDr(Integer staffId) {
        System.err.println("uploadFileDr " + filePart + " staffId " + staffId);
        if (filePart != null && staffId != null) {

            try {

                DrDoc doc = new DrDoc();
                doc.setIdStaff(staffId);
                doc.setDocumentCreated(new Timestamp(new Date().getTime()));
                doc.setDocumentExpire(getDrDocExpired() == null ? null : new Timestamp(getDrDocExpired().getTime()));
                doc.setDocumentNote(getDoctorNote());
                doc.setDocumentType("Feltöltött orvosi");
                doc.setDocumentUrl(getFileNameFromPart(filePart));
                doc.setDocumentBin(getBytesFromPart(filePart));

                service.saveDrDoc(doc);

                message("Sikeres orvosi feltöltés", getFileNameFromPart(filePart) + " feltöltve.");

            } catch (Exception e) {

                e.printStackTrace();
                e = ServiceException.takeReason(e);
                error("Feltöltési hiba", null, e);
            }
        } else {
            error("Hiba", "Nincs kiválasztva az orvosi vagy a munkatárs!");
            throw new AbortProcessingException("Nincs kiválasztva az orvosi!");
        }
    }

    public void uploadAttachment(Integer staffId) {
        System.err.println("uploadAttachment " + filePart + " staffId " + staffId);
        if (filePart != null && staffId != null) {

            try {

                Attachment doc = new Attachment();
                doc.setIdStaff(staffId);
                doc.setDocumentCreated(new Timestamp(new Date().getTime()));
                doc.setDocumentNote(getAttachmentNote());
                doc.setDocumentUrl(getFileNameFromPart(filePart));
                doc.setDocumentBin(getBytesFromPart(filePart));

                service.saveAttachment(doc);

                message("Új melléklet feltöltve.", doc.getFileInfo());

            } catch (Exception e) {
                e.printStackTrace();
                e = ServiceException.takeReason(e);
                error("Feltöltési hiba", null, e);
            }

        } else {
            error("Hiba", "Nincs kiválasztva a melléklet vagy a munkatárs!");
            throw new AbortProcessingException();
        }
    }

    private byte[] getBytesFromPart(Part part) throws IOException {
        if (part != null) {
            String fileName = getFileNameFromPart(part);
            byte[] bin = ObjectUtils.serializeFile(fileName, part.getInputStream());
            // return ObjectUtils.zip(bin, fileName); // problémás a zp docx újrazippelése
            return bin;
        }
        return new byte[0];
    }

    private String getFileNameFromPart(Part part) {
        if (part != null) {
            final String partHeader = part.getHeader("content-disposition");
            for (String content : partHeader.split(";")) {
                if (content.trim().startsWith("filename")) {
                    String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                    return fileName.length() > DocBean.MAX_URL_LENGTH ? fileName.substring(0, DocBean.MAX_URL_LENGTH) : fileName;
                }
            }
        }
        return "";
    }

    public Integer getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(Integer idStaff) {
        System.out.println("FileUpload setIdStaff: " + idStaff);
        this.idStaff = idStaff;
    }

    public Date getDrDocExpired() {
        return drDocExpired;
    }

    public void setDrDocExpired(Date drDocExpired) {
        this.drDocExpired = drDocExpired;
    }

    public String getContractNote() {
        if (contractNote != null && contractNote.length() > DocBean.MAX_NOTE_LENGTH)
            return contractNote.substring(0, DocBean.MAX_NOTE_LENGTH);
        return contractNote;
    }

    public void setContractNote(String contractNote) {
        this.contractNote = contractNote;
    }

    public String getDoctorNote() {
        if (doctorNote != null && doctorNote.length() > DocBean.MAX_NOTE_LENGTH)
            return doctorNote.substring(0, DocBean.MAX_NOTE_LENGTH);
        return doctorNote;
    }

    public void setDoctorNote(String doctorNote) {
        this.doctorNote = doctorNote;
    }

    public String getAttachmentNote() {
        if (attachmentNote != null && attachmentNote.length() > DocBean.MAX_NOTE_LENGTH)
            return attachmentNote.substring(0, DocBean.MAX_NOTE_LENGTH);
        return attachmentNote;
    }

    public void setAttachmentNote(String attachmentNote) {
        this.attachmentNote = attachmentNote;
    }

}