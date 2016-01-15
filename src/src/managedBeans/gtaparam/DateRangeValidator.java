package managedBeans.gtaparam;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
   
@FacesValidator("dateRangeValidator")
public class DateRangeValidator implements Validator {
     
	@Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; // Let required="true" handle.
        }

        UIInput startDateComponent = (UIInput) component.getAttributes().get("startDateComponent");

        if (!startDateComponent.isValid()) {
            return; // Already invalidated. Don't care about it then.
        }

        Date startDate = (Date) startDateComponent.getValue();

        if (startDate == null) {
            return; // Let required="true" handle.
        }

        Date endDate = (Date) value;
        int d = endDate.getYear()-startDate.getYear();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);  
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        
        
        
        if (((startDate.after(endDate)) && (d == 1)) && (daysBetween(cal1, cal2)==1)){
            startDateComponent.setValid(false);
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Start date may not be after end date.", null));
        }
        
        
    }
	public static long daysBetween(Calendar start, Calendar end) {  
    	  Calendar date = (Calendar) start.clone();  
    	  long f = 0;  
    	  while (date.before(end)) {  
    	    date.add(Calendar.DAY_OF_MONTH, 1);  
    	    f++;  
    	  }  
    	  return f;  
    	}  
}