package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.NatureJournee;
import com.yesserp.sessionbean.paramgta.gestionnaturejournee.GestionNatureJourneeLocal;

@ManagedBean
@RequestScoped
public class NatureJourneeConvertor implements Converter {

	@EJB
	GestionNatureJourneeLocal gestionNatureJourneeLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		NatureJournee natureJournee = null;
		if (!arg2.trim().equals(""))
			natureJournee = gestionNatureJourneeLocal
					.findNatureJourneeByCode(arg2);
		return natureJournee;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((NatureJournee) value).getCodenj());
	}

}
