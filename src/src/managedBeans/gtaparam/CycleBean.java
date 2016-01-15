package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gtaparam.CycleAssociateJourneeTypeEmbeddebale;
import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.CycleTravailAssociateJourneeType;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class CycleBean {

	private CycleTravail cycleTravail = new CycleTravail();
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private List<JourneeType> listajout = new ArrayList<>();
	private JourneeType journeeType = new JourneeType();
	private Libelle libelle = new Libelle();

	private List<CycleTravailAssociateJourneeType> ctajtList = new ArrayList<>();
	private CycleTravailAssociateJourneeType ctajt = new CycleTravailAssociateJourneeType();
	private List<JourneeType> journeesAjout = new ArrayList<>();
	private List<Integer> ordreJours = new ArrayList<>();
	private List<CycleTravail> cycleTravails = new ArrayList<>();
	private CycleTravail selectedCT = new CycleTravail();

	@EJB
	GestionCycleTravailLocal gestionCycleTravailLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;

	public void ajouter() {
		// cycleTravail.setJourneeTypes(listajout);
		// cycleTravail.setJourneeTypes(listajout);
		cycleTravail.setTypeCycle("j");
		gestionCycleTravailLocal.ajoutCycleTravail(cycleTravail);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setCycleTravail(cycleTravail);
		gestionLibelleLocal.modifierLibelle(libelle);

		gestionCycleTravailLocal.findCycleDeTravailParCode(cycleTravail
				.getCodect());

		definirCycleTravail();

		cycleTravail = new CycleTravail();
		libelle = new Libelle();

	}

	@PostConstruct
	public void init() {
		journeeTypes = gestionJourneeTypeLocal.listerJourneeType();
		cycleTravails = gestionCycleTravailLocal.findCycleTravailByType("j");
		for (int i = 0; i < cycleTravails.size(); i++) {
			try {
				cycleTravails.get(i).setLibelles(
						gestionLibelleLocal
								.findLibelleByCycleTravail(cycleTravails.get(i)
										.getIdct()));

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void definirCycleTravail() {

		for (int i = 0; i < ctajtList.size(); i++) {
			journeesAjout.add(ctajtList.get(i).getJourneeType());
			ordreJours.add(ctajtList.get(i)
					.getAssociateJourneeTypeEmbeddebale().getOrdreJournee());
		}
		try {
			gestionCycleTravailLocal.associateCycleTravailToJourneeType(
					cycleTravail.getIdct(), journeesAjout, ordreJours);
		} catch (Exception e) {
			e.printStackTrace();
		}
		journeesAjout = new ArrayList<>();
		ctajt = new CycleTravailAssociateJourneeType();
		ctajtList = new ArrayList<>();
		ordreJours = new ArrayList<>();

	}

	public void addDefJtp() {
		ctajt.setAssociateJourneeTypeEmbeddebale(new CycleAssociateJourneeTypeEmbeddebale());
		ctajtList.add(ctajt);
		ctajt = new CycleTravailAssociateJourneeType();
	}

	public void removeDefCtp() {
		ctajtList.remove(ctajtList.size() - 1);
		ctajt = new CycleTravailAssociateJourneeType();
	}

	public String redirect() {

		return "/gtaparam/ajoutcalendrier.jsf?faces-redirect=true";
	}

	public CycleTravail getCycleTravail() {
		return cycleTravail;
	}

	public void setCycleTravail(CycleTravail cycleTravail) {
		this.cycleTravail = cycleTravail;
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

	public List<JourneeType> getListajout() {
		return listajout;
	}

	public void setListajout(List<JourneeType> listajout) {
		this.listajout = listajout;
	}

	public List<CycleTravailAssociateJourneeType> getCtajtList() {
		return ctajtList;
	}

	public void setCtajtList(List<CycleTravailAssociateJourneeType> ctajtList) {
		this.ctajtList = ctajtList;
	}

	public CycleTravailAssociateJourneeType getCtajt() {
		return ctajt;
	}

	public void setCtajt(CycleTravailAssociateJourneeType ctajt) {
		this.ctajt = ctajt;
	}

	public List<JourneeType> getJourneesAjout() {
		return journeesAjout;
	}

	public void setJourneesAjout(List<JourneeType> journeesAjout) {
		this.journeesAjout = journeesAjout;
	}

	public List<Integer> getOrdreJours() {
		return ordreJours;
	}

	public void setOrdreJours(List<Integer> ordreJours) {
		this.ordreJours = ordreJours;
	}

	public List<CycleTravail> getCycleTravails() {
		return cycleTravails;
	}

	public void setCycleTravails(List<CycleTravail> cycleTravails) {
		this.cycleTravails = cycleTravails;
	}

	public CycleTravail getSelectedCT() {
		return selectedCT;
	}

	public void setSelectedCT(CycleTravail selectedCT) {
		this.selectedCT = selectedCT;
	}

}
