package managedBeans.gtaparam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionplagehoraire.GestionPlageHoraireLocal;

@ManagedBean
@ViewScoped
public class Bean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Field> m_lFields;
	private List<SelectOneMenu> m_onemenu;
	private List<PlageHoraire> plageHoraires = new ArrayList<>();
	private List<PlageHoraire> plageHoraires1 = new ArrayList<>();
	private PlageHoraire plageHoraire = new PlageHoraire();
	private List<Integer> heures = new ArrayList<>();
	private int heure;
	private JourneeType journeeType = new JourneeType();
	@EJB
	GestionPlageHoraireLocal gestionPlageHoraireLocal;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;

	public Bean() {
		m_lFields = new ArrayList<>();

		m_lFields.add(new Field());

		m_onemenu = new ArrayList<>();
		m_onemenu.add(new SelectOneMenu());
	}

	public void ajouter() {
		gestionJourneeTypeLocal.associatePlageHoraireToJourneeType(journeeType.getIdjt(), plageHoraires, heures);
		
		
	}
	
      public String redirect(){
		
		return "/gtaparam/gestioncodejour.jsf?faces-redirect=true";
	}

	@PostConstruct
	public void init() {

		setPlageHoraires1(gestionPlageHoraireLocal.listerPlageHoraire());
	}

	public void setFields(List<Field> p_lFields) {
		m_lFields = p_lFields;
	}

	public List<Field> getFields() {
		return m_lFields;
	}

	public void onButtonRemoveFieldClick(final Field p_oField) {
		m_lFields.remove(p_oField);
	}

	public void onButtonAddFieldClick(AjaxBehaviorEvent p_oEvent) {
		System.out.println("gooooooood");
		m_lFields.add(new Field());
        
		plageHoraires.add(plageHoraire);
		heures.add(heure);
		heure=0;
		plageHoraire = new PlageHoraire();
	}

	public List<SelectOneMenu> getM_onemenu() {
		return m_onemenu;
	}

	public void setM_onemenu(List<SelectOneMenu> m_onemenu) {
		this.m_onemenu = m_onemenu;
	}

	public List<PlageHoraire> getPlageHoraires() {
		return plageHoraires;
	}

	public void setPlageHoraires(List<PlageHoraire> plageHoraires) {
		this.plageHoraires = plageHoraires;
	}

	public List<PlageHoraire> getPlageHoraires1() {
		return plageHoraires1;
	}

	public void setPlageHoraires1(List<PlageHoraire> plageHoraires1) {
		this.plageHoraires1 = plageHoraires1;
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

	public int getHeure() {
		return heure;
	}

	public void setHeure(int heure) {
		this.heure = heure;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}
}
