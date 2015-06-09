package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.JourFerier;
import com.yesserp.sessionbean.paramgta.jourferier.GestionJourFerierLocal;

@ManagedBean
@ViewScoped
public class JourFerierBean {
	private JourFerier jourferier = new JourFerier();
	private List<JourFerier> feriers = new ArrayList<JourFerier>();
	private JourFerier selectedFerrier;
	private boolean headerButtonsDisabled = true;
	@EJB
	GestionJourFerierLocal gestionjourferier;

	public JourFerier getJourferier() {
		return jourferier;
	}

	public void setJourferier(JourFerier jourferier) {
		this.jourferier = jourferier;
	}

	public List<JourFerier> getFeriers() {
		feriers = gestionjourferier.listerJourFerier();
		return feriers;
	}

	public void setFeriers(List<JourFerier> feriers) {
		this.feriers = feriers;
	}

	public JourFerier getSelectedFerrier() {
		return selectedFerrier;
	}

	public void setSelectedFerrier(JourFerier selectedFerrier) {
		this.selectedFerrier = selectedFerrier;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}

	public void ajouter() {
		gestionjourferier.ajoutJourFerier(jourferier);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Jour férier ajouté avec succés", null);
		FacesContext.getCurrentInstance().addMessage(null, message);
		jourferier = new JourFerier();
	}

}
