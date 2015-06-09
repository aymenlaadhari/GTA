package managedBeans.gtaparam;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.NatureJournee;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;
import com.yesserp.sessionbean.paramgta.gestionnaturejournee.GestionNatureJourneeLocal;

@ManagedBean
@SessionScoped
public class NatureJourneeBean {
	private NatureJournee natureJournee = new NatureJournee();
	private Libelle libelle = new Libelle();
	
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionNatureJourneeLocal gestionNatureJourneeLocal;
	
	public void ajouter(){
		gestionNatureJourneeLocal.ajouterNatureJournee(natureJournee);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setNatureJournee(natureJournee);
		gestionLibelleLocal.modifierLibelle(libelle);
		libelle = new Libelle();
		natureJournee = new NatureJournee();
	}
	
	public NatureJournee getNatureJournee() {
		return natureJournee;
	}
	public void setNatureJournee(NatureJournee natureJournee) {
		this.natureJournee = natureJournee;
	}
	public Libelle getLibelle() {
		return libelle;
	}
	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

}
