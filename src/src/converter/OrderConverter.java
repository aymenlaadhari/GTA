package converter;

import java.io.Serializable;



import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.extensions.model.timeline.TimelineEvent;

import com.sun.xml.internal.ws.util.StringUtils;



@FacesConverter("org.primefaces.extensions.showcase.converter.OrderConverter")  
public class OrderConverter implements Converter, Serializable {  
  
    private List<TimelineEvent> events;  
  
    public OrderConverter() {  
    }  
  
 
    public List<TimelineEvent> getEvents() {  
        return events;  
    }  
  
    public void setEvents(List<TimelineEvent> events) {  
        this.events = events;  
    }


	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return null;
	}}