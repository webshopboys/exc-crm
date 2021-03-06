package hu.exclusive.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffBase;
import hu.exclusive.dao.model.StaffDetail;

@Service
public class StaffService {

	@Autowired
	transient hu.exclusive.dao.service.IExcDaoService excDao;

	public List<Staff> getStaffList(DaoFilter filter) {
		return excDao.getStaffList(filter);
	}

	public StaffDetail getStaffDetail(Integer idStaff) {
		return excDao.getStaffDetail(idStaff);
	}

	public void saveStaff(StaffDetail staff) {
		excDao.saveStaff(staff);
	}

	public void saveStaff(StaffBase staff) {
		excDao.saveStaff(staff);
	}

	public List<Staff> getStaffByName(String personName) {
		return excDao.getStaffByName(personName);
	}

}
