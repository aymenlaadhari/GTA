package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.Compteur;
import com.yesserp.sessionbean.paramgta.gestioncompteur.GestionCompteurLocal;

@ManagedBean
@RequestScoped
public class CompteurConvertor implements Converter {

	@EJB
	GestionCompteurLocal gestionCompteurLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		Compteur compteur = null;
		if (!arg2.trim().equals(""))
			compteur = gestionCompteurLocal.findCompteurByCode(arg2);
		return compteur;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((Compteur) value).getCodec());
	}

}
