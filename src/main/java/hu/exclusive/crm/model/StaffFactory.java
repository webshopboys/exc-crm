package hu.exclusive.crm.model;

import java.util.List;

import hu.exclusive.dao.model.StaffCafeteria;

public class StaffFactory {

	public static CafeteriaExcel createCafeteriaExcel(StaffCafeteria staff) {
		CafeteriaExcel ce = new CafeteriaExcel();
		ce.setStaff(staff);
		return ce;
	}

	public static CafeteriaExcel getCafeteriaExcel(List<CafeteriaExcel> staffs, StaffCafeteria staff) {
		CafeteriaExcel cex = null;
		for (CafeteriaExcel ce : staffs) {
			if (ce.getStaff().getIdStaff() == staff.getIdStaff()) {
				cex = ce;
				break;
			}
		}
		if (cex == null)
			cex = createCafeteriaExcel(staff);

		return cex;
	}
}
