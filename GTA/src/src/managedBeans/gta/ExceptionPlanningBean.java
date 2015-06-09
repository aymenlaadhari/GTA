package managedBeans.gta;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gtaparam.CycleTravail;
import com.yesserp.domain.gtaparam.JourneeType;
import com.yesserp.domain.gtaparam.SemaineType;
import com.yesserp.sessionbean.paramgta.gestionjourneetype.GestionJourneeTypeLocal;
import com.yesserp.sessionbean.paramgta.gestionsemainetype.GestionSemaineTypeLocal;
import com.yesserp.sessionbean.paramgta.gestioncycledetravail.GestionCycleTravailLocal;

@ManagedBean
@ViewScoped
public class ExceptionPlanningBean {
	private List<JourneeType> journeeTypes = new ArrayList<>();
	private List<SemaineType> semaineTypes = new ArrayList<>();
    private List<CycleTravail> cycleTravails = new ArrayList<>();
	@EJB
	GestionJourneeTypeLocal gestionJourneeTypeLocal;
	@EJB
	GestionSemaineTypeLocal gestionSemaineTypeLocal;
	@EJB
	GestionCycleTravailLocal gestionCycleTravailLocal;

	@PostConstruct
	public void init() {
		journeeTypes = gestionJourneeTypeLocal.listerJourneeType();
        semaineTypes = gestionSemaineTypeLocal.listeSemaineType();
        cycleTravails = gestionCycleTravailLocal.listerCycleTravail();
        
	}

	public List<JourneeType> getJourneeTypes() {
		return journeeTypes;
	}

	public void setJourneeTypes(List<JourneeType> journeeTypes) {
		this.journeeTypes = journeeTypes;
	}

	public List<SemaineType> getSemaineTypes() {
		return semaineTypes;
	}

	public void setSemaineTypes(List<SemaineType> semaineTypes) {
		this.semaineTypes = semaineTypes;
	}

	public List<CycleTravail> getCycleTravails() {
		return cycleTravails;
	}

	public void setCycleTravails(List<CycleTravail> cycleTravails) {
		this.cycleTravails = cycleTravails;
	}

}
