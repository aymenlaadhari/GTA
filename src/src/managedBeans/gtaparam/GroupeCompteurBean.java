package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gtaparam.Compteur;
import com.yesserp.domain.gtaparam.GroupeCompteur;
import com.yesserp.domain.gtaparam.GroupeCompteurAssociateIdentite;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.paramgta.gestioncompteur.GestionCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestiongroupecompteur.GestionGroupeCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@SessionScoped
public class GroupeCompteurBean {

	private Libelle libelle = new Libelle();
	private List<Compteur> compteurs = new ArrayList<>();
	private List<Compteur> selectedCompteurs = new ArrayList<>();
	private GroupeCompteur groupeCompteur = new GroupeCompteur();
	private Date dateEffet = new Date();
	private Date datefin = new Date(199, 05, 12);
	private List<Identite> identites;
	@EJB
	GestionCompteurLocal gestionCompteurLocal;
	@EJB
	GestionGroupeCompteurLocal gestionGroupeCompteurLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;
Identite identite;
	public void ajouter() {

		
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setGroupeCompteur(groupeCompteur);
		groupeCompteur.setCompteurs(selectedCompteurs);
		gestionGroupeCompteurLocal.ajouterGroupeCompteur(groupeCompteur);
		gestionGroupeCompteurLocal.affecterGroupetoIdentite(identite,
				groupeCompteur, dateEffet, datefin);

		libelle = new Libelle();
		selectedCompteurs = new ArrayList<>();
		groupeCompteur = new GroupeCompteur();

	}

	@PostConstruct
	public void init() {
		compteurs = gestionCompteurLocal.listeCompteurs();

		 identite = gestionIdentiteLocal
				.trouverParNumeroDossier((long) 1);
Date d = new Date(114, 07, 15);
	List<Compteur> cs=	gestionCompteurLocal.getCompteurByUser(identite, d);

	for(Compteur c :cs){
		System.out.println(c);
	}
	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public List<Compteur> getCompteurs() {
		return compteurs;
	}

	public void setCompteurs(List<Compteur> compteurs) {
		this.compteurs = compteurs;
	}

	public List<Compteur> getSelectedCompteurs() {
		return selectedCompteurs;
	}

	public void setSelectedCompteurs(List<Compteur> selectedCompteurs) {
		this.selectedCompteurs = selectedCompteurs;
	}

	public GroupeCompteur getGroupeCompteur() {
		return groupeCompteur;
	}

	public void setGroupeCompteur(GroupeCompteur groupeCompteur) {
		this.groupeCompteur = groupeCompteur;
	}

	public Date getDateEffet() {
		return dateEffet;
	}

	public void setDateEffet(Date dateEffet) {
		this.dateEffet = dateEffet;
	}

	public List<Identite> getIdentites() {
		return identites;
	}

	public void setIdentites(List<Identite> identites) {
		this.identites = identites;
	}

}
