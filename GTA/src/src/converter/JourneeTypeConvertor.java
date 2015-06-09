package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;

@ManagedBean
@RequestScoped
public class JourneeTypeConvertor implements Converter {
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		JourneeType journeeType = null;
		if (!arg2.trim().equals(""))
			journeeType = gestionJourneeTypeLocal.findJourneeTypeByCode(arg2);
		return journeeType;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((JourneeType) value).getCodejt());
	}

}
