package hu.exclusive.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the t_role database table.
 * 
 */
@Entity
@Table(name = "P_ROLE")
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r ORDER BY r.roleName")
public class Role implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    @Transient
    private boolean selected;

    @Id
    @Column(name = "id_role")
    private int idRole;

    @Column(name = "deleted")
    private Timestamp deleted;

    @Column(name = "role_comment")
    private String roleComment;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "K_ROLE_FUNCTION", joinColumns = @JoinColumn(name = "id_role", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_function", nullable = false))
    private List<Function> functions;

    public Role() {
    }

    public int getIdRole() {
        return this.idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public Timestamp getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Timestamp deleted) {
        this.deleted = deleted;
    }

    public String getRoleComment() {
        return this.roleComment;
    }

    public void setRoleComment(String roleComment) {
        this.roleComment = roleComment;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Function> getFunctions() {
        return functions == null ? new ArrayList<Function>() : functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Role [idRole=" + idRole + ", roleName=" + roleName + ", selected=" + selected + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Role) {
            return this.idRole == ((Role) obj).idRole;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Role.class.hashCode() + this.idRole;
    }
}