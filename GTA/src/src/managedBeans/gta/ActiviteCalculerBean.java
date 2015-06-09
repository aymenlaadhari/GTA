/*
 * Cette classe calcule des activités réelles pour tout les cas possibles (Sans ou avec pointage ...) Absences, Saisie d'activités
 */
package managedBeans.gta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.ga.SituationCivile;
import com.yesserp.domain.gta.Absence;
import com.yesserp.domain.gta.ActiviteReelCalculer;
import com.yesserp.domain.gta.ActiviteReelSaisie;
import com.yesserp.domain.gta.PlanningReel;
import com.yesserp.domain.gta.Pointage;
import com.yesserp.domain.gta.Tranche;
import com.yesserp.domain.gtaparam.ActivitePresence;
import com.yesserp.domain.gtaparam.HoraireDeReferenceAbsence;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionAbsence.GestionAbsenceLocal;
import com.yesserp.sessionbean.gta.gestionactivitereelcalculer.GestionActiviteReelCalculerLocal;
import com.yesserp.sessionbean.gta.gestionactivitereelsaisie.GestionActiviteReelSaisieLocal;
import com.yesserp.sessionbean.gta.gestionplanningreel.GestionPlanningReelLocal;
import com.yesserp.sessionbean.gta.gestionpointage.GestionPointageLocal;
import com.yesserp.sessionbean.paramgta.gestionactivitepresence.GestionActivitePresenceLocal;
import com.yesserp.sessionbean.paramgta.gestionhorairedereferenceabsence.GestionHoraireDeReferenceAbsenceLocal;
import com.yesserp.sessionbean.paramgta.tranche.GestionTrancheLocal;

@ManagedBean  // Pour gérer l'appel dans la page xhtml
@SessionScoped
public class ActiviteCalculerBean {
	// Liste des employés
	private List<Identite> identites = new ArrayList<Identite>();
	// L'objet de l'employé selectionné
	private Identite selectedIdentite;
	// Variable pour la Liste de plannigs reel	
	private List<PlanningReel> planningReels = new ArrayList<PlanningReel>();
	// Liste des activités réel saisies
	private List<ActiviteReelSaisie> activiteReelSaisies = new ArrayList<ActiviteReelSaisie>();
	// Liste des activités réel calculer
	private List<ActiviteReelCalculer> activiteReelCalculers = new ArrayList<ActiviteReelCalculer>();
	// Liste des pointages
	private List<Pointage> pointages = new ArrayList<Pointage>();
	// Liste des absences
	private List<Absence> absences = new ArrayList<Absence>();
	// Objet activité réel saisie utiliser pour l'ajout
	private ActiviteReelSaisie activiteReelSaisie = new ActiviteReelSaisie();
	private ActiviteReelSaisie activiteReelSaisieAjout = new ActiviteReelSaisie();
	// Objet activité réel calculer utiliser pour l'ajout
	private ActiviteReelCalculer activiteReelCalculer = new ActiviteReelCalculer();
	private ActiviteReelCalculer activiteReelCalculerAjout = new ActiviteReelCalculer();
	

	// Liste des activités présences et absences
	private List<ActivitePresence> activitePresences = new ArrayList<ActivitePresence>();
	private ActivitePresence activitePresence = new ActivitePresence();
	private ActivitePresence activitePresenceAjout = new ActivitePresence();

	// Variable pour forcer le calcule des activités que pour les plages obligatoires 
	private boolean horairesObligatoire = false;
	// Variable pour forcer le calcule des activités par les horaires de référence 
	private boolean horairesRef = false;
	private Date dateDebutFiltre = new Date();
	private Date dateFinFiltre = new Date();
	private Date dateDebutCalcule = new Date();
	private Date dateFinCalcule = new Date();
	
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

	private Date jour =new Date();
	
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
	@EJB
	GestionPlanningReelLocal gestionPlanningReelLocal;
	@EJB
	GestionAbsenceLocal gestionAbsenceLocal;
	@EJB
	GestionActiviteReelSaisieLocal gestionActiviteReelSaisieLocal;
	@EJB
	GestionActiviteReelCalculerLocal gestionActiviteReelCalculerLocal;
	@EJB
	GestionActivitePresenceLocal gestionActivitePresenceLocal;
	@EJB
	GestionTrancheLocal gestionTrancheLocal;
	@EJB
	GestionHoraireDeReferenceAbsenceLocal gestionHoraireDeReferenceAbsenceLocal;
	@EJB
	GestionPointageLocal gestionPointageLocal;

	/*
	 * Initialiser les données relatives à la page
	 */
	@PostConstruct
	public void init() {
		identites = gestionIdentiteLocal.findAll();
		activitePresences = gestionActivitePresenceLocal.findAll();
	}

	/*
	 * Ajouter l'activité saisie
	 */
	public void ajoutActiviteSaisie() {
		try {
			activiteReelSaisieAjout.setIdentite(selectedIdentite);
			activiteReelSaisieAjout.setActivitePresence(activitePresenceAjout);

			gestionActiviteReelSaisieLocal.ajouterActiviteReelSaisie(activiteReelSaisieAjout);
		} catch (Exception exception) {
			System.out.println("Selectionner un employé !");
		}
		activitePresenceAjout=new ActivitePresence();
		activiteReelSaisieAjout= new ActiviteReelSaisie();
	}
	
	
	// Modifier Activite saisie
	public void modifierActiviteSaisie() {
		
			gestionActiviteReelSaisieLocal.modifierActiviteReelSaisie(activiteReelSaisie);
			activiteReelSaisie=new ActiviteReelSaisie();
	}
	
	// supp Activite saisie
	public void supprimerActiviteSaisie() {
			gestionActiviteReelSaisieLocal.supprimerActiviteReelSaisie(activiteReelSaisie);
			activiteReelSaisie=new ActiviteReelSaisie();


	}


	/*
	 * Retourne la durée en format hh:mm:ss entre deux horaires
	 */
	@SuppressWarnings("deprecation")
	public Date getDuree(Date heureFin, Date heureDebut) {
		try {

			if (heureFin.getHours() > heureDebut.getHours()) {
				heureFin.setHours(Math.abs(heureFin.getHours()
						- heureDebut.getHours()));
			} else {
				heureFin.setHours(Math.abs((heureFin.getHours() + 24)
						- heureDebut.getHours()));
			}
			heureFin.setMinutes(Math.abs(heureFin.getMinutes()
					- heureDebut.getMinutes()));
			heureFin.setSeconds(Math.abs(heureFin.getSeconds()
					- heureDebut.getSeconds()));
			return heureFin;
		} catch (Exception exception) {
			System.out.println("erreur");
			return null;
		}
	}

	/*
	 * Pour gérer le sémaphore de l'affichage
	 */
	public String getActivity(ActiviteReelCalculer activiteReelCalculer) {

		if (activiteReelCalculer.getActiviteAbsence() == null)

			return activiteReelCalculer.getActivitePresence().getCodeap();
		else
			return activiteReelCalculer.getActiviteAbsence().getCodeaabs();
	}

	/*
	 * Pour gérer le sméphore de l'affichage du poid
	 */
	public int getWeightOfActivity(ActiviteReelCalculer activiteReelCalculer) {
		if (activiteReelCalculer.getActiviteAbsence() == null)

			return activiteReelCalculer.getActivitePresence().getPoids();

		else
			return activiteReelCalculer.getActiviteAbsence().getPoids();
	}

	/*
	 * Ajoute une Activité calculée de nature absence
	 */
	public void addRowActivityAbsence(PlanningReel planningReel) {
		ActiviteReelCalculer activiteReelCalculer;
		activiteReelCalculer = new ActiviteReelCalculer(planningReel.getDate(),
				planningReel.getHeuredeb(), planningReel.getHeurefin(),
				planningReel.getIdentite(), null, planningReel
						.getPlageHoraire().getActiviteAbsence());
		gestionActiviteReelCalculerLocal
				.ajouterActiviteReelCalculer(activiteReelCalculer);
	}

	/*
	 * Ajoute une Activité calculée de nature presence
	 */
	public void addRowActivityPresence(PlanningReel planningReel) {
		ActiviteReelCalculer activiteReelCalculer;
		activiteReelCalculer = new ActiviteReelCalculer(planningReel.getDate(),
				planningReel.getHeuredeb(), planningReel.getHeurefin(),
				planningReel.getIdentite(), planningReel.getPlageHoraire()
						.getActivitePresence(), null);
		gestionActiviteReelCalculerLocal
				.ajouterActiviteReelCalculer(activiteReelCalculer);

	}

	/*
	 * Ajouter une act de pres ==> OK
	 */
	public void addRowActivityPresencePointage(PlanningReel planningReel,
			Date heureDeb, Date heurFin) {
		ActiviteReelCalculer activiteReelCalculer;
		activiteReelCalculer = new ActiviteReelCalculer(planningReel.getDate(),
				heureDeb, heurFin, planningReel.getIdentite(), planningReel
						.getPlageHoraire().getActivitePresence(), null);
		gestionActiviteReelCalculerLocal
				.ajouterActiviteReelCalculer(activiteReelCalculer);
	}

	/*
	 * Idem pour l'abs
	 */
	public void addRowActivityAbsencePointage(PlanningReel planningReel,
			Date heureDeb, Date heurFin) {
		ActiviteReelCalculer activiteReelCalculer;
		activiteReelCalculer = new ActiviteReelCalculer(planningReel.getDate(),
				heureDeb, heurFin, planningReel.getIdentite(), null,
				planningReel.getPlageHoraire().getActiviteAbsence());
		gestionActiviteReelCalculerLocal
				.ajouterActiviteReelCalculer(activiteReelCalculer);
	}

	/*
	 * Ajoute une Activité calculée par nature (après test de l'actvité) et le type de plage.
	 */
	public void addActivityByType(PlanningReel planningReel) {
		switch (planningReel.getPlageHoraire().getTypePlage()) {
		case "obligatoire": {
			addRowActivityPresence(planningReel);
		}
			break;
			// Il faut changer cette liste en dynamique ==> lecture à partir de types.
		case "flexible":
		case "pause":
		case "dejeuner": {
			addRowActivityAbsence(planningReel);
		}
			break;
		}

	}

	/*
	 * Ajoute une liste d'activités calculées sans absences et sans activitées
	 * saisies
	 */
	public void addListOfActivities(List<PlanningReel> planningReels) {
		for (PlanningReel planR : planningReels) {
			addActivityByType(planR);
		}
	}

	/*
	 * Gestion des pointage (En cours)  ==> A affecter ..................... !!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	public void addListOfActivitiesPointage(List<PlanningReel> planningReels,
			List<Pointage> pointages) {
		List<PlanningReel> planParJour;
		List<Pointage> pointageParJour;
		List<Pointage> pointages2;
		for (int i = 0; i < planningReels.size();) {
			pointageParJour = gestionPointageLocal.findByDate(selectedIdentite,
					planningReels.get(i).getDate());
			planParJour = gestionPlanningReelLocal.findByDate(selectedIdentite,
					planningReels.get(i).getDate());
			if (pointageParJour.size() % 2 != 0) {
				System.out.println("Pointage impaire pour la journee "
						+ planningReels.get(i).getDate());
				i += planParJour.size();
			} else {
				Date d1 = null;
				Date d2 = null;
				boolean isPresent = true;
				for (PlanningReel p : planParJour) {
					pointages2 = gestionPointageLocal.findBetweenHours(
							selectedIdentite, p.getDate(), p.getHeuredeb(),
							p.getHeurefin());
					for (int j = 0; j < pointages2.size(); j++) {
						if (isPresent) {
							if (pointages2.get(j).getSens().equals("input")) {
								d1 = pointages2.get(j).getHeurep();
								isPresent = true;
							} else {
								addRowActivityPresencePointage(p, d1,
										pointages2.get(j).getHeurep());
								isPresent = false;
								d2 = pointages2.get(j).getHeurep();

							}

						} else {
							addRowActivityAbsencePointage(p, d2, pointages2
									.get(j).getHeurep());
							isPresent = true;
						}
					}

				}
			}

		}
	}

	/*
	 * Calcule des activités dans le cas où l'employé est soumi au pointage
	 */
	public void calculeActiviteReelAvecPointage(List<PlanningReel> planningReels,
												List<Absence> absences,
												List<ActiviteReelSaisie> activiteReelSaisies,
												List<Pointage> pointages) {
		/*
		 * ___Cas 1___ : L'employé ne posséde ni d'activitées saisies ni
		 * d'absences.
		 */

		if (activiteReelSaisies.size() == 0 && absences.size() == 0) {
			addListOfActivitiesPointage(planningReels, pointages);
		} else

		/*
		 * ___Cas2___: L'employé posséde des absences et n'a pas d'activités
		 * saisies.
		 */

		if (activiteReelSaisies.size() == 0) {
			
		} else
		/*
		 * ___Cas3___: L'employé posséde d'activités saisies et il n'a pas
		 * d'absences
		 */
		if (absences.size() == 0) {

		}
		/*
		 * ___Cas4___: L'employé posséde des activités saisies et des absences
		 */
		else {

		}

		dateDebutCalcule = new Date();
		dateFinCalcule = new Date();
		activiteReelCalculers = gestionActiviteReelCalculerLocal.findAll(selectedIdentite);

	}

	/*
	 * Calcule des activités dans le cas où l'employé est non soumi au pointage
	 */
	public void calculeActiviteReelSansPointage(
			List<PlanningReel> planningReels, List<Absence> absences,
			List<ActiviteReelSaisie> activiteReelSaisies) {

		/*
		 * ___Cas 1___ : L'employé ne posséde ni d'activitées saisies ni
		 * d'absences.
		 */

		if (activiteReelSaisies.size() == 0 && absences.size() == 0) {
			addListOfActivities(planningReels);
		} else

		/*
		 * ___Cas2___: L'employé posséde des absences et n'a pas d'activités
		 * saisies.
		 */

		if (activiteReelSaisies.size() == 0) {
			CalculatesActWithAbs(planningReels);
		} else
		/*
		 * ___Cas3___: L'employé posséde d'activités saisies et il n'a pas
		 * d'absences
		 */
		if (absences.size() == 0) {
			// En cours ...................
		}
		/*
		 * ___Cas4___: L'employé posséde des activités saisies et des absences
		 */
		else {

		}

		dateDebutCalcule = new Date();
		dateFinCalcule = new Date();
		activiteReelCalculers = gestionActiviteReelCalculerLocal.findAll(selectedIdentite);
	}

	
	/*
	 * Methode pricipale pour calcul des activités
	 * 
	 */
	public void calculeActiviteReel() {
		/*
		 * set liste des activités saisies de date début calcule -> date fin de
		 * calcule
		 */
		activiteReelSaisies = gestionActiviteReelSaisieLocal
				.findFromDateToDate(selectedIdentite, dateDebutCalcule,
						dateFinCalcule);
		/*
		 * set liste des absences de date début calcule -> date fin de calcule
		 */
		absences = gestionAbsenceLocal.findFromDateToDate(selectedIdentite,
				dateDebutCalcule, dateFinCalcule);

		/*
		 * set liste des des plannings réel de date début calcule -> date fin de
		 * calcule
		 */
		planningReels = gestionPlanningReelLocal.findFromDateToDate(
				selectedIdentite, dateDebutCalcule, dateFinCalcule);

		/*
		 * set liste des des plannings réel de date début calcule -> date fin de
		 * calcule
		 */
		pointages = gestionPointageLocal.findPointageFromDateToDate(
				selectedIdentite, dateDebutCalcule, dateFinCalcule);

		/*
		 * Suppression des activités réel calculés precédement
		 */
		gestionActiviteReelCalculerLocal.deleteAll();

		// affichage de données contrat GTA
		String msg ="";
		if (selectedIdentite.getContratGta().isGta()) {
			if (selectedIdentite.getContratGta().isPointage()) {
				msg = "- Soumis au pointage";
				calculeActiviteReelAvecPointage(planningReels, absences,
						activiteReelSaisies, pointages);
			} else {
				msg = "- Non soumis au pointage";
				calculeActiviteReelSansPointage(planningReels, absences,
						activiteReelSaisies);
			}
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Calcule effectué pour "
									+ selectedIdentite.getPrenom() + " "
									+ selectedIdentite.getNom() + "\n", msg));
		}
		else{
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Calcule Non effectué pour "
									+ selectedIdentite.getPrenom() + " "
									+ selectedIdentite.getNom() + "\n", "Non soumis à la GTA"));
		}

	}

	/*
	 * Calcule des activités avec absences
	 */
	@SuppressWarnings("deprecation")
	public void CalculatesActWithAbs(List<PlanningReel> planningReels) {
		int cptAbsences = 0;
		List<PlanningReel> l;
		for (int cpt = 0; cpt < planningReels.size();) {
			if (cptAbsences >= absences.size()
					|| !planningReels.get(cpt).getDate()
							.equals(absences.get(cptAbsences).getDateDebut())) {
				l = gestionPlanningReelLocal.findByDate(selectedIdentite,
						planningReels.get(cpt).getDate());
				addListOfActivities(l);
				cpt += l.size();
			} else {
				Date date = absences.get(cptAbsences).getDateDebut();
				while (!date.after(absences.get(cptAbsences).getDateFin())) {
					l = gestionPlanningReelLocal.findByDate(selectedIdentite,
							date);
					addAbsences(l, absences.get(cptAbsences), date);
					date.setDate(date.getDate() + 1);
					cpt += l.size();
				}
				cptAbsences++;
			}

		}
	}

	/*
	 * Ajoute une ligne d'activité en absence
	 */
	public void addRowAbsence(PlanningReel planR, Absence absence) {
		ActiviteReelCalculer activiteReelCalculer = new ActiviteReelCalculer(
				planR.getDate(), planR.getHeuredeb(), planR.getHeurefin(),
				planR.getIdentite(), null, absence.getActiviteAbsence());
		gestionActiviteReelCalculerLocal
				.ajouterActiviteReelCalculer(activiteReelCalculer);

	}

	/*
	 * Ajoute une ligne d'activité en absence forcé par l'horaire de référence
	 */
	public void addRowAbsenceHR(PlanningReel planR, Absence absence,
			HoraireDeReferenceAbsence horaireDeReferenceAbsence) {
		ActiviteReelCalculer activiteReelCalculer = new ActiviteReelCalculer(
				planR.getDate(), horaireDeReferenceAbsence.getHeureDeb(),
				horaireDeReferenceAbsence.getHeureFin(), planR.getIdentite(),
				null, absence.getActiviteAbsence());
		gestionActiviteReelCalculerLocal
				.ajouterActiviteReelCalculer(activiteReelCalculer);

	}

	/*
	 * Ajoute les absence d'une journée pour une date
	 */
	public void addAbsences(List<PlanningReel> l, Absence absence, Date date) {
		int i = 0;
		int j = 0;
		List<Tranche> tranches = gestionTrancheLocal.findByAbsence(absence,date);
		List<HoraireDeReferenceAbsence> horaireDeReferenceAbsences = gestionHoraireDeReferenceAbsenceLocal
				.findByJourneeType(l.get(0).getJourneeType());

		for (PlanningReel planningReel : l) {
			if (planningReel.getPlageHoraire().getTypePlage()
					.equals("obligatoire")) {
				if (tranches.get(i).isAbsent()) {
					if (horairesRef && horaireDeReferenceAbsences.size() != 0) {
						if ((j < horaireDeReferenceAbsences.size())
								&& ((planningReel.getHeuredeb()
										.after(horaireDeReferenceAbsences
												.get(j).getHeureDeb())) && (planningReel
										.getHeurefin()
										.before(horaireDeReferenceAbsences.get(
												j).getHeureFin())))) {

							addRowAbsenceHR(planningReel, absence,
									horaireDeReferenceAbsences.get(j));
							j++;
						}
					}
					addRowAbsence(planningReel, absence);
					i++;
				} else {
					addActivityByType(planningReel);
				}
			} else if (!horairesObligatoire) {
				addActivityByType(planningReel);
			}

		}

	}

	/*
	 * Permet le filtrage des activités affichées dans le datatable par période
	 * de date à date
	 */
	public void filtreDate() {
		absences = gestionAbsenceLocal.findFromDateToDate(selectedIdentite, dateDebutFiltre, dateFinFiltre);
		activiteReelSaisies = gestionActiviteReelSaisieLocal.findFromDateToDate(selectedIdentite, dateDebutFiltre,dateFinFiltre);
		activiteReelCalculers = gestionActiviteReelCalculerLocal.findFromDateToDate(selectedIdentite, dateDebutFiltre,dateFinFiltre);

	}

	
	// Selection d'un identite
	public void onRowSelect(SelectEvent event) {
		
		activiteReelSaisies = gestionActiviteReelSaisieLocal.findAll(selectedIdentite);
		absences = gestionAbsenceLocal.finAll(selectedIdentite);
		activiteReelCalculers = gestionActiviteReelCalculerLocal.findAll(selectedIdentite);
		
		activiteReelSaisies= new ArrayList<ActiviteReelSaisie>();
		activiteReelCalculers=new ArrayList<ActiviteReelCalculer>();
		activiteReelSaisies=gestionActiviteReelSaisieLocal.findAll(selectedIdentite);
		activiteReelCalculers=gestionActiviteReelCalculerLocal.findAll(selectedIdentite);

	}

	
	/////////////////////////////////////////////////////////////////////
	
	
	
	
	
	public List<Identite> getIdentites() {
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

	public List<PlanningReel> getPlanningReels() {
		return planningReels;
	}

	public void setPlanningReels(List<PlanningReel> planningReels) {
		this.planningReels = planningReels;
	}

	public Identite getSelectedIdentite() {
		return selectedIdentite;
	}

	public void setSelectedIdentite(Identite selectedIdentite) {
		this.selectedIdentite = selectedIdentite;
	}

	public List<ActiviteReelSaisie> getActiviteReelSaisies() {
		return activiteReelSaisies;
	}

	public void setActiviteReelSaisies(
			List<ActiviteReelSaisie> activiteReelSaisies) {
		this.activiteReelSaisies = activiteReelSaisies;
	}

	public List<ActiviteReelCalculer> getActiviteReelCalculers() {
		

		return activiteReelCalculers;
	}

	public void setActiviteReelCalculers(
			List<ActiviteReelCalculer> activiteReelCalculers) {
		this.activiteReelCalculers = activiteReelCalculers;
	}

	public List<Absence> getAbsences() {
		return absences;
	}

	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}

	public Date getDateDebutFiltre() {
		return dateDebutFiltre;
	}

	public void setDateDebutFiltre(Date dateDebutFiltre) {
		this.dateDebutFiltre = dateDebutFiltre;
	}

	public Date getDateFinFiltre() {
		return dateFinFiltre;
	}

	public void setDateFinFiltre(Date dateFinFiltre) {
		this.dateFinFiltre = dateFinFiltre;
	}

	public Date getDateDebutCalcule() {
		return dateDebutCalcule;
	}

	public void setDateDebutCalcule(Date dateDebutCalcule) {
		this.dateDebutCalcule = dateDebutCalcule;
	}

	public Date getDateFinCalcule() {
		return dateFinCalcule;
	}

	public void setDateFinCalcule(Date dateFinCalcule) {
		this.dateFinCalcule = dateFinCalcule;
	}

	public ActiviteReelSaisie getActiviteReelSaisie() {
		return activiteReelSaisie;
	}

	public void setActiviteReelSaisie(ActiviteReelSaisie activiteReelSaisie) {
		this.activiteReelSaisie = activiteReelSaisie;
	}

	public List<ActivitePresence> getActivitePresences() {
		return activitePresences;
	}

	public void setActivitePresences(List<ActivitePresence> activitePresences) {
		this.activitePresences = activitePresences;
	}

	public ActivitePresence getActivitePresence() {
		return activitePresence;
	}

	public void setActivitePresence(ActivitePresence activitePresence) {
		this.activitePresence = activitePresence;
	}

	public boolean isHorairesObligatoire() {
		return horairesObligatoire;
	}

	public void setHorairesObligatoire(boolean horairesObligatoire) {
		this.horairesObligatoire = horairesObligatoire;
	}

	public boolean isHorairesRef() {
		return horairesRef;
	}

	public void setHorairesRef(boolean horairesRef) {
		this.horairesRef = horairesRef;
	}

	public List<Pointage> getPointages() {
		return pointages;
	}

	public void setPointages(List<Pointage> pointages) {
		this.pointages = pointages;
	}

	public ActiviteReelSaisie getActiviteReelSaisieAjout() {
		return activiteReelSaisieAjout;
	}

	public void setActiviteReelSaisieAjout(ActiviteReelSaisie activiteReelSaisieAjout) {
		this.activiteReelSaisieAjout = activiteReelSaisieAjout;
	}

	public ActiviteReelCalculer getActiviteReelCalculer() {
		return activiteReelCalculer;
	}

	public void setActiviteReelCalculer(ActiviteReelCalculer activiteReelCalculer) {
		this.activiteReelCalculer = activiteReelCalculer;
	}

	public ActiviteReelCalculer getActiviteReelCalculerAjout() {
		return activiteReelCalculerAjout;
	}

	public void setActiviteReelCalculerAjout(ActiviteReelCalculer activiteReelCalculerAjout) {
		this.activiteReelCalculerAjout = activiteReelCalculerAjout;
	}

	public ActivitePresence getActivitePresenceAjout() {
		return activitePresenceAjout;
	}

	public void setActivitePresenceAjout(ActivitePresence activitePresenceAjout) {
		this.activitePresenceAjout = activitePresenceAjout;
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

	public Date getJour() {
		return jour;
	}

	public void setJour(Date jour) {
		this.jour = jour;
	}
	

	
	
}
