package hu.exclusive.crm.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "navigator")
@ViewScoped
public class Navigator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4088651227912133730L;

	transient protected java.util.logging.Logger log = Logger.getLogger(this.getClass().getName());

	private static final String PREFIX = "/pages/";
	private static final String SUFIX = ".xhtml";
	public static final String WELCOME = PREFIX + "welcome" + SUFIX;
	public static final String EMPTYSLIDER = PREFIX + "slider" + SUFIX;

	private String headerContent = PREFIX + "header" + SUFIX;
	private String footerContent = PREFIX + "footer" + SUFIX;
	private String mainContent = WELCOME;
	private String sliderContent = PREFIX + "slider" + SUFIX;
	private String sliderHeader = "---";
	private String mainHeader = "---";

	@PostConstruct
	public void init() {
		if (log == null)
			log = Logger.getLogger(this.getClass().getName());
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		init();
	}

	public void setContent(String mainContent, String sliderContent, String mainHeader, String sliderHeader) {
		setMainContent(mainContent);
		setSliderContent(sliderContent);
		setMainHeader(mainHeader);
		setSliderHeader(sliderHeader);
	}

	public void goHome() {
		setMainContent(Navigator.WELCOME);
		setSliderContent(Navigator.EMPTYSLIDER);
	}

	public void setContent(String mainContent, String sliderContent) {
		setMainContent(mainContent);
		setSliderContent(sliderContent);
	}

	public String getHeaderContent() {
		return preparePath(headerContent);
	}

	public void setHeaderContent(String headerContent) {
		this.headerContent = headerContent;
	}

	public String getFooterContent() {
		return preparePath(footerContent);
	}

	public void setFooterContent(String footerContent) {
		this.footerContent = footerContent;
	}

	public String getMainContent() {
		return preparePath(mainContent);
	}

	private String preparePath(String path) {
		System.out.println("--------------- get navigation: " + path);
		if (path != null && !path.startsWith(PREFIX))
			return PREFIX + path + SUFIX;
		return path;
	}

	// @PreAuthorize("hasPermission('fXX', 'pXX')")
	public void setMainContent(String mainContent) {
		System.out.println("--------------- set navigation.mainContent: " + mainContent);
		this.mainContent = mainContent;
	}

	public String getSliderContent() {
		return preparePath(sliderContent);
	}

	public void setSliderContent(String sliderContent) {
		this.sliderContent = sliderContent;
	}

	public void reset() {
		this.mainContent = (Navigator.WELCOME);
		this.sliderContent = null;
	}

	public String getSliderHeader() {
		return sliderHeader;
	}

	public void setSliderHeader(String sliderHeader) {
		this.sliderHeader = sliderHeader;
	}

	public String getMainHeader() {
		return mainHeader;
	}

	public void setMainHeader(String mainHeader) {
		this.mainHeader = mainHeader;
	}

}
