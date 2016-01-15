package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;

@ManagedBean
@ViewScoped
public class CycleTravailParSemaine {
	private Libelle libelle = new Libelle();
	private CycleTravail cycleTravail = new CycleTravail();
	private List<SemaineType> semaineTypes = new ArrayList<>();
	private List<SemaineType> listajout = new ArrayList<>();
	@EJB
	GestionCycleTravailLocal gestionCycleTravailLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;

	@PostConstruct
	public void init() {
		semaineTypes = gestionSemaineTypeLocal.listeSemaineType();
		for (SemaineType s : semaineTypes) {
			System.out.println(s.getIdst());
		}
	}

	public void ajouter() {
		cycleTravail.setSemaineTypes(listajout);
		cycleTravail.setTypeCycle("s");
		gestionCycleTravailLocal.ajoutCycleTravail(cycleTravail);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setCycleTravail(cycleTravail);
		gestionLibelleLocal.modifierLibelle(libelle);
		cycleTravail = new CycleTravail();
		libelle = new Libelle();

	}

	public String redirect() {

		return "/gtaparam/gestioncycletravail.jsf?faces-redirect=true";
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public CycleTravail getCycleTravail() {
		return cycleTravail;
	}

	public void setCycleTravail(CycleTravail cycleTravail) {
		this.cycleTravail = cycleTravail;
	}

	public List<SemaineType> getSemaineTypes() {
		return semaineTypes;
	}

	public void setSemaineTypes(List<SemaineType> semaineTypes) {
		this.semaineTypes = semaineTypes;
	}

	public List<SemaineType> getListajout() {
		return listajout;
	}

	public void setListajout(List<SemaineType> listajout) {
		this.listajout = listajout;
	}
}
