package hu.exclusive.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.dao.model.CafeteriaInfo;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.StaffCafeteria;

@Service
public class CafeteriaService {

	@Autowired
	transient hu.exclusive.dao.service.IExcDaoService excDao;

	public List<PCafeteriaCategory> getCafeteriaCategories(DaoFilter filter) {
		return excDao.getCafeteriaCategories(filter);
	}

	public List<PCafeteriaCategory> getCafeteriaCategoryByName(String catKey) {
		return excDao.getCafeteriaCategoryByName(catKey);
	}

	public void saveCafeteriaCategories(List<PCafeteriaCategory> categories) {
		if (categories == null)
			return;
		for (PCafeteriaCategory cat : categories) {
			excDao.saveCategory(cat);
		}
	}

	public void saveCafeteriaInfo(CafeteriaInfo info) {
		excDao.saveCafeteriaInfo(info);
	}

	public void saveCafeterias(List<Cafeteria> monthlyCafes) {
		for (Cafeteria m : monthlyCafes) {
			saveCafeteria(m);
		}
	}

	public void saveCafeteria(Cafeteria monthlyCafe) {
		excDao.saveCafeteria(monthlyCafe);
	}

	public List<StaffCafeteria> getCafeteriaList(DaoFilter filter) {
		return excDao.getCafeteriaList(filter);
	}

	public List<StaffCafeteria> getStaffCafByName(String personName, String tax) {
		return excDao.getStaffCafByName(personName, tax);
	}

	public void deleteCafeteriaInfo(CafeteriaInfo info) {
		excDao.deleteCafeteriaInfo(info);
	}

	public void deleteCafeteria(Cafeteria monthlyCafe) {
		excDao.deleteCafeteria(monthlyCafe);
	}

	public void deleteCafeteria(int staff, int year, int month) {
		excDao.deleteCafeteria(staff, year, month);
	}

	public void deleteCafeteria(int staff, int year, int month, int category) {
		excDao.deleteCafeteria(staff, year, month, category);
	}

}
