package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.ActiviteAbsence;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsence;

@ManagedBean
@RequestScoped
public class ActiviteAbsenceConvertor implements Converter {

	@EJB
	GestionActiviteAbsence gestionActiviteAbsence;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ActiviteAbsence activiteAbsence = null;
		if (!arg2.trim().equals(""))
			activiteAbsence = gestionActiviteAbsence
					.findActiviteAbsenceByCode(arg2);
		return activiteAbsence;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((ActiviteAbsence) value).getCodeaabs());
	}

}
