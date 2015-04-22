package hu.exclusive.crm.model;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.StaffExistsJobtitles;
import hu.exclusive.dao.StaffExistsWorkgroup;
import hu.exclusive.utils.ObjectUtils;

import java.util.Arrays;
import java.util.Date;

public class DocFilter extends DaoFilter {

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

    private Date drExpired;
    private Date contractExpired;

    private String[] includesDocTypes;
    private String[] excludedDocTypes;

    private String documentContent;

    private String privateNote;

    private String[] status;

    @Override
    public void reset() {

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
        privateNote = null;
        setContractExpired(null);
        documentContent = null;
        setDrExpired(null);
        excludedDocTypes = null;
        includesDocTypes = null;
        super.reset();

    }

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

            // System.out.println(filter + " filter dates:" + Arrays.toString(dates));

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

        // System.out.println("setJobtitles " + ObjectUtils.toString(jobtitles));

        this.jobtitles = jobtitles;
        DaoFilter filter = getFilter("idJobtitle", RELATION.IN, null);
        if (ObjectUtils.nv(jobtitles) != null && jobtitles.length > 0) {
            if (filter == null) {
                addFilter(new StaffExistsJobtitles(), "idJobtitle", RELATION.IN, jobtitles);
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

        // System.err.println("setWorkgroups " + ObjectUtils.toString(workgroups));

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

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
        System.out.println("filter statuses:" + Arrays.toString(status));

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

    public String[] getExcludedDocTypes() {
        return this.excludedDocTypes;
    }

    public void setExcludedDocTypes(String[] excludedDocTypes) {
        this.excludedDocTypes = excludedDocTypes;

        // DaoFilter filter = getFilter("status", RELATION.IN, status);
        // if (status != null && status.length > 0) {
        // if (filter == null) {
        // addFilter("status", RELATION.IN, status);
        // } else {
        // filter.setValues(status);
        // }
        // } else {
        // removeFilter(filter);
        // }

    }

    public String[] getIncludesDocTypes() {
        return this.includesDocTypes;
    }

    public void setIncludesDocTypes(String[] includesDocTypes) {
        this.includesDocTypes = includesDocTypes;

        // DaoFilter filter = getFilter("status", RELATION.IN, status);
        // if (status != null && status.length > 0) {
        // if (filter == null) {
        // addFilter("status", RELATION.IN, status);
        // } else {
        // filter.setValues(status);
        // }
        // } else {
        // removeFilter(filter);
        // }

    }

    public String getDocumentContent() {
        return this.documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
        // DaoFilter filter = getFilter("alarmCode", RELATION.LIKE, alarmCode);
        // if (ObjectUtils.nv(alarmCode) != null) {
        // if (filter == null) {
        // addFilter("Alarm", "alarmCode", RELATION.LIKE, alarmCode);
        // } else {
        // filter.setValues(alarmCode);
        // }
        // } else {
        // removeFilter(filter);
        // }
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

    public Date getDrExpired() {
        return drExpired;
    }

    public void setDrExpired(Date drExpired) {
        this.drExpired = drExpired;
    }

    public Date getContractExpired() {
        return contractExpired;
    }

    public void setContractExpired(Date contractExpired) {
        this.contractExpired = contractExpired;
    }

}
