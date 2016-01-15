package managedBeans.gta;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gta.ActiviteReelSaisie;
import com.yesserp.sessionbean.gta.gestionactivitereelsaisie.GestionActiviteReelSaisieLocal;

@ManagedBean
@ViewScoped
public class ActiviteReelSaisieBean {
	private ActiviteReelSaisie activiteReelSaisie = new ActiviteReelSaisie();

	@EJB
	GestionActiviteReelSaisieLocal gestionActiviteReelSaisieLocal;

	public void ajouter() {
		//activiteReelSaisie.setHeuredeb(new Time(Integer.parseInt(tmp.substring(0, 2)),
				//Integer.parseInt(tmp.substring(3, 5)), 0));
		gestionActiviteReelSaisieLocal
				.ajouterActiviteReelSaisie(activiteReelSaisie);
		activiteReelSaisie = new ActiviteReelSaisie();
	}

	public ActiviteReelSaisie getActiviteReelSaisie() {
		return activiteReelSaisie;
	}

	public void setActiviteReelSaisie(ActiviteReelSaisie activiteReelSaisie) {
		this.activiteReelSaisie = activiteReelSaisie;
	}
}
