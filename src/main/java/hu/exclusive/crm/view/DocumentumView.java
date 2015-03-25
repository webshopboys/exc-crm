package hu.exclusive.crm.view;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.DaoFilter.RELATION;
import hu.exclusive.dao.model.StaffDocument;
import hu.exclusive.dao.service.ParametersService;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "documentumView")
@ViewScoped
public class DocumentumView implements Serializable {

    private static final long serialVersionUID = -1774648789104774066L;
    @Autowired
    ParametersService service;

    @PostConstruct
    public void init() {
    }

    public List<StaffDocument> getDocuments() {
        DaoFilter filter = new DaoFilter();
        filter.addFilter("idStaff", RELATION.ISNULL, null);
        filter.addFilter("StaffDocument", RELATION.IN, new String[] { StaffDocument.TYPE_CONTRACT,
                StaffDocument.TYPE_CONTRACT_MODIFICATION });
        filter.setAnyEnought(false);
        return service.getDocuments(filter);
    }

    private UploadedFile filedrDoc;

    public UploadedFile getFileDrDoc() {
        return filedrDoc;
    }

    public void setFileDrDoc(UploadedFile file) {
        this.filedrDoc = file;
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

    private Integer idDocument;
}