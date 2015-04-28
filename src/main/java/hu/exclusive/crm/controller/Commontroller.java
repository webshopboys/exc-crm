package hu.exclusive.crm.controller;

import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.EntityCommons;
import hu.exclusive.utils.ObjectUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class Commontroller implements Serializable {

    private static final long serialVersionUID = -814806420683471266L;
    protected final Logger log = Logger.getLogger(this.getClass().getName());
    public static final SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    protected void message(String title, String message) {
        message(title, message, (String) null);
    }

    protected void message(String title, String message, String updateElements) {
        System.out.println("GROW: " + title + ": " + message);
        FacesMessage message3 = new FacesMessage(FacesMessage.SEVERITY_INFO, title, message);
        FacesContext.getCurrentInstance().addMessage(null, message3);
        if (updateElements != null)
            RequestContext.getCurrentInstance().update(updateElements);
    }

    protected void error(String title, String message) {
        error(title, message, (String) null);
    }

    protected void error(String title, String message, String updateElements) {
        System.err.println("GROW: " + title + ": " + message);
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
        log.log(Level.WARNING, "", e);
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
