package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;
import com.yesserp.domain.gtaparam.CycleTravail;


@ManagedBean
@RequestScoped
public class CycleTravailConvertor implements Converter {

	@EJB
	GestionCycleTravailLocal cycleTravailLocal;
	
	CycleTravail cycleTravail;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		cycleTravail = null;
		if (!arg2.trim().equals(""))
			cycleTravail = cycleTravailLocal.findCycleDeTravailParCode(arg2);
		return cycleTravail;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((CycleTravail) value).getCodect());
	}

}
