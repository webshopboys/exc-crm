package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the K_USER_ROLE database table.
 * 
 */
@Embeddable
public class UserRolePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "id_crm_user", insertable = false, updatable = false)
    private int idCrmUser;

    @Column(name = "id_role", insertable = false, updatable = false)
    private int idRole;

    public UserRolePK() {
    }

    public int getIdCrmUser() {
        return this.idCrmUser;
    }

    public void setIdCrmUser(int idCrmUser) {
        this.idCrmUser = idCrmUser;
    }

    public int getIdRole() {
        return this.idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserRolePK)) {
            return false;
        }
        UserRolePK castOther = (UserRolePK) other;
        return (this.idCrmUser == castOther.idCrmUser) && (this.idRole == castOther.idRole);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.idCrmUser;
        hash = hash * prime + this.idRole;

        return hash;
    }
}