package hu.exclusive.crm.controller;

import hu.exclusive.crm.model.StaffFilter;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffBase;
import hu.exclusive.dao.model.StaffDocument;
import hu.exclusive.dao.service.StaffService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.service.spi.ServiceException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "staffController")
@ViewScoped
public class StaffController implements Serializable {

    private static final long serialVersionUID = 3826321633790448440L;
    protected final Logger log = Logger.getLogger(this.getClass().getName());

    private List<Staff> staffList;
    private LazyDataModel<Staff> staffModel;
    private StaffFilter filter;

    @Autowired
    StaffService service;

    @PostConstruct
    public void init() {

        this.staffModel = new LazyDataModel<Staff>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Staff> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return pageDataTable(first, pageSize, sortField, sortOrder, filters);
            }

            @Override
            public List<Staff> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                return pageDataTable(first, pageSize, null, multiSortMeta, filters);
            }
        };

        System.err.println("StaffController init size: " + staffModel.getRowCount());
    }

    @SuppressWarnings("unchecked")
    private List<Staff> pageDataTable(int first, int pageSize, String sortField, Object sortOrderOrSortMeta,
            Map<String, Object> filters) {

        try {

            DaoFilter filter = getFilter();
            filter.setStartIndex(first);
            filter.setPageSize(pageSize);

            // filter.addFilter("birthDate", "notnull", null);

            staffList = service.getStaffList(filter);
            // ServiceResult result = demoDao.getTestList(demoFilter);
            //
            // staffList = (List<TestRecord>) result.getResultList();
            //
            staffModel.setPageSize(pageSize);
            staffModel.setRowCount((int) filter.getTotalCount());

        } catch (ServiceException e) {
            showErrorHint(e);
        }

        return staffList;
    }

    /**
     * Az aktuális szűrők alapján a filter összeállítása.
     * 
     * @return
     */
    public StaffFilter getFilter() {
        if (filter == null) {
            filter = new StaffFilter();
            filter.setStatus(new String[] { StaffBase.DEFAULT_STATUS });
        }
        return filter;
    }

    public void resetFilter() {
        System.out.println("resetfilter....");
        getFilter().reset();
        RequestContext.getCurrentInstance().reset("filterForm");
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public LazyDataModel<Staff> getStaffModel() {
        return staffModel;
    }

    private StaffDocument document;

    /**
     * A document alapján megadott sablonba kitölteni a munkatárs adatait és a készült fájlt letölteni.
     */
    public void createDocument() {

    }

    private void showErrorHint(Exception e) {
        log.log(Level.WARNING, "", e);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hiba!", "" + e));
    }

    public StaffDocument getDocument() {
        return document;
    }

    public void setDocument(StaffDocument document) {
        this.document = document;
    }

}
