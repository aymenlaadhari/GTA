package managedBeans.gtaparam;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.yesserp.domain.gtaparam.AnneeReference;
import com.yesserp.sessionbean.paramgta.anneereference.GestionAnneeReferenceLocal;

@ManagedBean
@ViewScoped 
public class AnneeReferenceBean {
	private String libelle;
	private Date debut;
	private Date fin;
	private AnneeReference anneereference = new AnneeReference();
	@EJB
	GestionAnneeReferenceLocal gestionAnneeReference;
	
	public AnneeReference getAnneeReference() {
		return anneereference;
	}

	public void setAnneeReference(AnneeReference AnneeReference) {
		this.anneereference = AnneeReference;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public void ajouter()
	{	
		 int d = fin.getYear()-debut.getYear();
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(debut);  
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(fin);
	        
		if ((debut.after(fin)) || (d == 1) || (daysBetween(cal1, cal2)==1))
		throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Erreur.", null));
		else
		{
			anneereference.setLibelle(libelle);
			anneereference.setDebut(debut);
			anneereference.setFin(fin);
			gestionAnneeReference.ajoutAnneeReference(anneereference);	
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Jour férier ajouté avec succés", null);
			FacesContext.getCurrentInstance().addMessage(null, message);
		    anneereference = new AnneeReference();
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
