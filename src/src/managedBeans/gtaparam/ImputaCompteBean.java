package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.ImputaCompte;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionimputacompte.GestionImputaCompteLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ImputaCompteBean {
	private Libelle libelle = new Libelle();
	private ImputaCompte imputaCompte = new ImputaCompte();
	private ImputaCompte selectedImputaCompte = new ImputaCompte();
	private List<ImputaCompte> imputaComptes = new ArrayList<ImputaCompte>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionImputaCompteLocal gestionImputaCompteLocal;

	@PostConstruct
	public void init() {
		try {
			this.setImputaComptes(gestionImputaCompteLocal.findAll());
			for (ImputaCompte p : imputaComptes) {
				try {
					p.setLibelles(gestionLibelleLocal
							.findLibelleByImputaCompte(p));
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

	public void ajouterImputaCompte() {
		try {

			List<Libelle> libelles = new ArrayList<Libelle>();
			libelles.add(libelle);
			imputaCompte.setLibelles(libelles);
			libelle.setImputaCompte(imputaCompte);
			gestionImputaCompteLocal.ajouterCompte(imputaCompte);
			imputaComptes.add(imputaCompte);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, imputaCompte
							.getCode(), "<"
							+ imputaCompte.getLibelles().get(0).getLib()
							+ "> a été ajouté avec succès."));
			libelle = new Libelle();
			imputaCompte = new ImputaCompte();

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! " + imputaCompte.getCode(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierImputaCompte() {
		try {

			gestionImputaCompteLocal.modifierCompte(selectedImputaCompte);
			gestionLibelleLocal.modifierLibelle(selectedImputaCompte
					.getLibelles().get(0));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaCompte.getCode(), "<"
									+ selectedImputaCompte.getLibelles().get(0)
											.getLib()
									+ "> a été modifié avec succès."));

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedImputaCompte.getCode(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerImputaCompte() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedImputaCompte.getLibelles().get(0));
		try {
			gestionImputaCompteLocal.supprimerCompte(selectedImputaCompte);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaCompte.getCode(), "<"
									+ selectedImputaCompte.getLibelles().get(0)
											.getLib()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			selectedImputaCompte.setLibelles(libs);
			gestionImputaCompteLocal.modifierCompte(selectedImputaCompte);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedImputaCompte.getCode(),
							" n'a pas pu être supprimé."));

		}

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

	public List<ImputaCompte> getImputaComptes() {
		return imputaComptes;
	}

	public void setImputaComptes(List<ImputaCompte> imputaComptes) {
		this.imputaComptes = imputaComptes;
	}

	public ImputaCompte getSelectedImputaCompte() {
		return selectedImputaCompte;
	}

	public void setSelectedImputaCompte(ImputaCompte selectedImputaCompte) {
		this.selectedImputaCompte = selectedImputaCompte;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
