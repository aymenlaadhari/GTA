package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gta.Calendrier;
import com.yesserp.sessionbean.gta.gestioncalendrier.GestionCalendrierLocal;

@ManagedBean
@RequestScoped
public class CalendrierConvertor implements Converter {

	@EJB
	GestionCalendrierLocal gestionCalendrierLocal;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		Calendrier calendrier = null;
		if (!arg2.trim().equals(""))
			calendrier = gestionCalendrierLocal.trouverCalendrierParNom(arg2);
		return calendrier;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((Calendrier) value).getNomJourFerier());
	}

}
