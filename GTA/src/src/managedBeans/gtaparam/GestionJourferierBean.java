package managedBeans.gtaparam;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gta.Calendrier;
import com.yesserp.domain.gta.Jourferier;
import com.yesserp.sessionbean.gta.gestioncalendrier.GestionJourferierLocal;

/*
 * ManagedBean Jourferier cree par omar le 23/08
 * pour gerer le CRUD des jour ferier d un calendier d id bien determiné
 */

@ManagedBean
@ViewScoped
public class GestionJourferierBean {

	private int id;
	private List<Jourferier> ljf;
	private Jourferier selectedjf;
	private String code;
	private Date datedeb;
	private Date datefin;
	private String type;

	@ManagedProperty(value = "#{calendrierBean.selectedcal}")
	private Calendrier selectedCal;

	@EJB
	GestionJourferierLocal ejb;

	@PostConstruct
	public void init() {
		System.out
				.println("*********************************************************");
		System.out
				.println("************ in init de jourFerier with selectedCal:"
						+ selectedCal.getCode());
		ljf = ejb.consulter(selectedCal);
	}

	public String delete() {
		ejb.supprimerJourFerier(selectedjf);
		return "GestionCalendrier.jsf?faces-redirect=true";
	}

	public void edit() {
	}

	public void select() {
	}

	public String add() {
		Jourferier jf = new Jourferier();
		jf.setCode(this.code);
		jf.setDatedeb(this.datedeb);
		jf.setDatefin(this.datefin);
		jf.setCalendrier(this.selectedCal);
		ejb.ajoutJourFerier(jf);
		return "GestionCalendrier.jsf?faces-redirect=true";
	}

	public void save() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Jourferier> getLjf() {
		return ljf;
	}

	public void setLjf(List<Jourferier> ljf) {
		this.ljf = ljf;
	}

	public Jourferier getSelectedjf() {
		return selectedjf;
	}

	public void setSelectedjf(Jourferier selectedjf) {
		this.selectedjf = selectedjf;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDatedeb() {
		return datedeb;
	}

	public void setDatedeb(Date datedeb) {
		this.datedeb = datedeb;
	}

	public Date getDatefin() {
		return datefin;
	}

	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Calendrier getSelectedCal() {
		return selectedCal;
	}

	public void setSelectedCal(Calendrier selectedCal) {
		this.selectedCal = selectedCal;
	}

}
