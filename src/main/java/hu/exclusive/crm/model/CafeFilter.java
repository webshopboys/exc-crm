package hu.exclusive.crm.model;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.StaffExistsCafeteria;
import hu.exclusive.utils.ObjectUtils;

/**
 * A munkatarsak lekerdezese cafeteria adatok alapjan.
 * A szerkezet: staff-abban egy cafeteria lista-azokon egy cafeteria kategória
 * 
 * StaffCafeteria (StaffBase)
 * - fullName
 * - List<Cafeteria> monthlyCafes
 * - monthlyCafes.cafeCategory
 * - monthlyCafes.idCafeteriaCat
 * - monthlyCafes.yearKey
 * - monthlyCafes.monthKey
 * - workgroup.idWorkgroup
 * 
 * @author Petnehazi
 *
 */
public class CafeFilter extends DaoFilter {

    private String fullName;

    private Integer[] workgroups;
    private Integer[] categories;

    private Integer yearKey;
    private Integer monthKey;

    @Override
    public void reset() {

        fullName = null;
        workgroups = null;
        categories = null;
        setYearKey(null);
        setMonthKey(null);
        super.reset();

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        DaoFilter filter = getFilter("fullName", RELATION.LIKE, null);
        if (ObjectUtils.nv(fullName) != null) {
            if (filter == null) {
                addFilter("fullName", RELATION.LIKE, fullName);
            } else {
                filter.setValues(fullName);
            }
        } else {
            removeFilter(filter);
        }
    }

    public Integer[] getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(Integer[] workgroups) {
        this.workgroups = workgroups;
        DaoFilter filter = getFilter("workgroup.idWorkgroup", RELATION.IN, null);
        if (ObjectUtils.nv(workgroups) != null && workgroups.length > 0) {
            if (filter == null) {
                addFilter("workgroup.idWorkgroup", RELATION.IN, workgroups);
            } else {
                filter.setValues(workgroups);
            }
        } else {
            removeFilter(filter);
        }
    }

    public Integer[] getCategories() {
        return categories;
    }

    public void setCategories(Integer[] categories) {
        this.categories = categories;
        DaoFilter filter = getFilter("idCafeteriaCat", RELATION.IN, null);
        if (ObjectUtils.nv(categories) != null && categories.length > 0) {
            if (filter == null) {
                // "yearKey", "monthKey","idCafeteriaCat"
                addFilter(new StaffExistsCafeteria(), "idCafeteriaCat", RELATION.IN, categories);
            } else {
                filter.setValues(categories);
            }
        } else {
            removeFilter(filter);
        }
    }

    public Integer getYearKey() {
        return yearKey;
    }

    public void setYearKey(Integer yearKey) {
        this.yearKey = yearKey;
        DaoFilter filter = getFilter("yearKey", RELATION.EQUAL, null);
        if (ObjectUtils.nv(yearKey) != null) {
            if (filter == null) {
                // "yearKey", "monthKey","idCafeteriaCat"
                addFilter(new StaffExistsCafeteria(), "yearKey", RELATION.EQUAL, yearKey);
            } else {
                filter.setValues(yearKey);
            }
        } else {
            removeFilter(filter);
        }
    }

    public Integer getMonthKey() {
        return monthKey;
    }

    public void setMonthKey(Integer monthKey) {
        this.monthKey = monthKey;
        DaoFilter filter = getFilter("monthKey", RELATION.LIKE, null);
        if (ObjectUtils.nv(monthKey) != null) {
            if (filter == null) {
                // "yearKey", "monthKey","idCafeteriaCat"
                addFilter(new StaffExistsCafeteria(), "monthKey", RELATION.EQUAL, monthKey);
            } else {
                filter.setValues(monthKey);
            }
        } else {
            removeFilter(filter);
        }
    }

}