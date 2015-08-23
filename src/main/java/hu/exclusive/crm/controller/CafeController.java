package hu.exclusive.crm.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.service.spi.ServiceException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import hu.exclusive.crm.model.CafeFilter;
import hu.exclusive.crm.service.ParametersService;
import hu.exclusive.crm.service.StaffService;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.PCafeteriaLimit;
import hu.exclusive.dao.model.StaffCafeteria;
import hu.exclusive.utils.FacesAccessor;

//@Component
@ManagedBean(name = "cafeController")
@ViewScoped
public class CafeController extends Commontroller implements Serializable {

	private static final long serialVersionUID = 3826321633790448440L;

	private LazyDataModel<StaffCafeteria> staffModel;
	private CafeFilter cafeFilter;

	@Autowired
	transient StaffService service;

	@Autowired
	transient ParametersService parameters;

	private StaffCafeteria staff;

	public void init() {

		if (service == null)
			service = (StaffService) FacesAccessor.getManagedBean("staffService");

		if (parameters == null)
			parameters = (ParametersService) FacesAccessor.getManagedBean("parametersService");

		this.setStaffModel(new LazyDataModel<StaffCafeteria>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<StaffCafeteria> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				return pageDataTable(this, first, pageSize, sortField, sortOrder, getFilter(), filters);
			}

			@Override
			public List<StaffCafeteria> load(int first, int pageSize, List<SortMeta> multiSortMeta,
					Map<String, Object> filters) {
				return pageDataTable(this, first, pageSize, null, multiSortMeta, getFilter(), filters);
			}
		});

	}

	public LazyDataModel<StaffCafeteria> getStaffModel() {
		return staffModel;
	}

	public void setStaffModel(LazyDataModel<StaffCafeteria> staffModel) {
		this.staffModel = staffModel;
	}

	private List<StaffCafeteria> pageDataTable(LazyDataModel<StaffCafeteria> dataModel, int first, int pageSize,
			String sortField, Object sortOrderOrSortMeta, DaoFilter filter, Map<String, Object> filters) {

		List<StaffCafeteria> staffList = null;
		try {
			filter.setStartIndex(first);
			filter.setPageSize(pageSize);
			staffList = service.getCafeteriaList(getFilter());
			dataModel.setPageSize(pageSize);
			dataModel.setRowCount((int) filter.getTotalCount());

		} catch (ServiceException e) {
			showErrorHint(e);
		}

		return staffList;
	}

	/**
	 * Az aktuális szűrők alapján a filter összeállítása.
	 * 
	 * @return
	 */
	public CafeFilter getFilter() {
		if (cafeFilter == null) {
			cafeFilter = new CafeFilter();
		}
		return cafeFilter;
	}

	public void resetFilter() {
		System.out.println("resetfilter....");
		getFilter().reset();
		RequestContext.getCurrentInstance().reset("filterForm");
	}

	public List<PCafeteriaCategory> getCategories() {
		return parameters.getCafeteriaCategories(null);
	}

	public List<PCafeteriaLimit> getCafeLimits() {
		return parameters.getCafeteriaLimits(null);
	}

	public void setStaff(StaffCafeteria staff) {
		this.staff = staff;
		System.out.println("selectStaff " + staff);

	}

	public StaffCafeteria getStaff() {
		return this.staff;
	}

	public void saveCafeteria() {
		// ez igy elegge mellebeszeles, mert igazabol havi kategoruiak és
		// osszegek vannak mentve.
	}

	public void closeCafeteria() {
		Navigator navigator = (Navigator) FacesAccessor.getManagedBean("navigator");
		navigator.setContent("cafeteria/cafeList", "cafeteria/cafeListFilter");

		RequestContext.getCurrentInstance().update("mainContentPanel");
		RequestContext.getCurrentInstance().update("sliderContentPanel");
	}

	public void exportExcel() {

	}

	public void importExcel() {

	}
}
