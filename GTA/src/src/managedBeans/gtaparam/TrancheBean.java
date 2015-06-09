package managedBeans.gtaparam;


import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gtaparam.TrancheDroit;
import com.yesserp.sessionbean.paramgta.tranchedroit.GestionTrancheDroitLocal;

@ManagedBean
@ViewScoped
public class TrancheBean {
	TrancheDroit tranche = new TrancheDroit();
	@EJB
	GestionTrancheDroitLocal gestiontranchedroit;

	public TrancheDroit getTranche() {
		return tranche;
	}


	public void setTranche(TrancheDroit tranche) {
		this.tranche = tranche;
	}

	public void ajouter()
	{
		gestiontranchedroit.ajoutTrancheDroit(tranche);	
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Tranche ajoutée avec succés", null);
		FacesContext.getCurrentInstance().addMessage(null, message);
		tranche = new TrancheDroit();
	}

}

