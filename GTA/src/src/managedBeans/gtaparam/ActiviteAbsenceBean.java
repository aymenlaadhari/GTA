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
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsenceLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ActiviteAbsenceBean {

	private ActiviteAbsence activiteAbsence = new ActiviteAbsence();
	private ActiviteAbsence selectedActiviteAbsence = new ActiviteAbsence();
	private List<ActiviteAbsence> activiteAbsences = new ArrayList<ActiviteAbsence>();
	private Libelle libelle = new Libelle();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionActiviteAbsenceLocal gestionActiviteAbsenceLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;

	@PostConstruct
	public void init() {
		this.setActiviteAbsences(gestionActiviteAbsenceLocal.findAll());
		for (int i = 0; i < activiteAbsences.size(); i++) {
			try {

				activiteAbsences.get(i).setLibelles(
						gestionLibelleLocal
								.findLibelleByActiviteAbsence(activiteAbsences
										.get(i)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void ajouterActiviteAbsence() {

		try {
			List<Libelle> libelles = new ArrayList<Libelle>();
			libelles.add(libelle);
			activiteAbsence.setLibelles(libelles);
			libelle.setActiviteAbsence(activiteAbsence);
			gestionActiviteAbsenceLocal.ajouterActiviteAbsence(activiteAbsence);
			activiteAbsences.add(activiteAbsence);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							activiteAbsence.getCodeaabs(), "<"
									+ activiteAbsence.getLibelles().get(0)
											.getLib()
									+ "> a été ajouté avec succès."));

			libelle = new Libelle();
			activiteAbsence = new ActiviteAbsence();
		} catch (Exception e) {

			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ activiteAbsence.getCodeaabs(),
									" n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}

	}

	public void modifierActiviteAbsence() {
		try {

			gestionActiviteAbsenceLocal
					.modifierActiviteAbsence(selectedActiviteAbsence);
			gestionLibelleLocal.modifierLibelle(selectedActiviteAbsence
					.getLibelles().get(0));

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedActiviteAbsence.getCodeaabs(), "<"
									+ selectedActiviteAbsence.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));

		} catch (Exception exception) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur!",
									"Aucune activité selectionné pour la modification."));
		}

	}

	public void supprimerActiviteAbsence() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedActiviteAbsence.getLibelles().get(0));
		try {
			gestionActiviteAbsenceLocal
					.supprimerActiviteAbsence(selectedActiviteAbsence);
			this.setHeaderButtonsDisabled(true);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedActiviteAbsence.getCodeaabs(), "<"
									+ selectedActiviteAbsence.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));

			init();
		} catch (Exception exception) {
			selectedActiviteAbsence.setLibelles(libs);
			gestionActiviteAbsenceLocal
					.modifierActiviteAbsence(selectedActiviteAbsence);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! "
							+ selectedActiviteAbsence.getCodeaabs(),
							"n'a pas pu être supprimé."));
		}
	}

	public ActiviteAbsence getActiviteAbsence() {
		return activiteAbsence;
	}

	public void setActiviteAbsence(ActiviteAbsence activiteAbsence) {
		this.activiteAbsence = activiteAbsence;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public ActiviteAbsence getSelectedActiviteAbsence() {
		return selectedActiviteAbsence;
	}

	public void setSelectedActiviteAbsence(
			ActiviteAbsence selectedActiviteAbsence) {
		this.selectedActiviteAbsence = selectedActiviteAbsence;
	}

	public List<ActiviteAbsence> getActiviteAbsences() {
		return activiteAbsences;
	}

	public void setActiviteAbsences(List<ActiviteAbsence> activiteAbsences) {
		this.activiteAbsences = activiteAbsences;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}

	public ActiviteAbsenceBean() {

		if (this.activiteAbsence == null) {
			this.activiteAbsence = new ActiviteAbsence();
		}

	}

}
