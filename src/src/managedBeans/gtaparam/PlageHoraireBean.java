package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.ActiviteAbsence;
import com.yesserp.domain.gtaparam.ActivitePresence;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsenceLocal;
import com.yesserp.sessionbean.paramgta.gestionactivitepresence.GestionActivitePresenceLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;
import com.yesserp.sessionbean.paramgta.gestionplagehoraire.GestionPlageHoraireLocal;

@ManagedBean
@ViewScoped
public class PlageHoraireBean {
	private PlageHoraire plageHoraire = new PlageHoraire();
	private Libelle libelle = new Libelle();
	private ActivitePresence activitePresence = new ActivitePresence();
	private ActiviteAbsence activiteAbsence = new ActiviteAbsence();
	private ActiviteAbsence acAbsenceEntree = new ActiviteAbsence();
	private ActiviteAbsence acAbsenceSortie = new ActiviteAbsence();
	private List<ActiviteAbsence> activiteAbsences = new ArrayList<>();
	private List<ActivitePresence> activitePresences = new ArrayList<>();
	private List<PlageHoraire> listeHoraires = new ArrayList<>();
	private PlageHoraire selectedPh;
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionPlageHoraireLocal gestionPlageHoraireLocal;

	@EJB
	GestionActiviteAbsenceLocal gestionActiviteAbsenceLocal;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;

	@EJB
	GestionActivitePresenceLocal gestionActivitePresenceLocal;

	@PostConstruct
	public void init() {
		listeHoraires = gestionPlageHoraireLocal.findAllPlagesHoraire();
		for (int i = 0; i < listeHoraires.size(); i++) {
			try {
				listeHoraires.get(i).setLibelles(
						gestionLibelleLocal
								.findLibelleByPlageHoraire(listeHoraires.get(i)
										.getIdph()));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		activiteAbsences = gestionActiviteAbsenceLocal.listerActiviteAbsence();
		activitePresences = gestionActivitePresenceLocal.findAll();
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

	public void ajouterPlageHoraire() {
		plageHoraire.setActiviteAbsence(activiteAbsence);
		plageHoraire.setActivitePresence(activitePresence);
		plageHoraire.setAcAbsenceEntree(acAbsenceEntree);
		plageHoraire.setAcAbsenceSortie(acAbsenceSortie);
		
		ArrayList<Libelle> libs = new ArrayList<>();
		libs.add(libelle);
		plageHoraire.setLibelles(libs);
		libelle.setPlageHoraire(plageHoraire);// si plusieurs libelles boucle
		gestionPlageHoraireLocal.ajouterPlageHoraire(plageHoraire);

		plageHoraire = new PlageHoraire();
		libelle = new Libelle();
		activiteAbsence = new ActiviteAbsence();
		activitePresence = new ActivitePresence();
		init();
	}

	public void modifierPlageHoraire() {

		gestionPlageHoraireLocal.modifierPlageHoraire(selectedPh);

		selectedPh = new PlageHoraire();

		init();
	}

	// throws NotSupportedException, SystemException

//	public void supprimerPlageHoraire() {
//		// Libelle lib = selectedPh.getLibelles().get(0);
//		try {
//			gestionPlageHoraireLocal.supprimerPlageHoraire(selectedPh);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			// cascade remove pour libelle --> restaurer libelle
//			// gestionLibelleLocal.modifierLibelle(lib);
//		}
//		init();
//	}

	public void supprimerPlageHoraire() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedPh.getLibelles().get(0));
		try {

			gestionPlageHoraireLocal
					.supprimerPlageHoraire(selectedPh);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedPh.getCodeph(), "<"
									+ selectedPh.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));

			init();
		} catch (Exception e) {
			e.printStackTrace();
			selectedPh.setLibelles(libs);
			gestionPlageHoraireLocal
					.modifierPlageHoraire(selectedPh);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedPh.getCodeph(),
							" n'a pas pu être supprimé."));
		}

	}
	public PlageHoraire getPlageHoraire() {
		return plageHoraire;
	}

	public void setPlageHoraire(PlageHoraire plageHoraire) {
		this.plageHoraire = plageHoraire;
	}

	public List<PlageHoraire> getListeHoraires() {
		return listeHoraires;
	}

	public void setListeHoraires(List<PlageHoraire> listeHoraires) {
		this.listeHoraires = listeHoraires;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<ActiviteAbsence> getActiviteAbsences() {
		return activiteAbsences;
	}

	public void setActiviteAbsences(List<ActiviteAbsence> activiteAbsences) {
		this.activiteAbsences = activiteAbsences;
	}

	public List<ActivitePresence> getActivitePresences() {
		return activitePresences;
	}

	public void setActivitePresences(List<ActivitePresence> activitePresences) {
		this.activitePresences = activitePresences;
	}

	public ActivitePresence getActivitePresence() {
		return activitePresence;
	}

	public void setActivitePresence(ActivitePresence activitePresence) {
		this.activitePresence = activitePresence;
	}

	public ActiviteAbsence getActiviteAbsence() {
		return activiteAbsence;
	}

	public void setActiviteAbsence(ActiviteAbsence activiteAbsence) {
		this.activiteAbsence = activiteAbsence;
	}
	

	public ActiviteAbsence getAcAbsenceEntree() {
		return acAbsenceEntree;
	}

	public void setAcAbsenceEntree(ActiviteAbsence acAbsenceEntree) {
		this.acAbsenceEntree = acAbsenceEntree;
	}

	public ActiviteAbsence getAcAbsenceSortie() {
		return acAbsenceSortie;
	}

	public void setAcAbsenceSortie(ActiviteAbsence acAbsenceSortie) {
		this.acAbsenceSortie = acAbsenceSortie;
	}

	public PlageHoraire getSelectedPh() {
		return selectedPh;
	}

	public void setSelectedPh(PlageHoraire selectedPh) {
		this.selectedPh = selectedPh;
	}

}
