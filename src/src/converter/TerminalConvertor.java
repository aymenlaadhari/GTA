package converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.yesserp.domain.gta.Terminal;
import com.yesserp.sessionbean.gta.gestionpointage.GestionPointageLocal;

@ManagedBean
@RequestScoped
public class TerminalConvertor implements Converter {
	@EJB
	GestionPointageLocal gestionPointageLocal;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String arg2) {
		Terminal terminal = null;
		if (!arg2.trim().equals(""))
			terminal = gestionPointageLocal.findTerminal(arg2);
		return terminal;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null || value.equals(""))
			return "";
		else
			return String.valueOf(((Terminal) value).getLibelle());
	}
}
