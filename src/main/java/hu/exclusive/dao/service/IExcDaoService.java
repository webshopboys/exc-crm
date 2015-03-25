package hu.exclusive.dao.service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffDocument;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;

import java.util.List;

public interface IExcDaoService {

    java.util.List<Staff> getStaffList(DaoFilter filter);

    List<Jobtitle> getJobtitles(DaoFilter filter);

    List<Workgroup> getWorkgroups(DaoFilter filter);

    List<Workplace> getWorkplaces(DaoFilter filter);

    List<StaffDocument> getDocuments(DaoFilter filter);

}
