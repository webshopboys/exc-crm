package hu.exclusive.crm.controller;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import hu.exclusive.dao.model.CrmUser;
import hu.exclusive.utils.FacesAccessor;

//@Controller
@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController extends Commontroller {

	private static final long serialVersionUID = -6169544625109774495L;
	private String userName = null;
	private String password = null;

	transient protected Logger log = Logger.getLogger(this.getClass().getName());

	public void init() {
		if (log == null)
			log = Logger.getLogger(this.getClass().getName());
	}

	public void checkReason() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		// HttpServletResponse res = (HttpServletResponse) FacesContext
		// .getCurrentInstance().getExternalContext().getResponse();

		resetNavigator();

		if (req != null && req.getParameter("r") != null) {
			String r = req.getParameter("r");

			log.fine("checkReason " + r);

			if ("expired".equals(r)) {
				FacesMessage msg = new FacesMessage("A rendelkezésre álló idő lejárt, lépjen be újra!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else if ("notfound".equals(r)) {
				FacesMessage msg = new FacesMessage("A keresett oldal nem érhető el!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else if ("404".equals(r)) {
				FacesMessage msg = new FacesMessage("A keresett oldal nem érhető el!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else if ("logout".equals(r)) {
				FacesMessage msg = new FacesMessage("Viszontlátásra!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				FacesMessage msg = new FacesMessage("Lépjen be az oldal megtekintéséhez! (" + r + ")");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		}

		// log.debug("after logout context: " +
		// SecurityContextHolder.getContext().getAuthentication());

	}

	@Autowired
	transient private AuthenticationManager authenticationManager = null;

	private String userMenuClass;

	public String login() {

		try {
			resetNavigator();

			if (authenticationManager == null)
				authenticationManager = (AuthenticationManager) FacesAccessor.getManagedBean("authenticationManager");

			Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), this.getPassword());

			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
			log.fine(userName + " authenticated...");

			return "/pages/exc.xhtml";

		} catch (BadCredentialsException e) {
			FacesMessage msg = new FacesMessage("Érvénytelen adatok!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			log.warning("Érvénytelen jelszó " + userName + "/" + password);

		} catch (UsernameNotFoundException e) {
			FacesMessage msg = new FacesMessage("Érvénytelen adatok!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			log.warning("Érvénytelen user " + userName);

		} catch (java.lang.IllegalArgumentException ie) {
			ie.printStackTrace();
			FacesMessage msg = new FacesMessage("Érvénytelen adatok!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Throwable e) {

			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Hiba a köbön: " + e.getLocalizedMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

		return "login.xhtml";
	}

	private void resetNavigator() {
		// a default siderekre tenni?

	}

	public CrmUser getPrincipal() {
		return (CrmUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public String cancel() {
		return null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Ezen át lehet kijelezni, ha a user pl. kapott valami üzenetet. Ez után
	 * frissíthető a menü, és akkor megjelenik a vizuális effekt.
	 * 
	 * @return
	 */
	public String getUserMenuClass() {
		return this.userMenuClass;
	}

	/**
	 * Ezen át lehet kijelezni, ha a user pl. kapott valami üzenetet. Ez után
	 * frissíthető a menü, és akkor megjelenik a vizuális effekt.
	 * 
	 * @param userMenuClass
	 */
	public void setUserMenuClass(String userMenuClass) {
		this.userMenuClass = userMenuClass;
		RequestContext.getCurrentInstance().update("menuForm:excMenu");
	}
}
