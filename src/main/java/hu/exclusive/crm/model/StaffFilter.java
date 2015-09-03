package hu.exclusive.crm.model;

import java.util.Date;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.StaffExistsJobtitle;
import hu.exclusive.dao.StaffExistsWorkgroup;
import hu.exclusive.utils.ObjectUtils;

public class StaffFilter extends DaoFilter {

	private String fullName;
	private String birthName;
	private Date birthDateFrom;
	private Date birthDateTo;
	private String birthPlace;
	private String homeAddress;
	private String motherName;
	private String residentAddress;

	private Integer[] jobtitles;
	private Integer[] workgroups;
	private Integer[] workplaces;

	private Date employStartFrom;
	private Date employStartTo;
	private Date employFinishFrom;
	private Date employFinishTo;
	private Integer[] employTypes;
	private String salaryType;
	private String sidSerial;
	private String[] status;
	private String tajSerial;
	private String taxSerial;
	private boolean trialPeriod; // Probaidőn van-e? Ha igen, akkor a
									// trialPeriod napjaival növelt employStart
									// még nem múlt el.

	private String alarmCode; // ha van hozzá riasztó rendelve akkor ez a kódja
	private boolean hasAlarm; // Van hozzá riasztó rendelve.

	private String privateNote;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {

		// System.out.println("filter fullName:" + fullName);
		this.fullName = fullName;
		DaoFilter filter = getFilter("fullName", RELATION.LIKE, fullName);
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

	public String getBirthName() {
		return birthName;
	}

	public void setBirthName(String birthName) {
		this.birthName = birthName;
		DaoFilter filter = getFilter("birthName", RELATION.LIKE, birthName);
		if (ObjectUtils.nv(birthName) != null) {
			if (filter == null) {
				addFilter("birthName", RELATION.LIKE, birthName);
			} else {
				filter.setValues(birthName);
			}
		} else {
			removeFilter(filter);
		}
	}

	public Date getBirthDateFrom() {
		return birthDateFrom;
	}

	public void setBirthDateFrom(Date birthDate) {
		// System.out.println("stafffilter birthDateFrom:" + birthDate);
		this.birthDateFrom = birthDate;
		DaoFilter filter = getFilter("birthDate", RELATION.BETWEEN, null);
		if (birthDateFrom != null) {

			Date[] dates = { birthDateFrom, MAXDATE };
			if (birthDateTo != null)
				dates[1] = birthDateTo;

			// System.out.println(filter + " filter dates:" +
			// Arrays.toString(dates));

			if (filter == null) {
				addFilter("birthDate", RELATION.BETWEEN, dates);
			} else {
				filter.setValues(dates);
			}

		} else if (birthDateTo == null) {
			// ha mindkettő üres, akkor lehet törölni a filtert
			removeFilter(filter);
		}
	}

	public Date getBirthDateTo() {
		return birthDateTo;
	}

	public void setBirthDateTo(Date birthDate) {
		// System.out.println("stafffilter birthDateTo:" + birthDate);
		this.birthDateTo = birthDate;
		DaoFilter filter = getFilter("birthDate", RELATION.BETWEEN, null);
		if (birthDateTo != null) {

			Date[] dates = { MINDATE, birthDateTo };
			if (birthDateFrom != null)
				dates[0] = birthDateFrom;

			// System.out.println("filter dates:" + Arrays.toString(dates));

			if (filter == null) {
				addFilter("birthDate", RELATION.BETWEEN, dates);
			} else {
				filter.setValues(dates);
			}
		} else if (birthDateFrom == null) {
			// ha mindkettő üres, akkor lehet törölni a filtert
			removeFilter(filter);
		}
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
		DaoFilter filter = getFilter("birthPlace", RELATION.LIKE, birthPlace);
		if (ObjectUtils.nv(birthPlace) != null) {
			if (filter == null) {
				addFilter("birthPlace", RELATION.LIKE, birthPlace);
			} else {
				filter.setValues(birthPlace);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
		DaoFilter filter = getFilter("homeAddress", RELATION.LIKE, homeAddress);
		if (ObjectUtils.nv(homeAddress) != null) {
			if (filter == null) {
				addFilter("homeAddress", RELATION.LIKE, homeAddress);
			} else {
				filter.setValues(homeAddress);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
		DaoFilter filter = getFilter("motherName", RELATION.LIKE, motherName);
		if (ObjectUtils.nv(motherName) != null) {
			if (filter == null) {
				addFilter("motherName", RELATION.LIKE, motherName);
			} else {
				filter.setValues(motherName);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getResidentAddress() {
		return residentAddress;
	}

	public void setResidentAddress(String residentAddress) {
		this.residentAddress = residentAddress;
		DaoFilter filter = getFilter("residentAddress", RELATION.LIKE, residentAddress);
		if (ObjectUtils.nv(residentAddress) != null) {
			if (filter == null) {
				addFilter("residentAddress", RELATION.LIKE, residentAddress);
			} else {
				filter.setValues(residentAddress);
			}
		} else {
			removeFilter(filter);
		}
	}

	public Integer[] getJobtitles() {
		return jobtitles;
	}

	public void setJobtitles(Integer[] jobtitles) {

		// System.out.println("setJobtitles " +
		// ObjectUtils.toString(jobtitles));

		this.jobtitles = jobtitles;
		DaoFilter filter = getFilter("idJobtitle", RELATION.IN, null);
		if (ObjectUtils.nv(jobtitles) != null && jobtitles.length > 0) {
			if (filter == null) {
				addFilter(new StaffExistsJobtitle(), "idJobtitle", RELATION.IN, jobtitles);
			} else {
				filter.setValues(jobtitles);
			}
		} else {
			removeFilter(filter);
		}
	}

	public Integer[] getWorkgroups() {
		return workgroups;
	}

	public void setWorkgroups(Integer[] workgroups) {

		// System.err.println("setWorkgroups " +
		// ObjectUtils.toString(workgroups));

		this.workgroups = workgroups;
		DaoFilter filter = getFilter("idWorkgroup", RELATION.IN, null);
		if (ObjectUtils.nv(workgroups) != null && workgroups.length > 0) {
			if (filter == null) {
				addFilter(new StaffExistsWorkgroup(), "idWorkgroup", RELATION.IN, workgroups);
			} else {
				filter.setValues(workgroups);
			}
		} else {
			removeFilter(filter);
		}
	}

	public Integer[] getWorkplaces() {
		return workplaces;
	}

	public void setWorkplaces(Integer[] workplaces) {
		this.workplaces = workplaces;
		DaoFilter filter = getFilter("idWorkplace", RELATION.IN, null);
		if (ObjectUtils.nv(workplaces) != null && workplaces.length > 0) {
			if (filter == null) {
				addFilter("workplace", "idWorkplace", RELATION.IN, workplaces);
			} else {
				filter.setValues(workplaces);
			}
		} else {
			removeFilter(filter);
		}
	}

	public Date getEmployStartFrom() {
		return employStartFrom;
	}

	public void setEmployStartFrom(Date employStart) {
		this.employStartFrom = employStart;

	}

	public Date getEmployStartTo() {
		return employStartTo;
	}

	public void setEmployStartTo(Date employStart) {
		this.employStartTo = employStart;

	}

	public Date getEmployFinishFrom() {
		return employFinishFrom;
	}

	public void setEmployFinishFrom(Date employFinish) {
		this.employFinishFrom = employFinish;

	}

	public Date getEmployFinishTo() {
		return employFinishTo;
	}

	public void setEmployFinishTo(Date employFinish) {
		this.employFinishTo = employFinish;

	}

	public Integer[] getEmployTypes() {
		return employTypes;
	}

	public void setEmployTypes(Integer[] employTypes) {
		this.employTypes = employTypes;
	}

	public String getSalaryType() {
		return salaryType;
	}

	public void setSalaryType(String salaryType) {
		this.salaryType = salaryType;
		DaoFilter filter = getFilter("salaryType", RELATION.EQUAL, salaryType);
		if (ObjectUtils.nv(salaryType) != null) {
			if (filter == null) {
				addFilter("salaryType", RELATION.EQUAL, salaryType);
			} else {
				filter.setValues(salaryType);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getSidSerial() {
		return sidSerial;
	}

	public void setSidSerial(String sidSerial) {
		this.sidSerial = sidSerial;
		DaoFilter filter = getFilter("sidSerial", RELATION.LIKE, sidSerial);
		if (ObjectUtils.nv(sidSerial) != null) {
			if (filter == null) {
				addFilter("sidSerial", RELATION.LIKE, sidSerial);
			} else {
				filter.setValues(sidSerial);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;

		DaoFilter filter = getFilter("status", RELATION.IN, status);
		if (status != null && status.length > 0) {
			if (filter == null) {
				addFilter("status", RELATION.IN, status);
			} else {
				filter.setValues(status);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getTajSerial() {
		return tajSerial;
	}

	public void setTajSerial(String tajSerial) {
		this.tajSerial = tajSerial;
		DaoFilter filter = getFilter("tajSerial", RELATION.LIKE, tajSerial);
		if (ObjectUtils.nv(tajSerial) != null) {
			if (filter == null) {
				addFilter("tajSerial", RELATION.LIKE, tajSerial);
			} else {
				filter.setValues(tajSerial);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getTaxSerial() {
		return taxSerial;
	}

	public void setTaxSerial(String taxSerial) {
		this.taxSerial = taxSerial;
		DaoFilter filter = getFilter("taxSerial", RELATION.LIKE, taxSerial);
		if (ObjectUtils.nv(taxSerial) != null) {
			if (filter == null) {
				addFilter("taxSerial", RELATION.LIKE, taxSerial);
			} else {
				filter.setValues(taxSerial);
			}
		} else {
			removeFilter(filter);
		}
	}

	public boolean isTrialPeriod() {
		return trialPeriod;
	}

	public void setTrialPeriod(boolean trialPeriod) {
		this.trialPeriod = trialPeriod;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
		DaoFilter filter = getFilter("alarmCode", RELATION.LIKE, alarmCode);
		if (ObjectUtils.nv(alarmCode) != null) {
			if (filter == null) {
				addFilter("Alarm", "alarmCode", RELATION.LIKE, alarmCode);
			} else {
				filter.setValues(alarmCode);
			}
		} else {
			removeFilter(filter);
		}
	}

	public boolean isHasAlarm() {
		return hasAlarm;
	}

	public void setHasAlarm(boolean hasAlarm) {
		this.hasAlarm = hasAlarm;
		DaoFilter filter = getFilter("idAlarm", RELATION.NOTNULL, null);
		if (hasAlarm) {
			if (filter == null) {
				addFilter("alarm", "idAlarm", RELATION.NOTNULL, null);
			}
		} else {
			removeFilter(filter);
		}

	}

	public String getPrivateNote() {
		return privateNote;
	}

	public void setPrivateNote(String privateNote) {
		this.privateNote = privateNote;
		DaoFilter filter = getFilter("privateNote", RELATION.LIKE, privateNote);
		if (ObjectUtils.nv(privateNote) != null) {
			if (filter == null) {
				addFilter("privateNote", RELATION.LIKE, privateNote);
			} else {
				filter.setValues(privateNote);
			}
		} else {
			removeFilter(filter);
		}
	}

	public String getActualFiltersInfo() {
		StringBuilder sb = new StringBuilder();
		for (DaoFilter filter : super.filterItems) {
			sb.append(getFilterInfo(filter)).append("<br/>");
		}
		return sb.toString();
		// return "szűrők.....";
	}

	private String getFilterInfo(DaoFilter filter) {
		if (filter != null) {
			String filterInfo = filter.getInfo();
			return filterInfo.replace("{fullName}", "'Munkatárs neve'").replace("{birthName}", "'Születési neve'")
					.replace("{birthDate}", "'Születési dátum'").replace("{birthPlace}", "'Születési hely'")
					.replace("{motherName}", "'Anyja neve'").replace("{homeAddress}", "'Állandó lakhely'")
					.replace("{residentAddress}", "'Tartózkodási hely'").replace("{idJobtitle}", "'Munkaköre'")
					.replace("{idWorkgroup}", "'Csoportja'").replace("{idWorkplace}", "'Munkahelye'")
					.replace("{alarmCode}", "'Hozzárendelt riasztókód'").replace("{hasAlarm}", "'Van hozzá riasztókód'")
					.replace("{privateNote}", "'Megjegyzés'");

		}
		return "";
	}

	@Override
	public void reset() {

		System.err.println("reset 1");

		fullName = null;
		birthName = null;
		birthDateFrom = null;
		birthDateTo = null;
		birthPlace = null;
		homeAddress = null;
		motherName = null;
		residentAddress = null;
		jobtitles = null;
		workgroups = null;
		workplaces = null;
		employStartFrom = null;
		employStartTo = null;
		employFinishFrom = null;
		employFinishTo = null;
		employTypes = null;
		salaryType = null;
		sidSerial = null;
		status = null;
		tajSerial = null;
		taxSerial = null;
		trialPeriod = false;
		alarmCode = null;
		hasAlarm = false;
		privateNote = null;
		super.reset();

	}

}
