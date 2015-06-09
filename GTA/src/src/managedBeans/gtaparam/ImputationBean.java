package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.ImputaCentreCout;
import com.yesserp.domain.gtaparam.ImputaCodeFact;
import com.yesserp.domain.gtaparam.ImputaCompte;
import com.yesserp.domain.gtaparam.ImputaSousCompte;
import com.yesserp.domain.gtaparam.Imputation;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionimputation.GestionImputationLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ImputationBean {
	private Libelle libelle = new Libelle();
	private Imputation imputation = new Imputation();
	private Imputation selectedImputation = new Imputation();
	private ImputaCompte imputaCompte = new ImputaCompte();
	private ImputaSousCompte imputaSousCompte = new ImputaSousCompte();
	private ImputaCodeFact imputaCodeFact = new ImputaCodeFact();
	private ImputaCentreCout imputaCentreCout = new ImputaCentreCout();
	private List<Imputation> imputations = new ArrayList<Imputation>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionImputationLocal gestionImputationLocal;

	@PostConstruct
	public void init() {
		try {

			this.setImputations(gestionImputationLocal.listeImputation());
			for (Imputation p : imputations) {
				try {
					p.setLibelles(gestionLibelleLocal
							.findLibelleByImputation(p));
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

	public void ajouterImputation() {
		try {

			List<Libelle> libelles = new ArrayList<Libelle>();
			libelles.add(libelle);
			imputation.setLibelles(libelles);
			imputation.setImputaCentreCout(imputaCentreCout);
			imputation.setImputaCompte(imputaCompte);
			imputation.setImputaSousCompte(imputaSousCompte);
			imputation.setImputaCodeFact(imputaCodeFact);
			libelle.setImputation(imputation);
			gestionImputationLocal.ajouterImputation(imputation);
			imputations.add(imputation);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, imputation
							.getCodei(), "<"
							+ imputation.getLibelles().get(0).getLib()
							+ "> a été ajouté avec succès."));

			libelle = new Libelle();
			imputation = new Imputation();
			imputaCentreCout = new ImputaCentreCout();
			imputaCompte = new ImputaCompte();
			imputaSousCompte = new ImputaSousCompte();
			imputaCodeFact = new ImputaCodeFact();

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! " + imputation.getCodei(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierImputation() {
		try {
			gestionImputationLocal.modifierImputation(selectedImputation);
			gestionLibelleLocal.modifierLibelle(selectedImputation
					.getLibelles().get(0));

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputation.getCodei(), "<"
									+ selectedImputation.getLibelles().get(0)
											.getLib()
									+ "> a été modifié avec succès."));

		} catch (Exception exception) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedImputation.getCodei(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerImputation() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedImputation.getLibelles().get(0));
		try {
			gestionImputationLocal.supprimerImputation(selectedImputation);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputation.getCodei(), "<"
									+ selectedImputation.getLibelles().get(0)
											.getLib()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			selectedImputation.setLibelles(libs);
			gestionImputationLocal.modifierImputation(selectedImputation);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedImputation.getCodei(),
							" n'a pas pu être supprimé."));

		}
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

	public ImputaCompte getImputaCompte() {
		return imputaCompte;
	}

	public void setImputaCompte(ImputaCompte imputaCompte) {
		this.imputaCompte = imputaCompte;
	}

	public ImputaSousCompte getImputaSousCompte() {
		return imputaSousCompte;
	}

	public void setImputaSousCompte(ImputaSousCompte imputaSousCompte) {
		this.imputaSousCompte = imputaSousCompte;
	}

	public ImputaCodeFact getImputaCodeFact() {
		return imputaCodeFact;
	}

	public void setImputaCodeFact(ImputaCodeFact imputaCodeFact) {
		this.imputaCodeFact = imputaCodeFact;
	}

	public ImputaCentreCout getImputaCentreCout() {
		return imputaCentreCout;
	}

	public void setImputaCentreCout(ImputaCentreCout imputaCentreCout) {
		this.imputaCentreCout = imputaCentreCout;
	}

	public List<Imputation> getImputations() {
		return imputations;
	}

	public void setImputations(List<Imputation> imputations) {
		this.imputations = imputations;
	}

	public Imputation getSelectedImputation() {
		return selectedImputation;
	}

	public void setSelectedImputation(Imputation selectedImputation) {
		this.selectedImputation = selectedImputation;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}

}
