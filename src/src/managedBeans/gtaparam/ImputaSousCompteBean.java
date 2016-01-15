package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.ImputaSousCompte;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionimputasouscompte.GestionImputaSousCompteLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ImputaSousCompteBean {
	private Libelle libelle = new Libelle();
	private ImputaSousCompte imputaSousCompte = new ImputaSousCompte();
	private ImputaSousCompte selectedImputaSousCompte = new ImputaSousCompte();
	private List<ImputaSousCompte> imputaSousComptes = new ArrayList<ImputaSousCompte>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionImputaSousCompteLocal gestionImputaSousCompteLocal;

	@PostConstruct
	public void init() {
		try {

			this.setImputaSousComptes(gestionImputaSousCompteLocal.findAll());
			for (ImputaSousCompte p : imputaSousComptes) {
				try {
					p.setLibelles(gestionLibelleLocal
							.findLibelleByImputaSousCompte(p));
				} catch (Exception exception) {
					exception.printStackTrace();
				}

			}
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}

	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void ajouterImputaSousCompte() {
		try {

			List<Libelle> libelles = new ArrayList<Libelle>();
			libelles.add(libelle);
			imputaSousCompte.setLibelles(libelles);
			libelle.setImputaSousCompte(imputaSousCompte);
			gestionImputaSousCompteLocal.ajouterSousCompte(imputaSousCompte);
			imputaSousComptes.add(imputaSousCompte);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							imputaSousCompte.getCode(), "<"
									+ imputaSousCompte.getLibelles().get(0)
											.getLib()
									+ "> a été ajouté avec succès."));

			libelle = new Libelle();
			imputaSousCompte = new ImputaSousCompte();

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ imputaSousCompte.getCode(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierImputaSousCompte() {
		try {

			gestionImputaSousCompteLocal
					.modifierSousCompte(selectedImputaSousCompte);
			gestionLibelleLocal.modifierLibelle(selectedImputaSousCompte
					.getLibelles().get(0));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaSousCompte.getCode(), "<"
									+ selectedImputaSousCompte.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedImputaSousCompte.getCode(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerImputaSousCompte() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedImputaSousCompte.getLibelles().get(0));
		try {
			gestionImputaSousCompteLocal
					.supprimerSousCompte(selectedImputaSousCompte);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaSousCompte.getCode(), "<"
									+ selectedImputaSousCompte.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			selectedImputaSousCompte.setLibelles(libs);
			gestionImputaSousCompteLocal
					.modifierSousCompte(selectedImputaSousCompte);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedImputaSousCompte.getCode(),
							" n'a pas pu être supprimé."));

		}

	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public ImputaSousCompte getImputaSousCompte() {
		return imputaSousCompte;
	}

	public void setImputaSousCompte(ImputaSousCompte imputaSousCompte) {
		this.imputaSousCompte = imputaSousCompte;
	}

	public List<ImputaSousCompte> getImputaSousComptes() {
		return imputaSousComptes;
	}

	public void setImputaSousComptes(List<ImputaSousCompte> imputaSousComptes) {
		this.imputaSousComptes = imputaSousComptes;
	}

	public ImputaSousCompte getSelectedImputaSousCompte() {
		return selectedImputaSousCompte;
	}

	public void setSelectedImputaSousCompte(
			ImputaSousCompte selectedImputaSousCompte) {
		this.selectedImputaSousCompte = selectedImputaSousCompte;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
