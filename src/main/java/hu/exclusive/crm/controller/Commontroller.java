package hu.exclusive.crm.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

import org.primefaces.context.RequestContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.EntityCommons;
import hu.exclusive.utils.ObjectUtils;

public abstract class Commontroller implements Serializable {

	private static final long serialVersionUID = -814806420683471266L;
	transient protected Logger LOG = Logger.getLogger(this.getClass().getName());
	public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	transient protected Part filePart;

	public void setFilePart(Part file) {
		this.filePart = file;
		// System.out.println("setFilePart " + file);
	}

	public Part getFilePart() {
		return filePart;
	}

	public void checker() {
		System.out.println("FilePart " + filePart);
	}

	protected boolean fileLoaded() {
		return filePart != null;
	}

	protected String getUploadedFileName() {
		return getFileNameFromPart(filePart);
	}

	protected String getFileNameFromPart(Part part) {
		if (part != null) {
			final String partHeader = part.getHeader("content-disposition");
			for (String content : partHeader.split(";")) {
				if (content.trim().startsWith("filename")) {
					String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
					return fileName.length() > DocBean.MAX_URL_LENGTH ? fileName.substring(0, DocBean.MAX_URL_LENGTH)
							: fileName;
				}
			}
		}
		return "";
	}

	protected byte[] getUploadedFileBytes() throws IOException {
		return getBytesFromPart(filePart);
	}

	protected byte[] getBytesFromPart(Part part) throws IOException {
		if (part != null) {
			String fileName = getFileNameFromPart(part);
			byte[] bin = ObjectUtils.serializeFile(fileName, part.getInputStream());
			// return ObjectUtils.zip(bin, fileName); // problémás a zp docx
			// újrazippelése
			return bin;
		}
		return new byte[0];
	}

	@PostConstruct
	public void autowireBean() {
		// System.out.println("commoninit " + getClass().getName());
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		ServletContext servletContext = (ServletContext) externalContext.getContext();
		WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getAutowireCapableBeanFactory()
				.autowireBean(this);
		if (LOG == null)
			LOG = Logger.getLogger(this.getClass().getName());
		init();
	}

	protected abstract void init();

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		autowireBean();
	}

	protected void message(String title, String message) {
		if (message != null)
			message(title, message, (String) null);
	}

	protected void message(String title, String message, String updateElements) {
		LOG.fine("GROW: " + title + ": " + message);
		FacesMessage message3 = new FacesMessage(FacesMessage.SEVERITY_INFO, title, message);
		FacesContext.getCurrentInstance().addMessage(null, message3);
		if (updateElements != null)
			RequestContext.getCurrentInstance().update(updateElements);
	}

	protected void error(String title, String message) {
		if (message != null)
			error(title, message, (String) null);
	}

	protected void error(String title, String message, String updateElements) {
		LOG.warning("GROW: " + title + ": " + message);
		FacesMessage message3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message);
		FacesContext.getCurrentInstance().addMessage(null, message3);
		if (updateElements != null)
			RequestContext.getCurrentInstance().update(updateElements);
	}

	protected void error(String title, String message, Exception e) {

		if (e instanceof ServiceException) {
			if (isEmpty(message))
				message = e.getLocalizedMessage();
		} else {

			e = ServiceException.takeReason(e);
			if (isEmpty(message)) {
				message = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage();
			} else {
				message += " " + e.getClass().getSimpleName() + ": " + e.getLocalizedMessage();
			}

			if (isEmpty(title)) {
				title = e.getClass().getSimpleName() + " hiba történt";
			}
		}
		FacesMessage message3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message);
		FacesContext.getCurrentInstance().addMessage("error", message3);
	}

	protected void showErrorHint(Exception e) {
		LOG.log(Level.WARNING, "", e);
		error(null, null, e);
	}

	protected boolean isEmpty(Object o) {
		if (o != null) {
			return String.valueOf(o).trim().length() == 0;
		}
		return true;
	}

	public void cancelDialog() {
		System.out.println("Cancel dialog (no refresh)");
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public <T> T getFromList(List<? extends T> list, int id) {
		return ObjectUtils.getFromList(list, id);
	}

	public int[] toIdArray(List<? extends EntityCommons> list) {
		return ObjectUtils.toIdArray(list);
	}

	public CrmUser getLoggedUser() {
		return (CrmUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public String getDateTimeString(Date date) {
		if (date != null)
			return DATETIME.format(date);
		return "";
	}
}
