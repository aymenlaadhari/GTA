package managedBeans.gta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.PlageHoraire;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestionplanningtheorique.GestionPlanningTheoriqueLocal;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionplagehoraire.GestionPlageHoraireLocal;

@ManagedBean
@ViewScoped
public class PlageHoraireView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<PlageHoraire> plageHoraires = new ArrayList<>();
	private TimelineModel model;
	private String locale; // current locale as String, java.util.Locale is
							// possible too.
	private Date start;
	private Date end;
	private Identite identite = new Identite();
	private int deb;
	private int fin;
	private int year;
	private int month;
	private int day;
	private List<Identite> identites = new ArrayList<>();
	private JourneeType journeeType = new JourneeType();
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
	@EJB
	GestionPlanningTheoriqueLocal gestionPlanningTheoriqueLocal;
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionPlageHoraireLocal gestionPlageHoraireLocal;

	public void initialize() {
		identite = gestionIdentiteLocal.trouverParNumeroDossier((long) 1);
		journeeType = gestionJourneeTypeLocal.findJourneeTypeById(1);
		plageHoraires = gestionPlanningTheoriqueLocal
				.findPlageHorairessByJourneeType(journeeType);
		// set initial start / end dates for the axis of the timeline
		// Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		// Date now = new Date();

		// cal.setTimeInMillis(now.getTime() - 2 * 60 * 60 * 1000);
		// start = cal.getTime();

		// cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
		// end = cal.getTime();

		// groups
		identites = gestionIdentiteLocal.findAll();

		// create timeline model
		
		model = new TimelineModel();
		DateTime dateTime = new DateTime(start);
		year=dateTime.getYear();
		month = dateTime.getMonthOfYear();
		day= dateTime.getDayOfMonth();

		// now = new Date();
		// Date end = new Date(now.getTime() - 12 * 60 * 60 * 1000);
		for (PlageHoraire p : plageHoraires) {
			deb = gestionPlageHoraireLocal.trouverHeureDeb(p, journeeType);
			fin = gestionPlageHoraireLocal.trouverHeureFin(p, journeeType);
			 start = new Date(year-1900,month-1,day,deb, 00);
			 end = new Date(year-1900,month-1,day,fin, 00);
			TimelineEvent event = new TimelineEvent(p.getCodeph(), start, end,
					true, identite.getNom(), p.getCodeph().toLowerCase());
			model.add(event);

		}
	}

	public void onChange(TimelineModificationEvent e) {
		TimelineEvent ev = e.getTimelineEvent();
		System.out.println(ev.getEndDate());
		System.out.println("okkkkkkkkkkkk");		
	}
	
	public void onSelect(TimelineSelectEvent e){
		System.out.println("seleeect");
	}
	
	

	public TimelineModel getModel() {
		return model;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public List<Identite> getIdentites() {
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

	public List<PlageHoraire> getPlageHoraires() {
		return plageHoraires;
	}

	public void setPlageHoraires(List<PlageHoraire> plageHoraires) {
		this.plageHoraires = plageHoraires;
	}

	public Identite getIdentite() {
		return identite;
	}

	public void setIdentite(Identite identite) {
		this.identite = identite;
	}

	public int getDeb() {
		return deb;
	}

	public void setDeb(int deb) {
		this.deb = deb;
	}

	public int getFin() {
		return fin;
	}

	public void setFin(int fin) {
		this.fin = fin;
	}

	public JourneeType getJourneeType() {
		return journeeType;
	}

	public void setJourneeType(JourneeType journeeType) {
		this.journeeType = journeeType;
	}

	public void setModel(TimelineModel model) {
		this.model = model;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
}