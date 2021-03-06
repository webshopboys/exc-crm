package hu.exclusive.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.exclusive.dao.model.CrmUser;

@Service
@Transactional(readOnly = true)
// @ManagedBean
// @SessionScoped
public class UserService implements UserDetailsService {

	@Autowired
	transient hu.exclusive.dao.service.IExcDaoService excDao;

	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

		try {

			CrmUser domainUser = excDao.getUser(login);

			if (domainUser == null)
				throw new UsernameNotFoundException(login);

			return domainUser;

		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UsernameNotFoundException(login);
	}

}