package managedBeans.gtaparam;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.DualListModel;

import com.yesserp.domain.gtaparam.ActiviteAbsence;
import com.yesserp.domain.gtaparam.ActivitePresence;
import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.Filtre;
import com.yesserp.domain.gtaparam.GroupeActivite;
import com.yesserp.domain.gtaparam.GroupeImputation;
import com.yesserp.domain.gtaparam.Imputation;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.NatureJournee;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsenceLocal;
import com.yesserp.sessionbean.paramgta.gestionactivitepresence.GestionActivitePresenceLocal;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestionfiltre.GestionFiltreLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupeactivite.GestionGroupeActiviteLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupeimputation.GestionGroupeImputationLocal;
import com.yesserp.sessionbean.paramgta.gestionimputation.GestionImputationLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;
import com.yesserp.sessionbean.paramgta.gestionnaturejournee.GestionNatureJourneeLocal;

@ManagedBean
@SessionScoped
public class FiltreBean {

	private Filtre filtre = new Filtre();

	private Libelle libelle = new Libelle();

	private List<GroupeActivite> groupeActivites = new ArrayList<>();
	private List<GroupeImputation> groupeImputations = new ArrayList<>();

	private List<GroupeActivite> groupeActivites2 = new ArrayList<>();
	private List<GroupeImputation> groupeImputations2 = new ArrayList<>();

	private GroupeImputation groupeImputation = new GroupeImputation();
	private GroupeActivite groupeActivite = new GroupeActivite();

	private List<SelectOneMenu> oneMenusOfGrpImp = new ArrayList<SelectOneMenu>();
	private List<SelectOneMenu> oneMenusOfGrpAct = new ArrayList<SelectOneMenu>();

	private List<CodeJour> codeJours = new ArrayList<>();
	private List<CodeJour> selectedCodeJour = new ArrayList<>();

	private List<NatureJournee> natureJournees = new ArrayList<>();
	private List<NatureJournee> selectedNatureJournees = new ArrayList<>();

	private List<Imputation> imputations = new ArrayList<>();
	private List<Imputation> selectedImputations = new ArrayList<>();

	private List<ActiviteAbsence> activiteAbsences = new ArrayList<>();
	private List<ActiviteAbsence> selectedActiviteAbsences = new ArrayList<>();

	private List<ActivitePresence> activitePresences = new ArrayList<>();
	private List<ActivitePresence> selectedActivitePresence = new ArrayList<>();

	private DualListModel<ActiviteAbsence> activiteAbsencePickList;
	private DualListModel<ActivitePresence> activitePresencePickList;

	private DualListModel<Imputation> imputationPickList;
	
	private List<Imputation> imputationSource = new ArrayList<Imputation>();
	private List<Imputation> imputationTarget = new ArrayList<Imputation>();
	private List<ActiviteAbsence> activiteAbsenceSource = new ArrayList<ActiviteAbsence>();
	private List<ActiviteAbsence> activiteAbsenceTarget = new ArrayList<ActiviteAbsence>();

	private List<ActivitePresence> activitePresenceSource = new ArrayList<ActivitePresence>();
	private List<ActivitePresence> activitePresenceTarget = new ArrayList<ActivitePresence>();


	private List<Filtre> listeFiltres = new ArrayList<Filtre>();
	
	private String tmp = null;
	private String tmpF = null;
	
	

	@EJB
	GestionGroupeActiviteLocal gestionGroupeActiviteLocal;
	@EJB
	GestionGroupeImputationLocal gestionGroupeImputationLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;
	@EJB
	GestionFiltreLocal gestionFiltreLocal;
	@EJB
	GestionNatureJourneeLocal gestionNatureJourneeLocal;
	@EJB
	GestionImputationLocal gestionImputationLocal;
	@EJB
	GestionActiviteAbsenceLocal gestionActiviteAbsenceLocal;
	@EJB
	GestionActivitePresenceLocal gestionActivitePresenceLocal;

	public FiltreBean() {
		oneMenusOfGrpImp.add(new SelectOneMenu());
		oneMenusOfGrpAct.add(new SelectOneMenu());
	}

	@SuppressWarnings("deprecation")
	public void ajouter() {

		if (!tmp.isEmpty()) {
			filtre.setHeuredeb(new Time(Integer.parseInt(tmp.substring(0, 2)),
					Integer.parseInt(tmp.substring(3, 5)), 0));
		}

		if (!tmpF.isEmpty()) {
			filtre.setHeurefin(new Time(Integer.parseInt(tmpF.substring(0, 2)),
					Integer.parseInt(tmpF.substring(3, 5)), 0));
		}

		groupeActivites2.add(groupeActivite);
		groupeImputations2.add(groupeImputation);
		gestionFiltreLocal.ajouterFiltre(filtre);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setFiltre(filtre);
		gestionLibelleLocal.modifierLibelle(libelle);
		if (groupeActivites2.size() != 0)
			filtre.setGroupeActivites(groupeActivites2);
		if (groupeImputations2.size() != 0)
			filtre.setGroupeImputations(groupeImputations2);
		if (selectedCodeJour.size() != 0)
			filtre.setCodeJours(selectedCodeJour);
		if (selectedNatureJournees.size() != 0)
			filtre.setNatureJournees(selectedNatureJournees);
		if (imputationTarget.size() != 0)
			filtre.setImputations(imputationTarget);
		if (activiteAbsenceTarget.size() != 0)
			filtre.setActiviteAbsences(activiteAbsenceTarget);
		if (activitePresenceTarget.size() != 0)
			filtre.setActivitePresences(activitePresenceTarget);
		
		gestionFiltreLocal.modifierFiltre(filtre);

		// Délibération des variable de la mémoire locale
		groupeActivite = new GroupeActivite();
		groupeImputation = new GroupeImputation();
		libelle = new Libelle();
		filtre = new Filtre();
		groupeActivites2 = new ArrayList<>();
		groupeImputations2 = new ArrayList<>();
		selectedCodeJour = new ArrayList<>();
		selectedImputations = new ArrayList<>();
		selectedNatureJournees = new ArrayList<>();
		selectedActiviteAbsences = new ArrayList<>();
		selectedActivitePresence = new ArrayList<>();
		tmp = null;
		tmpF = null;

	}

	@PostConstruct
	public void init() {
		
		
		// Les filtres
		listeFiltres = gestionFiltreLocal.listeFiltres();
		// Les codes des jours
		codeJours = gestionCodeJourLocal.listeCodeJours();
		// Les groupes d'activités
		groupeActivites = gestionGroupeActiviteLocal.listeGroupeActivite();
		// Les groupes d'imputations
		groupeImputations = gestionGroupeImputationLocal.listeGroupeImpuation();
		// Les natures des journées
		natureJournees = gestionNatureJourneeLocal.listeNatureJournee();
		// Les imputations
		imputations = gestionImputationLocal.listeImputation();
		// Les activités en absence
		activiteAbsences = gestionActiviteAbsenceLocal.listerActiviteAbsence();
		// Les activités en presence
		activitePresences = gestionActivitePresenceLocal
				.findAll();
		// les activité en absence de pickList

		activiteAbsenceTarget = gestionActiviteAbsenceLocal
				.listerActiviteAbsence();
		activiteAbsencePickList = new DualListModel<>(activiteAbsenceSource,
				activiteAbsenceTarget);

		activitePresenceTarget = gestionActivitePresenceLocal
				.findAll();
		activitePresencePickList = new DualListModel<>(activitePresenceSource,
				activitePresenceTarget);

		imputationTarget=gestionImputationLocal.listeImputation();
		imputationPickList= new DualListModel<>(imputationSource,imputationTarget);
		

	}

	public void rowselect() {

		activitePresenceTarget = new ArrayList<ActivitePresence>();
		activitePresenceSource = new ArrayList<ActivitePresence>();

		activiteAbsenceTarget = new ArrayList<ActiviteAbsence>();
		activiteAbsenceSource = new ArrayList<ActiviteAbsence>();

		activiteAbsenceSource = gestionActiviteAbsenceLocal
				.getActiviteAbsencesNotInGroupe(groupeActivite);
		activiteAbsenceTarget = gestionActiviteAbsenceLocal
				.getActiviteAbsencesParGroupe(groupeActivite);
		activiteAbsencePickList = new DualListModel<>(activiteAbsenceSource,
				activiteAbsenceTarget);
		activitePresenceSource = gestionActivitePresenceLocal
				.getActivitePresencesNotInGroupe(groupeActivite);
		activitePresenceTarget = gestionActivitePresenceLocal
				.getActivitePresencesParGroupe(groupeActivite);
		activitePresencePickList = new DualListModel<>(activitePresenceSource,
				activitePresenceTarget);
		
	
	}

public void modifierFiltre() {
		
		gestionFiltreLocal.modifierFiltre(filtre);

		filtre = new Filtre();

		init();
	}
	
	
public void supprimerFiltre() throws NotSupportedException,
SystemException {
	Libelle lib = filtre.getLibelles().get(0);
try {
gestionFiltreLocal.supprimerFiltre(filtre);

} catch (Exception e) {
e.printStackTrace();
// cascade remove pour libelle --> restaurer libelle
gestionLibelleLocal.modifierLibelle(lib);
}
init();
}


	
	
	
	public void selectImputation() {
	
		imputationSource = new ArrayList<Imputation>();
		imputationTarget = new ArrayList<Imputation>();
		imputationSource = gestionImputationLocal
				.getImputationNotInGroupe(groupeImputation);
		imputationTarget = gestionImputationLocal
				.getImputationInGroupe(groupeImputation);
		
		imputationPickList= new DualListModel<>(imputationSource,
				imputationTarget);
	}
	
	
	

	public Filtre getFiltre() {
		return filtre;
	}

	public void setFiltre(Filtre filtre) {
		this.filtre = filtre;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<CodeJour> getcodeJours() {
		return codeJours;
	}

	public void setcodeJours(List<CodeJour> codeJoursOfOneMenus) {
		this.codeJours = codeJoursOfOneMenus;
	}

	public List<GroupeActivite> getGroupeActivites() {
		return groupeActivites;
	}

	public void setGroupeActivites(List<GroupeActivite> groupeActivites) {
		this.groupeActivites = groupeActivites;
	}

	public List<GroupeImputation> getGroupeImputations() {
		return groupeImputations;
	}

	public void setGroupeImputations(List<GroupeImputation> groupeImputations) {
		this.groupeImputations = groupeImputations;
	}

	public List<CodeJour> getSelectedCodeJour() {
		return selectedCodeJour;
	}

	public void setSelectedCodeJour(List<CodeJour> selectedCodeJour) {
		this.selectedCodeJour = selectedCodeJour;
	}

	public GroupeImputation getGroupeImputation() {
		return groupeImputation;
	}

	public void setGroupeImputation(GroupeImputation groupeImputation) {
		this.groupeImputation = groupeImputation;
	}

	public GroupeActivite getGroupeActivite() {
		return groupeActivite;
	}

	public void setGroupeActivite(GroupeActivite groupeActivite) {
		this.groupeActivite = groupeActivite;
	}

	public void onButtonAddFieldClick(AjaxBehaviorEvent p_oEvent) {
		groupeImputations2.add(groupeImputation);
		oneMenusOfGrpImp.add(new SelectOneMenu());

	}

	public void onButtonRemoveFieldClick(final SelectOneMenu p_oField) {
		oneMenusOfGrpImp.remove(p_oField);
	}

	public void onButtonAddFieldClick2(AjaxBehaviorEvent p_oEvent) {
		groupeActivites2.add(groupeActivite);
		oneMenusOfGrpAct.add(new SelectOneMenu());

	}

	public void onButtonRemoveFieldClick2(final SelectOneMenu p_oField) {
		oneMenusOfGrpAct.remove(p_oField);
	}

	public List<SelectOneMenu> getoneMenusOfGrpImp() {
		return oneMenusOfGrpImp;
	}

	public void setoneMenusOfGrpImp(List<SelectOneMenu> oneMenusOfGrpImp) {
		this.oneMenusOfGrpImp = oneMenusOfGrpImp;
	}

	public List<SelectOneMenu> getOneMenusOfGrpAct() {
		return oneMenusOfGrpAct;
	}

	public void setOneMenusOfGrpAct(List<SelectOneMenu> oneMenusOfGrpAct) {
		this.oneMenusOfGrpAct = oneMenusOfGrpAct;
	}

	public List<GroupeImputation> getGroupeImputations2() {
		return groupeImputations2;
	}

	public void setGroupeImputations2(List<GroupeImputation> groupeImputations2) {
		this.groupeImputations2 = groupeImputations2;
	}

	public List<GroupeActivite> getGroupeActivites2() {
		return groupeActivites2;
	}

	public void setGroupeActivites2(List<GroupeActivite> groupeActivites2) {
		this.groupeActivites2 = groupeActivites2;
	}

	public List<NatureJournee> getSelectedNatureJournees() {
		return selectedNatureJournees;
	}

	public void setSelectedNatureJournees(
			List<NatureJournee> selectedNatureJournees) {
		this.selectedNatureJournees = selectedNatureJournees;
	}

	public List<NatureJournee> getNatureJournees() {
		return natureJournees;
	}

	public void setNatureJournees(List<NatureJournee> natureJournees) {
		this.natureJournees = natureJournees;
	}

	public List<Imputation> getImputations() {
		return imputations;
	}

	public void setImputations(List<Imputation> imputations) {
		this.imputations = imputations;
	}

	public List<Imputation> getSelectedImputations() {
		return selectedImputations;
	}

	public void setSelectedImputations(List<Imputation> selectedImputations) {
		this.selectedImputations = selectedImputations;
	}

	public List<ActiviteAbsence> getActiviteAbsences() {
		return activiteAbsences;
	}

	public void setActiviteAbsences(List<ActiviteAbsence> activiteAbsences) {
		this.activiteAbsences = activiteAbsences;
	}

	public List<ActiviteAbsence> getSelectedActiviteAbsences() {
		return selectedActiviteAbsences;
	}

	public void setSelectedActiviteAbsences(
			List<ActiviteAbsence> selectedActiviteAbsences) {
		this.selectedActiviteAbsences = selectedActiviteAbsences;
	}

	public List<ActivitePresence> getActivitePresences() {
		return activitePresences;
	}

	public void setActivitePresences(List<ActivitePresence> activitePresences) {
		this.activitePresences = activitePresences;
	}

	public List<ActivitePresence> getSelectedActivitePresence() {
		return selectedActivitePresence;
	}

	public void setSelectedActivitePresence(
			List<ActivitePresence> selectedActivitePresence) {
		this.selectedActivitePresence = selectedActivitePresence;
	}

	public DualListModel<ActiviteAbsence> getActiviteAbsencePickList() {
		return activiteAbsencePickList;
	}

	public void setActiviteAbsencePickList(
			DualListModel<ActiviteAbsence> activiteAbsencePickList) {
		this.activiteAbsencePickList = activiteAbsencePickList;
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

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String getTmpF() {
		return tmpF;
	}

	public void setTmpF(String tmpF) {
		this.tmpF = tmpF;
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

	public List<Imputation> getImputationSource() {
		return imputationSource;
	}

	public void setImputationSource(List<Imputation> imputationSource) {
		this.imputationSource = imputationSource;
	}

	public List<Imputation> getImputationTarget() {
		return imputationTarget;
	}

	public void setImputationTarget(List<Imputation> imputationTarget) {
		this.imputationTarget = imputationTarget;
	}

	public DualListModel<Imputation> getImputationPickList() {
		return imputationPickList;
	}

	public void setImputationPickList(
			DualListModel<Imputation> imputationPickList) {
		this.imputationPickList = imputationPickList;
	}
	public List<Filtre> getListeFiltres() {
		return listeFiltres;
	}

	public void setListeFiltres(List<Filtre> listeFiltres) {
		this.listeFiltres = listeFiltres;
	}
}
