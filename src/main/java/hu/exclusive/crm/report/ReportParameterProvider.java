package hu.exclusive.crm.report;

import hu.exclusive.dao.model.StaffDetail;
import hu.exclusive.dao.model.Workgroup;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A paraméterek megokosításához használt wrapper.
 * 
 * @author user
 *
 */
@Component
public class ReportParameterProvider {

    @Autowired
    hu.exclusive.crm.service.ParametersService parametersService;
    private Workgroup workgroup;
    private final static SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat MONTHNAMES = new SimpleDateFormat("MMMMM", new Locale("hu", "HU"));
    private final static SimpleDateFormat DAY = new SimpleDateFormat("DD");
    private final static String PLH = "..............";

    public String getValue(String barePlaceholder) {
        return "<<" + barePlaceholder + ">>";
    }

    private Workgroup getWorkgroup(Integer wgid) {
        if (workgroup == null) {
            workgroup = parametersService.getWorkgroup(wgid);
        }
        return workgroup;
    }

    public String getCompanyName(StaffDetail staff) {
        return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getCompanyName();
    }

    public String getCompanyAddress(StaffDetail staff) {
        return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getAddress();
    }

    public String getCompanyTax(StaffDetail staff) {
        return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getTaxNumber();
    }

    public String getCompanySerial(StaffDetail staff) {
        return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getCompanyNumber();
    }

    public String getCompanyRepresentative(StaffDetail staff) {
        return staff.getWorkgroup() == null ? PLH : staff.getWorkgroup().getRepresentative();
    }

    public String getStartYear(StaffDetail staff) {
        return staff.getEmployStart() == null ? PLH : YEAR.format(staff.getEmployStart());
    }

    public String getStartMonth(StaffDetail staff) {
        return staff.getEmployStart() == null ? PLH : MONTHNAMES.format(staff.getEmployStart());
    }

    public String getStartDay(StaffDetail staff) {
        return staff.getEmployStart() == null ? PLH : DAY.format(staff.getEmployStart());
    }

    public String getPlaces(StaffDetail staff) {
        return staff.getWorkplace() == null ? PLH : staff.getWorkplace().getWorkplaceName();
    }

    public String getSalaryNum(StaffDetail staff) {
        return staff.getBaseSalary() == null || staff.getBaseSalary().intValue() == 0 ? PLH : String.valueOf(staff
                .getBaseSalary().longValue());
    }

    public String getSalaryText(StaffDetail staff) {
        return staff.getBaseSalary() == null || staff.getBaseSalary().intValue() == 0 ? PLH : HungarianNumbers.convert(staff
                .getBaseSalary().longValue());
    }

    public String getEndYear(StaffDetail staff) {
        return staff.getEmployFinish() == null ? PLH : YEAR.format(staff.getEmployFinish());
    }

    public String getEndMonth(StaffDetail staff) {
        return staff.getEmployFinish() == null ? PLH : MONTHNAMES.format(staff.getEmployFinish());
    }

    public String getEndDay(StaffDetail staff) {
        return staff.getEmployFinish() == null ? PLH : DAY.format(staff.getEmployFinish());
    }

    public String getPartialJobType(StaffDetail staff) {
        return staff.getEmployType() == null ? PLH : staff.getEmployType();
    }

    public String getJobtitles(StaffDetail staff) {
        return staff.getJobtitles() == null || staff.getJobtitles().isEmpty() ? PLH : staff.getJobtitlesAsString();
    }

    public String getTrialPeriod(StaffDetail staff) {
        return staff.getTrialPeriod() == null || staff.getTrialPeriod().intValue() == 0 ? PLH : String.valueOf(staff
                .getTrialPeriod().intValue());
    }

}
