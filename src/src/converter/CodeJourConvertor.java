package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;

@ManagedBean
@RequestScoped
public class CodeJourConvertor implements Converter {

	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		CodeJour codeJour = null;
		if (!arg2.trim().equals(""))
			codeJour = gestionCodeJourLocal.findCodeJourByCode(arg2);
		return codeJour;

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((CodeJour) value).getCodecj());
	}

}
