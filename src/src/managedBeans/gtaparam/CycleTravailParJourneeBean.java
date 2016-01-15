package managedBeans.gtaparam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class CycleTravailParJourneeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CycleTravail cycleTravail = new CycleTravail();
	private List<CycleTravail> cycleTravails = new ArrayList<>();
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private List<JourneeType> lisajoutJourneeType = new ArrayList<>();
	private Libelle libelle = new Libelle();
	private List<String> codes = new ArrayList<>();
	private JourneeType journeeType = new JourneeType();
	private List<SelectOneMenu> m_onemenu;
	private List<Field> m_lFields;
	@EJB
	GestionCycleTravailLocal gestionCycleTravailLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;

	@PostConstruct
	public void init() {
		journeeTypes = gestionJourneeTypeLocal.listerJourneeType();
	}

	public void ajouterCycleTravail() {

		gestionCycleTravailLocal.ajoutCycleTravail(cycleTravail);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setCycleTravail(cycleTravail);
		gestionLibelleLocal.modifierLibelle(libelle);

	}

	public CycleTravail getCycleTravail() {
		return cycleTravail;
	}

	public void setCycleTravail(CycleTravail cycleTravail) {
		this.cycleTravail = cycleTravail;
	}

	public List<CycleTravail> getCycleTravails() {
		return cycleTravails;
	}

	public void setCycleTravails(List<CycleTravail> cycleTravails) {
		this.cycleTravails = cycleTravails;
	}

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public List<SelectOneMenu> getM_onemenu() {
		return m_onemenu;
	}

	public void setM_onemenu(List<SelectOneMenu> m_onemenu) {
		this.m_onemenu = m_onemenu;
	}

	public List<Field> getFields() {
		return m_lFields;
	}

	public void setM_lFields(List<Field> m_lFields) {
		this.m_lFields = m_lFields;
	}

	public void onButtonRemoveFieldClick(final Field p_oField) {
		m_lFields.remove(p_oField);
	}

	public List<JourneeType> getLisajoutJourneeType() {
		return lisajoutJourneeType;
	}

	public void setLisajoutJourneeType(List<JourneeType> lisajoutJourneeType) {
		this.lisajoutJourneeType = lisajoutJourneeType;
	}

}
