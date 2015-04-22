package hu.exclusive.crm.model;

import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * A rendszer szerepkörének adminisztrálása. Visszaadhat teljes és aktuális kapcsolódásokat.
 * 
 * @author Petnehazi
 *
 */
public class RoleBean extends Role {

    private static final long serialVersionUID = -1280953483889108166L;
    private List<Function> allFunction;

    public RoleBean() {
    }

    public RoleBean(Role wrappedRole) {
        this.setIdRole(wrappedRole.getIdRole());
        this.setRoleName(wrappedRole.getRoleName());
        this.setRoleComment(wrappedRole.getRoleComment());
        this.setFunctions(wrappedRole.getFunctions());
        this.setDeleted(wrappedRole.getDeleted());
    }

    public static Role getRoleFromList(List<? extends Role> list, int roleId) {
        for (Role role : list) {
            if (role.getIdRole() == roleId) {
                return role;
            }
        }
        return null;
    }

    public static List<Role> getSelectedSuperRoles(List<? extends Role> fulllist) {
        List<Role> list = new ArrayList<Role>();
        for (Role role : fulllist) {
            if ((role).isSelected()) {
                Role r = new Role();
                r.setIdRole(role.getIdRole());
                r.setRoleName(role.getRoleName());
                r.setRoleComment(role.getRoleComment());
                list.add(r);
            }
        }
        return list;
    }

    public String getTitle() {
        return getRoleComment() != null ? getRoleComment().length() > 200 ? getRoleComment().substring(0, 200) : getRoleComment()
                : "";
    }

    /**
     * Az összes rendszer funkció, benne selcted ami a role-hoz tartozik.
     * 
     * @return
     */
    public List<Function> getPossibleFunctions() {
        for (Function function : getFunctions()) {
            Function f = getFunction(getAllFunction(), function.getIdFunction());
            if (f != null) {
                f.setSelected(true);
            }
        }
        return getAllFunction();
    }

    public Function getFunction(int functionId) {
        return getFunction(getAllFunction(), functionId);
    }

    private Function getFunction(List<Function> list, int functionId) {
        for (Function function : list) {
            if (function.getIdFunction() == functionId)
                return function;
        }
        return null;
    }

    public void setAllFunction(List<Function> allFunction) {
        List<Function> clone = new ArrayList<Function>();
        if (allFunction != null) {
            for (Function function : allFunction) {
                Function f = new Function();
                f.setIdFunction(function.getIdFunction());
                f.setFunctionCode(function.getFunctionCode());
                f.setFunctionName(function.getFunctionName());
                clone.add(f);
            }
        }
        this.allFunction = clone;
    }

    public List<Function> getAllFunction() {
        if (allFunction == null)
            allFunction = new ArrayList<Function>();
        return allFunction;
    }

    public List<Function> getSelectedFunctions() {
        List<Function> list = new ArrayList<Function>();
        for (Function function : getAllFunction()) {
            if (function.isSelected())
                list.add(function);
        }
        return list;
    }

    @Override
    public String toString() {
        return "[" + this.getIdRole() + " '" + this.getRoleName() + "' " + this.hashCode() + " " + getFunctions().size() + " "
                + getAllFunction().size() + " " + getSelectedFunctions().size() + "]";
    }

}
