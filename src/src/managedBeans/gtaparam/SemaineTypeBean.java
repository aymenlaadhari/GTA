package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;

@ManagedBean
@ViewScoped
public class SemaineTypeBean {

	// attribut added by omar le 28/08
	private List<SemaineType> lsemaine;
	private SemaineType selectedsemaine;
	// fin add
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private List<JourneeType> listjournee = new ArrayList<>();
	private SemaineType semaineType = new SemaineType();
	private List<CodeJour> codeJours = new ArrayList<>();
	private CodeJour codeJour = new CodeJour();
	private JourneeType journeeType = new JourneeType();
	private Libelle libelle = new Libelle();
	private boolean headerButtonsDisabled = true;
	private int id = 1;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;

	// add by omar le 28/08

	public String select() {
		return null;
	}

	public String supprimer() {
		gestionSemaineTypeLocal.supprimerSemaineType(this.selectedsemaine);
		return redirect();
	}

	public String edit() {
		return null;
	}

	public String save() {
		return null;
	}

	// fin add

	@PostConstruct
	public void intit() {
		// add by omar le 29/08
		this.lsemaine = gestionSemaineTypeLocal.listeSemaineType();
		// fin add
		journeeTypes = gestionJourneeTypeLocal.listerJourneeType();
		codeJours = gestionCodeJourLocal.listeCodeJours();
		codeJour = gestionCodeJourLocal.findCodeJourById(2);
	}

	public void ajouter() {
		gestionSemaineTypeLocal.ajouterSemaineType(semaineType);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setSemaineType(semaineType);
		gestionLibelleLocal.modifierLibelle(libelle);
		libelle = new Libelle();
		 
		for (JourneeType j : listjournee) {
			if (id < 8) {
				gestionSemaineTypeLocal
						.ajouterDesJourneesPourSemaineAvecCodeJour(semaineType,
								j, gestionCodeJourLocal.findCodeJourById(id++));
			}
		}
		semaineType = new SemaineType();
		
		listjournee = new ArrayList<>();
		id = 1;
		
	}

	public String redirect() {

		return "/gtaparam2/Gestionsemainetype.jsf?faces-redirect=true";
	}

	public void test() {
		if (journeeType.getCodejt() != null) {
			listjournee.add(journeeType);
		}

	}

	public void afficher() {
		for (JourneeType j : listjournee) {
			System.out.println("*********" + j.getCodejt());
		}

	}

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public SemaineType getSemaineType() {
		return semaineType;
	}

	public void setSemaineType(SemaineType semaineType) {
		this.semaineType = semaineType;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<JourneeType> getListjournee() {
		return listjournee;
	}

	public void setListjournee(List<JourneeType> listjournee) {
		this.listjournee = listjournee;
	}

	public List<CodeJour> getCodeJours() {
		return codeJours;
	}

	public void setCodeJours(List<CodeJour> codeJours) {
		this.codeJours = codeJours;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public CodeJour getCodeJour() {
		return codeJour;
	}

	public void setCodeJour(CodeJour codeJour) {
		this.codeJour = codeJour;
	}

	public List<SemaineType> getLsemaine() {
		return lsemaine;
	}

	public void setLsemaine(List<SemaineType> lsemaine) {
		this.lsemaine = lsemaine;
	}

	public SemaineType getSelectedsemaine() {
		return selectedsemaine;
	}

	public void setSelectedsemaine(SemaineType selectedsemaine) {
		this.selectedsemaine = selectedsemaine;
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

}
