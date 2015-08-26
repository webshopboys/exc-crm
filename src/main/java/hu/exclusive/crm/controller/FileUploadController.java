package hu.exclusive.crm.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.crm.service.AttachmentService;
import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.utils.ObjectUtils;

//@Component
@ManagedBean(name = "fileUploadController")
@ViewScoped
public class FileUploadController extends Commontroller implements Serializable {

	private static final long serialVersionUID = -1774648789104774066L;
	@Autowired
	transient AttachmentService service;

	private UploadedFile filedrDoc;

	private Integer idDocument;
	private ContractDoc template;
	private Integer idStaff;
	private Date drDocExpired;
	private String contractNote;
	private String doctorNote;
	private String attachmentNote;

	@Override
	protected void init() {

	}

	public void uploadTemplates(FileUploadEvent event) {

		message(event.getFile().getFileName() + " feltöltése...", "");
		try {
			byte[] bin = ObjectUtils.serializeFile(event.getFile().getFileName(), event.getFile().getInputstream());

			byte[] zipped = bin;// ObjectUtils.zip(bin,
								// event.getFile().getFileName());

			String fileName = event.getFile().getFileName();
			String docTypeName = "Sablon: " + fileName;
			if (docTypeName.length() > DocBean.MAX_CONTRACTTYPE_LENGTH)
				docTypeName = docTypeName.substring(0, DocBean.MAX_CONTRACTTYPE_LENGTH);

			ContractDoc doc = new ContractDoc();
			doc.setDocumentType(docTypeName);
			doc.setDocumentNote(
					"Kiválasztással feltöltött fájl: " + fileName + " A le  írását utólag ki kell tölteni!");
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
		template = null;
	}

	public void uploadDrDoc() {
		if (filedrDoc != null) {
			message("Succesful", filedrDoc.getFileName() + " is uploaded.");
		}
	}

	public void removeTemplate() {
		if (template != null && template.getIdContractdoc() != null) {
			service.deleteContract(template);
			template = null;
		}
	}

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
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