package managedBeans.gtaparam;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
 




import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.yesserp.domain.gtaparam.JourFerier;
import com.yesserp.sessionbean.paramgta.jourferier.GestionJourFerierLocal;
 
@ManagedBean
@ViewScoped
public class ScheduleView implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ScheduleModel eventModel;
     
    private ScheduleModel lazyEventModel;
    
    private String type;
    private Date initialDate;
	JourFerier jourferier = new JourFerier();
	@EJB
	GestionJourFerierLocal gestionjourferier;
 
    private ScheduleEvent event = new DefaultScheduleEvent();
 
    @PostConstruct
    public void init() throws ParseException {
        eventModel = new DefaultScheduleModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateMin = dateFormat.parse("01/01/2014");
        Date dateMax = dateFormat.parse("31/12/2014");
     
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateMin);
        Calendar calMax = Calendar.getInstance();
        calMax.setTime(dateMax);
        for (; calendar.before(calMax); calendar.add(Calendar.DATE, 1)) 
        	if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
        	{
        		eventModel.addEvent(new DefaultScheduleEvent("Lundi", calendar.getTime(), calendar.getTime(), "myclass"));
	        	jourferier.setTitle("lundi");
	        	jourferier.setStartDate(calendar.getTime());
	        	jourferier.setEndDate (calendar.getTime());
	        	jourferier.setType("Autres");
	    		gestionjourferier.ajoutJourFerier(jourferier);	
	    		jourferier = new JourFerier();

        	}

        lazyEventModel = new LazyScheduleModel() {
             
           
        };
    }
     
    
    public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public JourFerier getJourferier() {
		return jourferier;
	}


	public void setJourferier(JourFerier jourferier) {
		this.jourferier = jourferier;
	}


	public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }
 
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(7) );         
        return t.getTime();
    }
     
    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(7) );         
        return t.getTime();
         
    }
     




    
     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null)
            {
        	eventModel.addEvent(event);
        	jourferier.setTitle(event.getTitle());
        	jourferier.setStartDate(event.getStartDate());
        	jourferier.setEndDate (event.getEndDate());
        	jourferier.setType(type);
    		gestionjourferier.ajoutJourFerier(jourferier);	
    		jourferier = new JourFerier();

            }
        	
        else
        {
            eventModel.updateEvent(event);
            jourferier.setTitle(event.getTitle());
        	jourferier.setStartDate(event.getStartDate());
        	jourferier.setEndDate (event.getEndDate());
        	jourferier.setType(event.getTitle());
    		gestionjourferier.modifierJourFerier(jourferier);	
    		jourferier = new JourFerier();

        }
         
        event = new DefaultScheduleEvent();
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject(),"myclass");
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}
    
    
}