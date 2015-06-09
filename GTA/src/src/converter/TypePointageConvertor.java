package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.yesserp.domain.gta.TypePointage;
import com.yesserp.sessionbean.gta.gestionpointage.GestionPointageLocal;


@ManagedBean
@RequestScoped
@FacesConverter(value="TypePointageConvertor")
public class TypePointageConvertor  implements Converter {
	@EJB
	GestionPointageLocal gestionPointageLocal;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		TypePointage typePointage = null;
		if (!arg2.trim().equals(""))
			typePointage = gestionPointageLocal.findTypePointage(arg2);
		return typePointage;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((TypePointage) value).getLibelle());
	}
}
