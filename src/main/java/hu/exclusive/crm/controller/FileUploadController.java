package hu.exclusive.crm.controller;

import hu.exclusive.crm.service.AttachmentService;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.Staff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "fileUploadController")
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
            byte[] bin = serializeFile(event.getFile().getFileName(), event.getFile().getInputstream());

            ContractDoc doc = new ContractDoc();
            doc.setDocumentType("Új szerződés sablon");
            doc.setDocumentNote("Kiválasztással feltöltött fájl. Utólag szerkesztendő!\nA munkatárs elérhető mezői: "
                    + Staff.DOCFIELDS_NAMES.getInfo());
            doc.setDocumentUrl(event.getFile().getFileName());
            doc.setDocumentBin(bin);
            service.saveDocument(doc);

            message("Új szerződés sablon elmentve.", doc.getDocumentNote());
        } catch (IOException e) {
            e.printStackTrace();
            error("Feltöltési hiba", null, e);
        }

    }

    public byte[] serializeFile(String fileName, InputStream in) throws IOException {
        try {

            // write the inputStream to a FileOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            return out.toByteArray();

        } catch (IOException e) {
            throw e;
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

    public void uploadFileContract(ActionEvent event) {
        System.err.println("uploadFileContract " + filePart);
        if (filePart != null) {
            message("Sikeres feltöltés", getFileNameFromPart(filePart) + " feltöltve.");
        } else {
            error("Hiba", "Nincs kiválasztva a szerződés!");
            throw new AbortProcessingException();
        }
    }

    public void uploadFileDr(ActionEvent event) {
        System.err.println("uploadFileDr " + filePart);
        if (filePart != null) {
            message("Sikeres feltöltés", getFileNameFromPart(filePart) + " feltöltve.");
        } else {
            error("Hiba", "Nincs kiválasztva az orvosi!");
            throw new AbortProcessingException();
        }
    }

    public void uploadAttachment(ActionEvent event) {
        System.err.println("uploadAttachment " + filePart);
        if (filePart != null) {
            message("Sikeres feltöltés", getFileNameFromPart(filePart) + " feltöltve.");
        } else {
            error("Hiba", "Nincs kiválasztva a melléklet!");
            throw new AbortProcessingException();
        }
    }

    private String getFileNameFromPart(Part part) {
        if (part != null) {
            final String partHeader = part.getHeader("content-disposition");
            for (String content : partHeader.split(";")) {
                if (content.trim().startsWith("filename")) {
                    String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                    return fileName;
                }
            }
        }
        return "";
    }

    public Integer getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(Integer idStaff) {
        this.idStaff = idStaff;
    }

    public Date getDrDocExpired() {
        return drDocExpired;
    }

    public void setDrDocExpired(Date drDocExpired) {
        this.drDocExpired = drDocExpired;
    }

    // private void restoreDoc(Attachment doc) throws Exception {
    // if (doc.getDocumentBin() != null && doc.getDocumentUrl() != null) {
    // FileOutputStream ow = null;
    // try {
    // String path = doc.getDocumentUrl().substring(0, doc.getDocumentUrl().lastIndexOf("/")); // " dir/dir "
    // String fileName = doc.getDocumentUrl().substring(doc.getDocumentUrl().lastIndexOf("/")); // " /name"
    // File dir = new File(path);
    // dir.mkdir();
    // dir = new File(path + "/restored");
    // dir.mkdir();
    // File file = new File(path + "/restored" + fileName);
    // file.delete();
    // file.createNewFile();
    // ow = new FileOutputStream(file);
    //
    // ow.write(doc.getDocumentBin());
    // ow.flush();
    // ow.close();
    // ow = null;
    //
    // } finally {
    // if (ow != null)
    // ow.close();
    // }
    //
    // }
    // }
    //
    // private void serializeDoc(ISerializableDoc doc) throws Exception {
    // FileInputStream fileInputStream = null;
    // try {
    // File f = new File(doc.getDocumentUrl());
    //
    // if (f.exists() && f.isFile()) {
    // fileInputStream = new FileInputStream(f);
    // long sizeInBytes = f.length();
    // // convert file into array of bytes
    // byte[] fileInBytes = new byte[(int) sizeInBytes];
    // fileInputStream.read(fileInBytes);
    //
    // fileInputStream.close();
    // fileInputStream = null;
    //
    // doc.setDocumentBin(fileInBytes);
    //
    // } else {
    // throw new IllegalAccessException("File not reach");
    // }
    // } finally {
    // if (fileInputStream != null)
    // fileInputStream.close();
    // }
    //
    // }
}