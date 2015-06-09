package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FlowEvent;

import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.PlageHAssociateJourneeTPK;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.domain.gtaparam.PlageHoraireAssociateJourneeType;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@SessionScoped
public class JourneeTypeBean {
	private JourneeType journeeType = new JourneeType();
	private Libelle libelle = new Libelle();
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private JourneeType selectedJt = new JourneeType();
	private List<PlageHoraire> plageHoraires = new ArrayList<>();
	private PlageHoraire plageHoraire = new PlageHoraire();
	private List<Integer> heures = new ArrayList<>();
	private List<Integer> heuresFin = new ArrayList<>();
	private boolean headerButtonsDisabled = true;
	
	 private boolean skip;

	
	private PlageHoraireAssociateJourneeType phajtp=new PlageHoraireAssociateJourneeType();
	private ArrayList<PlageHoraireAssociateJourneeType> phajtpList=new ArrayList<>();
	
	private PlageHoraireAssociateJourneeType selectedPhajtp=new PlageHoraireAssociateJourneeType();
	private ArrayList<PlageHoraireAssociateJourneeType> selectedPhajtpList=new ArrayList<>();

	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;

	@PostConstruct
	public void init() {
		
		journeeTypes = gestionJourneeTypeLocal.listerJourneeType();
		for (int i = 0; i < journeeTypes.size(); i++) {
			try {
				journeeTypes.get(i).setLibelles(
						gestionLibelleLocal
								.findLibelleByjourneeType(journeeTypes.get(i)
										.getIdjt()));

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public boolean isHeaderButtonsDisabled() {
		
		return headerButtonsDisabled;
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
	
	public void ajouterJourneeType() {
		gestionJourneeTypeLocal.ajoutJourneeType(journeeType);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setJourneeType(journeeType);
		gestionLibelleLocal.modifierLibelle(libelle);
		libelle = new Libelle();
gestionJourneeTypeLocal.findJourneeTypeByCode(journeeType.getCodejt());
		definirJourneeType();
		journeeType = new JourneeType();

		init();
	}

	public void supprimerJourneeType() {
		Libelle lib = selectedJt.getLibelles().get(0);
		try {
			
			gestionJourneeTypeLocal.supprimerJourneeType(selectedJt);	

		} catch (Exception e) {
			e.printStackTrace();
			gestionLibelleLocal
					.modifierLibelle(lib);
		}

	}

	public void modiferJourneeType() {
		gestionJourneeTypeLocal.modifierJourneeType(selectedJt);
	}

	public void definirJourneeType() {
		
		for(int i=0;i<phajtpList.size();i++)
		{
			plageHoraires.add(phajtpList.get(i).getPlageHoraire());
			heures.add(phajtpList.get(i).gethAssociateJourneeTPK().getHeureDeb());
			heuresFin.add(phajtpList.get(i).getHeureFin());
		}
		try{
		gestionJourneeTypeLocal.associatePlageHoraireToJourneeType(journeeType.getIdjt(), plageHoraires, heures, heuresFin);
		}catch(Exception e){
			e.printStackTrace();
		}
		plageHoraires=new ArrayList<>();
		phajtp=new PlageHoraireAssociateJourneeType();
		phajtpList=new ArrayList<>();
		heures=new ArrayList<>();
		heuresFin=new ArrayList<>();
		
	}
	public void addDefJtp(){
		phajtp.sethAssociateJourneeTPK(new PlageHAssociateJourneeTPK());
		phajtpList.add(phajtp);
		phajtp=new PlageHoraireAssociateJourneeType();
	}
	public void removeDefJtp(){
		phajtpList.remove(phajtpList.size()-1);
		phajtp=new PlageHoraireAssociateJourneeType();
	}
	
	public void afficherDefinition(){
		selectedPhajtpList=(ArrayList<PlageHoraireAssociateJourneeType>) gestionJourneeTypeLocal.findPlageAssociateJtByJt(selectedJt);
	}
	
	public String redirect() {

		return "/gtaparam/addplagehorairetojourneetype.jsf?faces-redirect=true";
	}
	
	//ajouter par sofien
	 public String onFlowProcess(FlowEvent event) {
	        if(skip) {
	            skip = false;   //reset in case user goes back
	            return "confirm";
	        }
	        else {
	            return event.getNewStep();
	        }
	    }

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public JourneeType getSelectedJt() {
		return selectedJt;
	}

	public void setSelectedJt(JourneeType selectedJt) {
		this.selectedJt = selectedJt;
	}

	public List<PlageHoraire> getPlageHoraires() {
		return plageHoraires;
	}

	public void setPlageHoraires(List<PlageHoraire> plageHoraires) {
		this.plageHoraires = plageHoraires;
	}

	public PlageHoraire getPlageHoraire() {
		return plageHoraire;
	}

	public void setPlageHoraire(PlageHoraire plageHoraire) {
		this.plageHoraire = plageHoraire;
	}

	public List<Integer> getHeures() {
		return heures;
	}

	public void setHeures(List<Integer> heures) {
		this.heures = heures;
	}



	public PlageHoraireAssociateJourneeType getPhajtp() {
		return phajtp;
	}

	public void setPhajtp(PlageHoraireAssociateJourneeType phajtp) {
		this.phajtp = phajtp;
	}

	public ArrayList<PlageHoraireAssociateJourneeType> getPhajtpList() {
		return phajtpList;
	}

	public void setPhajtpList(ArrayList<PlageHoraireAssociateJourneeType> phajtpList) {
		this.phajtpList = phajtpList;
	}

	public List<Integer> getHeuresFin() {
		return heuresFin;
	}

	public void setHeuresFin(List<Integer> heuresFin) {
		this.heuresFin = heuresFin;
	}

	public PlageHoraireAssociateJourneeType getSelectedPhajtp() {
		return selectedPhajtp;
	}

	public void setSelectedPhajtp(PlageHoraireAssociateJourneeType selectedPhajtp) {
		this.selectedPhajtp = selectedPhajtp;
	}

	public ArrayList<PlageHoraireAssociateJourneeType> getSelectedPhajtpList() {
		return selectedPhajtpList;
	}

	public void setSelectedPhajtpList(ArrayList<PlageHoraireAssociateJourneeType> selectedPhajtpList) {
		this.selectedPhajtpList = selectedPhajtpList;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

}
