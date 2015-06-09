package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.Imputation;
import com.yesserp.sessionbean.paramgta.gestionimputation.GestionImputationLocal;

@ManagedBean
@RequestScoped
public class ImputationConvertor implements Converter {

	@EJB
	GestionImputationLocal gestionImputationLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		Imputation imputation = null;
		if (!arg2.trim().equals(""))
			imputation = gestionImputationLocal.findImputationByCode(arg2);
		return imputation;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((Imputation) value).getCodei());
	}

}
