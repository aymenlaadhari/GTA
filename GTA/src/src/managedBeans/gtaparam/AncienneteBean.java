package managedBeans.gtaparam;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.yesserp.domain.gtaparam.Anciennete;

import com.yesserp.sessionbean.paramgta.anciennete.GestionAncienneteLocal;


@ManagedBean
@ViewScoped
public class AncienneteBean {
	private Anciennete anciennete = new Anciennete();

	private List<SelectOneMenu> oneMenus = new ArrayList<>();

	@EJB
	GestionAncienneteLocal gestionAncienneteLocal;

	public AncienneteBean() {
		oneMenus.add(new SelectOneMenu());
	}

	public Anciennete getAnciennete() {
		return anciennete;
	}

	public void setAnciennete(Anciennete anciennete) {
		this.anciennete = anciennete;
	}

	public List<SelectOneMenu> getOneMenus() {
		return oneMenus;
	}

	public void setOneMenus(List<SelectOneMenu> oneMenus) {
		this.oneMenus = oneMenus;
	}

	public void onButtonAddFieldClick(AjaxBehaviorEvent p_oEvent) {
		oneMenus.add(new SelectOneMenu());
		gestionAncienneteLocal.ajoutAnciennete(anciennete);

	}

	public void onButtonRemoveFieldClick(final SelectOneMenu p_oField) {
		oneMenus.remove(p_oField);
		gestionAncienneteLocal.supprimerAnciennete(anciennete);
	}


}
