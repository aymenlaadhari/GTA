package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.ImputaCodeFact;
import com.yesserp.sessionbean.paramgta.gestionimputacodefacturation.GestionImputaCodeFacturationLocal;

@ManagedBean
@RequestScoped
public class ImputaCodeFacturationConverter implements Converter {
	@EJB
	GestionImputaCodeFacturationLocal gestionImputaCodeFacturationLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ImputaCodeFact imputaCodeFact = null;
		if (!arg2.trim().equals(""))
			imputaCodeFact = gestionImputaCodeFacturationLocal.findByCode(arg2);
		return imputaCodeFact;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((ImputaCodeFact) value).getCode());
	}

}
