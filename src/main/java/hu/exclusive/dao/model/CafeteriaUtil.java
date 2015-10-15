package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A cefeteria infók és a havi cafék segéd listája, aktuális vagy számosított
 * részek kiemeléséhez, stb.
 * 
 * @author user
 *
 */
public class CafeteriaUtil implements Serializable {

	private static final long serialVersionUID = 8516671197652212180L;

	private List<CafeteriaInfo> infos;
	private List<Cafeteria> months;
	private int year;
	private List<PCafeteriaCategory> categories;

	public CafeteriaUtil() {
	}

	@SuppressWarnings("unchecked")
	public CafeteriaUtil(List<?> list) {
		if (list != null && list.size() > 0) {
			if (list.get(0) instanceof CafeteriaInfo) {
				this.infos = (List<CafeteriaInfo>) list;
			} else {
				this.months = (List<Cafeteria>) list;
			}
		}
	}

	public CafeteriaUtil(int year, List<CafeteriaInfo> infos, List<Cafeteria> months) {
		this.year = year;
		this.infos = infos;
		this.months = months;
	}

	public void setInfos(List<CafeteriaInfo> infos) {
		this.infos = infos;
	}

	public void setMonths(List<Cafeteria> months) {
		this.months = months;
	}

	public void setCategories(List<PCafeteriaCategory> categories) {
		this.categories = categories;
	}

	public int getCategoryMonthlyLimit(int categoryId) {
		for (PCafeteriaCategory cat : categories) {
			if (cat.getIdCafeteriaCat() == categoryId) {
				return cat.getMonthlyLimit() == null ? 0 : cat.getMonthlyLimit();
			}
		}
		return 0;
	}

	public int getCategoryYearlyLimit(int categoryId) {
		for (PCafeteriaCategory cat : categories) {
			if (cat.getIdCafeteriaCat() == categoryId) {
				return cat.getYearlyLimit() == null ? 0 : cat.getYearlyLimit();
			}
		}
		return 0;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getCurrentYear() {
		if (year < 1) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}
		return year;
	}

	public CafeteriaInfo getCurrentInfo() {
		return getInfo(getCurrentYear());
	}

	public CafeteriaInfo getInfo(int year) {
		if (infos != null && infos.size() > 0) {
			for (CafeteriaInfo info : infos) {
				if (info.getYearKey() == getCurrentYear())
					return info;
			}
		}
		return new CafeteriaInfo();
	}

	public List<Cafeteria> getYearlyCafes(int year) {
		List<Cafeteria> list = new ArrayList<Cafeteria>();
		for (int i = 1; i < 13; i++) {
			list.addAll(getMonthlyCafes(year, i));
		}
		return list;
	}

	public List<Cafeteria> getMonthlyCafes(int year, int month) {
		List<Cafeteria> list = new ArrayList<Cafeteria>();
		if (months != null && months.size() > 0) {
			for (Cafeteria cafe : months) {
				if (cafe.getYearKey() == year && cafe.getMonthKey() == month)
					list.add(cafe);
			}

		}
		return list;
	}
}
