package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gta.CodeCalendrier;
import com.yesserp.sessionbean.gta.gestioncodecalendrier.GestionCodeCalendrierLocal;

@ManagedBean
@RequestScoped
public class CodeCalendrierConvertor implements Converter {

	@EJB
	GestionCodeCalendrierLocal gestionCodeCalendrierLocal;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		CodeCalendrier codeCalendrier = null;
		if (!arg2.trim().equals(""))
			codeCalendrier = gestionCodeCalendrierLocal
					.trouverCodeParCode(arg2);
		return codeCalendrier;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((CodeCalendrier) value).getCode());
	}

}
