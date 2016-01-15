package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.ActivitePresence;
import com.yesserp.sessionbean.paramgta.gestionactivitepresence.GestionActivitePresenceLocal;

@ManagedBean
@RequestScoped
public class ActivitePresenceConvertor implements Converter {

	@EJB
	GestionActivitePresenceLocal gestionActivitePresenceLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ActivitePresence activitePresence = null;
		if (!arg2.trim().equals(""))
			activitePresence = gestionActivitePresenceLocal
					.findActivitePresenceByCode(arg2);
		return activitePresence;

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((ActivitePresence) value).getCodeap());

	}

}
