package hu.exclusive.crm.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import hu.exclusive.dao.model.Workgroup;
import hu.exclusive.utils.SelectItemsBaseConverter;

@ManagedBean(name = "workgroupConverter")
public class WorkgroupConverter extends SelectItemsBaseConverter {

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value instanceof Workgroup) {
			return String.valueOf(((Workgroup) value).getIdWorkgroup());
		}
		return String.valueOf(value);
	}
}
