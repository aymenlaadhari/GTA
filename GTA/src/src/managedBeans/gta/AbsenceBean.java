package managedBeans.gta;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.Absence;
import com.yesserp.domain.gtaparam.ActiviteAbsence;
import com.yesserp.sessionbean.gta.gestionAbsence.GestionAbsenceLocal;


@ManagedBean
@SessionScoped
public class AbsenceBean {

	private Absence absence = new Absence();
	private Identite selectedIdentite;
	private List<ActiviteAbsence> activiteabsencelist ; 
	private  Identite identite = new Identite();
	private List<Absence> absences ;
	private List<Absence> filteredAbsences;
	
	private List<SelectItem> filterOption ;

	public boolean formDisplay = false;

	@EJB
	GestionAbsenceLocal gestionabsenceLocal;

	public void ajouter() {
		
		absence = new Absence();
		gestionabsenceLocal.ajouterAbsence(absence);
		
	}
	
	
	
	
	

	public Absence getAbsence() {
		return absence;
	}

	public void setAbsence(Absence absence) {
		this.absence = absence;
	}
	


	//getters& setters 

	public List<ActiviteAbsence> getActiviteabsencelist() {
		return activiteabsencelist;
	}


	public void setActiviteabsencelist(List<ActiviteAbsence> activiteabsencelist) {
		this.activiteabsencelist = activiteabsencelist;
	}
	
	
	
	
	
	
	public void saveAbsence() {
		gestionabsenceLocal.ajouterAbsence(absence);
		absences = gestionabsenceLocal.finAll(identite);
		formDisplay = false;
	}

	@PostConstruct
	public void init() {
		absences = gestionabsenceLocal.finAll(identite);
		
	/*	List<Ranch> ranchs = runchRemote.getAllRanch();
		selectedItemForRanch = new ArrayList<SelectItem>(ranchs.size());
		for(Ranch ranch:ranchs){
			selectedItemForRanch.add(new SelectItem(ranch.getRanch_id(),ranch.getRanch_name()));
		}
		filterOption= new ArrayList<SelectItem>(ranchs.size()+1);
		filterOption.add(new SelectItem("", "select"));
		for(Ranch ranch:ranchs){
			filterOption.add(new SelectItem(ranch.getRanch_id(),ranch.getRanch_name()));
		}*/
	}

	public void newAbsence() {
		
		absence = new Absence();
		formDisplay = true;
		
		
	}

	public void deleteAbsence() {
		gestionabsenceLocal.suprimerAbsence(absence);;
		absences = gestionabsenceLocal.finAll(identite);
	}

	public void updateAbsense() {
		gestionabsenceLocal.modifierAbsence(absence);
		absences = gestionabsenceLocal.finAll(identite);
		formDisplay = false;
	}

	public void cancelH() {
		formDisplay = false;
		absence = new Absence();
	}
	
	public String refrech(){ return null; }
	
	public void doSaveOrUpdate(){
		
		absences = gestionabsenceLocal.finAll(identite);
		formDisplay = false;
	
	}
	
	

	public boolean isFormDisplay() {
		return formDisplay;
	}

	public void setFormDisplay(boolean formDisplay) {
		this.formDisplay = formDisplay;
	}
	
	
	
	
	public void onFilter(FilterEvent event){
		absence = new Absence();
		formDisplay = false;
	}
	
	public List<Absence> getFilteredAbsences() {
		return filteredAbsences;
	}

	public void setFilteredAbsences(List<Absence> filteredAbsences) {
		this.filteredAbsences = filteredAbsences;
	}



/*	public int getSelectedRanchId() {
		return selectedRanchId;
	}

	public void setSelectedRanchId(int selectedRanchId) {
		this.selectedRanchId = selectedRanchId;
	}

	public List<SelectItem> getSelectedItemForRanch() {
		return selectedItemForRanch;
	}

	public void setSelectedItemForRanch(List<SelectItem> selectedItemForRanch) {
		this.selectedItemForRanch = selectedItemForRanch;
	}*/

	public List<SelectItem> getFilterOption() {
		return filterOption;
	}

	public void setFilterOption(List<SelectItem> filterOption) {
		this.filterOption = filterOption;
	}






	public List<Absence> getAbsences() {
		return absences;
	}






	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}






	public Identite getSelectedIdentite() {
		return selectedIdentite;
	}

	public void setSelectedIdentite(Identite selectedIdentite) {
		this.selectedIdentite = selectedIdentite;
	}


	public Identite getIdentite() {
		return identite;
	}


	public void setIdentite(Identite identite) {
		this.identite = identite;
	}
	
	
	public void onRowSelect(SelectEvent event) {
	
		absences = gestionabsenceLocal.finAll(selectedIdentite);
		formDisplay = true;

	}
	
	
	
}
