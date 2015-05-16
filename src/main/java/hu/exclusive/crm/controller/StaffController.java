package hu.exclusive.crm.controller;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.crm.model.DocFilter;
import hu.exclusive.crm.model.StaffFilter;
import hu.exclusive.crm.report.ContractGenerator;
import hu.exclusive.crm.service.AttachmentGenerator;
import hu.exclusive.crm.service.AttachmentService;
import hu.exclusive.crm.service.ParametersService;
import hu.exclusive.crm.service.StaffService;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffBase;
import hu.exclusive.dao.model.StaffDetail;
import hu.exclusive.dao.model.StaffNote;
import hu.exclusive.utils.FacesAccessor;
import hu.exclusive.utils.ObjectUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import org.hibernate.service.spi.ServiceException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
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
    private final Map<Integer, List<DocBean>> staffDocCache = new HashMap<Integer, List<DocBean>>();
    private StaffDetail activeStaff;
    private StaffNote note;

    private AttachmentGenerator generator;

    private String originalNoteNote;
    @Autowired
    StaffService service;

    @Autowired
    AttachmentService attachments;

    @Autowired
    ParametersService parameters;

    private Integer templateId;

    @PostConstruct
    public void init() {

        generator = new AttachmentGenerator();

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
        System.err.println("               " + parameters.getSystemVersion());
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
        resetDocCache();
    }

    public void resetFilter() {
        System.out.println("resetfilter....");
        getFilter().reset();
        RequestContext.getCurrentInstance().reset("filterForm");
    }

    // FIXME vajon gondot okoz, ha a közös cache törlődik?
    public void resetDocCache() {
        staffDocCache.clear();
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
        // System.out.println("StaffController.getActiveStaff");
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

    public List<StaffNote> getStaffNotes(Integer idStaff) {
        List<StaffNote> list = new ArrayList<StaffNote>();
        if (idStaff != null) {
            List<DocBean> allDoc = getStaffDocuments(idStaff);
            if (allDoc != null) {
                for (DocBean docBean : allDoc) {
                    if (docBean.getDocumentKey().startsWith(DocBean.KEY_NOTE)) {
                        list.add(docBean.getStaffNote());
                    }
                }
            }
        }
        return list;
    }

    private DocBean getDoc(Integer staffId, Integer docId) {
        if (staffId != null && docId != null) {
            List<DocBean> allDoc = getStaffDocuments(staffId);
            if (allDoc != null) {
                for (DocBean docBean : allDoc) {
                    if (docBean.getId() == docId) {
                        return docBean;
                    }
                }
            }
        }
        return null;
    }

    private ContractDoc getTemplate(Integer docId) {
        if (docId != null) {
            for (ContractDoc doc : attachments.getContractTemplates()) {
                if (doc.getIdContractdoc() == docId)
                    return doc;
            }
        }
        return null;
    }

    public void saveStaff() {

        try {
            System.err.println("SaveStaff " + activeStaff);

            // if (isEmpty(user.getUserName()) || isEmpty(user.getLoginName())) {
            // error("Hiányos adatok", "A teljes név és login név is kötelező, és nem lehet már létező!", "userForm");
            // return;
            // }

            service.saveStaff(activeStaff);

            message("Feldolgozási eredmény", "Munkatárs sikeresen mentve.");

        } catch (Exception e) {
            e.printStackTrace();
            error("Mentési hiba", null, new hu.exclusive.dao.ServiceException(e));
        }
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

    public void setTemplate(Integer docId) {
        System.err.println("set template " + docId);
        this.templateId = docId;
    }

    public Integer getTemplate() {
        return this.templateId;
    }

    public StreamedContent generateContract() {
        try {
            ContractDoc contract = getTemplate(templateId);
            System.out.println("generateContract " + contract);
            if (contract != null) {

                byte[] content = new ContractGenerator(contract, activeStaff).processByExtension();

                generator
                        .setContent(content)
                        .setAttachmentName(
                                ObjectUtils.cleanFilename(activeStaff.getFullName() + " " + contract.getDocumentType() + " "
                                        + ObjectUtils.getDateTime(new Date()) + ".docx"))
                        .setMimeType(AttachmentGenerator.MIME_DOCX);
            }

            return generator.generateStream();
        } catch (Exception e) {
            e.printStackTrace();
            e = hu.exclusive.dao.ServiceException.takeReason(e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hiba", "A kitöltés nem sikerült: "
                    + e.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    public StaffNote getNote() {
        System.out.println("getnote ");
        return note;
    }

    public StaffNote getNote2() {
        System.out.println("getnote2 ");
        return note;
    }

    public void setNote(StaffNote note) {

        if (note != null && note.getCreatorUser() != null && note.getNote() != null) {
            String lastCreator = note.getCreatorUser();
            Timestamp lastSaved = note.getNoteCreated();
            originalNoteNote = "<" + lastCreator + " " + getDateTimeString(lastSaved) + "-kor>\n" + note.getNote()
                    + "\n___________________________________________\n";
            note.setNote(originalNoteNote);
        } else {
            originalNoteNote = null;
        }
        this.note = note;
    }

    public void saveNote(ActionEvent event) {
        if (note == null || activeStaff == null) {
            error("Hiányos adatok", "A munkatárs nincs meghatározva", "uploadmessages");
            throw new AbortProcessingException();
        }

        note.setNoteCreated(new Timestamp(new Date().getTime()));
        note.setIdStaff(activeStaff.getIdStaff());
        note.setCreatorUser(getLoggedUser().getUserName());

        attachments.saveNote(note);
        setNote(null);
    }

    public void addNote() {
        setNote(new StaffNote());
    }

    public String getOriginalNoteNote() {
        return originalNoteNote;
    }

    public void setOriginalNoteNote(String originalNoteNote) {
        this.originalNoteNote = originalNoteNote;
    }
}
