package hu.exclusive.crm.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;

import hu.exclusive.crm.model.RoleBean;
import hu.exclusive.crm.service.ParametersService;
import hu.exclusive.dao.ServiceException;
import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Role;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;
import hu.exclusive.utils.FacesAccessor;

//@Component
@ManagedBean(name = "sysController")
@ViewScoped
public class SystemController extends Commontroller implements Serializable {

	private static final long serialVersionUID = -567350278676848517L;

	@Autowired
	transient ParametersService service;

	private String functionCode;
	private String functionName;
	private String roleComment;
	private String roleName;
	private List<RoleBean> adminRolesBB = null;
	private List<Function> adminFunctionsBB = null;
	private List<CrmUser> adminUsersBB = null;
	private CrmUser selectedUser;
	private CrmUser newUser;
	private String originalPass;
	private Workgroup group;
	private Workplace workplace;

	public void init() {
		if (service == null)
			service = (ParametersService) FacesAccessor.getManagedBean("parametersService");
		// System.err.println("ParametersService " + service);
	}

	public CrmUser getSelectedUser() {
		// System.err.println("getSelectedUser " + selectedUser);
		// if (selectedUser == null) {
		// selectedUser = new CrmUser();
		// prepareUserSelectedRoles(selectedUser, getAdminRoles());
		// }
		return selectedUser;
	}

	public void setSelectedUser(CrmUser selectedUser) {
		System.err.println("setSelectedUser " + selectedUser);
		this.selectedUser = selectedUser;
		originalPass = selectedUser.getCrmPass();
		prepareUserSelectedRoles(selectedUser, getAdminRoles());
	}

	/**
	 * A user szerepekhez minden elerheto szerepet hozzaadunk.
	 * 
	 * @param user
	 * @param roles
	 */
	private void prepareUserSelectedRoles(CrmUser user, List<RoleBean> roles) {
		if (user == null)
			return;
		List<Role> userRoles = user.getRoles() == null ? new ArrayList<Role>() : user.getRoles();
		List<Role> allRoles = new ArrayList<Role>();
		for (RoleBean adminRole : roles) {
			Role userRole = new Role();
			userRole.setIdRole(adminRole.getIdRole());
			userRole.setRoleName(adminRole.getRoleName());
			userRole.setRoleComment(adminRole.getRoleComment());
			allRoles.add(userRole);

			if (RoleBean.getRoleFromList(userRoles, adminRole.getIdRole()) != null) {
				// benne van, selected lesz
				userRole.setSelected(true);
			} else {
				userRole.setSelected(false);
			}
		}
		user.setRoles(allRoles);
	}

	public CrmUser getNewUser() {
		if (newUser == null) {
			newUser = new CrmUser();
			prepareUserSelectedRoles(newUser, getAdminRoles());
		}
		return newUser;
	}

	public void updateUser() {
		CrmUser user = getSelectedUser();
		try {

			System.err.println("updateUser " + user);

			if (isEmpty(user.getUserName()) || isEmpty(user.getLoginName())) {
				error("Hiányos adatok", "A teljes név és login név is kötelező, és nem lehet már létező!", "userForm");
				return;
			}

			if (isEmpty(user.getCrmPass()) && isEmpty(user.getHintPass())) {
				// a jelszot cserélik csak a többi adatot
				if (isEmpty(originalPass)) {
					error("Rendszerhiba!", "Az eredeti jelszó kezelése hibás!", "userForm");
					return;
				}
				user.setCrmPass(originalPass);

			} else if (isEmpty(user.getCrmPass()) || !isEmpty(user.getHintPass())
					|| !user.getCrmPass().equals(user.getHintPass())) {
				error("Hiányos adatok", "A jelszavak hiányosak, vagy nem egyeznek meg!", "userForm");
				return;
			}
			user.setRoles(RoleBean.getSelectedSuperRoles(user.getRoles()));

			service.saveUser(user);

			message("Feldolgozási eredmény", "Sikeresen módosítva.");

			resetUserForms();

		} catch (Exception e) {
			e.printStackTrace();
			error("Mentési hiba", null, new ServiceException(e));
			prepareUserSelectedRoles(user, getAdminRoles());
		}

	}

	public void deleteUser() {
		message("Feldolgozási eredmény", "Csak a fejlesztő törölheti");
		resetUserForms();

	}

	public void createUser() {

		CrmUser user = getNewUser();

		try {
			System.err.println("createUser " + user);

			if (isEmpty(user.getUserName()) || isEmpty(user.getLoginName())) {
				error("Hiányos adatok", "A teljes név és login név is kötelező, és nem lehet már létező!", "userForm");
				return;
			}
			if (isEmpty(user.getCrmPass()) || isEmpty(user.getHintPass())
					|| !user.getCrmPass().equals(user.getHintPass())) {
				error("Hiányos adatok", "A jelszavak hiányosak, vagy nem egyeznek meg!", "userForm");
				return;
			}
			user.setRoles(RoleBean.getSelectedSuperRoles(user.getRoles()));

			service.saveUser(user);

			message("Feldolgozási eredmény", "Sikeresen létrehozva.");

			resetUserForms();

		} catch (Exception e) {
			e.printStackTrace();
			error("Mentési hiba", null, new ServiceException(e));
			prepareUserSelectedRoles(user, getAdminRoles());
		}

	}

	public void onUserRowSelect(SelectEvent event) {
	}

	public List<CrmUser> getUsers() {
		if (adminUsersBB == null)
			adminUsersBB = service.getUsers(null);
		return adminUsersBB;
	}

	private void resetUserForms() {
		adminUsersBB = null;
		selectedUser = null;
		newUser = null;

		RequestContext.getCurrentInstance().reset("userForm");
		RequestContext.getCurrentInstance().reset("usersForm");
		RequestContext.getCurrentInstance().update("usersForm");
		RequestContext.getCurrentInstance().update("userForm");
	}

	public Role getNewUserRole(int roleId) {
		Role roleBean = RoleBean.getRoleFromList(newUser.getRoles(), roleId);
		return roleBean;
	}

	public Role getSelectedUserRole(int roleId) {
		Role roleBean = RoleBean.getRoleFromList(selectedUser.getRoles(), roleId);
		return roleBean;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public List<RoleBean> getAdminRoles() {
		if (adminRolesBB == null) {
			adminRolesBB = new ArrayList<RoleBean>();
			List<Function> allFunction = service.getFunctions(null);
			for (Role role : service.getRoles(null)) {
				RoleBean rb = new RoleBean(role);
				rb.setAllFunction(allFunction);
				adminRolesBB.add(rb);
			}
		}
		return adminRolesBB;
	}

	public List<Function> getRoleFunctions(int roleId) {
		return getAdminRole(roleId).getPossibleFunctions();
	}

	public RoleBean getAdminRole(int roleId) {
		Role roleBean = RoleBean.getRoleFromList(getAdminRoles(), roleId);
		return roleBean == null ? new RoleBean() : (RoleBean) roleBean;
	}

	public List<Function> getAdminFunctions() {
		if (adminFunctionsBB == null)
			adminFunctionsBB = service.getFunctions(null);
		return adminFunctionsBB;
	}

	public void addFunction() {
		try {
			if (!isEmpty(functionCode) && !isEmpty(functionName)) {
				Function f = new Function();
				f.setFunctionCode(functionCode);
				f.setFunctionName(functionName);
				service.saveFunction(f);
				message("Feldolgozási eredmény", "Sikeresen létrehozva.");

				reset();
			} else {
				error("Hiányos adatok", "A funkció kód és név is kötelező, és nem lehet már létező!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			error("Mentési hiba", null, new ServiceException(e));
		}

	}

	public void updateFunction(int idFunction) {

		try {
			Function f = getFunction(idFunction);

			service.saveFunction(f);
			message("Feldolgozási eredmény", "Sikeresen módosítva.");

			reset();

		} catch (Exception e) {
			e.printStackTrace();
			error("Mentési hiba", null, new ServiceException(e));
		}
	}

	private Function getFunction(int idFunction) {
		if (adminFunctionsBB != null) {
			for (Function function : adminFunctionsBB) {
				if (function.getIdFunction() == idFunction)
					return function;
			}
		}
		return new Function();
	}

	public void deleteFunction(int idFunction) {
		message("Feldolgozási eredmény", "Csak a fejlesztő törölheti");

		reset();
	}

	public void addRole() {
		try {

			if (!isEmpty(roleName) && !isEmpty(roleComment)) {

				Role r = new Role();
				r.setRoleName(roleName);
				r.setRoleComment(roleComment);
				service.saveRole(r);
				message("Feldolgozási eredmény", "Sikeresen létrehozva.");

				reset();
			} else {
				error("Hiányos adatok", "A szerep név és leírás is kötelező, és nem lehet már létező!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			error("Mentési hiba", null, new ServiceException(e));
		}
	}

	public void updateRole(int idRole) {
		try {

			RoleBean r = getAdminRole(idRole);

			System.err.println("update role " + r.getRoleName());

			Role sr = new Role();
			sr.setIdRole(r.getIdRole());
			sr.setRoleName(r.getRoleName());
			sr.setRoleComment(r.getRoleComment());
			sr.setDeleted(r.getDeleted());
			sr.setFunctions(r.getSelectedFunctions());

			service.saveRole(sr);

			message("Feldolgozási eredmény", "Sikeresen módosítva.");
			reset();

		} catch (Exception e) {
			e.printStackTrace();
			error("Mentési hiba", null, new ServiceException(e));
		}
	}

	public void deleteRole(int idRole) {
		message("Feldolgozási eredmény", "Csak a fejlesztő törölheti");

		reset();
	}

	public void reset() {
		adminRolesBB = null;
		adminFunctionsBB = null;
		functionCode = null;
		functionName = null;
		roleComment = null;
		roleName = null;
		group = null;
		workplace = null;
	}

	public String getRoleComment() {
		return roleComment;
	}

	public void setRoleComment(String roleComment) {
		this.roleComment = roleComment;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOriginalPass() {
		return originalPass;
	}

	public void setOriginalPass(String originalPass) {
		this.originalPass = originalPass;
	}

	public Workgroup getGroup() {
		if (group == null)
			group = new Workgroup();
		return group;
	}

	public void setGroup(Workgroup group) {
		this.group = group;
	}

	public void addGroup() {
		try {

			if (!isEmpty(getGroup().getCompanyName()) && !isEmpty(getGroup().getGroupName())) {

				getGroup().setIdWorkgroup(null);
				service.saveWorkroup(getGroup());
				message("Feldolgozási eredmény", "Sikeresen létrehozva.");

				reset();
			} else {
				error("Hiányos adatok",
						"A cégcsoport és munkacsoport nevének kitöltése kötelező, és nem lehet már létező!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			error("Létrehozási hiba", null, new ServiceException(e));
		}
	}

	public void saveGroup() {
		try {
			System.err.println("saveGroup " + getGroup());
			if (!isEmpty(getGroup().getCompanyName()) && !isEmpty(getGroup().getGroupName())
					&& getGroup().getIdWorkgroup() != null) {

				service.saveWorkroup(getGroup());

				message("Feldolgozási eredmény", "Sikeresen módosítva.");

				reset();
			} else {
				error("Hiányos adatok",
						"A cégcsoport és munkacsoport nevének kitöltése kötelező! A módosítandó csoport talán nincs kiválasztva?");
			}

		} catch (Exception e) {
			e.printStackTrace();
			error("Módosítási hiba", null, new ServiceException(e));
		}
	}

	public Workplace getWorkplace() {
		if (workplace == null)
			workplace = new Workplace();
		return workplace;
	}

	public void setWorkplace(Workplace workplace) {
		this.workplace = workplace;
	}

	public void addWorkplace() {
		try {

			if (!isEmpty(getWorkplace().getWorkplaceName()) && getWorkplace().getIdWorkgroup() != null
					&& !isEmpty(getWorkplace().getOpenFriday()) && !isEmpty(getWorkplace().getOpenMonday())
					// && !isEmpty(getWorkplace().getOpenSaturday()) &&
					// !isEmpty(getWorkplace().getOpenSunday())
					&& !isEmpty(getWorkplace().getOpenThursday()) && !isEmpty(getWorkplace().getOpenTuesday())
					&& !isEmpty(getWorkplace().getOpenWednesday())) {

				getWorkplace().setIdWorkplace(null);
				service.saveWorkplace(getWorkplace());

				message("Feldolgozási eredmény", "Sikeresen létrehozva.");

				reset();
			} else {
				error("Hiányos adatok",
						"A munkahely nevének és a munkanapok nyitvatartási idejének kitöltése kötelező! A munkahely csoportjának kiválasztása kötelező!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			error("Létrehozási hiba", null, new ServiceException(e));
		}
	}

	public void saveWorkplace() {
		try {

			System.err.println("saveWorkplace " + getWorkplace());
			if (!isEmpty(getWorkplace().getWorkplaceName()) && getWorkplace().getWorkgroup() != null
					&& getWorkplace().getIdWorkplace() != null && !isEmpty(getWorkplace().getOpenFriday())
					&& !isEmpty(getWorkplace().getOpenMonday())
					// && !isEmpty(getWorkplace().getOpenSaturday()) &&
					// !isEmpty(getWorkplace().getOpenSunday())
					&& !isEmpty(getWorkplace().getOpenThursday()) && !isEmpty(getWorkplace().getOpenTuesday())
					&& !isEmpty(getWorkplace().getOpenWednesday())) {

				message("Feldolgozási eredmény", "Sikeresen módosítva.");

				service.saveWorkplace(getWorkplace());

				reset();
			} else {
				error("Hiányos adatok",
						"A munkahely nevének és a munkanapok nyitvatartási idejének kitöltése kötelező! A munkahely csoportjának kiválasztása kötelező! A módosítandó munkahely talán nincs kiválasztva?");
			}

		} catch (Exception e) {
			e.printStackTrace();
			error("Módosítási hiba", null, new ServiceException(e));
		}
	}
}
