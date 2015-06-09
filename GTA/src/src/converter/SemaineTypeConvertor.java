package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;

@ManagedBean
@RequestScoped
public class SemaineTypeConvertor implements Converter {
	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		SemaineType semaineType = null;
		if (!arg2.trim().equals(""))
			semaineType = gestionSemaineTypeLocal.trouverSemaineParCode(arg2);
		return semaineType;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((SemaineType) value).getCodest());
	}

}
