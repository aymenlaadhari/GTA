/*
 * Cette classe calcule le planning d'un employé (Par Période, jour ...)
 * Elle gère également les éxceptions (Modification du planning sur : Cycle, Semaine (En Cours), Journée, Plage(En cours))
 * Elle calcule également le planning réel.
 */
package managedBeans.gta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.AffectationCycle;
import com.yesserp.domain.gta.CalculGta;
import com.yesserp.domain.gta.Calendrier;
import com.yesserp.domain.gta.CodeCalendrier;
import com.yesserp.domain.gta.DateCalculGta;
import com.yesserp.domain.gta.ExceptionSemaineType;
import com.yesserp.domain.gta.Exceptionjournee;
import com.yesserp.domain.gta.TypeCalucle;
import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionOccurence.GestionOccurenceLocal;
import com.yesserp.sessionbean.gta.gestionaffectationcycle.GestionAffectationCycleLocal;
import com.yesserp.sessionbean.gta.gestioncalculgta.GestionCalculGtaPlanningLocal;
import com.yesserp.sessionbean.gta.gestioncalendrier.GestionCalendrierLocal;
import com.yesserp.sessionbean.gta.gestioncodecalendrier.GestionCodeCalendrierLocal;
import com.yesserp.sessionbean.gta.gestiondatecalculGta.GestionDateCalculLocal;
import com.yesserp.sessionbean.gta.gestiondatejournees.GestionDateJourneesLocal;
import com.yesserp.sessionbean.gta.gestionexceptionjournee.GestionExceptionJourneeLocal;
import com.yesserp.sessionbean.gta.gestionexceptionsemaine.GestionExceptionSemaineLocal;
import com.yesserp.sessionbean.gta.gestionplanningtheorique.GestionPlanningTheoriqueLocal;
import com.yesserp.sessionbean.gta.gestiontypecalcul.GestionTypeCalculLocal;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionplagehoraire.GestionPlageHoraireLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;

@ManagedBean
@ViewScoped		// !Important : @ViewScoped représente le cycle de vie de cette ManagedBean
				// Pour optimiser la gestion des varible (cycle de vie des variable) par le serveur d'appli (Il faut éviter le @SessionScoped)
				// Sauf par exemmple pour le cas de l'authentification (Pour garder la valeur des variables de connexion)
public class PlanningThBean {
	private Date start;	// Dates de la journée selectionné à partir du scheduler pour affichage de la PL
	private Date end;	// Dates de la journée selectionné à partir du scheduler pour affichage de la PL
	private String locale;
	private int deb;
	private int fin;
	private int year;
	private int month;
	private int day;
	private TimelineModel model;
	private JourneeType journeeTypePlage = new JourneeType();
	private Identite identite = new Identite();
	private boolean formdisplayed = false;
	private List<SemaineType> semaineTypes = new ArrayList<>();
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private List<Identite> identites = new ArrayList<>();
	private SemaineType semaineType = new SemaineType();
	private JourneeType journeeType = new JourneeType();
	private List<PlageHoraire> plageHoraires = new ArrayList<>();
	private List<AffectationCycle> affectationCycles = new ArrayList<>();
	private AffectationCycle affectationCycle = new AffectationCycle();
	private JourneeType journeeTypeException = new JourneeType();
	private Exceptionjournee exceptionjournee = new Exceptionjournee();
	private ExceptionSemaineType exceptionSemaineType = new ExceptionSemaineType();
	private SemaineType seaineexception = new SemaineType();
	private List<Exceptionjournee> exceptionjournees = new ArrayList<>();
	private Date dateDebut;
	private Date dateFin;
	private Date datejour;
	private Date dateEffet;
	private DateTime dateCalcul;
	private DateTime dateAffectationCycle;
	private DateTime dateFinCalcul;
	private CodeJour codeJour = new CodeJour();
	private CycleTravail cycleTravail = null;
	private CodeCalendrier codeCalendrier;
	private CalculGta calculGta = new CalculGta();
	private List<CalculGta> calculGtas = new ArrayList<>();
	private List<TypeCalucle> typeCalucles = new ArrayList<>();
	private List<TypeCalucle> selectedTypes = new ArrayList<>();
	private DateCalculGta dateCalculGta = new DateCalculGta();
	private List<SemaineType> ordredListSemaineTypes = new ArrayList<>();
	private List<CycleTravail> cycleTravails = new ArrayList<>();
	private List<JourneeType> journeeTypesCycle = new ArrayList<>();
	private List<Calendrier> calendriers = new ArrayList<>();
	private List<CalculGta> listPlanning = new ArrayList<>();
	private List<ExceptionSemaineType> exceptionSemaineTypes = new ArrayList<>();
	private boolean jourFerier = false;
	private int nbSemaines;
	private int numElements;
	private int nbParcour;
	private int nbElements;
	private int index;
	private int date;
	private int nbAnne;
	private int nbJours;
	private Date omar = new Date();
	private List<Date> differentAfeectationDates = new ArrayList<>();
	private CycleTravail cycleException = new CycleTravail();
	private Date dateChangementCycle;
	private String tabv;
	private ScheduleModel eventModel = new DefaultScheduleModel();
	private ScheduleEvent event = new DefaultScheduleEvent();
	private ScheduleModel eventModel2 = new DefaultScheduleModel();
	private ScheduleEvent event2 = new DefaultScheduleEvent();
	private JourneeType journeeTypeFerier = new JourneeType();
	private List<Date> datesAffectations = new ArrayList<>();
	private String matricul = "";
	private Long numdossier;
	private String nom = "";
	private String prenom = "";
	private String sexe = "";
	private String poste = "";
	private String uniteOrganisationnelle = "";
	private String stCivile = "";
	private int maxSelectCollaborateurs = 10;
	private ArrayList<Identite> collaborateurs = new ArrayList<>();
	/*
	 * Design Pattern abstruct Facory :
	 * Appel des interfaces local des session bean
	 * Le serveur d'application gère les appels et cycles de vie ...
	 */
	
	@EJB
	GestionExceptionSemaineLocal gestionExceptionSemaineLocal;
	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionDateJourneesLocal gestionDateJourneesLocal;
	@EJB
	GestionPlanningTheoriqueLocal gestionPlanningTheoriqueLocal;
	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;
	@EJB
	GestionTypeCalculLocal gestionTypeCalculLocal;
	@EJB
	GestionDateCalculLocal gestionDateCalculLocal;
	@EJB
	GestionCycleTravailLocal cycleTravailLocal;
	@EJB
	GestionCalendrierLocal gestionCalendrierLocal;
	@EJB
	GestionOccurenceLocal gestionOccurenceLocal;
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
	@EJB
	GestionCodeCalendrierLocal gestionCodeCalendrierLocal;
	@EJB
	GestionAffectationCycleLocal gestionAffectationCycleLocal;
	@EJB
	GestionExceptionJourneeLocal gestionExceptionJourneeLocal;
	@EJB
	GestionCalculGtaPlanningLocal gestionCalculGtaPlanningLocal;
	@EJB
	GestionPlageHoraireLocal gestionPlageHoraireLocal;

	public Identite getIdentite() {
		return identite;
	}

	public void setIdentite(Identite identite) {
		this.identite = identite;
	}

	/*
	 * Récupération des différents de l'employé selectionné
	 */
	public void rowselect() {
		List<CodeCalendrier> lisssst = gestionCalendrierLocal.findCalendrierForIdentity(identite);
		CodeCalendrier calendrier = lisssst.get(0);
		calendriers = gestionCodeCalendrierLocal
				.trouverJoursFerierParCalendrier(calendrier);
		
		cycleTravails = cycleTravailLocal
				.trouverCycleAffectePourEmployee(identite);
		for (CycleTravail c : cycleTravails) {
			System.out.println(c.getCodect());
		}
		cycleTravail = cycleTravails.get(0);
		System.out.println("Cyyyyclee" + cycleTravail.getCodect());
		setIndex(identite.getContratGta().getIndex());
		dateEffet = identite.getContratGta().getDateeffe();
		dateAffectationCycle = new DateTime(dateEffet);
		formdisplayed = true;

	}

	/*
	 * Affiche les plages pour la journée type selectionnée à partir du scheduler
	 */
	public void afficherPlagesPourJourneeType(List<PlageHoraire> plageHoraires,
			Identite identite, Date datedebu, Date datefin,
			JourneeType journeeType) {
		model = new TimelineModel();
		DateTime dateTime = new DateTime(datedebu);
		setYear(dateTime.getYear());
		setMonth(dateTime.getMonthOfYear());
		setDay(dateTime.getDayOfMonth());

		for (PlageHoraire p : plageHoraires) {
			deb = gestionPlageHoraireLocal.trouverHeureDeb(p, journeeType);
			System.out.println(deb);
			fin = gestionPlageHoraireLocal.trouverHeureFin(p, journeeType);
			System.out.println(fin);
			datedebu = new Date(year - 1900, month - 1, day, deb, 00);
			datefin = new Date(year - 1900, month - 1, day, fin, 00);
			TimelineEvent event = new TimelineEvent(p.getCodeph(), datedebu,
					datefin, true, identite.getNom(), p.getCodeph()
							.toLowerCase());
			model.add(event);

		}

	}

	/*
	 * Methode à travailler pour gérer l'exception (modification des données d'une plage.
	 * Attendre MARDI Max de la Part de Houssem
	 */
	public void changer(TimelineModificationEvent e) {
		System.out.println("okkkkk");
		System.out.println(e.getTimelineEvent());

	}

	/*
	 * Récupère des données nécéssaire à partir du sched et appel la fonction de l'affichage des plages
	 */
	public void onEventSelect(SelectEvent selectEvent) {
		Date date;
		Date date2;
		event = (ScheduleEvent) selectEvent.getObject();
		date = event.getEndDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		date = cal.getTime();
		date2 = date;
		System.out.println(date2);
		journeeTypePlage = gestionCalculGtaPlanningLocal
				.trouverJourneeParDate(date);
		plageHoraires = gestionPlanningTheoriqueLocal
				.findPlageHorairessByJourneeType(journeeTypePlage);
		afficherPlagesPourJourneeType(plageHoraires, identite, date, date2,
				journeeTypePlage);
		tabv = "0";
	}

	/*
	 * Supprime les données existant entre les deux dates de calcules mis en paramètres
	 * Calcule du planning théorique
	 * Gestion : 	Cycle Journée ou Semaine
	 * 				Gestion de l'index
	 * 				Gère les changements des cycles (par rapport à l'employé et par rapport au paramétrage.
	 * Affiche le résultat entre deux dates et sauvegarde les deux dates paramètres de calcul
	 * Trace le type de calcul, les dates, l'heure de lancement du calcul
	 * Alimente la BD par les résultats de calcul
	 */
	public void calculerPlanningTheorique() {
		gestionCalculGtaPlanningLocal.supprimerPlanningTheorique();
		datesAffectations = gestionAffectationCycleLocal
				.trouverDatesChangementDeCycle(identite);
		tabv = "1";
		calculGtas = new ArrayList<>();
		dateCalculGta = new DateCalculGta();
		dateCalcul = new DateTime(dateDebut);
		dateFinCalcul = new DateTime(dateFin);
		nbJours = Days.daysBetween(dateCalcul, dateFinCalcul).getDays() + 1;
		dateCalculGta.setDateDebutCalcul(dateDebut);
		dateCalculGta.setDateFinCalcul(dateFin);
		dateCalculGta.setTypeCalucles(selectedTypes);
		gestionDateCalculLocal.ajouterDateCalculGta(dateCalculGta);
		eventModel.clear();

		for (int i = 0; i < nbJours; i++) {
			for (Date d : datesAffectations) {
				DateTime daa = new DateTime(d);
				if (daa.equals(dateCalcul)) {
					System.out.println("Date de changement  : " + dateCalcul);
					cycleTravail = gestionAffectationCycleLocal
							.findAffectationCycleForEmployeeEtCycle(
									dateCalcul.toDate(), identite);

				}
			}
			System.out.println("Cyycle apres verification : "
					+ cycleTravail.getCodect());
			nbElements = 0;
			nbParcour = 0;
			nbSemaines = 0;
			date = 0;
			dateCalcul = new DateTime(dateDebut);
			if (cycleTravail.getTypeCycle().equals("j")) {
				journeeTypes = cycleTravailLocal
						.findJourneesForCycle(cycleTravail);

				index = identite.getContratGta().getIndex();
				for (int k = 0; k < journeeTypes.size(); k++) {
					System.out.println("index before : " + index);
					if (index == journeeTypes.size()) {
						index = 0;
					}
					System.out.println("index after :  : " + index);
					journeeTypesCycle.add(journeeTypes.get(index));
					index++;
				}

				nbElements = journeeTypesCycle.size();
				nbParcour = Days.daysBetween(dateAffectationCycle, dateCalcul)
						.getDays() + 1;

				numElements = nbParcour % nbElements;
				if (numElements == 0) {
					journeeType = journeeTypesCycle.get(journeeTypesCycle
							.size() - 1);
				} else {
					journeeType = journeeTypesCycle.get(numElements - 1);
				}
				if (dateDebut.getDay() == 0) {
					setDate(dateDebut.getDay() + 7);
				} else
					setDate(dateDebut.getDay());
				codeJour = gestionCodeJourLocal.findCodeJourById(date);
			} else {
				semaineTypes = gestionSemaineTypeLocal
						.findSemaineTypeForCycle(cycleTravail);
		//		index = identite.getContratGta().getIndex();
//				index = identite.getContratGta().getIndex();
//				index = gestionAffectationCycleLocal.findAffectationCycleForEmployeeEtCycle(identite.getContratGta().getDateEffet(), identite).getIndex();
				for (int j = 0; j < semaineTypes.size(); j++) {
					if (index == semaineTypes.size()) {
						index = 0;
					}
					ordredListSemaineTypes.add(semaineTypes.get(index));
					index++;
				}
				nbElements = ordredListSemaineTypes.size();
				nbParcour = Weeks.weeksBetween(
						dateAffectationCycle.dayOfWeek().withMinimumValue()
								.minusDays(1),
						dateCalcul.dayOfWeek().withMaximumValue().plusDays(1))
						.getWeeks();

				numElements = nbParcour % nbElements;

				if (numElements == 0) {
					semaineType = ordredListSemaineTypes
							.get(ordredListSemaineTypes.size() - 1);
				} else {
					setSemaineType(ordredListSemaineTypes.get(numElements - 1));
				}
				if (dateDebut.getDay() == 0) {
					setDate(dateDebut.getDay() + 7);
				} else
					setDate(dateDebut.getDay());
				codeJour = gestionCodeJourLocal.findCodeJourById(date);
				setJourneeType(gestionSemaineTypeLocal
						.trouverJourneParCodeJourEtSemaineType(semaineType,
								codeJour));
			}
			journeeTypeFerier = testerEnCalendrier(dateCalcul);
			calculGta.setCodeJour(codeJour);
			if (journeeTypeFerier == null)
				calculGta.setJourneeType(journeeType);
			else
				calculGta.setJourneeType(journeeTypeFerier);
			if (cycleTravail.getTypeCycle().equals("s")) {
				calculGta.setSemaineType(semaineType);
			}
			calculGta.setIdentite(identite);
			calculGta.setDateJour(dateDebut);
			calculGtas.add(calculGta);
			omar.setTime(dateDebut.getTime());
			dateDebut = new Date();
			eventModel.addEvent(new DefaultScheduleEvent(calculGta
					.getJourneeType().getCodejt(), dateDebut, dateDebut));
			dateDebut.setTime(omar.getTime() + 1 * 24 * 60 * 60 * 1000);
			gestionCalculGtaPlanningLocal.ajouterPlanning(calculGta);
			calculGta = new CalculGta();
		}

	}

	/*
	 * Test les données de calendrier GTA
	 * Sert à la fonction planning
	 */
	public JourneeType testerEnCalendrier(DateTime date) {
		JourneeType jourFerier = null;
		for (Calendrier c : calendriers) {
			DateTime datejourFerier = new DateTime(c.getDateJourFerier());
			if (datejourFerier.equals(date)) {
				jourFerier = new JourneeType();
				jourFerier.setCodejt(c.getNomJourFerier());
			}

		}

		return jourFerier;

	}

	/*
	 * En cours e construction by Houssem
	 */
	public List<JourneeType> testerSemaineException(Date date) {
		SemaineType sem = new SemaineType();

		exceptionSemaineTypes = gestionExceptionSemaineLocal
				.findExceptionSemaineForEmployee(identite);
		for (ExceptionSemaineType e : exceptionSemaineTypes) {
			if (date.equals(e.getDateChangement()))
				sem = e.getSemaineType();
		}
		return gestionSemaineTypeLocal.trouverJourneesParSemaine(sem);

	}

	/*
	 * Calcule le planning réel
	 * Le changement de de plage journée et semaine n'est pas encore achevé (16/08/2014)
	 */
	public void calculerPlanningReelCycleJournee() {
		eventModel2.clear();
		listPlanning = gestionCalculGtaPlanningLocal
				.trouverPlanningPourEmployee(identite);
		exceptionjournees = gestionExceptionJourneeLocal
				.findJourneFromException(identite);
		for (CalculGta pl : listPlanning) {
			for (Exceptionjournee e : exceptionjournees) {
				if (pl.getDateJour().equals(e.getDateJournee())) {
					pl.setJourneeType(e.getJourneeTypeApres());
				}

			}

			DateTime dateTime = new DateTime(pl.getDateJour());

			eventModel2.addEvent(new DefaultScheduleEvent(pl.getJourneeType()
					.getCodejt(), dateTime.plusDays(1).toDate(), dateTime
					.plusDays(1).toDate()));

		}

	}

	/*
	 * Fct d'initialisation des données
	 */
	@PostConstruct
	public void init() {
		typeCalucles = gestionTypeCalculLocal.findAllTypeCalucle();
		identites = gestionIdentiteLocal.findAll();

	}

	// **********Partiee Exception***************

	public void ajouterEcveptionJournee() {
		System.out.println(identite.getNom());
		exceptionjournee.setIdentite(identite);
		exceptionjournee.setJourneeTypeApres(journeeTypeException);
		gestionExceptionJourneeLocal.ajouterExceptionjournee(exceptionjournee);

	}

	public void ajouterExceptionSemaineType() {
		exceptionSemaineType.setIdentite(identite);
		exceptionSemaineType.setSemaineType(seaineexception);
		gestionExceptionSemaineLocal
				.ajouterExceptionSemaineType(exceptionSemaineType);
	}

	public void ajouterExceptionCycle() {
		cycleTravailLocal.affectCycleToEmployee(identite, cycleException,
				dateChangementCycle);
	}
	
	//*************************************************************** FIn de l'ajout des exceptions

	public boolean isFormdisplayed() {
		return formdisplayed;
	}

	public void setFormdisplayed(boolean formdisplayed) {
		this.formdisplayed = formdisplayed;
	}

	public List<SemaineType> getSemaineTypes() {
		return semaineTypes;
	}

	public void setSemaineTypes(List<SemaineType> semaineTypes) {
		this.semaineTypes = semaineTypes;
	}

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public CodeJour getCodeJour() {
		return codeJour;
	}

	public void setCodeJour(CodeJour codeJour) {
		this.codeJour = codeJour;
	}

	public Date getDatejour() {
		return datejour;
	}

	public void setDatejour(Date datejour) {
		this.datejour = datejour;
	}

	public int getNbSemaines() {
		return nbSemaines;
	}

	public void setNbSemaines(int nbSemaines) {
		this.nbSemaines = nbSemaines;
	}

	public int getNumElements() {
		return numElements;
	}

	public void setNumElements(int numElements) {
		this.numElements = numElements;
	}

	public int getNbParcour() {
		return nbParcour;
	}

	public void setNbParcour(int nbParcour) {
		this.nbParcour = nbParcour;
	}

	public DateTime getDateCalcul() {
		return dateCalcul;
	}

	public void setDateCalcul(DateTime dateCalcul) {
		this.dateCalcul = dateCalcul;
	}

	public DateTime getDateAffectationCycle() {
		return dateAffectationCycle;
	}

	public void setDateAffectationCycle(DateTime dateAffectationCycle) {
		this.dateAffectationCycle = dateAffectationCycle;
	}

	public int getNbElements() {
		return nbElements;
	}

	public void setNbElements(int nbElements) {
		this.nbElements = nbElements;
	}

	public SemaineType getSemaineType() {
		return semaineType;
	}

	public void setSemaineType(SemaineType semaineType) {
		this.semaineType = semaineType;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public int getNbAnne() {
		return nbAnne;
	}

	public void setNbAnne(int nbAnne) {
		this.nbAnne = nbAnne;
	}

	public CalculGta getCalculGta() {
		return calculGta;
	}

	public void setCalculGta(CalculGta calculGta) {
		this.calculGta = calculGta;
	}

	public List<CalculGta> getCalculGtas() {
		return calculGtas;
	}

	public void setCalculGtas(List<CalculGta> calculGtas) {
		this.calculGtas = calculGtas;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<TypeCalucle> getTypeCalucles() {
		return typeCalucles;
	}

	public void setTypeCalucles(List<TypeCalucle> typeCalucles) {
		this.typeCalucles = typeCalucles;
	}

	public List<TypeCalucle> getSelectedTypes() {
		return selectedTypes;
	}

	public void setSelectedTypes(List<TypeCalucle> selectedTypes) {
		this.selectedTypes = selectedTypes;
	}

	public DateCalculGta getDateCalculGta() {
		return dateCalculGta;
	}

	public void setDateCalculGta(DateCalculGta dateCalculGta) {
		this.dateCalculGta = dateCalculGta;
	}

	public int getNbJours() {
		return nbJours;
	}

	public void setNbJours(int nbJours) {
		this.nbJours = nbJours;
	}

	public List<PlageHoraire> getPlageHoraires() {
		return plageHoraires;
	}

	public void setPlageHoraires(List<PlageHoraire> plageHoraires) {
		this.plageHoraires = plageHoraires;
	}

	public List<SemaineType> getOrdredListSemaineTypes() {
		return ordredListSemaineTypes;
	}

	public void setOrdredListSemaineTypes(
			List<SemaineType> ordredListSemaineTypes) {
		this.ordredListSemaineTypes = ordredListSemaineTypes;
	}

	public Date getOmar() {
		return omar;
	}

	public void setOmar(Date omar) {
		this.omar = omar;
	}

	public String getTabv() {
		return tabv;
	}

	public void setTabv(String tabv) {
		this.tabv = tabv;
	}

	public List<CycleTravail> getCycleTravails() {
		return cycleTravails;
	}

	public void setCycleTravails(List<CycleTravail> cycleTravails) {
		this.cycleTravails = cycleTravails;
	}

	public List<JourneeType> getJourneeTypesCycle() {
		return journeeTypesCycle;
	}

	public void setJourneeTypesCycle(List<JourneeType> journeeTypesCycle) {
		this.journeeTypesCycle = journeeTypesCycle;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public CodeCalendrier getCodeCalendrier() {
		return codeCalendrier;
	}

	public void setCodeCalendrier(CodeCalendrier codeCalendrier) {
		this.codeCalendrier = codeCalendrier;
	}

	public List<Identite> getIdentites() {
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

	public List<Calendrier> getCalendriers() {
		return calendriers;
	}

	public void setCalendriers(List<Calendrier> calendriers) {
		this.calendriers = calendriers;
	}

	public boolean isJourFerier() {
		return jourFerier;
	}

	public void setJourFerier(boolean jourFerier) {
		this.jourFerier = jourFerier;
	}

	public JourneeType getJourneeTypeFerier() {
		return journeeTypeFerier;
	}

	public void setJourneeTypeFerier(JourneeType journeeTypeFerier) {
		this.journeeTypeFerier = journeeTypeFerier;
	}

	public List<AffectationCycle> getAffectationCycles() {
		return affectationCycles;
	}

	public void setAffectationCycles(List<AffectationCycle> affectationCycles) {
		this.affectationCycles = affectationCycles;
	}

	public List<Date> getDatesAffectations() {
		return datesAffectations;
	}

	public void setDatesAffectations(List<Date> datesAffectations) {
		this.datesAffectations = datesAffectations;
	}

	public JourneeType getJourneeTypeException() {
		return journeeTypeException;
	}

	public void setJourneeTypeException(JourneeType journeeTypeException) {
		this.journeeTypeException = journeeTypeException;
	}

	public Exceptionjournee getExceptionjournee() {
		return exceptionjournee;
	}

	public void setExceptionjournee(Exceptionjournee exceptionjournee) {
		this.exceptionjournee = exceptionjournee;
	}

	public ExceptionSemaineType getExceptionSemaineType() {
		return exceptionSemaineType;
	}

	public void setExceptionSemaineType(
			ExceptionSemaineType exceptionSemaineType) {
		this.exceptionSemaineType = exceptionSemaineType;
	}

	public SemaineType getSeaineexception() {
		return seaineexception;
	}

	public void setSeaineexception(SemaineType seaineexception) {
		this.seaineexception = seaineexception;
	}

	public List<CalculGta> getListPlanning() {
		return listPlanning;
	}

	public void setListPlanning(List<CalculGta> listPlanning) {
		this.listPlanning = listPlanning;
	}

	public ScheduleModel getEventModel2() {
		return eventModel2;
	}

	public void setEventModel2(ScheduleModel eventModel2) {
		this.eventModel2 = eventModel2;
	}

	public ScheduleEvent getEvent2() {
		return event2;
	}

	public void setEvent2(ScheduleEvent event2) {
		this.event2 = event2;
	}

	public List<Exceptionjournee> getExceptionjournees() {
		return exceptionjournees;
	}

	public void setExceptionjournees(List<Exceptionjournee> exceptionjournees) {
		this.exceptionjournees = exceptionjournees;
	}

	public CycleTravail getCycleException() {
		return cycleException;
	}

	public void setCycleException(CycleTravail cycleException) {
		this.cycleException = cycleException;
	}

	public Date getDateChangementCycle() {
		return dateChangementCycle;
	}

	public void setDateChangementCycle(Date dateChangementCycle) {
		this.dateChangementCycle = dateChangementCycle;
	}

	public List<Date> getDifferentAfeectationDates() {
		return differentAfeectationDates;
	}

	public void setDifferentAfeectationDates(
			List<Date> differentAfeectationDates) {
		this.differentAfeectationDates = differentAfeectationDates;
	}

	public List<ExceptionSemaineType> getExceptionSemaineTypes() {
		return exceptionSemaineTypes;
	}

	public void setExceptionSemaineTypes(
			List<ExceptionSemaineType> exceptionSemaineTypes) {
		this.exceptionSemaineTypes = exceptionSemaineTypes;
	}

	public JourneeType getJourneeTypePlage() {
		return journeeTypePlage;
	}

	public void setJourneeTypePlage(JourneeType journeeTypePlage) {
		this.journeeTypePlage = journeeTypePlage;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getDeb() {
		return deb;
	}

	public void setDeb(int deb) {
		this.deb = deb;
	}

	public int getFin() {
		return fin;
	}

	public void setFin(int fin) {
		this.fin = fin;
	}

	public TimelineModel getModel() {
		return model;
	}

	public void setModel(TimelineModel model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	
	public String getMatricul() {
		return matricul;
	}

	public void setMatricul(String matricul) {
		this.matricul = matricul;
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

	

	public AffectationCycle getAffectationCycle() {
		return affectationCycle;
	}

	public void setAffectationCycle(AffectationCycle affectationCycle) {
		this.affectationCycle = affectationCycle;
	}
	
	
}
