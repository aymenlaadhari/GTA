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

import com.yesserp.domain.gtaparam.GroupeImputation;
import com.yesserp.domain.gtaparam.Imputation;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestiongroupeimputation.GestionGroupeImputationLocal;
import com.yesserp.sessionbean.paramgta.gestionimputation.GestionImputationLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class GroupeImputationBean {
	private GroupeImputation groupeImputation = new GroupeImputation();
	private GroupeImputation selectedGroupeImputation = new GroupeImputation();
	private List<GroupeImputation> groupeImputations = new ArrayList<GroupeImputation>();
	private List<Imputation> detailsGrpImputations = new ArrayList<Imputation>();
	private Imputation imputation = new Imputation();
	private List<Imputation> imputations = new ArrayList<>();
	private Libelle libelle = new Libelle();
	private DualListModel<Imputation> listModel = new DualListModel<>();
	private List<Imputation> imputaSource = new ArrayList<Imputation>();
	private List<Imputation> imputaTarget = new ArrayList<Imputation>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionImputationLocal gestionImputationLocal;
	@EJB
	GestionGroupeImputationLocal gestionGroupeImputationLocal;

	public GroupeImputationBean() {
	}

	@PostConstruct
	public void init() {
		try {
			this.setGroupeImputations(gestionGroupeImputationLocal
					.listeGroupeImpuation());
			for (GroupeImputation gi : groupeImputations) {
				try {
					gi.setLibelles(gestionLibelleLocal
							.findLibelleByGroupeImputation(gi));
				} catch (Exception exception) {
					exception.printStackTrace();
				}

			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void commandAddGroupe() {
		imputaSource = gestionImputationLocal.listeImputation();
		imputaTarget = new ArrayList<Imputation>();
		listModel = new DualListModel<>(imputaSource, imputaTarget);
	}

	public void commandEditGroupe() {
		imputaTarget = gestionGroupeImputationLocal
				.findImputationsByGroupeImputations(selectedGroupeImputation);
		imputaSource = gestionImputationLocal
				.getImputationNotInGroupe(selectedGroupeImputation);
		listModel = new DualListModel<>(imputaSource, imputaTarget);
	}

	public void ajouterGroupeImputation() {
		try {

			List<Libelle> libelles = new ArrayList<>();
			libelles.add(libelle);
			groupeImputation.setLibelles(libelles);
			groupeImputation.setImputations(listModel.getTarget());
			libelle.setGroupeImputation(groupeImputation);
			gestionGroupeImputationLocal
					.ajouterGroupeImputation(groupeImputation);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							groupeImputation.getCodegrp(), "<"
									+ groupeImputation.getLibelles().get(0)
											.getLib()
									+ "> a été ajouté avec succès."));
			groupeImputation = new GroupeImputation();
			imputaTarget = new ArrayList<>();
			libelle = new Libelle();
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ groupeImputation.getCodegrp(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierGroupeImputation() {
		try {
			selectedGroupeImputation.setImputations(listModel.getTarget());
			gestionGroupeImputationLocal
					.modifierGroupeImputation(selectedGroupeImputation);
			gestionLibelleLocal.modifierLibelle(selectedGroupeImputation
					.getLibelles().get(0));
			detailsGroupeImputation();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedGroupeImputation.getCodegrp(), "<"
									+ selectedGroupeImputation.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));

			init();
		} catch (Exception exception) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedGroupeImputation.getCodegrp(),
							"n'a pas pu être modifié."));
		}

	}

	public void supprimerGroupeImputation() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedGroupeImputation.getLibelles().get(0));
		try {
			gestionGroupeImputationLocal
					.supprimerGroupeImputation(selectedGroupeImputation);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedGroupeImputation.getCodegrp(), "<"
									+ selectedGroupeImputation.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			selectedGroupeImputation.setLibelles(libs);
			gestionGroupeImputationLocal
					.modifierGroupeImputation(selectedGroupeImputation);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedGroupeImputation.getCodegrp(),
							" n'a pas pu être supprimé."));

		}
	}

	public void detailsGroupeImputation() {

		try {
			detailsGrpImputations = gestionGroupeImputationLocal
					.findImputationsByGroupeImputations(selectedGroupeImputation);
			for (Imputation i : detailsGrpImputations) {
				try {
					i.setLibelles(gestionLibelleLocal
							.findLibelleByImputation(i));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public GroupeImputation getGroupeImputation() {
		return groupeImputation;
	}

	public void setGroupeImputation(GroupeImputation groupeImputation) {
		this.groupeImputation = groupeImputation;
	}

	public Imputation getImputation() {
		return imputation;
	}

	public void setImputation(Imputation imputation) {
		this.imputation = imputation;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<Imputation> getImputations() {
		return imputations;
	}

	public void setImputations(List<Imputation> imputations) {
		this.imputations = imputations;
	}

	public List<GroupeImputation> getGroupeImputations() {
		return groupeImputations;
	}

	public void setGroupeImputations(List<GroupeImputation> groupeImputations) {
		this.groupeImputations = groupeImputations;
	}

	public GroupeImputation getSelectedGroupeImputation() {
		return selectedGroupeImputation;
	}

	public void setSelectedGroupeImputation(
			GroupeImputation selectedGroupeImputation) {
		this.selectedGroupeImputation = selectedGroupeImputation;
	}

	public List<Imputation> getDetailsGrpImputations() {
		return detailsGrpImputations;
	}

	public void setDetailsGrpImputations(List<Imputation> detailsGrpImputations) {
		this.detailsGrpImputations = detailsGrpImputations;
	}

	public DualListModel<Imputation> getListModel() {
		return listModel;
	}

	public void setListModel(DualListModel<Imputation> listModel) {
		this.listModel = listModel;
	}

	public List<Imputation> getImputaTarget() {
		return imputaTarget;
	}

	public void setImputaTarget(List<Imputation> imputaTarget) {
		this.imputaTarget = imputaTarget;
	}

	public List<Imputation> getImputaSource() {
		return imputaSource;
	}

	public void setImputaSource(List<Imputation> imputaSource) {
		this.imputaSource = imputaSource;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
