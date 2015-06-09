package managedBeans.gtaparam;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.Anciennete;
import com.yesserp.domain.gtaparam.Droit;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.TrancheDroit;
import com.yesserp.domain.gtaparam.Unite;
import com.yesserp.sessionbean.paramgta.unite.GestionUniteLocal;
import com.yesserp.sessionbean.paramgta.droit.GestionDroitLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;



@ManagedBean
@SessionScoped
public class DroitBean {
	
	private Libelle libelle = new Libelle();
	private Droit droit = new Droit();
	private List<Unite> unites =  new ArrayList<>();
	private List<Unite> selectedUnites = new ArrayList<>();
	private List<Anciennete> anciennetes =  new ArrayList<>();
	private List<Anciennete> selectedAnciennetes = new ArrayList<>();
	private List<TrancheDroit> tranches = new ArrayList<>();
	
	
	@EJB
	GestionDroitLocal gestionDroitLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionUniteLocal gestionUniteLocal;

	

	
	@PostConstruct
	public void init() {
	unites = gestionUniteLocal.listerUnite();
	for(int i = 0; i<unites.size();i++)
	{
		 System.out.println(unites.get(i))  ;
	}
 }
	
	public List<Unite> getUnites() {
		for(int i = 0; i<unites.size();i++)
		{
			 System.out.println(unites.get(i))  ;
		}
		return unites;
	}
	public void setUnites(List<Unite> unites) {
		this.unites = unites;
	}
	public void ajouter() {

		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setDroit(droit);
		gestionLibelleLocal.modifierLibelle(libelle);
		gestionDroitLocal.ajoutDroit(droit);
		libelle = new Libelle();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Droit ajouté avec succés", null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}



	public Droit getDroit() {
		return droit;
	}



	public void setDroit(Droit droit) {
		this.droit = droit;
	}



	public List<Unite> getSelectedUnites() {
		return selectedUnites;
	}



	public void setSelectedUnites(List<Unite> selectedUnites) {
		this.selectedUnites = selectedUnites;
	}



	public List<Anciennete> getAnciennetes() {
		return anciennetes;
	}



	public void setAnciennetes(List<Anciennete> anciennetes) {
		this.anciennetes = anciennetes;
	}



	public List<Anciennete> getSelectedAnciennetes() {
		return selectedAnciennetes;
	}



	public void setSelectedAnciennetes(List<Anciennete> selectedAnciennetes) {
		this.selectedAnciennetes = selectedAnciennetes;
	}



	public List<TrancheDroit> getTranches() {
		return tranches;
	}



	public void setTranches(List<TrancheDroit> tranches) {
		this.tranches = tranches;
	}



	public Libelle getLibelle() {
		return libelle;
	}



	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}





	

}
