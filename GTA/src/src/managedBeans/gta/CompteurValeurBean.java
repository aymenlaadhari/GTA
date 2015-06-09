package managedBeans.gta;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.ActiviteReelCalculer;
import com.yesserp.domain.gta.ContratGta;
import com.yesserp.domain.gta.ImputationReelCalculer;
import com.yesserp.domain.gtaparam.ActiviteAbsence;
import com.yesserp.domain.gtaparam.Compteur;
import com.yesserp.domain.gtaparam.Filtre;
import com.yesserp.domain.gtaparam.ParamClass;
import com.yesserp.domain.gtaparam.ValeurCompteur;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionactivitereelcalculer.GestionActiviteReelCalculerLocal;
import com.yesserp.sessionbean.gta.gestioncontratGTA.GestionContraGtaLocal;
import com.yesserp.sessionbean.gta.gestionimputationreel.gestionImputationReelCalculer;
import com.yesserp.sessionbean.gta.valeurCompteur.GestionValeurCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionFormuleCompteur.GestionFormuleCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsenceLocal;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestioncompteur.GestionCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionfiltre.GestionFiltreLocal;

@ManagedBean
@SessionScoped
public class CompteurValeurBean {

	/**
	 * 
	 */

	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
	@EJB
	GestionCompteurLocal compteurLocal;
	@EJB
	GestionValeurCompteurLocal valeurCompteurLocal;
	@EJB
	GestionFiltreLocal filtreLocal;
	@EJB
	GestionActiviteAbsenceLocal activiteAbsenceLocal;
	@EJB
	private GestionActiviteReelCalculerLocal activiteReelCalculerLocal;
	@EJB
	private GestionCodeJourLocal codeJourLocal;
	@EJB
	private GestionFormuleCompteurLocal formuleCompteurLocal;
	@EJB
	private GestionContraGtaLocal gestionContraGtaLocal;
	private List<Identite> identites = new ArrayList<>();
	private List<Object[]> valeurCompteurs = new ArrayList<>();
	private List<Compteur> compteurs = new ArrayList<>();
	private List<Filtre> filtres = new ArrayList<>();
	private Compteur cmp = new Compteur();
	private List<ActiviteAbsence> activiteAbsences = new ArrayList<>();
	private Identite identite = new Identite();
	private Long numdossier;
	private ValeurCompteur val = new ValeurCompteur();
	private List<ActiviteReelCalculer> activiteReelCalculersAbsence = new ArrayList<>();
	private Date dateDebutFiltre = new Date();
	private Date dateFinFiltre = new Date();
	private List<ActiviteReelCalculer> activiteReelCalculersPresence = new ArrayList<>();
	private int valNumerique = 0;
	private int hoursOfNextDay = 0;
	Identite id;
	ContratGta contratGta;
	@EJB
	private GestionValeurCompteurLocal gestionValeurCompteur;
	@EJB
	private gestionImputationReelCalculer gestionImputationReelCalculer;

	private ValeurCompteur valeurCompteur = new ValeurCompteur();

	private ScheduleModel eventModel = new DefaultScheduleModel();

	private ScheduleEvent event = new DefaultScheduleEvent();

	@PostConstruct
	public void init() {

		// identites = gestionIdentiteLocal.findAll();
		valeurCompteurs = null;
		identites = null;
		compteurs = compteurLocal.findAll();
	}

	@SuppressWarnings("deprecation")
	public void test() throws ParseException {

		id = gestionIdentiteLocal.trouverParNumeroDossier((long) 1);

		contratGta = gestionContraGtaLocal.getContratGtaByUser(id);

		// ***********************************************************************
		// Recuperation de tous les formules de ce compteur
		// et boucle
		// selon date d effet
		// ***********************************************************************

		// *****************************************************
		// Recuperation des filtres de ce compteur qui correspand
		// ******************************************************

		filtres = filtreLocal.findFilterByCompteur(cmp);

		// ******************************************************
		// Si le l'action a la fin du compteur est initialisation
		// *******************************************************

		valeurCompteurLocal.supprimerTousLesLignes(cmp);

		// ******************************************************
		// boucle pour une journee
		// *******************************************************
		int resultat = 0;
		valNumerique = 0;
		if (cmp.getTypeop().equalsIgnoreCase("journaliere")) {

			LocalDate dateDebut = new LocalDate(2014, 7, 01);
			LocalDate dateFin = new LocalDate(2014, 9, 2);

			int days = Days.daysBetween(dateDebut, dateFin).getDays();
			if (cmp.getOperation().equalsIgnoreCase("Initialisation")) {
				for (int i = 0; i <= days; i++) {

					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);

					resultat = resultat + hoursOfNextDay;
					hoursOfNextDay = 0;
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								resultat, cmp);
					} else

					{
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						System.out.println("vallllllllllllll" + resultat);
						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								valNumerique, cmp);
					}
				}
			} else if (cmp.getOperation().equalsIgnoreCase("Cumul")) {
				for (int i = 0; i < days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);

					resultat = resultat + hoursOfNextDay;
					hoursOfNextDay = 0;
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								resultat, cmp);
					} else

					{
						valNumerique = valNumerique
								+ calculdeValeurParJournee(filtres, dateDejour,
										valNumerique);
						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								valNumerique, cmp);
					}
				}

			} else if (cmp.getOperation().equalsIgnoreCase("ecretage")) {

				resultat = cmp.getValinit();
				valNumerique = cmp.getValinit();
				for (int i = 0; i < days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						resultat = resultat + hoursOfNextDay;
						hoursOfNextDay = 0;
						if (resultat > cmp.getValmax())
							resultat = cmp.getValmax();
						if (resultat < cmp.getValmin())
							resultat = cmp.getValmin();

						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								resultat, cmp);
					} else

					{
						if (valNumerique > cmp.getValmax())
							valNumerique = cmp.getValmax();
						if (valNumerique < cmp.getValmin())
							valNumerique = cmp.getValmin();

						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								valNumerique, cmp);
					}

				}

			}
			if (cmp.getOperation().equalsIgnoreCase("ecretageinit")) {

				for (int i = 0; i < days; i++) {
					resultat = cmp.getValinit();
					valNumerique = cmp.getValinit();
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						resultat = resultat + hoursOfNextDay;
						hoursOfNextDay = 0;
						if (resultat > cmp.getValmax())
							resultat = cmp.getValmax();
						if (resultat < cmp.getValmin())
							resultat = cmp.getValmin();

						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								resultat, cmp);
					} else

					{
						if (valNumerique > cmp.getValmax())
							valNumerique = cmp.getValmax();
						if (valNumerique < cmp.getValmin())
							valNumerique = cmp.getValmin();

						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						ajouterLaValeurDeCompteur(dateDejour.toDate(),
								valNumerique, cmp);
					}

				}

			}
			// ******************************************************
			// boucle pour une semaine
			// *******************************************************

		} else if (cmp.getTypeop().equalsIgnoreCase("hebdomadaire")) {

			LocalDate dateDebut = new LocalDate(2014, 7, 01);
			LocalDate dateFin = new LocalDate(2014, 8, 30);

			int days = Days.daysBetween(dateDebut, dateFin).getDays();
			resultat = 0;
			valNumerique = 0;
			if (cmp.getOperation().equalsIgnoreCase("Initialisation")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateDejour.dayOfWeek().get() == 7) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfWeek().get() == 7) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfWeek().withMaximumValue())))
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), valNumerique, cmp);

			}
			if (cmp.getOperation().equalsIgnoreCase("Cumul")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateDejour.dayOfWeek().get() == 7) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = valNumerique
								+ calculdeValeurParJournee(filtres, dateDejour,
										valNumerique);
						if (dateDejour.dayOfWeek().get() == 7) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfWeek().withMaximumValue())))
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), valNumerique, cmp);

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretage")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateDejour.dayOfWeek().get() == 7) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfWeek().get() == 7) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfWeek().withMaximumValue()))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;

					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), valNumerique, cmp);

				}

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretageinit")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateDejour.dayOfWeek().get() == 7) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = init;
							valNumerique = init;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfWeek().get() == 7) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = init;
							valNumerique = init;
						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfWeek().withMaximumValue()))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfWeek()
								.withMaximumValue().toDate(), valNumerique, cmp);

				}
			}

		} else if (cmp.getTypeop().equalsIgnoreCase("mensuelle")) {
			LocalDate dateDebut = new LocalDate(2014, 7, 01);
			LocalDate dateFin = new LocalDate(2014, 8, 30);

			int days = Days.daysBetween(dateDebut, dateFin).getDays();
			resultat = 0;
			valNumerique = 0;
			if (cmp.getOperation().equalsIgnoreCase("Initialisation")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfMonth().withMaximumValue())))
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), valNumerique, cmp);

			}
			if (cmp.getOperation().equalsIgnoreCase("Cumul")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = valNumerique
								+ calculdeValeurParJournee(filtres, dateDejour,
										valNumerique);
						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfMonth().withMaximumValue())))
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), valNumerique, cmp);

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretage")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfMonth().withMaximumValue()))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;

					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), valNumerique, cmp);

				}

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretageinit")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = init;
							valNumerique = init;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfMonth().withMaximumValue()
								.equals(dateDejour)) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = init;
							valNumerique = init;
						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfMonth().withMaximumValue()))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfMonth()
								.withMaximumValue().toDate(), valNumerique, cmp);

				}
			}
			
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////Annuelle////////////////////////////		
		} else if (cmp.getTypeop().equalsIgnoreCase("Annuelle")) {
			LocalDate dateDebut = new LocalDate(2014, 7, 01);
			LocalDate dateFin = new LocalDate(2015, 8, 30);

			int days = Days.daysBetween(dateDebut, dateFin).getDays();
			resultat = 0;
			valNumerique = 0;
			if (cmp.getOperation().equalsIgnoreCase("Initialisation")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfYear().withMaximumValue())))
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), valNumerique, cmp);

			}
			if (cmp.getOperation().equalsIgnoreCase("Cumul")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = valNumerique
								+ calculdeValeurParJournee(filtres, dateDejour,
										valNumerique);
						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfYear().withMaximumValue())))
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), valNumerique, cmp);

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretage")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfYear().withMaximumValue()))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;

					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), valNumerique, cmp);

				}

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretageinit")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = init;
							valNumerique = init;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateDejour.dayOfYear().withMaximumValue()
								.equals(dateDejour)) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = init;
							valNumerique = init;
						}
					}

				}
				if (!(dateFin.equals(dateFin.dayOfYear().withMaximumValue()))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.dayOfYear()
								.withMaximumValue().toDate(), valNumerique, cmp);

				}
			}
		}
		
		
		////////////////////////////////////////////////////////////////////////Periode//////////////////////////////////////////////////////////////////
		else if (cmp.getTypeop().equalsIgnoreCase("Periode")) {
		
			cmp.getDateDebutPeroide();
			LocalDate dateDebut = new LocalDate(cmp.getDateDebutPeroide());
			LocalDate dateFin = new LocalDate(cmp.getDateDebutPeroide());

			int days = Days.daysBetween(dateDebut, dateFin).getDays();
			resultat = 0;
			valNumerique = 0;
			if (cmp.getOperation().equalsIgnoreCase("Initialisation")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateFin
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateFin
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = 0;
							valNumerique = 0;
						}
					}

				}
				
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.toDate(), valNumerique, cmp);

			}
			if (cmp.getOperation().equalsIgnoreCase("Cumul")) {

				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);
						if (dateFin
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = valNumerique
								+ calculdeValeurParJournee(filtres, dateDejour,
										valNumerique);
						if (dateFin
								.equals(dateDejour)) {
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.toDate(), valNumerique, cmp);

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretage")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateFin
								.equals(dateDejour)) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);

						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateFin
								.equals(dateDejour)) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);

						}
					}

				}
				if (!(dateFin.equals(dateFin))) {

					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;

					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.toDate(), valNumerique, cmp);

				}

			}

			if (cmp.getOperation().equalsIgnoreCase("ecretageinit")) {
				int init = cmp.getValinit();
				resultat = init;
				valNumerique = init;
				int valMax = cmp.getValmax();
				int valMin = cmp.getValmin();
				for (int i = 0; i <= days; i++) {
					LocalDate dateDejour = dateDebut.withFieldAdded(
							DurationFieldType.days(), i);
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						resultat = calculdeValeurParJournee(filtres,
								dateDejour, resultat);

						if (dateFin
								.equals(dateDejour)) {
							if (resultat > valMax)
								resultat = valMax;

							if (resultat < valMin)
								resultat = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									resultat, cmp);
							resultat = init;
							valNumerique = init;
						}
					} else {
						valNumerique = calculdeValeurParJournee(filtres,
								dateDejour, valNumerique);
						if (dateFin.equals(dateDejour)) {

							if (valNumerique > valMax)
								valNumerique = valMax;

							if (valNumerique < valMin)
								valNumerique = valMin;
							ajouterLaValeurDeCompteur(dateDejour.toDate(),
									valNumerique, cmp);
							resultat = init;
							valNumerique = init;
						}
					}

				}
			
					if (resultat > valMax)
						resultat = valMax;

					if (resultat < valMin)
						resultat = valMin;
					if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
						ajouterLaValeurDeCompteur(dateFin.toDate(), resultat, cmp);
					} else
						ajouterLaValeurDeCompteur(dateFin.toDate(), valNumerique, cmp);

				
			}
		}

		// Time time = new Time(resultat / 60, +resultat % 60, 00);
		// System.out.println(time);
		// LocalDate ld = new LocalDate(daux);
		// ajouterLaValeurDeCompteur(ld
		// .withDayOfWeek(DateTimeConstants.SUNDAY).toDate(), time,
		// cmp);
		// resultat = 0;
		// valNumerique = 0;

		//
		// else if (cmp.getTypeop().equalsIgnoreCase("mensuelle")) {
		//
		// List<Date> tests=activiteReelCalculerLocal.findAllByDate(null);
		// firstDay=tests.get(0);
		// System.out.println(firstDay);
		// DateTime dtm = new DateTime(firstDay);
		// LocalDate dt = new
		// LocalDate(dtm.getYear(),dtm.getMonthOfYear(),dtm.getDayOfMonth());
		// LocalDate lastDayOfMonth = dt.dayOfMonth().withMaximumValue();
		// System.out.println(lastDayOfMonth);
		//
		//
		// for (Date d : activiteReelCalculerLocal.findAllByDate(null)) {
		// DateTime dtmTest = new DateTime(d);
		// LocalDate dtTest = new
		// LocalDate(dtmTest.plusDays(1).getYear(),dtmTest.plusDays(1).getMonthOfYear(),dtmTest.plusDays(1).getDayOfMonth());
		// LocalDate lastDayOfMonthTest =
		// dtTest.dayOfMonth().withMaximumValue();
		// System.out.println(dt);
		// System.out.println(lastDayOfMonth);
		//
		//
		// if (filtres.size() > 0) {
		// resultat = appliquerfiltreSurLesJours(filtres, resultat, d);
		//
		// System.out.println(resultat);
		// } else {
		// resultat = appliquerFormuleCompteur(resultat, d);
		// }
		//
		// if (!(lastDayOfMonthTest.equals(lastDayOfMonth))){
		// Time time = new Time(resultat / 60, +resultat % 60, 00);
		// ajouterLaValeurDeCompteur(lastDayOfMonth.toDate(), time, cmp);
		// a=resultat;
		// resultat = 0;
		// valNumerique = 0;
		//

		// }
		// daux = d;
		// }
		//
		//
		// Time time = new Time(a / 60, +a % 60, 00);
		// ajouterLaValeurDeCompteur(lastDayOfMonth.toDate(), time, cmp);
		// a=0;
		// }
		//

		// ***********************************************
		// affichage du calendrier
		// ***********************************************

		affichageDuCalndrier(cmp);

	}

	private int calculdeValeurParJournee(List<Filtre> filtres2,
			LocalDate dateDejour, int resultat) {
		if (filtres.size() > 0) {
			resultat = appliquerfiltreSurLesJours(filtres, resultat,
					dateDejour.toDate());
			resultat = appliquerFormuleCompteur(resultat, dateDejour.toDate());

		} else {
			resultat = appliquerFormuleCompteur(resultat, dateDejour.toDate());

		}
		return resultat;
	}

	private int appliquerfiltreSurLesJours(List<Filtre> filtres2, int res,
			Date d) {
		for (Filtre f : filtres2) {
			// **********************************************************
			// filtre sur les jours
			// ***********************************************************
			if (codeJourLocal.getCodeJourFromFilter(f).contains(
					new Integer(new DateTime(d).getDayOfWeek()))) {

				res = res + CalculerLesActiviteEnAbsence(d, f);
				res = res + CalculerLesActiviteEnPresence(d, f);
				res = res + calculerImputation(d, f);
			}

		}

		return res;
	}

	private int appliquerFormuleCompteur(int resultat, Date d) {
		// test sur la date d'effet de cette formule

		// test sur la condition de formule

		// appliquer la formule

		ParamClass p = null;

		if (!(formuleCompteurLocal.getParmByCompteur(cmp, d) == null)) {
			p = formuleCompteurLocal.getParmByCompteur(cmp, d);
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String infix = p.getCondition();
			Matcher matcher = Pattern.compile("F\\s*(\\w+)").matcher(infix);
			while (matcher.find()) {
				engine.getContext().setAttribute("F" + matcher.group(1),
						resultat, ScriptContext.ENGINE_SCOPE);
			}
			matcher = Pattern.compile("CP\\s*(\\w+)").matcher(infix);
			while (matcher.find()) {

				engine.getContext().setAttribute("CP" + matcher.group(1),
						getValueOfCompteur("CP" + matcher.group(1), d),
						ScriptContext.ENGINE_SCOPE);
			}

			try {
				System.out
						.println(new BigDecimal(engine.eval(infix).toString())
								.intValue());
				return new BigDecimal(engine.eval(infix).toString()).intValue();
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			return resultat;
		}
		else
			return 0;
		//
		//
		

		// cas d'un compteur par annee
		// TODO Auto-generated method stub

	}

	// private boolean appliquerConditionSurLeCompteur(Compteur cmp2,
	// int resultat, Date d) {
	// FormuleCompteur formuleCompteur = new FormuleCompteur();
	// formuleCompteur = formuleCompteurLocal.getFormuleByCompteur(cmp2);
	//
	// if (!formuleCompteur.getFormule().isEmpty()) {
	// ScriptEngineManager mgr = new ScriptEngineManager();
	// ScriptEngine engine = mgr.getEngineByName("JavaScript");
	// String infix = formuleCompteur.getCondition();
	// Matcher matcher = Pattern.compile("CP\\s*(\\w+)").matcher(infix);
	// while (matcher.find()) {
	// engine.getContext().setAttribute("CP" + matcher.group(1),
	// getValueOfCompteur("CP" + matcher.group(1), d),
	// ScriptContext.ENGINE_SCOPE);
	// }
	// // TODO Auto-generated method stub
	// try {
	// return (Boolean) engine.eval(infix);
	// } catch (ScriptException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return false;
	// } else
	// return true;
	// }

	private Object getValueOfCompteur(String code, Date d) {
		Compteur cmp = compteurLocal.findCompteurByCode(code);

		if (valeurCompteurLocal.getValeurCompteurParCompteurEtParDate(cmp, d) == null) {
			return 0;
		}

		else {
			ValeurCompteur vc = valeurCompteurLocal
					.getValeurCompteurParCompteurEtParDate(cmp, d);
			if (cmp.getTypeDeCalcul().equalsIgnoreCase("Duree")) {
				long a = vc.getValeurHoraire();
				return a;
			} else
				return vc.getValeurNumerique();
		}
	}

	@SuppressWarnings("deprecation")
	private void affichageDuCalndrier(Compteur cmp2) {
		// TODO Auto-generated method stub
		eventModel.clear();
		valeurCompteurs = new ArrayList<>();
		valeurCompteurs = valeurCompteurLocal
				.rechercheValeurCompteurParCompteur(cmp);

		for (Object[] result : valeurCompteurs) {
			if (cmp.getTypeDeCalcul().equalsIgnoreCase("Duree")) {
				long val = Long.parseLong(result[1].toString());
				String heure = String.valueOf(val / 3600);
				String minutes = String.valueOf((val % 3600) / 60);
				String secondes = String.valueOf(val % 3600 % 60);

				eventModel
						.addEvent(new DefaultScheduleEvent(heure + "h:"
								+ minutes + "m:" + secondes + "s",
								new DateTime(result[0]).plusDays(1).toDate(),
								new DateTime(result[0]).plusDays(1).toDate(),
								"myclass"));

			} else {
				eventModel
						.addEvent(new DefaultScheduleEvent(
								result[2].toString(), new DateTime(result[0])
										.plusDays(1).toDate(), new DateTime(
										result[0]).plusDays(1).toDate(),
								"myclass"));

			}

		}

	}

	private void ajouterLaValeurDeCompteur(Date d, int val, Compteur cmp2) {
		// TODO Auto-generated method stub
		ValeurCompteur valeurCompteur = new ValeurCompteur();
		valeurCompteur.setDateCalc(d);

		if (cmp.getTypeDeCalcul().equalsIgnoreCase("duree")) {
			valeurCompteur.setValeurHoraire(val);
		} else
			valeurCompteur.setValeurNumerique(val);
		valeurCompteur.setCompteur(cmp);

		valeurCompteurLocal.ajoutervaleurCompteur(valeurCompteur);
	}

	public void lisnerCompteur(AjaxBehaviorEvent ev) {

	}

	public void rowselect() {
		// identite = gestionIdentiteLocal.trouverParNumeroDossier(numdossier);

		// compteurs = compteurLocal.findCompteurByIdentite(identite);

	}

	public Identite getIdentite() {
		return identite;
	}

	public void setIdentite(Identite identite) {
		this.identite = identite;
	}

	public List<Identite> getIdentites() {
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

	public List<Compteur> getCompteurs() {
		return compteurs;
	}

	public void setCompteurs(List<Compteur> compteurs) {
		this.compteurs = compteurs;
	}

	public List<Object[]> getValeurCompteurs() {
		return valeurCompteurs;
	}

	public void setValeurCompteurs(List<Object[]> valeurCompteurs) {
		this.valeurCompteurs = valeurCompteurs;
	}

	public Compteur getCmp() {
		return cmp;
	}

	public void setCmp(Compteur cmp) {
		this.cmp = cmp;
	}

	public ValeurCompteur getVal() {
		return val;
	}

	public void setVal(ValeurCompteur val) {
		this.val = val;
	}

	public Long getNumdossier() {
		return numdossier;
	}

	public void setNumdossier(Long numdossier) {
		this.numdossier = numdossier;
	}

	public List<Filtre> getFiltres() {
		return filtres;
	}

	public void setFiltres(List<Filtre> filtres) {
		this.filtres = filtres;
	}

	public List<ActiviteAbsence> getActiviteAbsences() {
		return activiteAbsences;
	}

	public void setActiviteAbsences(List<ActiviteAbsence> activiteAbsences) {
		this.activiteAbsences = activiteAbsences;
	}

	public List<ActiviteReelCalculer> getActiviteReelCalculersAbsence() {
		return activiteReelCalculersAbsence;
	}

	public void setActiviteReelCalculersAbsence(
			List<ActiviteReelCalculer> activiteReelCalculersAbsence) {
		this.activiteReelCalculersAbsence = activiteReelCalculersAbsence;
	}

	public List<ActiviteReelCalculer> getActiviteReelCalculersPresence() {
		return activiteReelCalculersPresence;
	}

	public void setActiviteReelCalculersPresence(
			List<ActiviteReelCalculer> activiteReelCalculersPresence) {
		this.activiteReelCalculersPresence = activiteReelCalculersPresence;
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

	public long comparer(Date d1, Date d2, Date df1, Date df2) {
		DateTime dateRest1 = null;
		DateTime dateRest2 = null;

		if ((df1 == null) && (df2 == null)) {

			DateTime startTime, endTime;
			startTime = new DateTime(d1);
			endTime = new DateTime(d2);
			hoursOfNextDay = 0;
			if (d1.after(d2)) {

				DateTime endOfToday = startTime.toDateMidnight().toInterval()
						.getEnd();
				DateTime startOfToday = endTime.toDateMidnight().toInterval()
						.getStart();
				Interval interval = new Interval(startTime, endOfToday);
				Interval interval2 = new Interval(startOfToday, endTime);
				if (cmp.getMethodeDeCalcul().equalsIgnoreCase("jour")) {
					int a = (int) interval.toDuration().getStandardSeconds();

					a = a + (int) interval2.toDuration().getStandardSeconds();

					System.out.println(a + "seeeeeeeeeeeeeeeeeeeeeec");

					// hoursOfNextDay=a*3600;
					return a;

				} else if (cmp.getMethodeDeCalcul().equalsIgnoreCase("veille")) {

					hoursOfNextDay = (int) interval.toDuration()
							.getStandardSeconds();
					hoursOfNextDay = hoursOfNextDay
							+ (int) interval2.toDuration().getStandardSeconds();
					return 0;
				} else if (cmp.getMethodeDeCalcul().equalsIgnoreCase(
						"defalquer")) {

					hoursOfNextDay = (int) interval2.toDuration()
							.getStandardSeconds();
					return (int) interval.toDuration().getStandardSeconds();
				}

			} else
				return (Seconds.secondsBetween(startTime, endTime).getSeconds());
		} else {
			hoursOfNextDay = 0;
			// si la date debut de filter inferieur a date debut heure et date
			// fin
			// filter superieur a date fin
			if ((df1.compareTo(d1) >= 0) && (df2.compareTo(d2) <= 0)) {
				dateRest1 = new DateTime(df1);
				dateRest2 = new DateTime(df2);
				return (Seconds.secondsBetween(dateRest1, dateRest2)
						.getSeconds());

			} else if ((df1.compareTo(d1) <= 0) && (df2.compareTo(d2) <= 0))
			// si la date debut de filter inferieur a date debut heure et date
			// fin
			// filter inferieur a date fin
			{
				dateRest1 = new DateTime(d1);
				dateRest2 = new DateTime(df2);
				return (Seconds.secondsBetween(dateRest1, dateRest2)
						.getSeconds());

			} else if ((df1.compareTo(d1) >= 0) && (df2.compareTo(d2) >= 0)) {
				dateRest1 = new DateTime(df1);
				dateRest2 = new DateTime(d2);
				return (Seconds.secondsBetween(dateRest1, dateRest2)
						.getSeconds());
			} else if ((df1.compareTo(d1) <= 0) && (df2.compareTo(d2) >= 0)) {
				dateRest1 = new DateTime(d1);
				dateRest2 = new DateTime(d2);
				return (Seconds.secondsBetween(dateRest1, dateRest2)
						.getSeconds());
			}
			return 0;
		}
		return 0;
	}

	// **************************************
	// Caluler les activite en absence
	// **************************************

	public int calculerImputation(Date d, Filtre filtre) {

		int calcul = 0;
		int formule = 0;
		for (ImputationReelCalculer a : gestionImputationReelCalculer
				.findImputationReel(filtre, d)) {

			formule = (int) this.comparer(a.getHeuredeb(), a.getHeurefin(),
					filtre.getHeuredeb(), filtre.getHeurefin());

			// tester si la duree est negative
			if (formule < 0)
				formule = 0;
			else {
				valNumerique++;
				calcul = calcul + formule;
			}

		}

		return calcul;

	}

	public int CalculerLesActiviteEnAbsence(Date d, Filtre filtre) {
		int calcul = 0;
		int formule = 0;

		for (ActiviteReelCalculer a : activiteReelCalculerLocal
				.findActiviteReelAbsence(filtre, d)) {
			formule = (int) this.comparer(a.getHeuredeb(), a.getHeurefin(),
					filtre.getHeuredeb(), filtre.getHeurefin());

			// tester si la duree est negative
			if (formule < 0)
				formule = 0;
			else {
				valNumerique++;
				calcul = calcul + formule;
			}
		}
		return calcul;

	}

	// **************************************
	// Calculer les activites en Presence
	// **************************************

	public int CalculerLesActiviteEnPresence(Date d, Filtre filtre) {
		int calcul = 0;
		int formule = 0;

		for (ActiviteReelCalculer a : activiteReelCalculerLocal
				.findActiviteReelPresence(filtre, d)) {
			formule = (int) this.comparer(a.getHeuredeb(), a.getHeurefin(),
					filtre.getHeuredeb(), filtre.getHeurefin());

			// tester si la duree est negative
			if (formule < 0)
				formule = 0;
			else {
				calcul = calcul + formule;
				valNumerique++;
			}
		}

		return calcul;

	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ValeurCompteur getValeurCompteur() {
		return valeurCompteur;
	}

	public void setValeurCompteur(ValeurCompteur valeurCompteur) {
		this.valeurCompteur = valeurCompteur;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public Date getInitialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY,
				calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar.getTime();
	}

	public void addEvent(ActionEvent actionEvent) {

		gestionValeurCompteur.ajoutervaleurCompteur(valeurCompteur);

		valeurCompteur = new ValeurCompteur();
		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();

	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(),
				(Date) selectEvent.getObject());
	}

	// Redimentionement evenement
	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Event resized", "difference jour:" + event.getDayDelta()
						+ ", difference minute:" + event.getMinuteDelta());

		addMessage(message);
	}

	// Affichage message
	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
