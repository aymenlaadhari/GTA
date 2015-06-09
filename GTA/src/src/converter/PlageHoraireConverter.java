package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.sessionbean.paramgta.gestionplagehoraire.GestionPlageHoraireLocal;

@ManagedBean
@RequestScoped
public class PlageHoraireConverter implements Converter {

	@EJB
	GestionPlageHoraireLocal gestionPlageHoraireLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		PlageHoraire plageHoraire = null;
		if (!arg2.trim().equals(""))
			plageHoraire = gestionPlageHoraireLocal
					.findPlageHoraireByCode(arg2);
		return plageHoraire;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((PlageHoraire) value).getCodeph());
	}

}
