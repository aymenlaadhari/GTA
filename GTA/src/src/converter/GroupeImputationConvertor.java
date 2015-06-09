package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gtaparam.GroupeImputation;
import com.yesserp.sessionbean.paramgta.gestiongroupeimputation.GestionGroupeImputationLocal;

@ManagedBean
@RequestScoped
public class GroupeImputationConvertor implements Converter {

	@EJB
	GestionGroupeImputationLocal gestionGroupeImputationLocal;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		GroupeImputation groupeImputation = null;
		if (!arg2.trim().equals(""))
			groupeImputation = gestionGroupeImputationLocal
					.findGroupeImputationByCode(arg2);

		return groupeImputation;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((GroupeImputation) value).getCodegrp());
	}

}
