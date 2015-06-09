package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gta.TypePointage;
import com.yesserp.sessionbean.paramgta.gestiontypepointage.GestionTypePointageLocal;

@ManagedBean
@ViewScoped
public class TypePointageBean {
	private TypePointage typePointage = new TypePointage();
	private TypePointage selectedTypePointage = new TypePointage();
	private List<TypePointage> typePointages = new ArrayList<TypePointage>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionTypePointageLocal gestionTypePointageLocal;

	@PostConstruct
	public void init() {
		try {
			this.setTypePointages(gestionTypePointageLocal.findAll());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void ajouterTypePointage() {
		try {
			gestionTypePointageLocal.ajouterTypePointage(typePointage);
			typePointages.add(typePointage);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, typePointage
							.getCode(), "<" + typePointage.getLibelle()
							+ "> a été ajouté avec succès."));

			typePointage = new TypePointage();
		} catch (Exception exception) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! " + typePointage.getCode(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));

		}
	}

	public void modifierTypePointage() {
		try {
			gestionTypePointageLocal.modifierTypePointage(selectedTypePointage);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedTypePointage.getCode(), "<"
									+ selectedTypePointage.getLibelle()
									+ "> a été modifié avec succès."));
		} catch (Exception exception) {
			exception.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedTypePointage.getCode(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerTypePointage() {
		try {
			gestionTypePointageLocal
					.supprimerTypePointage(selectedTypePointage);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedTypePointage.getCode(), "<"
									+ selectedTypePointage.getLibelle()
									+ "> a été supprimé avec succès."));

			init();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public TypePointage getTypePointage() {
		return typePointage;
	}

	public void setTypePointage(TypePointage typePointage) {
		this.typePointage = typePointage;
	}

	public TypePointage getSelectedTypePointage() {
		return selectedTypePointage;
	}

	public void setSelectedTypePointage(TypePointage selectedTypePointage) {
		this.selectedTypePointage = selectedTypePointage;
	}

	public List<TypePointage> getTypePointages() {
		return typePointages;
	}

	public void setTypePointages(List<TypePointage> typePointages) {
		this.typePointages = typePointages;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
