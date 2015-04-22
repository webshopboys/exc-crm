package hu.exclusive.crm.controller;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.crm.model.DocFilter;
import hu.exclusive.crm.model.StaffFilter;
import hu.exclusive.crm.service.AttachmentService;
import hu.exclusive.crm.service.ParametersService;
import hu.exclusive.crm.service.StaffService;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffBase;
import hu.exclusive.dao.model.StaffDetail;
import hu.exclusive.utils.FacesAccessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
public class StaffController extends Commontroller implements Serializable {

    private static final long serialVersionUID = 3826321633790448440L;

    private LazyDataModel<Staff> staffModel;
    private LazyDataModel<Staff> docModel;
    private StaffFilter staffFilter;
    private DocFilter docFilter;
    private Map<Integer, List<DocBean>> staffDocCache = new HashMap<Integer, List<DocBean>>();
    private StaffDetail activeStaff;

    @Autowired
    StaffService service;
    @Autowired
    AttachmentService attachments;

    @Autowired
    ParametersService parameters;

    @PostConstruct
    public void init() {
        if (service == null)
            service = (StaffService) FacesAccessor.getManagedBean("staffService");

        if (parameters == null)
            parameters = (ParametersService) FacesAccessor.getManagedBean("parametersService");

        if (attachments == null)
            attachments = (AttachmentService) FacesAccessor.getManagedBean("attachmentService");

        this.staffModel = new LazyDataModel<Staff>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Staff> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return pageDataTable(this, first, pageSize, sortField, sortOrder, getFilter(), filters);
            }

            @Override
            public List<Staff> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                return pageDataTable(this, first, pageSize, null, multiSortMeta, getFilter(), filters);
            }
        };

        this.docModel = new LazyDataModel<Staff>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Staff> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return pageDataTable(this, first, pageSize, sortField, sortOrder, getDocFilter(), filters);
            }

            @Override
            public List<Staff> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                return pageDataTable(this, first, pageSize, null, multiSortMeta, getDocFilter(), filters);
            }
        };

        System.err.println("==============================================================");
        System.err.println("                EXC CRM (2015.03.30) ");
        System.err.println("==============================================================");

    }

    private List<Staff> pageDataTable(LazyDataModel<Staff> dataModel, int first, int pageSize, String sortField,
            Object sortOrderOrSortMeta, DaoFilter filter, Map<String, Object> filters) {

        // System.err.println("pageDataTable filter: " + filter.getClass().getSimpleName());
        staffDocCache.clear();

        List<Staff> staffList = null;
        try {
            filter.setStartIndex(first);
            filter.setPageSize(pageSize);
            staffList = service.getStaffList(filter);
            dataModel.setPageSize(pageSize);
            dataModel.setRowCount((int) filter.getTotalCount());

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
        if (staffFilter == null) {
            staffFilter = new StaffFilter();
            staffFilter.setStatus(new String[] { StaffBase.DEFAULT_STATUS });
        }
        return staffFilter;
    }

    public DocFilter getDocFilter() {
        if (docFilter == null) {
            docFilter = new DocFilter();
            docFilter.setStatus(new String[] { StaffBase.DEFAULT_STATUS });
        }
        return docFilter;
    }

    public void resetDocFilter() {
        System.out.println("resetDocfilter....");
        getDocFilter().reset();
        RequestContext.getCurrentInstance().reset("docFilterForm");
        staffDocCache.clear();
    }

    public void resetFilter() {
        System.out.println("resetfilter....");
        getFilter().reset();
        RequestContext.getCurrentInstance().reset("filterForm");
    }

    public LazyDataModel<Staff> getStaffModel() {
        return staffModel;
    }

    public LazyDataModel<Staff> getDocModel() {

        return docModel;
    }

    public List<DocBean> getStaffDocuments(Integer staffId) {
        if (staffId == null || staffId == 0)
            return null;
        if (!staffDocCache.containsKey(staffId))
            staffDocCache.put(staffId, attachments.getStaffDocuments(staffId));

        return staffDocCache.get(staffId);
    }

    public int[] getAsJobtitles() {
        return toIdArray(getActiveStaff().getJobtitles());
    }

    public void setAsJobtitles(int[] titles) {
        List<Jobtitle> list = new ArrayList<Jobtitle>();
        if (titles != null && titles.length > 0) {
            for (int i = 0; i < titles.length; i++) {
                int titleId = Integer.valueOf(titles[i]);
                list.add(parameters.getJobtitle(titleId));
            }
        }
        System.err.println("setAsJobtitles int[] " + list);
        getActiveStaff().setJobtitles(list);
    }

    public StaffDetail getActiveStaff() {
        if (activeStaff == null)
            activeStaff = new StaffDetail();
        return activeStaff;
    }

    public void setActiveStaff(Integer idStaff) {
        if (idStaff != null) {
            setActiveStaff(service.getStaffDetail(idStaff));
        }
    }

    public void setActiveStaff(StaffDetail activeStaff) {
        this.activeStaff = activeStaff;
    }

    public void saveStaff() {
        System.err.println("SaveStaff " + activeStaff);

        service.saveStaff(activeStaff);
        closeActiveStaff();
    }

    public void closeActiveStaff() {
        // FIXME az adatlap bezárása és a lista újratltése a korábbi felterekkel. Vagyis mi van, ha új munkatárs? Akkor is.
        Navigator navigator = (Navigator) FacesAccessor.getManagedBean("navigator");
        navigator.setContent("staff/staffList", "staff/staffListFilter");

        // getFilter().reset();
        // getFilter().setFullName(activeStaff.getFullName());

        activeStaff = null;

        RequestContext.getCurrentInstance().update("mainContentPanel");
        RequestContext.getCurrentInstance().update("sliderContentPanel");
    }

    public void deleteStaff(Integer idStaff) {
        message("Feldolgozási eredmény", "Csak a fejlesztő törölheti");
    }

}
