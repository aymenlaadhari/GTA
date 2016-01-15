package managedBeans.gta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.CalculGta;
import com.yesserp.domain.gta.PlanningTheorique;
import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionaffectationcycle.GestionAffectationCycleLocal;
import com.yesserp.sessionbean.gta.gestiondatejournees.GestionDateJourneesLocal;
import com.yesserp.sessionbean.gta.gestionexceptionjournee.GestionExceptionJourneeLocal;
import com.yesserp.sessionbean.gta.gestionplanningtheorique.GestionPlanningTheoriqueLocal;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;

@ManagedBean
@ViewScoped
public class TestBean {

	@ManagedProperty("#{planningThBean.calculGtas}")
	private List<CalculGta> calculGtas;

	private JourneeType journeeType = new JourneeType();
	private SemaineType semaineType = new SemaineType();
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private CodeJour codeJour = new CodeJour();
	private JourneeType jouAchanger = new JourneeType();
	private Date date1 = new Date();
	private Date date2;
	private DateTime date3;
	private DateTime date4;
	private List<PlanningTheorique> planningTheoriques = new ArrayList<>();
	private Identite identite = new Identite();
	private CycleTravail cycleTravail = new CycleTravail();
	private int nbElements;
	private List<SemaineType> semaineTypes = new ArrayList<>();
	private List<Date> dates = new ArrayList<>();
	private int nbSemaines;
	private int numElements;
	private int nbParcour;
	private int index;
	private int date;

	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;
	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;
	@EJB
	GestionDateJourneesLocal gestionDateJourneesLocal;
	@EJB
	GestionPlanningTheoriqueLocal gestionPlanningTheoriqueLocal;
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
	@EJB
	GestionCycleTravailLocal gestionCycleTravailLocal;
	@EJB
	GestionExceptionJourneeLocal gestionExceptionJourneeLocal;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionAffectationCycleLocal affectationCycleLocal;

	public void tester() {
		dates = affectationCycleLocal.trouverDatesChangementDeCycle(identite);
		for(Date d:dates){
		System.out.println(d);
		}
		
		
//		cycleTravail = affectationCycleLocal
//				.findAffectationCycleForEmployeeEtCycle(date2, identite);
//		System.out.println(cycleTravail.getCodect());

		// Exceptionjournee exceptionjournee = new Exceptionjournee();
		// // exceptionjournee.setDateChangement(date2);
		// // exceptionjournee.setJourneeTypeApres(journeeType);
		// // exceptionjournee.setIdentite(identite);
		// //
		// gestionExceptionJourneeLocal.ajouterExceptionjournee(exceptionjournee);
		// exceptionjournee =
		// gestionExceptionJourneeLocal.findJourneFromException(identite);
		// for(CalculGta c:calculGtas){
		// if(c.getDateJour().equals(date2)){
		// c.setJourneeType(exceptionjournee.getJourneeTypeApres());
		// }
		// }

	}

	@PostConstruct
	public void init() {
		// semaineType = gestionSemaineTypeLocal.findSemaineByID(1);
		// codeJour = gestionCodeJourLocal.findCodeJourById(6);
		// planningTheorique=gestionPlanningTheoriqueLocal.findPlanningThById(20);
		identite = gestionIdentiteLocal.trouverParNumeroDossier((long) 1);
		journeeType = gestionJourneeTypeLocal.findJourneeTypeById(2);
		// jouAchanger = gestionJourneeTypeLocal.find
		// cycleTravail = gestionCycleTravailLocal.findCycleTravailById(2);
		// setCycleTravailGTA(gestionCycleGTALocal.findCycleTravailGTAById(1));
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public SemaineType getSemaineType() {
		return semaineType;
	}

	public void setSemaineType(SemaineType semaineType) {
		this.semaineType = semaineType;
	}

	public CodeJour getCodeJour() {
		return codeJour;
	}

	public void setCodeJour(CodeJour codeJour) {
		this.codeJour = codeJour;
	}

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Identite getIdentite() {
		return identite;
	}

	public void setIdentite(Identite identite) {
		this.identite = identite;
	}

	public List<PlanningTheorique> getPlanningTheoriques() {
		return planningTheoriques;
	}

	public void setPlanningTheoriques(List<PlanningTheorique> planningTheoriques) {
		this.planningTheoriques = planningTheoriques;
	}

	public CycleTravail getCycleTravail() {
		return cycleTravail;
	}

	public void setCycleTravail(CycleTravail cycleTravail) {
		this.cycleTravail = cycleTravail;
	}

	public int getNbElements() {
		return nbElements;
	}

	public void setNbElements(int nbElements) {
		this.nbElements = nbElements;
	}

	public int getNbSemaines() {
		return nbSemaines;
	}

	public void setNbSemaines(int nbSemaines) {
		this.nbSemaines = nbSemaines;
	}

	public DateTime getDate3() {
		return date3;
	}

	public void setDate3(DateTime date3) {
		this.date3 = date3;
	}

	public DateTime getDate4() {
		return date4;
	}

	public void setDate4(DateTime date4) {
		this.date4 = date4;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public JourneeType getJouAchanger() {
		return jouAchanger;
	}

	public void setJouAchanger(JourneeType jouAchanger) {
		this.jouAchanger = jouAchanger;
	}

	public List<CalculGta> getCalculGtas() {
		return calculGtas;
	}

	public void setCalculGtas(List<CalculGta> calculGtas) {
		this.calculGtas = calculGtas;
	}

	public List<SemaineType> getSemaineTypes() {
		return semaineTypes;
	}

	public void setSemaineTypes(List<SemaineType> semaineTypes) {
		this.semaineTypes = semaineTypes;
	}

	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

}
