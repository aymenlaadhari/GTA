package managedBeans.gta;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gta.Calendrier;
import com.yesserp.domain.gta.CodeCalendrier;
import com.yesserp.sessionbean.gta.gestioncalendrier.GestionCalendrierLocal;
import com.yesserp.sessionbean.gta.gestioncodecalendrier.GestionCodeCalendrierLocal;

@ManagedBean
@ViewScoped
public class CodeCalendrierBean {
	private CodeCalendrier codeCalendrier = new CodeCalendrier();
	private List<Calendrier> calendriers = new ArrayList<>();
	private List<Calendrier> listajout = new ArrayList<>();


	@EJB
	GestionCodeCalendrierLocal gestionCodeCalendrierLocal;
	@EJB
	GestionCalendrierLocal gestionCalendrierLocal;

	@PostConstruct
	public void init() {
		calendriers = gestionCalendrierLocal.findAllCalendriers();
	}

	public void ajouter() {

		codeCalendrier.setCalendriers(listajout);
		gestionCodeCalendrierLocal.ajouterCodeCalendrier(codeCalendrier);
		codeCalendrier = new CodeCalendrier();

	}

	public CodeCalendrier getCodeCalendrier() {
		return codeCalendrier;
	}

	public void setCodeCalendrier(CodeCalendrier codeCalendrier) {
		this.codeCalendrier = codeCalendrier;
	}

	public List<Calendrier> getCalendriers() {
		return calendriers;
	}

	public void setCalendriers(List<Calendrier> calendriers) {
		this.calendriers = calendriers;
	}

	public List<Calendrier> getListajout() {
		return listajout;
	}

	public void setListajout(List<Calendrier> listajout) {
		this.listajout = listajout;
	}
}
