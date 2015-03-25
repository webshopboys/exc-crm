package hu.exclusive.dao.service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.StaffDocument;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParametersService {

    @Autowired
    hu.exclusive.dao.service.IExcDaoService excDao;

    public List<Jobtitle> getJobtitles(DaoFilter filter) {
        return excDao.getJobtitles(filter);
    }

    public List<Workgroup> getWorkgroups(DaoFilter filter) {
        return excDao.getWorkgroups(filter);
    }

    public List<Workplace> getWorkplaces(DaoFilter filter) {
        return excDao.getWorkplaces(filter);
    }

    public List<StaffDocument> getDocuments(DaoFilter filter) {
        return excDao.getDocuments(filter);
    }

}
