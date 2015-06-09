package managedBeans.gtaparam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.yesserp.domain.gtaparam.Compteur;
import com.yesserp.sessionbean.paramgta.gestioncompteur.GestionCompteurLocal;

@ManagedBean(name = "compteurUserService")
@ApplicationScoped
public class compteurUserService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	GestionCompteurLocal compteurLocal;

	private List<Compteur> compteurs = new ArrayList<>();

	private Compteur selectedCompteur;

	@PostConstruct
	public void init() {
		selectedCompteur = new Compteur();
		setCompteurs(compteurLocal.listeCompteurs());

	}

	//
	// public List<Compteur> createCars(int size) {
	// List<Compteur> list = new ArrayList<Compteur>();
	// for(int i = 0 ; i < size ; i++) {
	// list.add(new Compteur("aaaaa"));
	// }
	//
	// return list;
	// }

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage(" Selected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage(" Unselected");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<Compteur> getCompteurs() {
		return compteurs;
	}

	public void setCompteurs(List<Compteur> compteurs) {
		this.compteurs = compteurs;
	}

	public Compteur getSelectedCompteur() {
		return selectedCompteur;
	}

	public void setSelectedCompteur(Compteur selectedCompteur) {
		this.selectedCompteur = selectedCompteur;
	}

}