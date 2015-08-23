package hu.exclusive.crm.view;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;

import hu.exclusive.crm.controller.Commontroller;
import hu.exclusive.crm.service.ParametersService;
import hu.exclusive.dao.DaoFilter;
import hu.exclusive.dao.DaoFilter.RELATION;
import hu.exclusive.dao.model.Function;
import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.dao.model.Role;
import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.dao.model.Workplace;
import hu.exclusive.utils.PageUtils;

//@Component
@ManagedBean(name = "parameterView")
@ViewScoped
public class ParameterView extends Commontroller implements Serializable {

	private static final long serialVersionUID = -1774648789104774066L;

	private ScheduleModel eventModel;

	private ScheduleModel lazyEventModel;

	private ScheduleEvent event = new DefaultScheduleEvent();
	@Autowired
	transient ParametersService service;

	public void init() {

		eventModel = new DefaultScheduleModel();
		eventModel.addEvent(new DefaultScheduleEvent("Lehetett izgulni ezerrel", previousDay8Pm(), previousDay11Pm()));
		eventModel.addEvent(new DefaultScheduleEvent("Irány a vonat", today1Pm(), today6Pm()));
		eventModel.addEvent(new DefaultScheduleEvent("Gondolatokat rögziteni", nextDay9Am(), nextDay11Am()));
		eventModel.addEvent(new DefaultScheduleEvent("Munka, munka, munka", theDayAfter3Pm(), fourDaysLater3pm()));

		lazyEventModel = new LazyScheduleModel() {

			private static final long serialVersionUID = 3593721913278982422L;

			@Override
			public void loadEvents(Date start, Date end) {
				Date random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));

				random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
			}
		};
	}

	public List<PCafeteriaCategory> getCafeteriaCategories() {
		return service.getCafeteriaCategories(null);
	}

	public List<Role> getRoles() {
		List<Role> r = service.getRoles((DaoFilter) null);
		System.err.println("Van most " + r.size() + " szerep a rendszerben");
		return r;
	}

	public List<Function> getFunctions() {
		return service.getFunctions((DaoFilter) null);
	}

	public List<Jobtitle> getJobtitles() {
		return service.getJobtitles(null);
	}

	public List<Workgroup> getWorkgroups() {
		return service.getWorkgroups(null);
	}

	public List<Workplace> getWorkplaces() {
		return service.getWorkplaces(null);
	}

	public List<Workplace> getWorkplacesForGroup(Integer groupId) {
		DaoFilter filter = new DaoFilter("Workplace.findForGroup", "idWorkgroup", RELATION.NAMED_QUERY, groupId);
		return service.getWorkplaces(filter);
	}

	/**
	 * Kikeresi es visszadja a munkatars aktualis csoportjat es munkahelyet
	 * osszefuzve.
	 * 
	 * @param idStaff
	 * @return
	 */
	public String getStaffWorkGroupPlace(Integer idStaff) {
		DaoFilter filter = new DaoFilter();
		filter.setRelation(RELATION.NAMED_QUERY);
		filter.setEntity("Workgroup.findForStaff");
		filter.setFieldName("idStaff");
		filter.setValues(idStaff);

		List<Workgroup> groups = service.getWorkgroups(filter);
		String group = groups.isEmpty() ? "" : groups.get(0).getGroupName();

		filter.setEntity("Workplace.findForStaff");

		StringBuilder sb = new StringBuilder(group).append(" - ");
		List<Workplace> places = service.getWorkplaces(filter);
		if (places != null) {
			for (Workplace wp : places) {
				sb.append(wp.getWorkplaceName()).append(PageUtils.BR_UNESCAPE);
			}
		}
		return sb.toString();
	}

	/**
	 * Visszadja felsorolva a munkatars munkaköreit.
	 * 
	 * @param idStaff
	 * @return
	 */
	public String getStaffJobTitles(Integer idStaff) {

		DaoFilter filter = new DaoFilter();
		filter.setRelation(RELATION.NAMED_QUERY);
		filter.setEntity("Jobtitle.findForStaff");
		filter.setFieldName("idStaff");
		filter.setValues(idStaff);

		List<Jobtitle> titles = service.getJobtitles(filter);
		StringBuilder sb = new StringBuilder();
		if (titles != null) {
			for (Jobtitle jobtitle : titles) {
				sb.append(jobtitle.getJobtitle()).append(PageUtils.BR_UNESCAPE);
			}
		}
		return sb.toString();
	}

	public Date getRandomDate(Date base) {
		Calendar date = Calendar.getInstance();
		date.setTime(base);
		date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1); // set random
																	// day of
																	// month

		return date.getTime();
	}

	public Date getInitialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar.getTime();
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleModel getLazyEventModel() {
		return lazyEventModel;
	}

	private Calendar today() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar;
	}

	private Date previousDay8Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
		t.set(Calendar.HOUR, 8);

		return t.getTime();
	}

	private Date previousDay11Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
		t.set(Calendar.HOUR, 11);

		return t.getTime();
	}

	private Date today1Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.HOUR, 1);

		return t.getTime();
	}

	private Date theDayAfter3Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.HOUR, 3);

		return t.getTime();
	}

	private Date today6Pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.HOUR, 6);

		return t.getTime();
	}

	private Date nextDay9Am() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.AM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
		t.set(Calendar.HOUR, 9);

		return t.getTime();
	}

	private Date nextDay11Am() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.AM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
		t.set(Calendar.HOUR, 11);

		return t.getTime();
	}

	private Date fourDaysLater3pm() {
		Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
		t.set(Calendar.HOUR, 3);

		return t.getTime();
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void addEvent(ActionEvent actionEvent) {
		if (event.getId() == null)
			eventModel.addEvent(event);
		else
			eventModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved",
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized",
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String getSystemVersion() {
		return service.getSystemVersion();
	}
}