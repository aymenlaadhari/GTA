package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.ImputaCompte;
import com.yesserp.sessionbean.paramgta.gestionimputacompte.GestionImputaCompteLocal;

@ManagedBean
@RequestScoped
public class ImputaCompteConverter implements Converter {

	@EJB
	GestionImputaCompteLocal gestionImputaCompteLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ImputaCompte imputaCompte = null;
		if (!arg2.trim().equals(""))
			imputaCompte = gestionImputaCompteLocal.findByCode(arg2);
		return imputaCompte;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((ImputaCompte) value).getCode());
	}

}
