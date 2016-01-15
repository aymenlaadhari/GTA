package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.ImputaCodeFact;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionimputacodefacturation.GestionImputaCodeFacturationLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ImputaCodeFactBean {
	private Libelle libelle = new Libelle();
	private ImputaCodeFact imputaCodeFact = new ImputaCodeFact();
	private ImputaCodeFact selectedImputaCodeFact = new ImputaCodeFact();
	private List<ImputaCodeFact> imputaCodeFacts = new ArrayList<ImputaCodeFact>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionImputaCodeFacturationLocal gestionImputaCodeFacturationLocal;

	@PostConstruct
	public void init() {
		try {
			this.setImputaCodeFacts(gestionImputaCodeFacturationLocal.findAll());
			for (ImputaCodeFact p : imputaCodeFacts) {
				try {

					p.setLibelles(gestionLibelleLocal
							.findLibelleByImputaCodeFact(p));
				} catch (Exception exception) {
					exception.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void ajouterImputaCodeFact() {
		try {

			List<Libelle> libelles = new ArrayList<Libelle>();
			libelles.add(libelle);
			imputaCodeFact.setLibelles(libelles);
			libelle.setImputaCodeFact(imputaCodeFact);
			gestionImputaCodeFacturationLocal
					.ajouterCodeFacturation(imputaCodeFact);
			imputaCodeFacts.add(imputaCodeFact);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, imputaCodeFact
							.getCode(), "<"
							+ imputaCodeFact.getLibelles().get(0).getLib()
							+ "> a été ajouté avec succès."));
			libelle = new Libelle();
			imputaCodeFact = new ImputaCodeFact();

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ imputaCodeFact.getCode(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierImputaCodeFact() {
		try {

			gestionImputaCodeFacturationLocal
					.modifierCodeFacturation(selectedImputaCodeFact);
			gestionLibelleLocal.modifierLibelle(selectedImputaCodeFact
					.getLibelles().get(0));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaCodeFact.getCode(), "<"
									+ selectedImputaCodeFact.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedImputaCodeFact.getCode(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerImputaCodeFact() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedImputaCodeFact.getLibelles().get(0));
		try {
			gestionImputaCodeFacturationLocal
					.supprimerCodeFacturation(selectedImputaCodeFact);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaCodeFact.getCode(), "<"
									+ selectedImputaCodeFact.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			selectedImputaCodeFact.setLibelles(libs);
			gestionImputaCodeFacturationLocal
					.modifierCodeFacturation(selectedImputaCodeFact);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedImputaCodeFact.getCode(),
							" n'a pas pu être supprimé."));

		}
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public ImputaCodeFact getImputaCodeFact() {
		return imputaCodeFact;
	}

	public void setImputaCodeFact(ImputaCodeFact imputaCodeFact) {
		this.imputaCodeFact = imputaCodeFact;
	}

	public List<ImputaCodeFact> getImputaCodeFacts() {
		return imputaCodeFacts;
	}

	public void setImputaCodeFacts(List<ImputaCodeFact> imputaCodeFacts) {
		this.imputaCodeFacts = imputaCodeFacts;
	}

	public ImputaCodeFact getSelectedImputaCodeFact() {
		return selectedImputaCodeFact;
	}

	public void setSelectedImputaCodeFact(ImputaCodeFact selectedImputaCodeFact) {
		this.selectedImputaCodeFact = selectedImputaCodeFact;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
