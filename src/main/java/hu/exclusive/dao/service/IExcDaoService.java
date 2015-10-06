package hu.exclusive.dao.service;

import java.util.List;

import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.model.Attachment;
import hu.exclusive.dao.model.Cafeteria;
import hu.exclusive.dao.model.CafeteriaInfo;
import hu.exclusive.dao.model.ContractDoc;
import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.DrDoc;
import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.PCafeteriaLimit;
import hu.exclusive.dao.model.PSystem;
import hu.exclusive.dao.model.Role;
import hu.exclusive.dao.model.Staff;
import hu.exclusive.dao.model.StaffCafeteria;
import hu.exclusive.dao.model.StaffDetail;
import hu.exclusive.dao.model.StaffNote;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;

public interface IExcDaoService {

	java.util.List<Staff> getStaffList(DaoFilter filter);

	List<Jobtitle> getJobtitles(DaoFilter filter);

	List<Workgroup> getWorkgroups(DaoFilter filter);

	List<Workplace> getWorkplaces(DaoFilter filter);

	List<Role> getRoles(DaoFilter daoFilter);

	List<Function> getFunctions(DaoFilter daoFilter);

	void saveFunction(Function f);

	void saveRole(Role r);

	CrmUser getUser(String login);

	List<CrmUser> getUsers(DaoFilter filter);

	void saveUser(CrmUser user);

	List<Attachment> getAttachments(DaoFilter daoFilter);

	List<DrDoc> getDrDocs(DaoFilter filter);

	List<ContractDoc> getContractDocs(DaoFilter filter);

	List<StaffNote> getStaffNotes(DaoFilter filter);

	StaffDetail getStaffDetail(Integer idStaff);

	void saveDocument(ContractDoc document);

	void saveDocument(DrDoc document);

	void saveDocument(Attachment document);

	void saveDocument(StaffNote document);

	void saveStaff(StaffDetail staff);

	String getSystemParameter(String sysgroup, String syskey);

	List<PSystem> getSystemParameters(String sysgroup, String syskey);

	void saveWorkroup(Workgroup group);

	void saveWorkplace(Workplace workplace);

	void deleteDoc(ContractDoc document);

	void deleteDoc(DrDoc document);

	void deleteAttachment(Attachment document);

	void deleteStaffNote(StaffNote note);

	List<StaffCafeteria> getCafeteriaList(DaoFilter filter);

	List<PCafeteriaCategory> getCafeteriaCategories(DaoFilter filter);

	List<PCafeteriaLimit> getCafeteriaLimits(DaoFilter filter);

	List<Staff> getStaffByName(String personName);

	List<StaffCafeteria> getStaffCafByName(String personName, String tax);

	List<PCafeteriaCategory> getCafeteriaCategoryByName(String catKey);

	void saveCafeteriaInfo(CafeteriaInfo info);

	void saveCafeteria(Cafeteria monthlyCafe);

	void deleteCafeteriaInfo(CafeteriaInfo info);

	void deleteCafeteria(Cafeteria monthlyCafe);

	void deleteCafeteria(int staff, int year, int month);

	void deleteCafeteria(int staff, int year, int month, int category);

}
