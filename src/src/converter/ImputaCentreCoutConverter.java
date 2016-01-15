package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.ImputaCentreCout;
import com.yesserp.sessionbean.paramgta.gestionimputacentrecout.GestionImputaCentreCoutLocal;

@ManagedBean
@RequestScoped
public class ImputaCentreCoutConverter implements Converter {
	@EJB
	GestionImputaCentreCoutLocal gestionImputaCentreCoutLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ImputaCentreCout imputaCentreCout = null;
		if (!arg2.trim().equals(""))
			imputaCentreCout = gestionImputaCentreCoutLocal.findByCode(arg2);
		return imputaCentreCout;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((ImputaCentreCout) value).getCode());
	}

}
