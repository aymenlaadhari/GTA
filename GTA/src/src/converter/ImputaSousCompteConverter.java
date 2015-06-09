package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.ImputaSousCompte;
import com.yesserp.sessionbean.paramgta.gestionimputasouscompte.GestionImputaSousCompteLocal;

@ManagedBean
@RequestScoped
public class ImputaSousCompteConverter implements Converter {

	@EJB
	GestionImputaSousCompteLocal gestionImputaSousCompteLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ImputaSousCompte imputaSousCompte = null;
		if (!arg2.trim().equals(""))
			imputaSousCompte = gestionImputaSousCompteLocal.findByCode(arg2);
		return imputaSousCompte;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((ImputaSousCompte) value).getCode());
	}

}
