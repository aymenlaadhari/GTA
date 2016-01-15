package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.Droit;
import com.yesserp.domain.gtaparam.GroupeDroit;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.droit.GestionDroitLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupedroit.GestionGroupeDroitLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@SessionScoped
public class GroupeDroitBean {

	private Libelle libelle = new Libelle();
	private List<Droit> Droits = new ArrayList<>();
	private List<Droit> selectedDroits = new ArrayList<>();
	private GroupeDroit groupeDroit = new GroupeDroit();

	@EJB
	GestionDroitLocal gestionDroitLocal;
	@EJB
	GestionGroupeDroitLocal gestionGroupeDroitLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;

	public void ajouter() {
		
		groupeDroit.setGtaParamDroits(selectedDroits);
		gestionLibelleLocal.ajoutLibelle(libelle);
		gestionLibelleLocal.modifierLibelle(libelle);
		groupeDroit.setGtaParamDroits(selectedDroits);
		gestionGroupeDroitLocal.ajouterGroupeDroit(groupeDroit);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Groupe droit ajouté avec succés", null);
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		libelle = new Libelle();
		selectedDroits = new ArrayList<>();
		groupeDroit = new GroupeDroit();
	
		
	}

	@PostConstruct
	public void init() {
		//Droits = gestionDroitLocal.listeDroits();

	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<Droit> getDroits() {
		return Droits;
	}

	public void setDroits(List<Droit> Droits) {
		this.Droits = Droits;
	}

	public List<Droit> getSelectedDroits() {
		return selectedDroits;
	}

	public void setSelectedDroits(List<Droit> selectedDroits) {
		this.selectedDroits = selectedDroits;
	}

	public GroupeDroit getGroupeDroit() {
		return groupeDroit;
	}

	public void setGroupeDroit(GroupeDroit groupeDroit) {
		this.groupeDroit = groupeDroit;
	}

}
