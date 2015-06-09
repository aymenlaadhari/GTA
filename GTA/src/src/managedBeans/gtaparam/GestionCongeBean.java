package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;









import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.yesserp.domain.gtaparam.Anciennete;
import com.yesserp.domain.gtaparam.Compteur;
import com.yesserp.domain.gtaparam.GestionConge;
import com.yesserp.domain.gtaparam.GestionPeriodeConge;
import com.yesserp.domain.gtaparam.Unite;
import com.yesserp.sessionbean.paramgta.gestioncompteur.GestionCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionconge.GestionGestionCongeLocal;
import com.yesserp.sessionbean.paramgta.gestionperiodeconge.GestionGestionPeriodeCongeLocal;
import com.yesserp.sessionbean.paramgta.unite.GestionUniteLocal;

@ManagedBean
@ViewScoped
public class GestionCongeBean {
	
	private GestionConge gestionconge = new GestionConge();
	private GestionPeriodeConge gestionperiodeconge = new GestionPeriodeConge();
	private Anciennete anciennete = new Anciennete();
	private List<Compteur> compteurs = new ArrayList<>();
	private List<Unite> unites = new ArrayList<>();
	private List<SelectOneMenu> oneMenusOfGrpImp = new ArrayList<SelectOneMenu>();
	private List<SelectOneMenu> oneMenusOfGrpAct = new ArrayList<SelectOneMenu>();
	
	@EJB
	GestionGestionCongeLocal gestiongestioncongeLocal;
	@EJB
	GestionGestionPeriodeCongeLocal gestiongestionperiodecongeLocal;
	@EJB
	GestionUniteLocal gestionUniteLocal;
	@EJB
	GestionCompteurLocal gestionCompteurLocal;
	
		@PostConstruct
		public void init() 
		{
		unites = gestionUniteLocal.listerUnite();
		compteurs = gestionCompteurLocal.listeCompteurs();
		}
		
		public GestionConge getGestionconge() {
			return gestionconge;
		}

		public void setGestionconge(GestionConge gestionconge) {
			this.gestionconge = gestionconge;
		}
		
		public List<Compteur> getCompteurs() {
			return compteurs;
		}

		public void setCompteurs(List<Compteur> compteurs) {
			this.compteurs = compteurs;
		}

		public GestionPeriodeConge getGestionperiodeconge() {
			return gestionperiodeconge;
		}

		public List<Unite> getUnites() {
			return unites;
		}

		public void setUnites(List<Unite> unites) {
			this.unites = unites;
		}

		public void setGestionperiodeconge(GestionPeriodeConge gestionperiodeconge) {
			this.gestionperiodeconge = gestionperiodeconge;
		}

		public Anciennete getAnciennete() {
			return anciennete;
		}

		public void setAnciennete(Anciennete anciennete) {
			this.anciennete = anciennete;
		}

		public List<SelectOneMenu> getOneMenusOfGrpImp() {
			return oneMenusOfGrpImp;
		}


		public void setOneMenusOfGrpImp(List<SelectOneMenu> oneMenusOfGrpImp) {
			this.oneMenusOfGrpImp = oneMenusOfGrpImp;
		}


		public List<SelectOneMenu> getOneMenusOfGrpAct() {
			return oneMenusOfGrpAct;
		}


		public void setOneMenusOfGrpAct(List<SelectOneMenu> oneMenusOfGrpAct) {
			this.oneMenusOfGrpAct = oneMenusOfGrpAct;
		}

		public void onButtonRemoveFieldClick(final SelectOneMenu p_oField) {
			//oneMenusOfGrpImp.remove(p_oField);
		}
		
		public void ajouterGestionConge() {

			gestiongestioncongeLocal.ajoutGestionConge(gestionconge);
			gestionconge = new GestionConge();
			
		}

		public void ajouterGestionPeriodeConge() {
			gestionperiodeconge.setDatefin(gestionperiodeconge.getDatefin());
			gestiongestionperiodecongeLocal.ajoutGestionPeriodeConge(gestionperiodeconge);
			gestionperiodeconge = new GestionPeriodeConge();
			
		}


}
