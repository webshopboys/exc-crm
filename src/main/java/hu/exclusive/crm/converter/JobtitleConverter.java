package hu.exclusive.crm.converter;

import hu.exclusive.dao.model.Jobtitle;
import hu.exclusive.utils.SelectItemsBaseConverter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "jobtitleConverter")
public class JobtitleConverter extends SelectItemsBaseConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value instanceof Jobtitle) {
            String.valueOf(((Jobtitle) value).getIdJobtitle());
        }
        return String.valueOf(value);
    }

}
