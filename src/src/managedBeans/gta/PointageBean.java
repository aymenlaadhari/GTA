package managedBeans.gta;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.yesserp.domain.access.Groupe;
import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.Pointage;
import com.yesserp.domain.gta.Terminal;
import com.yesserp.domain.gta.TypePointage;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionpointage.GestionPointageLocal;

@ManagedBean
@SessionScoped
public class PointageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private GestionPointageLocal gestionPointageLocal;
	@EJB
	private GestionIdentiteLocal gestionIdentite;

	private Pointage pointage = new Pointage();

	private List<Pointage> pointages;

	private List<Pointage> pointagespardate;

	private List<SelectItem> filterOption;
	private List<Pointage> filtredPointages;

	private List<Identite> identites;
	private Identite selectedIdentite;
	private StreamedContent photoC = null;
	private String disabledBtnPlus = "true";
	private StreamedContent photo = null;
	private String renderedimage = "false";
	private TypePointage sens = new TypePointage();
	private Terminal terminal = new Terminal();
	private TypePointage selectedSens;
	private Terminal selectedterminal;

	private List<TypePointage> collectionSens = new ArrayList<TypePointage>();
	private List<Terminal> terminaux = new ArrayList<Terminal>();

	// identite
	private Long numdossier;
	private String matricul = "";
	private String nom = "";

	public String getMatricul() {
		return matricul;
	}

	public void setMatricul(String matricul) {
		this.matricul = matricul;
	}

	private String prenom = "";
	private String sexe = "";
	private String poste = "";
	private String uniteOrganisationnelle = "";
	private String stCivile = "";
	private int maxSelectCollaborateurs = 10;
	private String automatique = "true";
	private String manuelle = "false";

	public PointageBean() {
		super();
		sens = new TypePointage();
		terminal = new Terminal();
	}

	@PostConstruct
	public void init() {
		selectedIdentite = new Identite();
		pointages = new ArrayList<Pointage>();
		pointage = new Pointage();
		photoC = null;
		disabledBtnPlus = "true";
		photo = null;
		renderedimage = "false";
		sens = new TypePointage();
		terminal = new Terminal();
		selectedSens = new TypePointage();
		selectedterminal = new Terminal();

		// pointages = gestionPointageLocal.listPointage();
	}

	public void valider() {
	}

	public void chargerCollaborateurs() {

		if ((!uniteOrganisationnelle.equals("") || !poste.equals(""))
				&& (!nom.equals("") || !prenom.equals("") || !sexe.equals("") || !stCivile
						.equals(""))) {
			if (uniteOrganisationnelle.equals(""))
				uniteOrganisationnelle = "%";
			if (poste.equals(""))
				poste = "%";
			if (matricul.equals(""))
				matricul = "%";
			if (nom.equals(""))
				nom = "%";
			if (prenom.equals(""))
				prenom = "%";
			if (sexe.equals(""))
				sexe = "%";
			if (stCivile.equals(""))
				stCivile = "%";

			identites = (ArrayList<Identite>) gestionIdentite
					.trouverParLikeMatriculeNomPrenomSexePosteUo(matricul, nom,
							prenom, sexe, poste, uniteOrganisationnelle,
							maxSelectCollaborateurs);
		} else if (!uniteOrganisationnelle.equals("") || !poste.equals("")) {
			if (uniteOrganisationnelle.equals(""))
				uniteOrganisationnelle = "%";
			if (poste.equals(""))
				poste = "%";
			if (matricul.equals(""))
				matricul = "%";

			identites = (ArrayList<Identite>) gestionIdentite
					.trouverParLikeMatriculePosteUo(matricul, poste,
							uniteOrganisationnelle, maxSelectCollaborateurs);
		} else if (!matricul.equals("") || !nom.equals("")
				|| !prenom.equals("") || !sexe.equals("")
				|| !stCivile.equals("")) {
			if (nom.equals(""))
				nom = "%";
			if (prenom.equals(""))
				prenom = "%";
			if (sexe.equals(""))
				sexe = "%";
			if (stCivile.equals(""))
				stCivile = "%";
			if (matricul.equals(""))
				matricul = "%";

			identites = (ArrayList<Identite>) gestionIdentite
					.trouverParLikeMatriculeNomPrenomSexe(matricul, nom,
							prenom, sexe, maxSelectCollaborateurs);
		}
		if (uniteOrganisationnelle.equals("%"))
			uniteOrganisationnelle = "";
		if (poste.equals("%"))
			poste = "";
		if (nom.equals("%"))
			nom = "";
		if (prenom.equals("%"))
			prenom = "";
		if (sexe.equals("%"))
			sexe = "";
		if (stCivile.equals("%"))
			stCivile = "";
		if (matricul.equals("%"))
			matricul = "";

	}

	public void ajoutPointage() {

		pointage = new Pointage();
		pointages.add(pointage);
	}

	public void savePointage() {
		

		 pointages.get(pointages.size() - 1).setIdentite(selectedIdentite);
		// pointages.get(pointages.size() - 1).setSens(selectedSens);
		System.out.println("je veux ajouter "
				+  pointages.get(pointages.size() - 1).getSens().getLibelle());
		gestionPointageLocal
				.updatePointage(pointages.get(pointages.size() - 1));

		pointages = gestionPointageLocal.listPointage();

		pointage = new Pointage();

	}

	public void modifierPointage() {

		try {

			pointage.setIdentite(selectedIdentite);

			gestionPointageLocal.updatePointage(pointage);
			pointages = gestionPointageLocal.listPointage();

			pointage = new Pointage();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void chargerPointages() {

		disabledBtnPlus = "false";
		renderedimage = "true";
		pointages = new ArrayList<Pointage>();
		pointages = gestionPointageLocal
				.findPointageByIdentite(selectedIdentite);
	}

	public void onRowSelect(SelectEvent event) {
		disabledBtnPlus = "false";
	}

	public void deletePointage() {

		gestionPointageLocal.deletePointage(pointage);

		gestionPointageLocal.deletePointage(pointage);
		pointages = gestionPointageLocal.listPointage();

		pointage = new Pointage();
	}

	public void cancel() {
		pointage = new Pointage();
	}

	public Pointage getPointage() {
		return pointage;
	}

	public void setPointage(Pointage pointage) {
		this.pointage = pointage;
	}

	public List<Pointage> getPointages() {
		return pointages;
	}

	public void setPointages(List<Pointage> pointages) {
		this.pointages = pointages;
	}

	public List<SelectItem> getFilterOption() {
		return filterOption;
	}

	public void setFilterOption(List<SelectItem> filterOption) {
		this.filterOption = filterOption;
	}

	public List<Pointage> getFiltredPointages() {
		return filtredPointages;
	}

	public void setFiltredPointages(List<Pointage> filtredPointages) {
		this.filtredPointages = filtredPointages;
	}

	public List<Identite> getIdentites() {

		// identites = gestionIdentite.findAll();
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

	public Identite getSelectedIdentite() {
		return selectedIdentite;
	}

	public void setSelectedIdentite(Identite selectedIdentite) {
		this.selectedIdentite = selectedIdentite;
	}

	public void listeDesPointages() {

	}

	public String getDisabledBtnPlus() {
		return disabledBtnPlus;
	}

	public void setDisabledBtnPlus(String disabledBtnPlus) {
		this.disabledBtnPlus = disabledBtnPlus;
	}

	public StreamedContent getPhotoC() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real
			// StreamedContent with the image bytes.
			String idColl = context.getExternalContext()
					.getRequestParameterMap().get("collid");
			byte[] img = gestionIdentite.trouverParNumeroDossier(
					Long.valueOf(idColl)).getPhoto();
			if (img != null)
				return new DefaultStreamedContent(
						new ByteArrayInputStream(img), "image/png");
			else {
				return null;
			}
		}

	}

	public void setPhotoC(StreamedContent photoC) {
		this.photoC = photoC;
	}

	public StreamedContent getPhoto() {
		byte[] imageInByte2 = null;
		photo = null;
		if (selectedIdentite != null && selectedIdentite.getPhoto() != null) {

			imageInByte2 = selectedIdentite.getPhoto();
			InputStream is = new ByteArrayInputStream(imageInByte2);

			photo = new DefaultStreamedContent(is, "image/png");

		}

		return photo;
	}

	public void setPhoto(StreamedContent photo) {
		this.photo = photo;
	}

	public String getRenderedimage() {
		return renderedimage;
	}

	public void setRenderedimage(String renderedimage) {
		this.renderedimage = renderedimage;
	}

	public void TrouverPointage() {
		if (pointage != null) {
			pointage = gestionPointageLocal.findPointageById(pointage.getIdp());
			if (pointage.getTerminal().getId() == null) {
				automatique = "false";
				manuelle = "true";
			} else {
				automatique = "true";
				manuelle = "false";
			}
		}
	}

	public String getAutomatique() {
		return automatique;
	}

	public void setAutomatique(String automatique) {
		this.automatique = automatique;
	}

	public String getManuelle() {
		return manuelle;
	}

	public void setManuelle(String manuelle) {
		this.manuelle = manuelle;
	}

	public TypePointage getSens() {
		return sens;
	}

	public void setSens(TypePointage sens) {
		sens = new TypePointage();
		this.sens = sens;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		terminal = new Terminal();
		this.terminal = terminal;
	}

	public void AjouterSens() {

		gestionPointageLocal.SauvegarderTypePointage(sens);
		sens = new TypePointage();

	}

	public void AjouterTerminal() {
		gestionPointageLocal.SauvegarderTerminal(terminal);
	}

	public void SupprimerSens() {

		gestionPointageLocal.SupprimerTypePointage(selectedSens);
		selectedSens = new TypePointage();
	}

	public void SupprimerTerminal() {
		gestionPointageLocal.SupprimerTerminal(selectedterminal);
		selectedterminal = new Terminal();
	}

	public List<TypePointage> getCollectionSens() {
		collectionSens = gestionPointageLocal.ChercherSensPointage();
		return collectionSens;
	}

	public void setCollectionSens(List<TypePointage> collectionSens) {
		this.collectionSens = collectionSens;
	}

	public List<Terminal> getTerminaux() {
		terminaux = gestionPointageLocal.ChercherTerminaux();
		return terminaux;
	}

	public void setTerminaux(List<Terminal> terminaux) {
		this.terminaux = terminaux;
	}

	public List<Pointage> getPointagespardate() {
		return pointagespardate;
	}

	public void setPointagespardate(List<Pointage> pointagespardate) {
		this.pointagespardate = pointagespardate;
	}

	public TypePointage getSelectedSens() {

		return selectedSens;
	}

	public void setSelectedSens(TypePointage selectedSens) {
		this.selectedSens = selectedSens;
	}

	public Terminal getSelectedterminal() {
		return selectedterminal;
	}

	public void setSelectedterminal(Terminal selectedterminal) {
		this.selectedterminal = selectedterminal;
	}

	public void ModifierTypePointage() {
		gestionPointageLocal.ModifierTypePointage(selectedSens);
		selectedSens = new TypePointage();
	}

	public void ModifierTerminal() {
		gestionPointageLocal.ModifierTerminal(selectedterminal);
		selectedterminal = new Terminal();
	}

}
