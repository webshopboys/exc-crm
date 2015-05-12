package hu.exclusive.crm.service;

import hu.exclusive.crm.model.DocBean;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.DaoFilter.RELATION;
import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.dao.model.StaffNote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentService {

    @Autowired
    hu.exclusive.dao.service.IExcDaoService excDao;

    public List<Attachment> getAttachments(DaoFilter daoFilter) {
        return excDao.getAttachments(daoFilter);
    }

    public Attachment getAttachment(DaoFilter daoFilter) {
        List<Attachment> list = getAttachments(daoFilter);
        if (list.size() == 1)
            return list.get(0);
        if (list.size() > 1)
            throw new ServiceException(list.size() + " találat egy helyett!");
        return null;
    }

    public List<DrDoc> getDrDocs(DaoFilter filter) {
        return excDao.getDrDocs(filter);
    }

    public DrDoc getDrDoc(DaoFilter daoFilter) {
        List<DrDoc> list = getDrDocs(daoFilter);
        if (list.size() == 1)
            return list.get(0);
        if (list.size() > 1)
            throw new ServiceException(list.size() + " találat egy helyett!");
        return null;
    }

    public List<ContractDoc> getContractDocs(DaoFilter filter) {
        return excDao.getContractDocs(filter);
    }

    public ContractDoc getContractDoc(DaoFilter daoFilter) {
        List<ContractDoc> list = getContractDocs(daoFilter);
        if (list.size() == 1)
            return list.get(0);
        if (list.size() > 1)
            throw new ServiceException(list.size() + " találat egy helyett!");
        return null;
    }

    public List<StaffNote> getStaffNotes(DaoFilter filter) {
        return excDao.getStaffNotes(filter);
    }

    public StaffNote getStaffNote(DaoFilter daoFilter) {
        List<StaffNote> list = getStaffNotes(daoFilter);
        if (list.size() == 1)
            return list.get(0);
        if (list.size() > 1)
            throw new ServiceException(list.size() + " találat egy helyett!");
        return null;
    }

    public List<DocBean> getStaffDocuments(Integer staffId) {

        // System.out.println("getStaffDocuments " + staffId);
        DaoFilter filter = new DaoFilter();
        filter.setFieldName("idStaff");
        filter.setRelation(RELATION.NAMED_QUERY);
        filter.setValues(staffId);

        List<DocBean> list = new ArrayList<DocBean>();

        filter.setEntity("ContractDoc.findForStaff");
        for (ContractDoc doc : getContractDocs(filter)) {
            list.add(new DocBean(doc, null, null, null));
        }

        filter.setEntity("DrDoc.findForStaff");
        for (DrDoc doc : getDrDocs(filter)) {
            list.add(new DocBean(null, doc, null, null));
        }
        filter.setEntity("StaffNote.findForStaff");
        for (StaffNote doc : getStaffNotes(filter)) {
            list.add(new DocBean(null, null, doc, null));
        }

        filter.setEntity("Attachment.findForStaff");
        for (Attachment doc : getAttachments(filter)) {
            list.add(new DocBean(null, null, null, doc));
        }

        // System.out.println("getStaffDocuments " + staffId + " count " + list.size());
        Collections.sort(list);
        return list;
    }

    public void saveDocument(ContractDoc template) {
        excDao.saveDocument(template);
    }

    public List<ContractDoc> getContractTemplates() {
        DaoFilter filter = new DaoFilter();
        filter.addFilter("idStaff", RELATION.ISNULL, null);
        return excDao.getContractDocs(filter);
    }

    public void saveNote(StaffNote note) {
        excDao.saveDocument(note);
    }

    public void saveAttachment(Attachment attachment) {
        excDao.saveDocument(attachment);
    }

    public void saveDrDoc(DrDoc drdoc) {
        excDao.saveDocument(drdoc);
    }
}
