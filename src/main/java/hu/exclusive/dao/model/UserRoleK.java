package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the K_USER_ROLE database table.
 * 
 */
@Entity
@Table(name = "K_USER_ROLE")
public class UserRoleK implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserRolePK id;

    public UserRoleK() {
    }

    public UserRolePK getId() {
        return this.id;
    }

    public void setId(UserRolePK id) {
        this.id = id;
    }

}