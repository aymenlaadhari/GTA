package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.ActivitePresence;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionactivitepresence.GestionActivitePresence;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ActivitePresenceBean {
	private ActivitePresence activitePresence = new ActivitePresence();
	private Libelle libelle = new Libelle();
	private List<ActivitePresence> activitePresences = new ArrayList<ActivitePresence>();
	private ActivitePresence selectedActivitePresence = new ActivitePresence();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionActivitePresence gestionActivitePresence;

	@PostConstruct
	public void init() {

		this.setactivitePresences(gestionActivitePresence.findAll());
		for (ActivitePresence ap : activitePresences) {
			try {
				ap.setLibelles(gestionLibelleLocal
						.findLibelleByActivitePresence(ap.getIdap()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}
	
	public void ajouterActivitePresence() {
		try {
			ArrayList<Libelle> libelles = new ArrayList<>();
			libelles.add(libelle);
			activitePresence.setLibelles(libelles);
			libelle.setActivitePresence(activitePresence);
			gestionActivitePresence.ajouterActivitePresence(activitePresence);
			activitePresences.add(activitePresence);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							activitePresence.getCodeap(), "<"
									+ activitePresence.getLibelles().get(0)
											.getLib()
									+ "> a été ajouté avec succès."));

			libelle = new Libelle();
			activitePresence = new ActivitePresence();
		} catch (Exception e) {

			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ activitePresence.getCodeap(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));

		}
	}

	public void modifierActivitePresence() {
		try {

			gestionActivitePresence
					.modifierActivitePresence(selectedActivitePresence);
			gestionLibelleLocal.modifierLibelle(selectedActivitePresence
					.getLibelles().get(0));

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedActivitePresence.getCodeap(), "<"
									+ selectedActivitePresence.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));

		} catch (Exception exception) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedActivitePresence.getCodeap(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerActivitePresence() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedActivitePresence.getLibelles().get(0));
		try {

			gestionActivitePresence
					.supprimerActivitePresence(selectedActivitePresence);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedActivitePresence.getCodeap(), "<"
									+ selectedActivitePresence.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));

			init();
		} catch (Exception e) {
			e.printStackTrace();
			selectedActivitePresence.setLibelles(libs);
			gestionActivitePresence
					.modifierActivitePresence(selectedActivitePresence);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedActivitePresence.getCodeap(),
							" n'a pas pu être supprimé."));
		}

	}

	public ActivitePresence getActivitePresence() {
		return activitePresence;
	}

	public void setActivitePresence(ActivitePresence activitePresence) {
		this.activitePresence = activitePresence;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<ActivitePresence> getactivitePresences() {
		return activitePresences;
	}

	public void setactivitePresences(List<ActivitePresence> activitePresences) {
		this.activitePresences = activitePresences;
	}

	public ActivitePresence getSelectedActivitePresence() {
		return selectedActivitePresence;
	}

	public void setSelectedActivitePresence(
			ActivitePresence selectedActivitePresence) {
		this.selectedActivitePresence = selectedActivitePresence;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}

}
