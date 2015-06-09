package managedBeans.gta;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.yesserp.domain.gta.PlanningReel;
import com.yesserp.sessionbean.gta.gestionplanningreel.GestionPlanningReelLocal;

@ManagedBean
@SessionScoped
public class PlanningReelBean {
	private List<PlanningReel> planningReels = new ArrayList<PlanningReel>();

	@EJB
	GestionPlanningReelLocal gestionPlanningReelLocal;

	public List<PlanningReel> getPlanningReels() {
		return planningReels;
	}

	public void setPlanningReels(List<PlanningReel> planningReels) {
		this.planningReels = planningReels;
	}
}
