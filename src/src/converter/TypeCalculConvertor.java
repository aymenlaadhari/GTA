package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gta.TypeCalucle;
import com.yesserp.sessionbean.gta.gestiontypecalcul.GestionTypeCalculLocal;

@ManagedBean
@RequestScoped
public class TypeCalculConvertor implements Converter {
	@EJB
	GestionTypeCalculLocal gestionTypeCalculLocal;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		TypeCalucle typeCalucle = null;
		if (!arg2.trim().equals(""))
			typeCalucle = gestionTypeCalculLocal.findTypeCalculParNom(arg2);
		return typeCalucle;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((TypeCalucle) value).getType());
	}

}
