package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionplagehoraire.GestionPlageHoraireLocal;

@ManagedBean
@ViewScoped
public class JtPlBean {
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private List<PlageHoraire> plageHoraires = new ArrayList<>();
	private JourneeType journeeType = new JourneeType();
	private PlageHoraire plageHoraire = new PlageHoraire();
	private PlageHoraire plageHoraire1 = new PlageHoraire();
	private PlageHoraire plageHoraire2 = new PlageHoraire();
	private PlageHoraire plageHoraire3 = new PlageHoraire();
	private List<PlageHoraire> plageHoraires2 = new ArrayList<>();
	private int heure1;
	private int heure2;
	private int heure3;

	private List<Integer> heures = new ArrayList<>();

	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionPlageHoraireLocal gestionPlageHoraireLocal;

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public void ajouter() {
		plageHoraires2.add(plageHoraire1);
		plageHoraires2.add(plageHoraire2);
		plageHoraires2.add(plageHoraire3);
		for (PlageHoraire p : plageHoraires2) {
			System.out.println(p.getCodeph());
		}
		heures.add(heure1);
		heures.add(heure2);
		heures.add(heure3);

		for (Integer x : heures) {
			System.out.println(x);
		}
		gestionJourneeTypeLocal.associatePlageHoraireToJourneeType(
				journeeType.getIdjt(), plageHoraires2, heures);
		plageHoraires2 = new ArrayList<>();
		heures = new ArrayList<>();

	}

	@PostConstruct
	public void init() {
		journeeTypes = gestionJourneeTypeLocal.listerJourneeType();
		plageHoraires = gestionPlageHoraireLocal.listerPlageHoraire();
	}

	public List<PlageHoraire> getPlageHoraires() {
		return plageHoraires;
	}

	public void setPlageHoraires(List<PlageHoraire> plageHoraires) {
		this.plageHoraires = plageHoraires;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
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

	public PlageHoraire getPlageHoraire1() {
		return plageHoraire1;
	}

	public void setPlageHoraire1(PlageHoraire plageHoraire1) {
		this.plageHoraire1 = plageHoraire1;
	}

	public PlageHoraire getPlageHoraire2() {
		return plageHoraire2;
	}

	public void setPlageHoraire2(PlageHoraire plageHoraire2) {
		this.plageHoraire2 = plageHoraire2;
	}

	public PlageHoraire getPlageHoraire3() {
		return plageHoraire3;
	}

	public void setPlageHoraire3(PlageHoraire plageHoraire3) {
		this.plageHoraire3 = plageHoraire3;
	}

	public List<PlageHoraire> getPlageHoraires2() {
		return plageHoraires2;
	}

	public void setPlageHoraires2(List<PlageHoraire> plageHoraires2) {
		this.plageHoraires2 = plageHoraires2;
	}

	public int getHeure1() {
		return heure1;
	}

	public void setHeure1(int heure1) {
		this.heure1 = heure1;
	}

	public int getHeure2() {
		return heure2;
	}

	public void setHeure2(int heure2) {
		this.heure2 = heure2;
	}

	public int getHeure3() {
		return heure3;
	}

	public void setHeure3(int heure3) {
		this.heure3 = heure3;
	}

}
