package managedBeans.gta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.ClasseEmp;
import com.yesserp.domain.gta.CodeCalendrier;
import com.yesserp.domain.gta.ContratGta;
import com.yesserp.domain.gta.DateCalculGta;
import com.yesserp.domain.gta.PlanningTheorique;
import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionOccurence.GestionOccurenceLocal;
import com.yesserp.sessionbean.gta.gestioncodecalendrier.GestionCodeCalendrierLocal;
import com.yesserp.sessionbean.gta.gestiondatejournees.GestionDateJourneesLocal;
import com.yesserp.sessionbean.gta.gestionplanningtheorique.GestionPlanningTheoriqueLocal;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;

@ManagedBean
@ViewScoped
public class OccurenceBean {
	private ContratGta contratGta = new ContratGta();
	private String pointage;
	private String gta;
	private Identite identite = new Identite();
	private List<CycleTravail> cycleTravails = new ArrayList<>();
	private ClasseEmp classeEmp =new ClasseEmp();
	private CycleTravail cycleTravail = new CycleTravail();
	private CodeCalendrier codeCalendrier = new CodeCalendrier();
	private List<Identite> identites = new ArrayList<>();
	private List<CodeCalendrier> codeCalendriers = new ArrayList<>();
	private PlanningTheorique planningTheorique = new PlanningTheorique();
	private List<SemaineType> semaineTypes = new ArrayList<>();
	private DateCalculGta dateJournees = new DateCalculGta();
	private JourneeType journeeType = new JourneeType();
	private Date dateplanning = new Date();
	private CodeJour codeJour = new CodeJour();
	private int nbjours = 7;
	private int nbParcours;
	private int codejournee;

	
	
	@EJB
	GestionOccurenceLocal gestionOccurenceLocal;
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

	public void rowselect() {

	}

	public void ajoutOccurence() {
		if (gta.equals("oui")) {
			contratGta.setPointage(true);
		} else {
			contratGta.setPointage(false);
		}
		if (pointage.equals("oui")) {
			contratGta.setGta(true);
		} else {
			contratGta.setGta(false);
		}
		
		
		
		gestionOccurenceLocal.ajouterOccurence(contratGta);
		cycleTravail = gestionCycleTravailLocal
				.findCycleTravailById(cycleTravail.getIdct());
		gestionCycleTravailLocal.affectCycleToEmployee(identite, cycleTravail,
				contratGta.getDateeffe());
		identite.setCodeCalendrier(codeCalendrier);
		identite.setContratGta(contratGta);
		gestionIdentiteLocal.modifierIdentite(identite);

		if (contratGta.isGta()) {
			planningTheorique.setIdentite(identite);
			planningTheorique.setDatePlanning(contratGta.getDateeffe());
			gestionPlanningTheoriqueLocal.ajouterPlanningTh(planningTheorique);
			semaineTypes = gestionSemaineTypeLocal
					.findSemaineTypeForCycle(cycleTravail);
			dateplanning = planningTheorique.getDatePlanning();

		}

		contratGta = new ContratGta();
		planningTheorique = new PlanningTheorique();
		System.out.println("Ajout effectué");
	}

	@PostConstruct
	public void init() {
		cycleTravails = gestionCycleTravailLocal.listerCycleTravail();
		identites = gestionIdentiteLocal.findAll();
		codeCalendriers = gestionCodeCalendrierLocal.findAllCodeCalendriers();

	}

	public ContratGta getOccurence() {
		return contratGta;
	}

	public void setOccurence(ContratGta contratGta) {
		this.contratGta = contratGta;
	}

	


	public String getPointage() {
		return pointage;
	}

	public void setPointage(String pointage) {
		this.pointage = pointage;
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

	public String annuler(){
		return null ;
	}

	public ClasseEmp getClasseEmp() {
		return classeEmp;
	}

	public void setClasseEmp(ClasseEmp classeEmp) {
		this.classeEmp = classeEmp;
	}
}
