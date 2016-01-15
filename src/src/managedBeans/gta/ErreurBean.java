package managedBeans.gta;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.yesserp.domain.gta.Erreur;
import com.yesserp.sessionbean.gta.GestionErreur.GestionErreurLocal;


@ManagedBean
@ViewScoped
public class ErreurBean {

	private Erreur erreur = new Erreur();
	private List<Erreur> Erreurs = new ArrayList<>();
	

	@EJB
	GestionErreurLocal gestionerreurLocal;

	public void ajouter() {
		
		gestionerreurLocal.ajouterErreur(erreur);
		erreur = new Erreur();
	}
	public List<Erreur> getErreurs() {
		return Erreurs;
	}


	public Erreur getErreur() {
		return erreur;
	}

	public void setErreur(Erreur erreur) {
		this.erreur = erreur;
	}
	public void setErreurs(List<Erreur> erreurs) {
		Erreurs = erreurs;
	}
}
