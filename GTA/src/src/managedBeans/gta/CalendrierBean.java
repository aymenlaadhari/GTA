package managedBeans.gta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.*;

import com.yesserp.domain.gta.Calendrier;
import com.yesserp.sessionbean.gta.gestioncalendrier.GestionCalendrierLocal;

@ManagedBean
@SessionScoped
public class CalendrierBean {

	@EJB
	GestionCalendrierLocal gestioncalendrierLocal;

	private Calendrier calendrier = new Calendrier();

	// add by omar le 23/08

	private List<Calendrier> lcal = new ArrayList<Calendrier>();
	private Calendrier selectedcal;
	private String code;
	private Date date;
	private boolean cond = false;

	@PostConstruct
	public void init() {
		lcal = gestioncalendrierLocal.findAllCalendriers();
	}

	public void select() {
		System.out.println("*************** in slected");
		System.out.println("*******************" + this.selectedcal.getCode());
		System.out.println("*******************"
				+ this.selectedcal.getDateEffet());
	}

	public void edit() {
		System.out.println("*************** in edit");
		System.out.println("*******************" + this.selectedcal.getCode());
		System.out.println("*******************"
				+ this.selectedcal.getDateEffet());
		this.code = this.selectedcal.getCode();
		this.date = this.selectedcal.getDateEffet();
	}

	public String save() {
		Calendrier c = gestioncalendrierLocal
				.findCalendrierById(this.selectedcal.getId());
		c.setCode(this.code);
		c.setDateEffet(this.date);
		gestioncalendrierLocal.modifierCalendrier(c);
		lcal = gestioncalendrierLocal.findAllCalendriers();
		return "GestionCalendrier.jsf?faces-redirect=true";
	}

	public String add() {
		Calendrier c = new Calendrier();
		c.setCode(this.code);
		c.setDateEffet(this.date);
		gestioncalendrierLocal.ajouterCalendrier(c);
		lcal = gestioncalendrierLocal.findAllCalendriers();
		return "GestionCalendrier.jsf?faces-redirect=true";
	}

	public String delete() {
		gestioncalendrierLocal.supprimerCalendrier(this.selectedcal);
		lcal = gestioncalendrierLocal.findAllCalendriers();
		return "GestionCalendrier.jsf?faces-redirect=true";
	}

	public String gererJourferier() {
		this.cond = true;
		return "GestionCalendrier.jsf?faces-redirect=true";
	}

	// fin add by omar le 23/08

	public void ajouter() {
		gestioncalendrierLocal.ajouterCalendrier(calendrier);
		calendrier = new Calendrier();
	}

	public String redirect() {

		return "/gtaparam/codecalendrier.jsf?faces-redirect=true";
	}

	public Calendrier getCalendrier() {
		return calendrier;
	}

	public void setCalendrier(Calendrier calendrier) {
		this.calendrier = calendrier;
	}

	public List<Calendrier> getLcal() {
		return lcal;
	}

	public void setLcal(List<Calendrier> lcal) {
		this.lcal = lcal;
	}

	public Calendrier getSelectedcal() {
		return selectedcal;
	}

	public void setSelectedcal(Calendrier selectedcal) {
		this.selectedcal = selectedcal;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isCond() {
		return cond;
	}

	public void setCond(boolean cond) {
		this.cond = cond;
	}
}
