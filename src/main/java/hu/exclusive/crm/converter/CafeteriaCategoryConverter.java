package hu.exclusive.crm.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import hu.exclusive.dao.model.PCafeteriaCategory;
import hu.exclusive.utils.SelectItemsBaseConverter;

@ManagedBean(name = "cafeteriaCategoryConverter")
public class CafeteriaCategoryConverter extends SelectItemsBaseConverter {

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value instanceof PCafeteriaCategory) {
			return String.valueOf(((PCafeteriaCategory) value).getIdCafeteriaCat());
		}
		return String.valueOf(value);
	}
}
