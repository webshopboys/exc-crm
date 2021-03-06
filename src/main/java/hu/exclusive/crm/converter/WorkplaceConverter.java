package hu.exclusive.crm.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import hu.exclusive.dao.model.Workplace;
import hu.exclusive.utils.SelectItemsBaseConverter;

@ManagedBean(name = "workplaceConverter")
public class WorkplaceConverter extends SelectItemsBaseConverter {

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Workplace) {
			return String.valueOf(((Workplace) value).getIdWorkplace());
		}
		return String.valueOf(value);
	}
}
