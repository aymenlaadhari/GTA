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
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestionimputacentrecout.GestionImputaCentreCoutLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class ImputaCentreCoutBean {
	private Libelle libelle = new Libelle();
	private ImputaCentreCout imputaCentreCout = new ImputaCentreCout();
	private ImputaCentreCout selectedImputaCentreCout = new ImputaCentreCout();
	private List<ImputaCentreCout> imputaCentreCouts = new ArrayList<ImputaCentreCout>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionImputaCentreCoutLocal gestionImputaCentreCoutLocal;

	@PostConstruct
	public void init() {
		try {
			this.setImputaCentreCouts(gestionImputaCentreCoutLocal.findAll());
			for (ImputaCentreCout p : imputaCentreCouts) {
				try {
					p.setLibelles(gestionLibelleLocal
							.findLibelleByImputaCentreCout(p));
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

	public void ajouterImputaCentreCout() {
		try {

			List<Libelle> libelles = new ArrayList<Libelle>();
			libelles.add(libelle);
			imputaCentreCout.setLibelles(libelles);
			libelle.setImputaCentreCout(imputaCentreCout);
			gestionImputaCentreCoutLocal.ajouterCentreCout(imputaCentreCout);
			imputaCentreCouts.add(imputaCentreCout);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							imputaCentreCout.getCode(), "<"
									+ imputaCentreCout.getLibelles().get(0)
											.getLib()
									+ "> a été ajouté avec succès."));
			libelle = new Libelle();
			imputaCentreCout = new ImputaCentreCout();

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! "
											+ imputaCentreCout.getCode(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierImputaCentreCout() {
		try {

			gestionImputaCentreCoutLocal
					.modifierCentreCout(selectedImputaCentreCout);
			gestionLibelleLocal.modifierLibelle(selectedImputaCentreCout
					.getLibelles().get(0));
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaCentreCout.getCode(), "<"
									+ selectedImputaCentreCout.getLibelles()
											.get(0).getLib()
									+ "> a été modifié avec succès."));

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedImputaCentreCout.getCode(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerImputaCentreCout() {
		List<Libelle> libs = new ArrayList<Libelle>();
		libs.add(selectedImputaCentreCout.getLibelles().get(0));
		try {
			gestionImputaCentreCoutLocal
					.supprimerCentreCout(selectedImputaCentreCout);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedImputaCentreCout.getCode(), "<"
									+ selectedImputaCentreCout.getLibelles()
											.get(0).getLib()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			selectedImputaCentreCout.setLibelles(libs);
			gestionImputaCentreCoutLocal
					.modifierCentreCout(selectedImputaCentreCout);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedImputaCentreCout.getCode(),
							" n'a pas pu être supprimé."));

		}
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public ImputaCentreCout getImputaCentreCout() {
		return imputaCentreCout;
	}

	public void setImputaCentreCout(ImputaCentreCout imputaCentreCout) {
		this.imputaCentreCout = imputaCentreCout;
	}

	public List<ImputaCentreCout> getImputaCentreCouts() {
		return imputaCentreCouts;
	}

	public void setImputaCentreCouts(List<ImputaCentreCout> imputaCentreCouts) {
		this.imputaCentreCouts = imputaCentreCouts;
	}

	public ImputaCentreCout getSelectedImputaCentreCout() {
		return selectedImputaCentreCout;
	}

	public void setSelectedImputaCentreCout(
			ImputaCentreCout selectedImputaCentreCout) {
		this.selectedImputaCentreCout = selectedImputaCentreCout;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
