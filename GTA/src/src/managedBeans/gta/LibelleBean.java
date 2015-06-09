package managedBeans.gta;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.yesserp.domain.gtaparam.Libelle;


@ManagedBean
@SessionScoped
public class LibelleBean {
	
	private Libelle libelle = new Libelle();
	
	

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

}
