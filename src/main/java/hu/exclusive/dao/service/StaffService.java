package hu.exclusive.dao.service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {

    @Autowired
    hu.exclusive.dao.service.IExcDaoService excDao;

    public List<Staff> getStaffList(DaoFilter filter) {
        return excDao.getStaffList(filter);
    }

}
