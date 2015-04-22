package hu.exclusive.crm.service;

import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Role;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@ManagedBean
@SessionScoped
public class UserService implements UserDetailsService {

    @Autowired
    hu.exclusive.dao.service.IExcDaoService excDao;

    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        try {

            CrmUser domainUser = excDao.getUser(login);

            if (domainUser == null)
                throw new UsernameNotFoundException(login);

            return prepareUser(domainUser);

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new UsernameNotFoundException(login);
    }

    private UserDetails prepareUser(CrmUser domainUser) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (Role r : domainUser.getRoles()) {

            for (Function f : r.getFunctions()) {
                authorities.add(new SimpleGrantedAuthority(f.getFunctionCode()));
            }
        }

        UserDetails user = new User(domainUser.getLoginName(), domainUser.getCrmPass(), authorities);

        System.out.println(user.toString());

        return user;
    }
}