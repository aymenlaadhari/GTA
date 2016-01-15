package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import com.yesserp.domain.gtaparam.AnneeReference;
import com.yesserp.domain.gtaparam.Unite;
import com.yesserp.sessionbean.paramgta.anneereference.GestionAnneeReferenceLocal;
import com.yesserp.sessionbean.paramgta.unite.GestionUniteLocal;
 
@ManagedBean
public class UniteBean {
         
    private String[] selectedHolidays;   
    private List<String> holidays;
    private Boolean jourferier;
    private Unite unite = new Unite();
    private AnneeReference anneereference = new AnneeReference();
	private List<AnneeReference> anneereferences = new ArrayList<>();
 
    @EJB
	GestionUniteLocal gestionUniteLocal;
    @EJB
   	GestionAnneeReferenceLocal gestionAnneeReferenceLocal;
    
	@PostConstruct
    public void init() {
		anneereferences = gestionAnneeReferenceLocal.listerAnneeReference();
        holidays = new ArrayList<String>();
        holidays.add("Lundi");
        holidays.add("Mardi");
        holidays.add("Mercredi");
        holidays.add("Jeudi");
        holidays.add("Vendredi");
        holidays.add("Samedi");
        holidays.add("Dimanche");

    }
 
    public String[] getSelectedHolidays() {
    	System.out.println("true");
        return selectedHolidays;
    }
 
    public void setSelectedHolidays(String[] selectedHolidays) {
        this.selectedHolidays = selectedHolidays;
    }
 
    public List<String> getHolidays() {
        return holidays;
    }
    
    public Boolean getJourferier() {
		return jourferier;
	}

	public void setJourferier(Boolean jourferier) {
		this.jourferier = jourferier;
	}

	public String redirect(){
		return "/gtaparam/creationunite.jsf?faces-redirect=true";
	}

	public Unite getUnite() {
		return unite;
	}

	public void setUnite(Unite unite) {
		this.unite = unite;
	}

	public List<AnneeReference> getAnneereferences() {
		return anneereferences;
	}

	public void setAnneereferences(List<AnneeReference> anneereferences) {
		this.anneereferences = anneereferences;
	}

	public AnneeReference getAnneereference() {
		return anneereference;
	}

	public void setAnneereference(AnneeReference anneereference) {
		this.anneereference = anneereference;
	}

    
}