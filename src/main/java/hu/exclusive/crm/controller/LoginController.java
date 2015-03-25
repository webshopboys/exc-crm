package hu.exclusive.crm.controller;

import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController {

    private String userName = null;
    private String password = null;

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    public void checkReason() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
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

        // log.debug("after logout context: " + SecurityContextHolder.getContext().getAuthentication());

    }

    public String login() {
        try {

            // log.debug("before login context: " + SecurityContextHolder.getContext().getAuthentication());

            resetNavigator();

            return "/pages/exc.xhtml";

        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage("Hiba a köbön: " + e.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

        return "login.xhtml";
    }

    private void resetNavigator() {

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

}
