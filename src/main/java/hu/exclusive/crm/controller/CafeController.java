package hu.exclusive.crm.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.service.spi.ServiceException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import hu.exclusive.crm.businesslogic.CafeteriaRule;
import hu.exclusive.crm.model.CafeFilter;
import hu.exclusive.crm.model.CafeteriaExcel;
import hu.exclusive.crm.model.StaffFactory;
import hu.exclusive.crm.report.POIUtil;
import hu.exclusive.crm.service.CafeteriaService;
import hu.exclusive.crm.service.ParametersService;
import hu.exclusive.crm.service.StaffService;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.dao.model.CafeteriaInfo;
import hu.exclusive.dao.model.CafeteriaUtil;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.StaffCafeteria;
import hu.exclusive.utils.FacesAccessor;
import hu.exclusive.utils.ObjectUtils;

//@Component
@ManagedBean(name = "cafeController")
@ViewScoped
public class CafeController extends Commontroller implements Serializable {

	private static final long serialVersionUID = 3826321633790448440L;

	private LazyDataModel<StaffCafeteria> staffModel;
	private CafeFilter cafeFilter;

	@Autowired
	transient CafeteriaService cafeService;

	@Autowired
	transient StaffService staffServive;

	@Autowired
	transient ParametersService parameters;

	private StaffCafeteria staffBean;

	private List<CafeteriaExcel> validStaffs = new ArrayList<>();
	private List<String> problems = new ArrayList<>();
	private final CafeteriaUtil util = new CafeteriaUtil();
	private List<PCafeteriaCategory> categories;

	public void init() {

		// if (service == null)
		// service = (CafeteriaService)
		// FacesAccessor.getManagedBean("cafeteriaService");
		//
		// if (parameters == null)
		// parameters = (ParametersService)
		// FacesAccessor.getManagedBean("parametersService");

		this.staffModel = (new LazyDataModel<StaffCafeteria>() {
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
			staffList = cafeService.getCafeteriaList(getFilter());
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
		if (categories == null || categories.isEmpty())
			categories = cafeService.getCafeteriaCategories(null);
		return categories;
	}

	public void setStaff(StaffCafeteria staff) {
		this.staffBean = staff;
		System.err.println("selectStaff " + staff);

	}

	public StaffCafeteria getStaff() {
		return this.staffBean;
	}

	public void saveCategories() {
		System.err.println("saveCategories:: " + this.categories);
		cafeService.saveCafeteriaCategories(categories);
	}

	public void saveStaff() {
		// a munkatárs alapadatainak frissítése, amennyiben van rá joga
		staffServive.saveStaff(staffBean);
	}

	public void saveCafeteria() {
		// ez igy elegge mellebeszeles, mert igazabol havi kategoruiak és
		// osszegek vannak mentve.
		System.err.println("saveCafeteria:: " + this.staffBean);
		CafeteriaRule ruler = new CafeteriaRule();
		ruler.init(staffBean); // itt kell vajon munkaidőből valami?
		ruler.setCategories(getCategories());
		try {
			if (ruler.calculateRule(Calendar.getInstance().get(Calendar.YEAR), staffBean.getInfos(),
					staffBean.getMonthlyCafes())) {

				cafeService.saveCafeteriaInfo(ruler.getUtil().getCurrentInfo());
				cafeService.saveCafeterias(ruler.getUtil().getYearlyCafes(ruler.getUtil().getCurrentYear()));

				message("Cafetéria frissítése", ruler.infoMessage("Sikeres frissítés"));
			} else {
				error("Cafetéria hibák", ruler.errorMessage());
			}

		} catch (Exception e) {
			LOG.log(Level.WARNING, "Cafetéria módosítás hiba", e);
			error("Cafetéria módosítás hiba", null, e);
		}

	}

	public void closeCafeteria() {
		Navigator navigator = (Navigator) FacesAccessor.getManagedBean("navigator");
		navigator.setContent("cafeteria/cafeList", "cafeteria/cafeListFilter");

		RequestContext.getCurrentInstance().update("mainContentPanel");
		RequestContext.getCurrentInstance().update("sliderContentPanel");
	}

	public void closeCategories() {
		Navigator navigator = (Navigator) FacesAccessor.getManagedBean("navigator");
		navigator.goHome();

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

		RequestContext.getCurrentInstance().openDialog("/pages/cafeteria/cafeImportDialog", options, null);

	}

	final Lock lock = new ReentrantLock();

	public void showProcessLong() throws InterruptedException {
		lock.lock();
		try {
			Thread.sleep(5000);
		} finally {
			lock.unlock();
		}
		System.out.println("processed...");
	}

	public void processCafeteria(boolean withCreation, boolean withUpdate) {
		try {

			// egy infó ami az éves keretet menti el, felülírva a mostanit
			// StaffCafeteria.<CafeteriaInfo>[year]

			// minden hónaphoz az összeg
			// StaffCafeteria.<Cafeteria>[month]

			for (CafeteriaExcel excel : validStaffs) {
				if (excel.getYearlyLimit() != null && excel.getYearlyLimit().intValue() > 0) {

					CafeteriaInfo info = excel.getStaff().getYearlyInfo(excel.getYear());
					info.setYearLimit(excel.getYearlyLimit());
					info.setIdStaff(excel.getStaff().getIdStaff());
					info.setUpdater(getLoggedUser().getLoginName());
					cafeService.saveCafeteriaInfo(info);
				}

				// Itt egyszerre a régieket és az újakat is bejárjuk. Elsőnek a
				// régit töröljük
				for (Cafeteria cafeteria : excel.getStaff().getMonthlyCafes()) {
					// az aktualis evre
					if (excel.getYear() == cafeteria.getYearKey()) {
						// adott honap adott kategoriaja most...
						Cafeteria excelCafeteria = excel.getMonthlyCafeteria(cafeteria.getMonthKey(),
								cafeteria.getCafeCategory());
						// ha most is van akkor az alapadatokat atemeljuk
						if (excelCafeteria != null && !ObjectUtils.isEmpty(excelCafeteria.getAmount())) {

							excelCafeteria.setIdStaff(cafeteria.getIdStaff());
							excelCafeteria.setIdCafeteria(cafeteria.getIdCafeteria());

						} else { // most nincs vagy most üres, delete a honapra
									// az adott kategoria
							cafeService.deleteCafeteria(excel.getStaff().getIdStaff(), cafeteria.getYearKey(),
									cafeteria.getMonthKey(), cafeteria.getCafeCategory().getIdCafeteriaCat());

						}
					}
				}

				for (Cafeteria cafeteria : excel.getExcelMonthlyCafes()) {
					// vagy van id vagy nincs
					if (excel.getYear() == cafeteria.getYearKey() && !ObjectUtils.isEmpty(cafeteria.getAmount())) {

						cafeteria.setUpdater(getLoggedUser().getLoginName());
						cafeService.saveCafeteria(cafeteria);
					}
				}

			}

		} catch (Exception e) {
			LOG.log(Level.WARNING, "Cafetéria módosítás hiba", e);
			error("Cafetéria módosítás hiba", null, e);
		}

	}

	public List<String> getProblems() {
		return problems;
	}

	public List<String[]> getProblems2() {
		List<String[]> list2 = new ArrayList<>();
		for (int i = 0; i < problems.size(); i++) {
			String[] s = { "", "" };
			s[0] = problems.get(i);
			if (i + 1 < problems.size()) {
				i++;
				s[1] = problems.get(i);
			}

			list2.add(s);
		}
		return list2;
	}

	public List<String[]> getProblems3() {
		List<String[]> list3 = new ArrayList<>();
		for (int i = 0; i < problems.size(); i++) {
			String[] s = { "", "", "" };
			s[0] = problems.get(i);
			if (i + 1 < problems.size()) {
				i++;
				s[1] = problems.get(i);
			}
			if (i + 1 < problems.size()) {
				i++;
				s[2] = problems.get(i);
			}
			list3.add(s);
		}
		return list3;
	}

	public List<CafeteriaExcel> getValidStaffs() {
		return validStaffs;
	}

	/**
	 * Az excel bejárása és a munkatársak kikeresése. Szétválogatva hibás és
	 * valid listára. A validhoz az excel mezőkből az adatok átemelése.
	 */
	public void checkExcel() {

		if (fileLoaded()) {

			try {

				problems.clear();
				validStaffs.clear();
				byte[] poibytes = getUploadedFileBytes();
				java.io.ByteArrayInputStream in = new ByteArrayInputStream(poibytes);

				Workbook wb = WorkbookFactory.create(in);
				org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);

				List<String> wrongHeaders = checkExcelCategories(sheet);

				for (int i = 2; i < sheet.getLastRowNum(); i++) {

					String xname = POIUtil.getRowCell(sheet, i, POIUtil.CELL_CAF_XLS_NAME);

					if (StringUtils.isNotEmpty(xname) && !"Összesen".equals(xname)) {

						BigDecimal xtaxnum = POIUtil.getRowCellNumber(sheet, i, POIUtil.CELL_CAF_XLS_TAX);
						String xtax = xtaxnum == null ? null : String.valueOf(xtaxnum.longValue());

						List<StaffCafeteria> staffs = cafeService.getStaffCafByName(xname, xtax);

						if (staffs.isEmpty()) {
							problems.add(POIUtil.asCell(i, POIUtil.CELL_CAF_XLS_NAME) + " '" + xname
									+ "' nem taláható az adatbázisban.");
						} else if (staffs.size() > 1) {
							problems.add(POIUtil.asCell(i, POIUtil.CELL_CAF_XLS_NAME) + " '" + xname
									+ "' több ilyen nevű munkatárs is van.");
						} else {
							BigDecimal xlimit = POIUtil.getRowCellNumber(sheet, i, POIUtil.CELL_CAF_XLS_LIMIT);
							String xgroup = POIUtil.getRowCell(sheet, i, POIUtil.CELL_CAF_XLS_GROUP);

							CafeteriaExcel ce = StaffFactory.createCafeteriaExcel(staffs.get(0));
							ce.setYearlyLimit(xlimit);
							ce.setTaxSerial(xtax);
							ce.setWorkgroup(parameters.getWorkgroupByName(xgroup));

							ce.setStartDate(POIUtil.getRowCellDate(sheet, i, POIUtil.CELL_CAF_XLS_START));

							collectExcelCategories(sheet, ce, i, wrongHeaders);

							validStaffs.add(ce);
						}
					}
				}

				// System.out.println(wrongHeaders);

			} catch (java.lang.IllegalArgumentException iae) {
				if ("Your InputStream was neither an OLE2 stream, nor an OOXML stream".equals(iae.getMessage())) {
					LOG.log(Level.WARNING, "Excel feldolgozási hiba", iae);
					error("Excel feldolgozási hiba", "A fájl nem tűnik Excelnek.");
					problems.add("Excel feldolgozási hiba: A fájl nem tűnik Excelnek.");
				} else {
					LOG.log(Level.WARNING, "Excel feldolgozási hiba", iae);
					error("Excel feldolgozási hiba", null, iae);
					problems.add("Excel feldolgozási hiba " + iae);
				}
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Excel feldolgozási hiba", e);
				error("Excel feldolgozási hiba", null, e);
				problems.add("Excel feldolgozási hiba " + e);
			}

		} else {
			error("Hiba", "Nincs kiválasztva a cafetéria excel!");
			throw new AbortProcessingException();
		}

	}

	/**
	 * Az excel kategória oszlopait végigjárva a validStaff mezőibe beírja az
	 * összegeket.
	 * 
	 * @param sheet
	 */
	private void collectExcelCategories(Sheet sheet, CafeteriaExcel bean, int rowIndex, List<String> unprocHeaders) {

		String yearName = POIUtil.getRowCell(sheet, 0, 0);
		yearName = ObjectUtils.getNumbers(yearName);

		int year = yearName == null ? 0 : Integer.valueOf(yearName);
		bean.setYear(year);

		for (int c = POIUtil.CELL_CAF_XLS_CATFROM; c < sheet.getRow(rowIndex).getLastCellNum(); c++) {

			if (!unprocHeaders.contains(POIUtil.asCell(c))) {

				String catHeader = POIUtil.getRowCell(sheet, 1, c);
				int monthValue = Integer.valueOf(ObjectUtils.getNumbers(catHeader));
				String catKey = ObjectUtils.getAlphas(catHeader, "").trim();

				List<PCafeteriaCategory> cat = parameters.getCafeteriaCategoryByName(catKey);

				if (cat.size() == 1) {
					BigDecimal amount = POIUtil.getRowCellNumber(sheet, rowIndex, c);
					if (amount != null) {
						// System.out.println(rowIndex + "" + POIUtil.asCell(c)
						// + " ebben a hónapban " + monthValue
						// + " ehhez a kategóriához " + catKey + " " + amount +
						// " Ft...");
						bean.getSetMonthlyCafeteria(monthValue, cat.get(0)).setYearKey(year);
						bean.getSetMonthlyCafeteria(monthValue, cat.get(0)).setAmount(amount);
					} else {
						// TODO nothing: nincs összeg a hónaphoz
					}

				} else {
					throw new IllegalStateException("Hibás feldolgozási pont! " + POIUtil.asCell(rowIndex, c));
				}
			}

		}

	}

	private List<String> checkExcelCategories(Sheet sheet) {

		String yearName = POIUtil.getRowCell(sheet, 0, 0);
		yearName = ObjectUtils.getNumbers(yearName);
		List<String> unprocessedHeaders = new ArrayList<>();
		if (StringUtils.isEmpty(yearName)) {
			problems.add(POIUtil.asCell(0, 0) + " oszlopban nincs benne az év.");
			unprocessedHeaders.add(POIUtil.asCell(0));
		}

		int lastProcessedCol = sheet.getRow(1).getLastCellNum() - 1;

		for (int c = POIUtil.CELL_CAF_XLS_CATFROM; c < sheet.getRow(1).getLastCellNum(); c++) {

			String catHeader = POIUtil.getRowCell(sheet, 1, c);
			String monthValue = ObjectUtils.getNumbers(catHeader);
			String catKey = ObjectUtils.getAlphas(catHeader, "").trim();

			if (catKey.equals("Összesen cafeteria")) {
				// ez és innen jobbra nem kell mar
				lastProcessedCol = c - 1;
			}
			if (c < lastProcessedCol) {
				if (StringUtils.isNotEmpty(monthValue) && StringUtils.isNotEmpty(catKey)) {
					List<PCafeteriaCategory> cat = parameters.getCafeteriaCategoryByName(catKey);
					if (cat.size() == 1) {
						// valid
					} else {
						problems.add(POIUtil.asCell(1, c) + " Nem található a '" + catKey + "' kategória (" + cat.size()
								+ " van).");
						unprocessedHeaders.add(POIUtil.asCell(c));
					}

				} else {
					problems.add(POIUtil.asCell(1, c) + " Nics beírva hónap és kategória (" + catHeader + ").");
					unprocessedHeaders.add(POIUtil.asCell(c));
				}
			} else {
				unprocessedHeaders.add(POIUtil.asCell(c));
			}
		}

		return unprocessedHeaders;
	}

}