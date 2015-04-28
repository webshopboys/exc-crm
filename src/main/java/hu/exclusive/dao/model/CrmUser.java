package hu.exclusive.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The persistent class for the t_crmuser database table.
 * 
 */
@Entity
@Table(name = "T_CRMUSER")
@NamedQueries({ @NamedQuery(name = "CrmUser.findAll", query = "SELECT c FROM CrmUser c ORDER BY c.userName"),
        @NamedQuery(name = "CrmUser.find", query = "SELECT c FROM CrmUser c WHERE upper(c.loginName) = upper(:loginName) ") })
public class CrmUser extends EntityCommons implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_crm_user")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCrmUser;

    @Column(name = "crm_pass")
    private String crmPass;

    @Column(name = "hint_pass")
    private String hintPass;

    @Column(name = "login_name")
    private String loginName = "";

    @Column(name = "user_name")
    private String userName = "";

    @Column(name = "user_status")
    private String userStatus;

    @Column(name = "user_email")
    private String userEmail;

    @Transient
    List<GrantedAuthority> authorities;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "K_USER_ROLE", joinColumns = @JoinColumn(name = "id_crm_user", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_role", nullable = false))
    private List<Role> roles;

    public CrmUser() {
    }

    @Override
    public Integer getId() {
        return getIdCrmUser();
    }

    public Integer getIdCrmUser() {
        return this.idCrmUser;
    }

    public void setIdCrmUser(Integer idCrmUser) {
        this.idCrmUser = idCrmUser;
    }

    public String getCrmPass() {
        return this.crmPass;
    }

    public void setCrmPass(String crmPass) {
        this.crmPass = crmPass;
    }

    public String getHintPass() {
        return this.hintPass;
    }

    public void setHintPass(String hintPass) {
        this.hintPass = hintPass;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        // System.err.println("--------------setLoginName " + loginName);
        this.loginName = loginName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        // System.err.println("--------------setUserName " + userName);
        this.userName = userName;
    }

    public String getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User[ id:" + idCrmUser + ", loginName:" + loginName + ", name:" + userName + ", p:" + crmPass + ", roles: "
                + getRoles() + "]";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (authorities == null) {
            authorities = new ArrayList<GrantedAuthority>();
            for (Role r : this.getRoles()) {
                for (Function f : r.getFunctions()) {
                    authorities.add(new SimpleGrantedAuthority(f.getFunctionCode()));
                }
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return crmPass;
    }

    @Override
    public String getUsername() {
        return this.loginName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"FELFÜGGESZTETT".equals(this.userStatus);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "AKTÍV".equals(this.userStatus);
    }

}