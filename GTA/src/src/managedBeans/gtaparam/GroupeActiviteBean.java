package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;

import com.yesserp.domain.gtaparam.ActiviteAbsence;
import com.yesserp.domain.gtaparam.ActivitePresence;
import com.yesserp.domain.gtaparam.GroupeActivite;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsenceLocal;
import com.yesserp.sessionbean.paramgta.gestionactivitepresence.GestionActivitePresenceLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupeactivite.GestionGroupeActiviteLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class GroupeActiviteBean {

	private GroupeActivite groupeActivite = new GroupeActivite();
	private List<GroupeActivite> groupeActivites = new ArrayList<>();
	private GroupeActivite selectedGroupeActivite = new GroupeActivite();
	private Libelle libelle = new Libelle();
	private List<ActiviteAbsence> activiteAbsences = new ArrayList<>();
	private List<ActivitePresence> activitePresences = new ArrayList<>();
	private DualListModel<ActiviteAbsence> activiteAbsencePickList = new DualListModel<>();
	private DualListModel<ActivitePresence> activitePresencePickList = new DualListModel<>();
	private List<ActivitePresence> activitePresenceSource = new ArrayList<ActivitePresence>();
	private List<ActivitePresence> activitePresenceTarget = new ArrayList<ActivitePresence>();
	private List<ActiviteAbsence> activiteAbsenceSource = new ArrayList<ActiviteAbsence>();
	private List<ActiviteAbsence> activiteAbsenceTarget = new ArrayList<ActiviteAbsence>();
	private List<ActiviteAbsence> detailsActiviteAbsence = new ArrayList<ActiviteAbsence>();
	private List<ActivitePresence> detailsActivitePresence = new ArrayList<ActivitePresence>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionGroupeActiviteLocal gestionGroupeActiviteLocal;
	@EJB
	GestionActiviteAbsenceLocal gestionActiviteAbsenceLocal;
	@EJB
	GestionActivitePresenceLocal gestionActivitePresenceLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;

	public GroupeActiviteBean() {
		super();

	}

	@PostConstruct
	public void init() {
		try {
			this.setGroupeActivites(gestionGroupeActiviteLocal
					.listeGroupeActivite());
			for (GroupeActivite groupeActivite : groupeActivites) {
				groupeActivite
						.setLibelles(gestionLibelleLocal
								.findLibelleByGroupeActivite(groupeActivite
										.getIdgrpa()));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void commandAddGroupe() {
		activiteAbsenceSource = gestionActiviteAbsenceLocal.findAll();
		activiteAbsenceTarget = new ArrayList<ActiviteAbsence>();
		activitePresenceSource = gestionActivitePresenceLocal.findAll();
		activitePresenceTarget = new ArrayList<ActivitePresence>();

		activiteAbsencePickList = new DualListModel<ActiviteAbsence>(
				activiteAbsenceSource, activiteAbsenceTarget);
		activitePresencePickList = new DualListModel<ActivitePresence>(
				activitePresenceSource, activitePresenceTarget);
	}

	public void commandEditGroupe() {
		activiteAbsenceTarget = gestionActiviteAbsenceLocal
				.getActiviteAbsencesParGroupe(selectedGroupeActivite);
		activiteAbsenceSource = gestionActiviteAbsenceLocal
				.getActiviteAbsencesNotInGroupe(selectedGroupeActivite);
		activiteAbsencePickList = new DualListModel<>(activiteAbsenceSource,
				activiteAbsenceTarget);
		activitePresenceTarget = gestionActivitePresenceLocal
				.getActivitePresencesParGroupe(selectedGroupeActivite);
		activitePresenceSource = gestionActivitePresenceLocal
				.getActivitePresencesNotInGroupe(selectedGroupeActivite);
		activitePresencePickList = new DualListModel<>(activitePresenceSource,
				activitePresenceTarget);
	}

	public void detailsGroupeActivite() {
		try {
			detailsActiviteAbsence = gestionActiviteAbsenceLocal
					.getActiviteAbsencesParGroupe(selectedGroupeActivite);
			for (ActiviteAbsence absence : detailsActiviteAbsence) {
				absence.setLibelles(gestionLibelleLocal
						.findLibelleByActiviteAbsence(absence));
			}
			detailsActivitePresence = gestionActivitePresenceLocal
					.getActivitePresencesParGroupe(selectedGroupeActivite);
			for (ActivitePresence presence : detailsActivitePresence) {
				presence.setLibelles(gestionLibelleLocal
						.findLibelleByActivitePresence(presence.getIdap()));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void ajouterGroupeActivite() {
		try {

			List<Libelle> libs = new ArrayList<>();
			libs.add(libelle);
			groupeActivite.setLibelles(libs);
			groupeActivite.setActiviteAbsences(activiteAbsencePickList
					.getTarget());
			groupeActivite.setActivitePresences(activitePresencePickList
					.getTarget());
			libelle.setGroupeActivite(groupeActivite);
			gestionGroupeActiviteLocal.ajouterGroupeActivite(groupeActivite);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, groupeActivite
							.getCodegrpa(), "<"
							+ groupeActivite.getLibelles().get(0).getLib()
							+ "> a été ajouté avec succès."));

			libelle = new Libelle();
			groupeActivite = new GroupeActivite();
			init();
		} catch (Exception exception) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ groupeActivite.getCodegrpa(),
									" n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierGroupeActivite() {
		try {
			selectedGroupeActivite.setActiviteAbsences(activiteAbsencePickList
					.getTarget());
			selectedGroupeActivite
					.setActivitePresences(activitePresencePickList.getTarget());
			gestionGroupeActiviteLocal
					.modifierGroupeActivite(selectedGroupeActivite);
			gestionLibelleLocal.modifierLibelle(selectedGroupeActivite
					.getLibelles().get(0));

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedGroupeActivite.getCodegrpa(), "<"
									+ selectedGroupeActivite.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));

			detailsGroupeActivite();
			init();
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

	public void supprimerGroupeAct() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedGroupeActivite.getLibelles().get(0));
		try {
			gestionGroupeActiviteLocal
					.supprimerGroupeActivite(selectedGroupeActivite);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedGroupeActivite.getCodegrpa(), "<"
									+ selectedGroupeActivite.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));

			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			selectedGroupeActivite.setLibelles(libs);
			gestionGroupeActiviteLocal
					.modifierGroupeActivite(selectedGroupeActivite);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! "
							+ selectedGroupeActivite.getCodegrpa(),
							"n'a pas pu être supprimé."));
		}
	}

	public GroupeActivite getGroupeActivite() {
		return groupeActivite;
	}

	public void setGroupeActivite(GroupeActivite groupeActivite) {
		this.groupeActivite = groupeActivite;
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

	public List<GroupeActivite> getGroupeActivites() {
		return groupeActivites;
	}

	public void setGroupeActivites(List<GroupeActivite> groupeActivites) {
		this.groupeActivites = groupeActivites;
	}

	public GroupeActivite getSelectedGroupeActivite() {
		return selectedGroupeActivite;
	}

	public void setSelectedGroupeActivite(GroupeActivite selectedGroupeActivite) {
		this.selectedGroupeActivite = selectedGroupeActivite;
	}

	public DualListModel<ActiviteAbsence> getActiviteAbsencePickList() {
		return activiteAbsencePickList;
	}

	public void setActiviteAbsencePickList(
			DualListModel<ActiviteAbsence> activiteAbsencePickList) {
		this.activiteAbsencePickList = activiteAbsencePickList;
	}

	public DualListModel<ActivitePresence> getActivitePresencePickList() {
		return activitePresencePickList;
	}

	public void setActivitePresencePickList(
			DualListModel<ActivitePresence> activitePresencePickList) {
		this.activitePresencePickList = activitePresencePickList;
	}

	public List<ActivitePresence> getActivitePresenceSource() {
		return activitePresenceSource;
	}

	public void setActivitePresenceSource(
			List<ActivitePresence> activitePresenceSource) {
		this.activitePresenceSource = activitePresenceSource;
	}

	public List<ActivitePresence> getActivitePresenceTarget() {
		return activitePresenceTarget;
	}

	public void setActivitePresenceTarget(
			List<ActivitePresence> activitePresenceTarget) {
		this.activitePresenceTarget = activitePresenceTarget;
	}

	public List<ActiviteAbsence> getActiviteAbsenceSource() {
		return activiteAbsenceSource;
	}

	public void setActiviteAbsenceSource(
			List<ActiviteAbsence> activiteAbsenceSource) {
		this.activiteAbsenceSource = activiteAbsenceSource;
	}

	public List<ActiviteAbsence> getActiviteAbsenceTarget() {
		return activiteAbsenceTarget;
	}

	public void setActiviteAbsenceTarget(
			List<ActiviteAbsence> activiteAbsenceTarget) {
		this.activiteAbsenceTarget = activiteAbsenceTarget;
	}

	public List<ActiviteAbsence> getDetailsActiviteAbsence() {
		return detailsActiviteAbsence;
	}

	public void setDetailsActiviteAbsence(
			List<ActiviteAbsence> detailsActiviteAbsence) {
		this.detailsActiviteAbsence = detailsActiviteAbsence;
	}

	public List<ActivitePresence> getDetailsActivitePresence() {
		return detailsActivitePresence;
	}

	public void setDetailsActivitePresence(
			List<ActivitePresence> detailsActivitePresence) {
		this.detailsActivitePresence = detailsActivitePresence;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}

}
