package hu.exclusive.crm.service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.Role;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParametersService {

    @Autowired
    hu.exclusive.dao.service.IExcDaoService excDao;
    private List<Jobtitle> jobtitlePuffer;

    public List<Jobtitle> getJobtitles(DaoFilter filter) {
        this.jobtitlePuffer = excDao.getJobtitles(filter);
        return this.jobtitlePuffer;
    }

    public List<Workgroup> getWorkgroups(DaoFilter filter) {
        return excDao.getWorkgroups(filter);
    }

    public List<Workplace> getWorkplaces(DaoFilter filter) {
        return excDao.getWorkplaces(filter);
    }

    public List<Role> getRoles(DaoFilter daoFilter) {
        return excDao.getRoles(daoFilter);
    }

    public List<Function> getFunctions(DaoFilter daoFilter) {
        return excDao.getFunctions(daoFilter);
    }

    public void saveFunction(Function f) {
        excDao.saveFunction(f);
    }

    public void saveRole(Role r) {
        excDao.saveRole(r);
    }

    public List<CrmUser> getUsers(DaoFilter filter) {
        return excDao.getUsers(filter);
    }

    public String getSystemVersion() {
        return excDao.getSystemParameter("SYS", "VERSION");
    }

    public void saveUser(CrmUser user) {
        excDao.saveUser(user);
    }

    public Jobtitle getJobtitle(Integer idJobtitle) {
        for (Jobtitle jobtitle : getJobtitlePuffer()) {
            if (jobtitle.getIdJobtitle() == idJobtitle)
                return jobtitle;
        }
        return null;
    }

    public List<Jobtitle> getJobtitlePuffer() {
        if (jobtitlePuffer == null)
            getJobtitles(null);
        return jobtitlePuffer;
    }
}
