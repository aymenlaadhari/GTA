package managedBeans.gta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.yesserp.domain.gtaparam.ValeurCompteur;
import com.yesserp.sessionbean.gta.valeurCompteur.GestionValeurCompteurLocal;

@ManagedBean
@SessionScoped
public class TacheBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private GestionValeurCompteurLocal gestionValeurCompteur;

	private ValeurCompteur valeurCompteur = new ValeurCompteur();

	private List<ValeurCompteur> valeurCompteurs = new ArrayList<ValeurCompteur>();

	private ScheduleModel eventModel = new DefaultScheduleModel();

	private ScheduleEvent event = new DefaultScheduleEvent();

	@PostConstruct
	public void init() {
		setValeurCompteur(new ValeurCompteur());

		valeurCompteurs = new ArrayList<ValeurCompteur>();
		eventModel = new DefaultScheduleModel();
		event = new DefaultScheduleEvent();

		valeurCompteurs = gestionValeurCompteur.findAllvaleurCompteur();
		for (ValeurCompteur v : valeurCompteurs) {
		//	eventModel.addEvent(new DefaultScheduleEvent(v.getValeurHoraire().getHours()+"h"+v.getValeurHoraire().getMinutes()+"m"+v.getValeurHoraire().getSeconds()+"s", v.getDateCalc(), v.getDateCalc()));

		}

	}

	public Date getInitialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY,
				calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar.getTime();
	}

	// Creation Calendar//
	private Calendar today() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

		return calendar;
	}

	// Ajout Tache
	public void addEvent(ActionEvent actionEvent) {

		gestionValeurCompteur.ajoutervaleurCompteur(valeurCompteur);

		valeurCompteur = new ValeurCompteur();
		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();

	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(),
				(Date) selectEvent.getObject());
	}

	// Redimentionement evenement
	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Event resized", "difference jour:" + event.getDayDelta()
						+ ", difference minute:" + event.getMinuteDelta());

		addMessage(message);
	}

	// Affichage message
	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public ValeurCompteur getValeurCompteur() {
		return valeurCompteur;
	}

	public void setValeurCompteur(ValeurCompteur valeurCompteur) {
		this.valeurCompteur = valeurCompteur;
	}

	public List<ValeurCompteur> getValeurCompteurs() {
		return valeurCompteurs;
	}

	public void setValeurCompteurs(List<ValeurCompteur> valeurCompteurs) {
		this.valeurCompteurs = valeurCompteurs;
	}
}
