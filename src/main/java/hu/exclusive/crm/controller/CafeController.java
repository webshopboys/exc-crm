package hu.exclusive.crm.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.service.spi.ServiceException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import hu.exclusive.crm.model.CafeFilter;
import hu.exclusive.crm.model.CafeteriaExcel;
import hu.exclusive.crm.model.StaffFactory;
import hu.exclusive.crm.report.POIUtil;
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
	private List<String> unknownStaffs = new ArrayList<>();
	private List<CafeteriaExcel> validStaffs = new ArrayList<>();

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

	public void showImportDialog() {

		Map<String, Object> options = new HashMap<String, Object>();
		// options.put("height", 400);
		// options.put("width", 700);

		options.put("modal", true);
		options.put("closeOnEscape", true);
		options.put("draggable", false);
		options.put("resizable", true);
		options.put("dynamic", true);

		options.put("contentHeight", 700);
		options.put("contentWidth", 1100);

		RequestContext.getCurrentInstance().openDialog("cafeteria/cafeImportDialog", options, null);

	}

	public List<String> getUnknownStaffs() {
		return unknownStaffs;
	}

	public List<CafeteriaExcel> getValidStaffs() {
		return validStaffs;
	}

	public void checkExcel() {

		if (fileLoaded()) {

			try {

				unknownStaffs.clear();
				validStaffs.clear();
				byte[] poibytes = getUploadedFileBytes();
				java.io.ByteArrayInputStream in = new ByteArrayInputStream(poibytes);

				Workbook wb = WorkbookFactory.create(in);
				org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);

				for (int i = 2; i < sheet.getLastRowNum(); i++) {

					String xname = POIUtil.getRowCell(sheet, i, POIUtil.CELL_CAF_XLS_NAME);

					if (StringUtils.isNotEmpty(xname) && !"Összesen".equals(xname)) {

						BigDecimal xtaxnum = POIUtil.getRowCellNumber(sheet, i, POIUtil.CELL_CAF_XLS_TAX);
						String xtax = xtaxnum == null ? null : String.valueOf(xtaxnum.longValue());

						if (xtaxnum != null)
							System.out.println("excel " + xname + " xtaxnum:" + xtaxnum);

						List<StaffCafeteria> staffs = service.getStaffCafByName(xname, xtax);

						if (staffs.isEmpty()) {
							unknownStaffs.add(xname + " (nincs meg)");
						} else if (staffs.size() > 1) {
							unknownStaffs.add(xname + " (több is van)");
						} else {
							BigDecimal xlimit = POIUtil.getRowCellNumber(sheet, i, POIUtil.CELL_CAF_XLS_LIMIT);
							String xgroup = POIUtil.getRowCell(sheet, i, POIUtil.CELL_CAF_XLS_GROUP);

							CafeteriaExcel ce = StaffFactory.createCafeteriaExcel(staffs.get(0));
							ce.setYearlyLimit(xlimit);
							ce.setTaxSerial(xtax);
							ce.setWorkgroup(parameters.getWorkgroupByName(xgroup));

							ce.setStartDate(POIUtil.getRowCellDate(sheet, i, POIUtil.CELL_CAF_XLS_START));

							validStaffs.add(ce);
						}
					}
				}

			} catch (Exception e) {
				LOG.log(Level.WARNING, "Excel feldolgozási hiba", e);
				error("Excel feldolgozási hiba", null, e);
			}

		} else {
			error("Hiba", "Nincs kiválasztva a cafetéria excel!");
			throw new AbortProcessingException();
		}
	}

	public void importExcel() {

	}

	public void setFilePart(Part file) {
		this.filePart = file;
	}

	public Part getFilePart() {
		return filePart;
	}
}
