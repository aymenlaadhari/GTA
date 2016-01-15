package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.yesserp.domain.gtaparam.GroupeActivite;
import com.yesserp.sessionbean.paramgta.gestiongroupeactivite.GestionGroupeActiviteLocal;

@ManagedBean
@RequestScoped
@FacesConverter(value = "GroupeActiviteConvertor")
public class GroupeActiviteConvertor implements Converter {

	@EJB
	GestionGroupeActiviteLocal gestionGroupeActiviteLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		GroupeActivite groupeActivite = null;
		if (!arg2.trim().equals(""))
			groupeActivite = gestionGroupeActiviteLocal
					.findGroupeActiviteByCode(arg2);
		return groupeActivite;

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((GroupeActivite) value).getCodegrpa());
	}

}
