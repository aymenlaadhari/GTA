package managedBeans.gta;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Null;

import org.jboss.security.NobodyPrincipal;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.yesserp.domain.ga.Adresse;
import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.ga.Nationalite;
import com.yesserp.domain.gta.AffectationCycle;
import com.yesserp.domain.gta.ClasseEmp;
import com.yesserp.domain.gta.CodeCalendrier;
import com.yesserp.domain.gta.ContratGta;
import com.yesserp.domain.gta.DateCalculGta;
import com.yesserp.domain.gta.PlanningTheorique;
import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.GroupeActivite;
import com.yesserp.domain.gtaparam.GroupeCompteur;
import com.yesserp.domain.gtaparam.GroupeImputation;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.domain.gtaparam.TerminalMobile;
import com.yesserp.sessionbean.ga.affectation.GestionAffectationLocal;
import com.yesserp.sessionbean.ga.attachementemploye.GestionAttachementEmployeLocal;
import com.yesserp.sessionbean.ga.attachementmanager.GestionAttachementManagerLocal;
import com.yesserp.sessionbean.ga.cetification.GestionCertificationLocal;
import com.yesserp.sessionbean.ga.contrat.GestionContratLocal;
import com.yesserp.sessionbean.ga.coordonneebancaire.GestionCoordonneeBancaireLocal;
import com.yesserp.sessionbean.ga.cycle.GestionCycleLocal;
import com.yesserp.sessionbean.ga.diplome.GestionDiplomeGaLocal;
import com.yesserp.sessionbean.ga.embauche.GestionEmbaucheLocal;
import com.yesserp.sessionbean.ga.experience.GestionExperienceLocal;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.ga.infosindividuelles.GestionInfosIndividuellesLocal;
import com.yesserp.sessionbean.gta.gestionClasseEmp.GestionClasseEmpLocal;
import com.yesserp.sessionbean.gta.gestionOccurence.GestionOccurenceLocal;
import com.yesserp.sessionbean.gta.gestionaffectationcycle.GestionAffectationCycle;
import com.yesserp.sessionbean.gta.gestionaffectationcycle.GestionAffectationCycleLocal;
import com.yesserp.sessionbean.gta.gestioncodecalendrier.GestionCodeCalendrierLocal;
import com.yesserp.sessionbean.gta.gestioncontratGTA.GestionContraGtaLocal;
import com.yesserp.sessionbean.gta.gestiondatejournees.GestionDateJourneesLocal;
import com.yesserp.sessionbean.gta.gestionplanningtheorique.GestionPlanningTheoriqueLocal;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupeactivite.GestionGroupeActivite;
import com.yesserp.sessionbean.paramgta.gestiongroupeactivite.GestionGroupeActiviteLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupecompteur.GestionGroupeCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupeimputation.GestionGroupeImputationLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;

@ManagedBean
@SessionScoped
public class ContratGTABean {

	// identite
	private List<Identite> identites = new ArrayList<>();
	private ArrayList<Identite> collaborateurs = new ArrayList<>();
	private String renderedimage = "false";
	private StreamedContent photo = null;
	private Identite identite = new Identite();

	// Vars
	private int nbjours = 7;
	private int nbParcours;
	private int codejournee;
	private String matricul = "";
	private Long numdossier;
	private String nom = "";
	private String prenom = "";
	private String sexe = "";
	private String poste = "";
	private String uniteOrganisationnelle = "";
	private String stCivile = "";
	private int maxSelectCollaborateurs = 10;

	// Cycle de trvail
	private CycleTravail cycleTravail = new CycleTravail();
	private List<CycleTravail> cycleTravails = new ArrayList<>();
	private AffectationCycle affectationCycle = new AffectationCycle();
	private List<AffectationCycle> affectationCycles = new ArrayList<AffectationCycle>();

	private CodeCalendrier codeCalendrier = new CodeCalendrier();
	private List<CodeCalendrier> codeCalendriers = new ArrayList<>();
	private PlanningTheorique planningTheorique = new PlanningTheorique();
	private List<SemaineType> semaineTypes = new ArrayList<>();
	private DateCalculGta dateJournees = new DateCalculGta();
	private JourneeType journeeType = new JourneeType();
	private Date dateplanning = new Date();
	private CodeJour codeJour = new CodeJour();

	private Long nombreCycledetravail = null;
	private String precedentCycledetravail = "true";
	private String suivantCycledetravail = "true";
	private int restePrecedCycle = 0;
	private int resteSuivantCycle = 0;
	private int nombreSelectionCycle = 3;

	// Soumission GTA
	private ContratGta newsoumissionGta = new ContratGta();
	private ContratGta soumissonGta = new ContratGta();
	private List<ContratGta> soumissionGtas = new ArrayList<ContratGta>();
	private ArrayList<ContratGta> soumissionGtashistorique = new ArrayList<ContratGta>();

	private String gta;
	private int nombreSoumissionGta = 0;
	private String precedentSoumissionGta = "true";
	private String suivantSoumissionGta = "true";
	private int restePrecedGta = 0;
	private int resteSuivantGta = 0;
	private int nombreSelectionGta = 3;

	// Classe employe
	private ClasseEmp newclasseEmp = new ClasseEmp();
	private ClasseEmp classeEmp = new ClasseEmp();
	private List<ClasseEmp> classeEmps = new ArrayList<ClasseEmp>();
	private List<ClasseEmp> classeEmpshistorique = new ArrayList<ClasseEmp>();
	
	
	
	private Long nombreClasseempolye = null;
	private String precedentClasseempolye = "true";
	private String suivantClasseempolye = "true";
	private int restePrecedClasse = 0;
	private int resteSuivantClasse = 0;
	private int nombreSelectionClasse = 3;

	// groupe compteurs
	private GroupeCompteur newgroupeCompteur = new GroupeCompteur();
	private GroupeCompteur groupeCompteur = new GroupeCompteur();
	private List<GroupeCompteur> groupeCompteurs = new ArrayList<GroupeCompteur>();
	private ArrayList<GroupeCompteur> groupeCompteurshistorique = new ArrayList<GroupeCompteur>();

	private Long nombreGroupeCompteur = null;
	private String precedentGroupeCompteur = "true";
	private String suivantGroupeCompteur = "true";
	private int restePrecedCompteur = 0;
	private int resteSuivantCompteur = 0;
	private int nombreSelectionCompteur = 3;

	// groupes activité
	private GroupeActivite newgroupeActivite = new GroupeActivite();
	private GroupeActivite groupeActivite = new GroupeActivite();
	private List<GroupeActivite> groupeActivites = new ArrayList<GroupeActivite>();
	private ArrayList<GroupeActivite> groupeActivitehistorique = new ArrayList<GroupeActivite>();

	private Long nombreGroupeActivite = null;
	private String precedentGroupeActivite = "true";
	private String suivantGroupeActivite = "true";
	private int restePrecedActivite = 0;
	private int resteSuivantActivite = 0;
	private int nombreSelectionActivite = 3;

	// groupe imputation
	private GroupeImputation newgroupeImputation = new GroupeImputation();
	private GroupeImputation groupeImputation = new GroupeImputation();
	private List<GroupeImputation> groupeImputations = new ArrayList<GroupeImputation>();
	private ArrayList<GroupeImputation> groupeImputationhistorique = new ArrayList<GroupeImputation>();

	private Long nombreGroupeImputation = null;
	private String precedentGroupeImputation = "true";
	private String suivantGroupeImputation = "true";
	private int restePrecedImputation = 0;
	private int resteSuivantImputation = 0;
	private int nombreSelectionImputation = 3;

	// affectation
	@EJB
	GestionAffectationCycleLocal gestionAffectationCycleLocal;
	// groupe
	@EJB
	GestionGroupeImputationLocal gestionGroupeImputationLocal;
	@EJB
	GestionGroupeActiviteLocal gestionGroupeActiviteLocal;
	@EJB
	GestionGroupeCompteurLocal gestionGroupeCompteurLocal;
	
	//Contrat GTA
	@EJB
	GestionContraGtaLocal gestionContraGtaLocal ;

	//classe emp
	
	@EJB
	GestionClasseEmpLocal classeEmpLocal ;
	@EJB
	GestionOccurenceLocal gestionOccurenceLocal;
	@EJB
	GestionInfosIndividuellesLocal gestionInfosIndividuellesLocal;
	@EJB
	GestionCycleTravailLocal gestionCycleTravailLocal;
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
	@EJB
	GestionCodeCalendrierLocal gestionCodeCalendrierLocal;
	@EJB
	GestionPlanningTheoriqueLocal gestionPlanningTheoriqueLocal;
	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;
	@EJB
	GestionDateJourneesLocal gestionDateJourneesLocal;
	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;
	@EJB
	GestionDiplomeGaLocal gestionDiplomeGaLocal;

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private List<String> checkedValues;
    private List<String> availableValues;
    private List<TerminalMobile> mobiles ;

    @PostConstruct
    public void init() {
        availableValues = new ArrayList<String>();
        
        mobiles = gestionContraGtaLocal.listAllTerminal();
        for (Object object : mobiles) {
        	availableValues.add(object != null ? object.toString() : null);
	    }
    }

	public void rowselect() {

	}

	public void resetSearch() {
		poste = "";
		nom = "";
		prenom = "";
		matricul = "";
		uniteOrganisationnelle = "";
		sexe = "";
		stCivile = "";

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

			collaborateurs = (ArrayList<Identite>) gestionIdentiteLocal
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

			collaborateurs = (ArrayList<Identite>) gestionIdentiteLocal
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

			collaborateurs = (ArrayList<Identite>) gestionIdentiteLocal
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

	public void rechercher() {

		// imputation
		groupeImputation = new GroupeImputation();
		groupeImputations = new ArrayList<>();
		groupeImputationhistorique = new ArrayList<>();
		// actvite
		groupeActivite = new GroupeActivite();
		groupeActivites = new ArrayList<>();
		groupeActivitehistorique = new ArrayList<>();

		// Compteurs
		groupeCompteur = new GroupeCompteur();
		groupeCompteurs = new ArrayList<>();
		groupeCompteurshistorique = new ArrayList<>();
		
		//soumission GTA
		
		soumissonGta = new ContratGta();
		soumissionGtas = new ArrayList<>();
		soumissionGtashistorique = new ArrayList<>();
		
		//classe emp
		classeEmp = new ClasseEmp();
		classeEmps = new ArrayList<>();
		classeEmpshistorique = new ArrayList<>();

		if (identite != null) {

			// identities
			identites = gestionIdentiteLocal.findAll();

			// groupes imputations
			groupeImputations = (ArrayList<GroupeImputation>) gestionGroupeImputationLocal
					.trouverGroupesImputationsParIdentite(identite);
			nombreGroupeImputation = gestionGroupeImputationLocal
					.trouverNombreOccurrencesParIdentite(identite);
			afficherHistoriqueGroupesImputations();

			// groupes activités
			groupeActivites = (ArrayList<GroupeActivite>) gestionGroupeActiviteLocal
					.trouverGroupesActivitesParIdentite(identite);
			nombreGroupeActivite = gestionGroupeActiviteLocal
					.trouverNombreOccurrencesParIdentite(identite);
			afficherHistoriqueGroupeActivites();

			// groupes compteurs
			groupeCompteurs = (ArrayList<GroupeCompteur>) gestionGroupeCompteurLocal
					.trouverGroupesCompteursParIdentite(identite);
			nombreGroupeCompteur = gestionGroupeCompteurLocal
					.trouverNombreOccurrencesParIdentite(identite);
			afficherHistoriqueCompteurs();
			// soumission GTA
			
			soumissionGtas = (ArrayList<ContratGta>) gestionContraGtaLocal
					.trouverContratGtaParIdentite(identite);
			nombreGroupeCompteur = gestionContraGtaLocal
					.trouverNombreOccurrencesParIdentite(identite);
			afficherHistoriqueSoumissionGTA();

			// CYCLE DE TRAVAILS
			cycleTravails = gestionCycleTravailLocal
					.trouverCycleAffectePourEmployee(identite);
			cycleTravails = gestionCycleTravailLocal
					.trouverCycleAffectePourEmployee(identite);

	

			// classe employe
			classeEmps = (ArrayList<ClasseEmp>) classeEmpLocal.trouverClasseEmpParIdentite(identite);
			nombreClasseempolye = classeEmpLocal
					.trouverNombreOccurrencesParIdentite(identite);
			afficherHistoriqueClasseEmp();
		}
	}

	/*
	 * ********************************************************** Cycle de
	 * travail ***********************************************************
	 */

	public void ajoutcycledetravail() {
		if (identite != null && identite.getNumedoss() != null) {

//			try {

//				gestionCycleTravailLocal.affectCycleToEmployee(identite,
//						cycleTravail, cycleTravail.getDateff());
//				gestionCodeCalendrierLocal.affectCodeCalToEmployee(identite,
//						codeCalendrier, cycleTravail.getDateff());
//
//				affectationCodeCal = new AffectationCodeCal();
//				affectationCycle = new AffectationCycle();
//				System.out.println("c bon ");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			nombreCycledetravail++;
			afficherHistoriqueCycledetravails();

		}

	}

	public void afficherHistoriqueCycledetravails() {
//
//		cycledetravailhistorique = (ArrayList<Adresse>) gestionAdresseLocal
//				.trouverHistoriqueAdressesParIdentite(identite,
//						nombreSelectionCycle);
		restePrecedCycle = 0;
		resteSuivantCycle = 0;
		if (nombreCycledetravail > nombreSelectionCycle) {
			restePrecedCycle = (int) (nombreCycledetravail - nombreSelectionCycle);
			precedentCycledetravail = "false";
		} else {
			precedentCycledetravail = "true";
		}
		suivantCycledetravail = "true";

	}

	public void premiersHistoriqueCycleTravail() {
//		cycledetravailhistorique = (ArrayList<Adresse>) gestionAdresseLocal
//				.trouverPremiersHistoriqueAdressesParIdentite(identite,
//						nombreSelectionCycle);
		restePrecedCycle = 0;
		resteSuivantCycle = 0;
		if (nombreCycledetravail > nombreSelectionCycle) {
			resteSuivantCycle = (int) (nombreCycledetravail - nombreSelectionCycle);
			suivantCycledetravail = "false";
		} else {
			suivantCycledetravail = "true";
		}
		precedentCycledetravail = "true";
	}

	public void derniersHistoriqueCycleTravail() {
		afficherHistoriqueCycledetravails();
	}

	public void precedentHistoriqueCycleTravail() {
//		DATE MAX = CYCLEDETRAVAILHISTORIQUE.GET(
//				CYCLEDETRAVAILHISTORIQUE.SIZE() - 1).GETDATEEFFE();
//
//		CYCLEDETRAVAILHISTORIQUE = (ARRAYLIST<ADRESSE>) GESTIONADRESSELOCAL
//				.TROUVERHISTORIQUEADRESSESPARIDENTITEETDATEMAX(IDENTITE, MAX,
//						NOMBRESELECTIONCYCLE);
		suivantCycledetravail = "false";
		resteSuivantCycle += nombreSelectionCycle;
		restePrecedCycle -= nombreSelectionCycle;
		if (restePrecedCycle < 1)
			precedentCycledetravail = "true";
		if (resteSuivantCycle < 1)
			suivantCycledetravail = "true";

	}

	public void suivantHistoriqueCycledetravail() {
//		Date min = cycledetravailhistorique.get(0).getDateeffe();
//		cycledetravailhistorique = (ArrayList<Adresse>) gestionAdresseLocal
//				.trouverPremiersHistoriqueAdressesParIdentiteEtDateMin(
//						identite, min, nombreSelectionCycle);
		restePrecedCycle += nombreSelectionCycle;
		precedentCycledetravail = "false";
		resteSuivantCycle -= nombreSelectionCycle;
		if (resteSuivantCycle < 1)
			suivantCycledetravail = "true";
		if (restePrecedCycle < 1)
			precedentCycledetravail = "true";
	}

	public void supprimerCycledetravail() {
		if (cycleTravail.getIdct() != 0) {

			nombreCycledetravail--;
			afficherHistoriqueCycledetravails();
		}
	}

	public void modifierCycledetravail() {
		if (identite.getNumedoss() != null)
			gestionAffectationCycleLocal
					.modifierAffectationCycledeTravail(affectationCycle);
		afficherHistoriqueCycledetravails();
	}

	/*
	 * ********************************************************** SoumissionGta
	 * ***********************************************************
	 */

	public void afficherHistoriqueSoumissionGTA() {

		soumissionGtas = (ArrayList<ContratGta>) gestionContraGtaLocal
				.trouverHistoriqueContratGtaParIdentite(identite, nombreSelectionGta);
		restePrecedGta = 0;
		resteSuivantGta = 0;
		
		try {
			if (nombreSoumissionGta > nombreSelectionGta) {
				restePrecedGta = (nombreSoumissionGta - nombreSelectionGta);
				precedentSoumissionGta = "false";
			} else {
				precedentSoumissionGta = "true";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suivantSoumissionGta = "true";

	}

	public void premiersHistoriqueSoumissionGTA() {
		soumissionGtas = (ArrayList<ContratGta>) gestionContraGtaLocal
				.trouverPremiersHistoriqueContratGtaParIdentite(identite,
						nombreSelectionGta);
		restePrecedGta = 0;
		resteSuivantGta = 0;
		if (nombreSoumissionGta > nombreSelectionGta) {
			resteSuivantGta =  (nombreSoumissionGta - nombreSelectionGta);
			suivantSoumissionGta = "false";
		} else {
			suivantSoumissionGta = "true";
		}
		precedentSoumissionGta = "true";
	}

	public void derniersHistoriqueSoumissionGTA() {
		afficherHistoriqueSoumissionGTA();
	}

	public void precedentHistoriqueSoumissionGTA() {
		Date max = soumissionGtas.get(soumissionGtas.size() - 1)
				.getDateeffe();

		soumissionGtas = (ArrayList<ContratGta>) gestionContraGtaLocal
				.trouverHistoriqueContratGtaParIdentiteEtDateMax(identite, max,
						nombreSelectionGta);
		suivantSoumissionGta = "false";
		resteSuivantGta += nombreSelectionGta;
		restePrecedGta -= nombreSelectionGta;
		if (restePrecedGta < 1)
			precedentSoumissionGta = "true";
		if (resteSuivantGta < 1)
			suivantSoumissionGta = "true";

	}

	public void suivantHistoriqueSoumissionGTA() {
		Date min = soumissionGtas.get(0).getDateeffe();
		soumissionGtas = (ArrayList<ContratGta>) gestionContraGtaLocal
				.trouverPremiersHistoriqueContratGtaParIdentiteEtDateMin(
						identite, min, nombreSelectionGta);
		restePrecedGta += nombreSelectionGta;
		precedentSoumissionGta = "false";
		resteSuivantGta -= nombreSelectionGta;
		if (resteSuivantGta < 1)
			suivantSoumissionGta = "true";
		if (restePrecedGta < 1)
			precedentSoumissionGta = "true";
	}

	public void ajouterSoumissionGTA() {
		if (identite != null && identite.getNumedoss() != null) {
			gestionIdentiteLocal.ajouterSoumissionGta(identite, newsoumissionGta);
//			for (TerminalMobile item : checkedValues){
//				try {
//					gestionContraGtaLocal.ajouterTerminalMobile(soumissonGta, item);
//					System.out.println("terminauxxxxxxxxxxxxxxxxxxxxxxxxx");
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
			
			newsoumissionGta = new ContratGta();
			
			nombreSoumissionGta++;
			afficherHistoriqueSoumissionGTA();
		}
	}

	public void supprimerSoumissionGTA() {
		boolean test = true;
		if (soumissionGtashistorique.size() == 1) {
			FacesContext.getCurrentInstance().addMessage(
					"grpi",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Au moins un soumission obligatoire", ""));
			test = false;
		}

		if (test == true) {
			if (soumissonGta.getId() != 0) {
						gestionContraGtaLocal.deleteContratGta(soumissonGta);
								
						nombreSoumissionGta --;
						afficherHistoriqueSoumissionGTA();
					
			}
		}
	}

	public void modifierSoumissionGTA() {
		if (soumissonGta != null && soumissonGta.getId() != 0)
			gestionContraGtaLocal.modifierContratGta(soumissonGta);
		afficherHistoriqueSoumissionGTA();
	}

	/*
	 * **********************************************************Classe Employe
	 * ***********************************************************
	 */

	public void afficherHistoriqueClasseEmp() {

		classeEmps = (ArrayList<ClasseEmp>) classeEmpLocal
				.trouverHistoriqueClasseEmpParIdentite(identite, nombreSelectionClasse);
		restePrecedClasse = 0;
		resteSuivantClasse = 0;
		if (nombreClasseempolye > nombreSelectionClasse) {
			restePrecedClasse = (int) (nombreClasseempolye - nombreSelectionClasse);
			precedentClasseempolye = "false";
		} else {
			precedentClasseempolye = "true";
		}
		suivantClasseempolye = "true";

	}

	public void premiersHistoriqueClasseEmp() {
		classeEmps = (ArrayList<ClasseEmp>) classeEmpLocal
				.trouverPremiersHistoriqueGClasseEmpParIdentite(identite, nombreSelectionClasse);
		restePrecedClasse = 0;
		resteSuivantClasse = 0;
		if (nombreClasseempolye > nombreSelectionClasse) {
			resteSuivantClasse = (int) (nombreClasseempolye - nombreSelectionClasse);
			suivantClasseempolye = "false";
		} else {
			suivantClasseempolye = "true";
		}
		precedentClasseempolye = "true";
	}

	public void derniersHistoriqueClassEmp() {
		afficherHistoriqueClasseEmp();
	}

	public void precedentHistoriqueClasseEmp() {
		Date max = classeEmps.get(classeEmps.size() - 1)
				.getDateeffet();

		classeEmps = (ArrayList<ClasseEmp>) classeEmpLocal.trouverHistoriqueClasseEmpParIdentiteEtDateMax(identite, max, nombreSelectionClasse);
			
		suivantClasseempolye = "false";
		resteSuivantClasse += nombreSelectionClasse;
		restePrecedClasse -= nombreSelectionClasse;
		if (restePrecedClasse < 1)
			precedentClasseempolye = "true";
		if (resteSuivantClasse < 1)
			suivantClasseempolye = "true";

	}

	public void suivantHistoriqueClasseEmp() {
		Date min = classeEmps.get(0).getDateeffet();
		classeEmps = (ArrayList<ClasseEmp>) classeEmpLocal
				.trouverPremiersHistoriqueClasseEmpParIdentiteEtDateMin(identite, min, nombreSelectionClasse);
						
		restePrecedClasse += nombreSelectionClasse;
		precedentClasseempolye = "false";
		resteSuivantClasse -= nombreSelectionClasse;
		if (resteSuivantClasse < 1)
			suivantClasseempolye = "true";
		if (restePrecedClasse < 1)
			precedentClasseempolye = "true";
	}

	public void ajouterClasseEmp() {
		if (identite != null && identite.getNumedoss() != null) {
			gestionIdentiteLocal.ajouterClasseEmp(identite, newclasseEmp);
			newclasseEmp = new ClasseEmp();
			
			try {
				nombreClasseempolye++;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			nombreClasseempolye++;
			afficherHistoriqueClasseEmp();
		}
	}

	public void supprimerClasseEmp() {
		boolean test = true;
		if (classeEmpshistorique.size() == 1) {
			FacesContext.getCurrentInstance().addMessage(
					"grpi",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Au moins un Classe Emp obligatoire", ""));
			test = false;
		}

		if (test == true) {
			if (classeEmp.getId() != 0) {

				classeEmpLocal
						.supprimerClasseEmp(classeEmp);
				nombreClasseempolye--;
				afficherHistoriqueClasseEmp();
			}
		}
		
	}

	public void modifierClasseEmp() {
		if (classeEmp != null && classeEmp.getId() != null)
			classeEmpLocal.modifierClasseEmp(classeEmp);
		afficherHistoriqueClasseEmp();
	}

	

	/*
	 * ********************************************************** Groupe
	 * compteurs ***********************************************************
	 */

	public void afficherHistoriqueCompteurs() {

		groupeCompteurs = (ArrayList<GroupeCompteur>) gestionGroupeCompteurLocal
				.trouverHistoriqueGroupesCompteursParIdentite(identite,
						nombreSelectionCompteur);
		restePrecedCompteur = 0;
		resteSuivantCompteur = 0;
		if (nombreGroupeCompteur > nombreSelectionCompteur) {
			restePrecedCompteur = (int) (nombreGroupeCompteur - nombreSelectionCompteur);
			precedentGroupeCompteur = "false";
		} else {
			precedentGroupeCompteur = "true";
		}
		suivantGroupeCompteur = "true";

	}

	public void premiersHistoriqueCompteurs() {
		groupeCompteurs = (ArrayList<GroupeCompteur>) gestionGroupeCompteurLocal
				.trouverPremiersHistoriqueGroupesCompteurssParIdentite(
						identite, nombreSelectionCompteur);
		restePrecedCompteur = 0;
		resteSuivantCompteur = 0;
		if (nombreGroupeCompteur > nombreSelectionCompteur) {
			resteSuivantCompteur = (int) (nombreGroupeCompteur - nombreSelectionCompteur);
			suivantGroupeCompteur = "false";
		} else {
			suivantGroupeCompteur = "true";
		}
		precedentGroupeCompteur = "true";
	}

	public void derniersHistoriqueCompteurs() {
		afficherHistoriqueCompteurs();
	}

	public void precedentHistoriqueCompteurs() {
		Date max = groupeCompteurs.get(groupeCompteurs.size() - 1)
				.getDateeffe();

		groupeCompteurs = (ArrayList<GroupeCompteur>) gestionGroupeCompteurLocal
				.trouverHistoriqueGroupesCompteursParIdentiteEtDateMax(
						identite, max, nombreSelectionCompteur);
		suivantGroupeCompteur = "false";
		resteSuivantCompteur += nombreSelectionCompteur;
		restePrecedCompteur -= nombreSelectionCompteur;
		if (restePrecedCompteur < 1)
			precedentGroupeCompteur = "true";
		if (resteSuivantCompteur < 1)
			suivantGroupeCompteur = "true";

	}

	public void suivantHistoriqueCompteurs() {
		Date min = groupeCompteurs.get(0).getDateeffe();
		groupeCompteurs = (ArrayList<GroupeCompteur>) gestionGroupeCompteurLocal
				.trouverPremiersHistoriqueGroupesCompteursParIdentiteEtDateMin(
						identite, min, nombreSelectionCompteur);
		restePrecedCompteur += nombreSelectionCompteur;
		precedentGroupeCompteur = "false";
		resteSuivantCompteur -= nombreSelectionCompteur;
		if (resteSuivantCompteur < 1)
			suivantGroupeCompteur = "true";
		if (restePrecedCompteur < 1)
			precedentGroupeCompteur = "true";
	}

	public void ajouterGroupeCompteurs() {
		if (identite != null && identite.getNumedoss() != null) {
			gestionIdentiteLocal.ajouterGroupeCompteur(identite,
					newgroupeCompteur);
			newgroupeCompteur = new GroupeCompteur();
			nombreGroupeCompteur++;
			afficherHistoriqueCompteurs();
		}
	}

	public void supprimerGroupeCompteurs() {
		boolean test = true;
		if (groupeCompteurshistorique.size() == 1) {
			FacesContext.getCurrentInstance().addMessage(
					"grpi",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Au moins un groupe compteurs obligatoire", ""));
			test = false;
		}

		if (test == true) {
			if (groupeCompteur.getIdgrpc() != 0) {

				gestionGroupeCompteurLocal
						.supprimerGroupeCompteur(groupeCompteur);
				nombreGroupeCompteur--;
				afficherHistoriqueCompteurs();
			}
		}
	}

	public void modifierGroupeCompteurs() {
		if (groupeCompteur != null && groupeCompteur.getIdgrpc() != 0)
			gestionGroupeCompteurLocal.modifierGroupeCompteur(groupeCompteur);
		afficherHistoriqueCompteurs();
	}

	/*
	 * ********************************************************** Groupes
	 * activites ***********************************************************
	 */

	public void afficherHistoriqueGroupeActivites() {

		groupeActivites = (ArrayList<GroupeActivite>) gestionGroupeActiviteLocal
				.trouverHistoriqueGroupesActivitesParIdentite(identite,
						nombreSelectionActivite);
		restePrecedActivite = 0;
		resteSuivantActivite = 0;
		if (nombreGroupeActivite > nombreSelectionActivite) {
			restePrecedActivite = (int) (nombreGroupeActivite - nombreSelectionActivite);
			precedentGroupeActivite = "false";
		} else {
			precedentGroupeActivite = "true";
		}
		suivantGroupeActivite = "true";

	}

	public void premiersHistoriqueGroupeActivites() {
		groupeActivites = (ArrayList<GroupeActivite>) gestionGroupeActiviteLocal
				.trouverPremiersHistoriqueGroupesActivitessParIdentite(
						identite, nombreSelectionActivite);
		restePrecedActivite = 0;
		resteSuivantActivite = 0;
		if (nombreGroupeActivite > nombreSelectionActivite) {
			resteSuivantActivite = (int) (nombreGroupeActivite - nombreSelectionActivite);
			suivantGroupeActivite = "false";
		} else {
			suivantGroupeActivite = "true";
		}
		precedentGroupeActivite = "true";
	}

	public void derniersHistoriqueGroupeActivites() {
		afficherHistoriqueGroupeActivites();
	}

	public void precedentHistoriqueGroupeActivites() {
		Date max = groupeActivites.get(groupeActivites.size() - 1)
				.getDateeffe();

		groupeActivites = (ArrayList<GroupeActivite>) gestionGroupeActiviteLocal
				.trouverHistoriqueGroupesActivitesParIdentiteEtDateMax(
						identite, max, nombreSelectionActivite);
		suivantGroupeActivite = "false";
		resteSuivantActivite += nombreSelectionActivite;
		restePrecedActivite -= nombreSelectionActivite;
		if (restePrecedActivite < 1)
			precedentGroupeActivite = "true";
		if (resteSuivantActivite < 1)
			suivantGroupeActivite = "true";

	}

	public void suivantHistoriqueGroupeActivites() {
		Date min = groupeActivites.get(0).getDateeffe();
		groupeActivites = (ArrayList<GroupeActivite>) gestionGroupeActiviteLocal
				.trouverPremiersHistoriqueGroupesActivitesParIdentiteEtDateMin(
						identite, min, nombreSelectionActivite);
		restePrecedActivite += nombreSelectionActivite;
		precedentGroupeActivite = "false";
		resteSuivantActivite -= nombreSelectionActivite;
		if (resteSuivantActivite < 1)
			suivantGroupeActivite = "true";
		if (restePrecedActivite < 1)
			precedentGroupeActivite = "true";
	}

	public void ajouterGroupeActivites() {
		if (identite != null && identite.getNumedoss() != 0) {

			gestionIdentiteLocal.ajouterGroupeactivite(identite,
					newgroupeActivite);
			newgroupeActivite = new GroupeActivite();

			nombreGroupeActivite++;
			afficherHistoriqueGroupeActivites();
		}
	}

	public void supprimerGroupeActivites() {
		boolean test = true;
		if (groupeActivitehistorique.size() == 1) {
			FacesContext.getCurrentInstance().addMessage(
					"grpi",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Au moins un groupe Activite obligatoire", ""));
			test = false;
		}

		if (test == true) {
			if (groupeActivite.getIdgrpa() != 0) {

				gestionGroupeActiviteLocal
						.supprimerGroupeActivite(groupeActivite);
				nombreGroupeActivite--;
				afficherHistoriqueGroupeActivites();
			}
		}
	}

	public void modifierGroupeActivites() {
		if (groupeActivite != null && groupeActivite.getIdgrpa() != 0)
			gestionGroupeActiviteLocal.modifierGroupeActivite(groupeActivite);
		afficherHistoriqueGroupeActivites();
	}

	/*
	 * ********************************************************** Groupes
	 * Imputation ***********************************************************
	 */

	public void afficherHistoriqueGroupesImputations() {
		groupeImputations = gestionGroupeImputationLocal
				.trouverHistoriqueGroupesImputationsParIdentite(identite,
						nombreSelectionImputation);

		restePrecedImputation = 0;
		resteSuivantImputation = 0;
		if (nombreGroupeImputation > nombreSelectionImputation) {
			restePrecedImputation = (int) (nombreGroupeImputation - nombreSelectionImputation);
			precedentGroupeImputation = "false";
		} else {
			precedentGroupeImputation = "true";
		}
		suivantGroupeImputation = "true";

	}

	public void premiersHistoriqueGroupesImputations() {
		groupeImputations = gestionGroupeImputationLocal
				.trouverPremiersHistoriqueGroupesImputationssParIdentite(
						identite, nombreSelectionImputation);
		restePrecedImputation = 0;
		resteSuivantImputation = 0;
		if (nombreGroupeImputation > nombreSelectionImputation) {
			resteSuivantImputation = (int) (nombreGroupeImputation - nombreSelectionImputation);
			suivantGroupeImputation = "false";
		} else {
			suivantGroupeImputation = "true";
		}
		precedentGroupeImputation = "true";
	}

	public void derniersHistoriqueGroupesImputations() {
		afficherHistoriqueGroupesImputations();
	}

	public void precedentHistoriqueGroupesImputations() {
		Date max = groupeImputations.get(groupeImputations.size() - 1)
				.getDateeffe();

		groupeImputations = gestionGroupeImputationLocal
				.trouverHistoriqueGroupesImputationsParIdentiteEtDateMax(
						identite, max, nombreSelectionImputation);
		suivantGroupeImputation = "false";
		resteSuivantImputation += nombreSelectionImputation;
		restePrecedImputation -= nombreSelectionImputation;
		if (restePrecedImputation < 1)
			precedentGroupeImputation = "true";
		if (resteSuivantImputation < 1)
			suivantGroupeImputation = "true";

	}

	public void suivantHistoriqueGroupesImputations() {
		Date min = groupeImputations.get(0).getDateeffe();
		groupeImputations = gestionGroupeImputationLocal
				.trouverHistoriqueGroupesImputationsParIdentiteEtDateMin(
						identite, min, nombreSelectionImputation);
		restePrecedImputation += nombreSelectionImputation;
		precedentGroupeImputation = "false";
		resteSuivantImputation -= nombreSelectionImputation;
		if (resteSuivantImputation < 1)
			suivantGroupeImputation = "true";
		if (restePrecedImputation < 1)
			precedentGroupeImputation = "true";
	}

	public void ajouterGroupesImputations() {
		if (identite != null && identite.getNumedoss() != null) {
			gestionIdentiteLocal.ajouterGroupeimputation(identite,
					newgroupeImputation);
			newgroupeImputation = new GroupeImputation();
			nombreGroupeImputation++;
			afficherHistoriqueGroupesImputations();
		}
	}

	public void supprimerGroupesImputations() {

		boolean test = true;
		if (groupeImputationhistorique.size() == 1) {
			FacesContext.getCurrentInstance().addMessage(
					"grpi",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Au moins un groupe imputation obligatoire", ""));
			test = false;
		}

		if (test == true) {
			if (groupeImputation.getIdgrp() != 0) {

				gestionGroupeImputationLocal
						.supprimerGroupeImputation(groupeImputation);
				nombreGroupeImputation--;
				afficherHistoriqueGroupesImputations();
			}
		}
	}

	public void modifierGroupesImputations() {
		if (groupeImputation != null && groupeImputation.getIdgrp() != 0)
			gestionGroupeImputationLocal
					.modifierGroupeImputation(groupeImputation);
		afficherHistoriqueGroupesImputations();
	}

	public String getGta() {
		return gta;
	}

	public void setGta(String gta) {
		this.gta = gta;
	}

	public List<CycleTravail> getCycleTravails() {
		return cycleTravails;
	}

	public void setCycleTravails(List<CycleTravail> cycleTravails) {
		this.cycleTravails = cycleTravails;
	}

	public CycleTravail getCycleTravail() {
		return cycleTravail;
	}

	public void setCycleTravail(CycleTravail cycleTravail) {
		this.cycleTravail = cycleTravail;
	}

	public List<Identite> getIdentites() {
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

	public Identite getIdentite() {
		return identite;
	}

	public void setIdentite(Identite identite) {
		this.identite = identite;
	}

	public CodeCalendrier getCodeCalendrier() {
		return codeCalendrier;
	}

	public void setCodeCalendrier(CodeCalendrier codeCalendrier) {
		this.codeCalendrier = codeCalendrier;
	}

	public List<CodeCalendrier> getCodeCalendriers() {
		return codeCalendriers;
	}

	public void setCodeCalendriers(List<CodeCalendrier> codeCalendriers) {
		this.codeCalendriers = codeCalendriers;
	}

	public List<SemaineType> getSemaineTypes() {
		return semaineTypes;
	}

	public void setSemaineTypes(List<SemaineType> semaineTypes) {
		this.semaineTypes = semaineTypes;
	}

	public DateCalculGta getDateJournees() {
		return dateJournees;
	}

	public void setDateJournees(DateCalculGta dateJournees) {
		this.dateJournees = dateJournees;
	}

	public Date getDateplanning() {
		return dateplanning;
	}

	public void setDateplanning(Date dateplanning) {
		this.dateplanning = dateplanning;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public int getNbjours() {
		return nbjours;
	}

	public void setNbjours(int nbjours) {
		this.nbjours = nbjours;
	}

	public int getNbParcours() {
		return nbParcours;
	}

	public void setNbParcours(int nbParcours) {
		this.nbParcours = nbParcours;
	}

	public int getCodejournee() {
		return codejournee;
	}

	public void setCodejournee(int codejournee) {
		this.codejournee = codejournee;
	}

	public CodeJour getCodeJour() {
		return codeJour;
	}

	public void setCodeJour(CodeJour codeJour) {
		this.codeJour = codeJour;
	}

	public String annuler() {
		return null;
	}

	public ClasseEmp getClasseEmp() {
		return classeEmp;
	}

	public void setClasseEmp(ClasseEmp classeEmp) {
		this.classeEmp = classeEmp;
	}

	public StreamedContent getPhoto() {

		byte[] imageInByte2 = null;
		photo = null;
		if (identite != null && identite.getPhoto() != null) {

			imageInByte2 = identite.getPhoto();
			InputStream is = new ByteArrayInputStream(imageInByte2);

			photo = new DefaultStreamedContent(is, "image/png");

		}

		return photo;

	}

	public String getRenderedimage() {
		return renderedimage;
	}

	public void setRenderedimage(String renderedimage) {
		this.renderedimage = renderedimage;
	}

	public String getMatricul() {
		return matricul;
	}

	public void setMatricul(String matricul) {
		this.matricul = matricul;
	}

	public ArrayList<Identite> getCollaborateurs() {
		return collaborateurs;
	}

	public void setCollaborateurs(ArrayList<Identite> collaborateurs) {
		this.collaborateurs = collaborateurs;
	}

	public Long getNumdossier() {
		return numdossier;
	}

	public void setNumdossier(Long numdossier) {
		this.numdossier = numdossier;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getUniteOrganisationnelle() {
		return uniteOrganisationnelle;
	}

	public void setUniteOrganisationnelle(String uniteOrganisationnelle) {
		this.uniteOrganisationnelle = uniteOrganisationnelle;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public int getMaxSelectCollaborateurs() {
		return maxSelectCollaborateurs;
	}

	public void setMaxSelectCollaborateurs(int maxSelectCollaborateurs) {
		this.maxSelectCollaborateurs = maxSelectCollaborateurs;
	}

	public String getStCivile() {
		return stCivile;
	}

	public void setStCivile(String stCivile) {
		this.stCivile = stCivile;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public List<ClasseEmp> getClasseEmps() {
		return classeEmps;
	}

	public void setClasseEmps(List<ClasseEmp> classeEmps) {
		this.classeEmps = classeEmps;
	}

	public Long getNombreCycledetravail() {
		return nombreCycledetravail;
	}

	public void setNombreCycledetravail(Long nombreCycledetravail) {
		this.nombreCycledetravail = nombreCycledetravail;
	}

	public String getPrecedentCycledetravail() {
		return precedentCycledetravail;
	}

	public void setPrecedentCycledetravail(String precedentCycledetravail) {
		this.precedentCycledetravail = precedentCycledetravail;
	}

	public int getRestePrecedCycle() {
		return restePrecedCycle;
	}

	public void setRestePrecedCycle(int restePrecedCycle) {
		this.restePrecedCycle = restePrecedCycle;
	}

	public int getResteSuivantCycle() {
		return resteSuivantCycle;
	}

	public void setResteSuivantCycle(int resteSuivantCycle) {
		this.resteSuivantCycle = resteSuivantCycle;
	}

	public String getSuivantCycledetravail() {
		return suivantCycledetravail;
	}

	public void setSuivantCycledetravail(String suivantCycledetravail) {
		this.suivantCycledetravail = suivantCycledetravail;
	}

	public int getNombreSelectionCycle() {
		return nombreSelectionCycle;
	}

	public void setNombreSelectionCycle(int nombreSelectionCycle) {
		this.nombreSelectionCycle = nombreSelectionCycle;
	}

	public int getNombreSoumissionGta() {
		return nombreSoumissionGta;
	}

	public void setNombreSoumissionGta(int nombreSoumissionGta) {
		this.nombreSoumissionGta = nombreSoumissionGta;
	}

	public String getPrecedentSoumissionGta() {
		return precedentSoumissionGta;
	}

	public void setPrecedentSoumissionGta(String precedentSoumissionGta) {
		this.precedentSoumissionGta = precedentSoumissionGta;
	}

	public String getSuivantSoumissionGta() {
		return suivantSoumissionGta;
	}

	public void setSuivantSoumissionGta(String suivantSoumissionGta) {
		this.suivantSoumissionGta = suivantSoumissionGta;
	}

	public int getRestePrecedGta() {
		return restePrecedGta;
	}

	public void setRestePrecedGta(int restePrecedGta) {
		this.restePrecedGta = restePrecedGta;
	}

	public int getResteSuivantGta() {
		return resteSuivantGta;
	}

	public void setResteSuivantGta(int resteSuivantGta) {
		this.resteSuivantGta = resteSuivantGta;
	}

	public int getNombreSelectionGta() {
		return nombreSelectionGta;
	}

	public void setNombreSelectionGta(int nombreSelectionGta) {
		this.nombreSelectionGta = nombreSelectionGta;
	}

	public Long getNombreClasseempolye() {
		return nombreClasseempolye;
	}

	public void setNombreClasseempolye(Long nombreClasseempolye) {
		this.nombreClasseempolye = nombreClasseempolye;
	}

	public String getPrecedentClasseempolye() {
		return precedentClasseempolye;
	}

	public void setPrecedentClasseempolye(String precedentClasseempolye) {
		this.precedentClasseempolye = precedentClasseempolye;
	}

	public int getRestePrecedClasse() {
		return restePrecedClasse;
	}

	public void setRestePrecedClasse(int restePrecedClasse) {
		this.restePrecedClasse = restePrecedClasse;
	}

	public String getSuivantClasseempolye() {
		return suivantClasseempolye;
	}

	public void setSuivantClasseempolye(String suivantClasseempolye) {
		this.suivantClasseempolye = suivantClasseempolye;
	}

	public int getNombreSelectionClasse() {
		return nombreSelectionClasse;
	}

	public void setNombreSelectionClasse(int nombreSelectionClasse) {
		this.nombreSelectionClasse = nombreSelectionClasse;
	}

	public int getResteSuivantClasse() {
		return resteSuivantClasse;
	}

	public void setResteSuivantClasse(int resteSuivantClasse) {
		this.resteSuivantClasse = resteSuivantClasse;
	}

	public Long getNombreGroupeCompteur() {
		return nombreGroupeCompteur;
	}

	public void setNombreGroupeCompteur(Long nombreGroupeCompteur) {
		this.nombreGroupeCompteur = nombreGroupeCompteur;
	}

	public String getPrecedentGroupeCompteur() {
		return precedentGroupeCompteur;
	}

	public void setPrecedentGroupeCompteur(String precedentGroupeCompteur) {
		this.precedentGroupeCompteur = precedentGroupeCompteur;
	}

	public String getSuivantGroupeCompteur() {
		return suivantGroupeCompteur;
	}

	public void setSuivantGroupeCompteur(String suivantGroupeCompteur) {
		this.suivantGroupeCompteur = suivantGroupeCompteur;
	}

	public int getRestePrecedCompteur() {
		return restePrecedCompteur;
	}

	public void setRestePrecedCompteur(int restePrecedCompteur) {
		this.restePrecedCompteur = restePrecedCompteur;
	}

	public int getNombreSelectionCompteur() {
		return nombreSelectionCompteur;
	}

	public void setNombreSelectionCompteur(int nombreSelectionCompteur) {
		this.nombreSelectionCompteur = nombreSelectionCompteur;
	}

	public int getResteSuivantCompteur() {
		return resteSuivantCompteur;
	}

	public void setResteSuivantCompteur(int resteSuivantCompteur) {
		this.resteSuivantCompteur = resteSuivantCompteur;
	}

	public Long getNombreGroupeActivite() {
		return nombreGroupeActivite;
	}

	public void setNombreGroupeActivite(Long nombreGroupeActivite) {
		this.nombreGroupeActivite = nombreGroupeActivite;
	}

	public int getRestePrecedActivite() {
		return restePrecedActivite;
	}

	public void setRestePrecedActivite(int restePrecedActivite) {
		this.restePrecedActivite = restePrecedActivite;
	}

	public String getSuivantGroupeActivite() {
		return suivantGroupeActivite;
	}

	public void setSuivantGroupeActivite(String suivantGroupeActivite) {
		this.suivantGroupeActivite = suivantGroupeActivite;
	}

	public int getResteSuivantActivite() {
		return resteSuivantActivite;
	}

	public void setResteSuivantActivite(int resteSuivantActivite) {
		this.resteSuivantActivite = resteSuivantActivite;
	}

	public String getPrecedentGroupeActivite() {
		return precedentGroupeActivite;
	}

	public void setPrecedentGroupeActivite(String precedentGroupeActivite) {
		this.precedentGroupeActivite = precedentGroupeActivite;
	}

	public int getNombreSelectionActivite() {
		return nombreSelectionActivite;
	}

	public void setNombreSelectionActivite(int nombreSelectionActivite) {
		this.nombreSelectionActivite = nombreSelectionActivite;
	}

	public Long getNombreGroupeImputation() {
		return nombreGroupeImputation;
	}

	public void setNombreGroupeImputation(Long nombreGroupeImputation) {
		this.nombreGroupeImputation = nombreGroupeImputation;
	}

	public String getPrecedentGroupeImputation() {
		return precedentGroupeImputation;
	}

	public void setPrecedentGroupeImputation(String precedentGroupeImputation) {
		this.precedentGroupeImputation = precedentGroupeImputation;
	}

	public String getSuivantGroupeImputation() {
		return suivantGroupeImputation;
	}

	public void setSuivantGroupeImputation(String suivantGroupeImputation) {
		this.suivantGroupeImputation = suivantGroupeImputation;
	}

	public int getRestePrecedImputation() {
		return restePrecedImputation;
	}

	public void setRestePrecedImputation(int restePrecedImputation) {
		this.restePrecedImputation = restePrecedImputation;
	}

	public int getResteSuivantImputation() {
		return resteSuivantImputation;
	}

	public void setResteSuivantImputation(int resteSuivantImputation) {
		this.resteSuivantImputation = resteSuivantImputation;
	}

	public int getNombreSelectionImputation() {
		return nombreSelectionImputation;
	}

	public void setNombreSelectionImputation(int nombreSelectionImputation) {
		this.nombreSelectionImputation = nombreSelectionImputation;
	}

	public List<GroupeCompteur> getGroupeCompteurs() {
		return groupeCompteurs;
	}

	public void setGroupeCompteurs(List<GroupeCompteur> groupeCompteurs) {
		this.groupeCompteurs = groupeCompteurs;
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

	public GroupeCompteur getGroupeCompteur() {
		return groupeCompteur;
	}

	public void setGroupeCompteur(GroupeCompteur groupeCompteur) {
		this.groupeCompteur = groupeCompteur;
	}

	public GroupeActivite getGroupeActivite() {
		return groupeActivite;
	}

	public void setGroupeActivite(GroupeActivite groupeActivite) {
		this.groupeActivite = groupeActivite;
	}

	public GroupeImputation getGroupeImputation() {
		return groupeImputation;
	}

	public void setGroupeImputation(GroupeImputation groupeImputation) {
		this.groupeImputation = groupeImputation;
	}

	public AffectationCycle getAffectationCycle() {
		return affectationCycle;
	}

	public void setAffectationCycle(AffectationCycle affectationCycle) {
		this.affectationCycle = affectationCycle;
	}

	public PlanningTheorique getPlanningTheorique() {
		return planningTheorique;
	}

	public void setPlanningTheorique(PlanningTheorique planningTheorique) {
		this.planningTheorique = planningTheorique;
	}

	public List<AffectationCycle> getAffectationCycles() {
		return affectationCycles;
	}

	public void setAffectationCycles(List<AffectationCycle> affectationCycles) {
		this.affectationCycles = affectationCycles;
	}

	public GroupeImputation getNewgroupeImputation() {
		return newgroupeImputation;
	}

	public void setNewgroupeImputation(GroupeImputation newgroupeImputation) {
		this.newgroupeImputation = newgroupeImputation;
	}

	public GroupeActivite getNewgroupeActivite() {
		return newgroupeActivite;
	}

	public void setNewgroupeActivite(GroupeActivite newgroupeActivite) {
		this.newgroupeActivite = newgroupeActivite;
	}

	public ArrayList<GroupeActivite> getGroupeActivitehistorique() {
		return groupeActivitehistorique;
	}

	public void setGroupeActivitehistorique(
			ArrayList<GroupeActivite> groupeActivitehistorique) {
		this.groupeActivitehistorique = groupeActivitehistorique;
	}

	public ArrayList<GroupeImputation> getGroupeImputationhistorique() {
		return groupeImputationhistorique;
	}

	public void setGroupeImputationhistorique(
			ArrayList<GroupeImputation> groupeImputationhistorique) {
		this.groupeImputationhistorique = groupeImputationhistorique;
	}

	public ArrayList<GroupeCompteur> getGroupeCompteurshistorique() {
		return groupeCompteurshistorique;
	}

	public void setGroupeCompteurshistorique(
			ArrayList<GroupeCompteur> groupeCompteurshistorique) {
		this.groupeCompteurshistorique = groupeCompteurshistorique;
	}

	public GroupeCompteur getNewgroupeCompteur() {
		return newgroupeCompteur;
	}

	public void setNewgroupeCompteur(GroupeCompteur newgroupeCompteur) {
		this.newgroupeCompteur = newgroupeCompteur;
	}

	public ContratGta getNewsoumissionGta() {
		return newsoumissionGta;
	}

	public void setNewsoumissionGta(ContratGta newsoumissionGta) {
		this.newsoumissionGta = newsoumissionGta;
	}

	public ContratGta getSoumissonGta() {
		return soumissonGta;
	}

	public void setSoumissonGta(ContratGta soumissonGta) {
		this.soumissonGta = soumissonGta;
	}

	public List<ContratGta> getSoumissionGtas() {
		return soumissionGtas;
	}

	public void setSoumissionGtas(List<ContratGta> soumissionGtas) {
		this.soumissionGtas = soumissionGtas;
	}

	public ArrayList<ContratGta> getSoumissionGtashistorique() {
		return soumissionGtashistorique;
	}

	public void setSoumissionGtashistorique(
			ArrayList<ContratGta> soumissionGtashistorique) {
		this.soumissionGtashistorique = soumissionGtashistorique;
	}

	public List<ClasseEmp> getClasseEmpshistorique() {
		return classeEmpshistorique;
	}

	public void setClasseEmpshistorique(List<ClasseEmp> classeEmpshistorique) {
		this.classeEmpshistorique = classeEmpshistorique;
	}

	public ClasseEmp getNewclasseEmp() {
		return newclasseEmp;
	}

	public void setNewclasseEmp(ClasseEmp newclasseEmp) {
		this.newclasseEmp = newclasseEmp;
	}
	
	
	

	


	    

	    public void submit() {
	    		System.out.println(checkedValues);
	    }

	    public List<String> getCheckedValues() {
	        return checkedValues;
	    }

	    public void setCheckedValues(List<String> checkedValues) {
	        this.checkedValues = checkedValues;
	    }

	    public List<String> getAvailableValues() {
	        return availableValues;
	    }

}
