package managedBeans.gtaparam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.yesserp.domain.ga.Identite;
import com.yesserp.domain.gta.ClasseEmp;
import com.yesserp.domain.gta.ContratGta;
import com.yesserp.domain.gtaparam.Anomalie;
import com.yesserp.domain.gtaparam.Compteur;
import com.yesserp.domain.gtaparam.FilsCompteur;
import com.yesserp.domain.gtaparam.Filtre;
import com.yesserp.domain.gtaparam.FormuleCompteur;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.domain.gtaparam.ParamClass;
import com.yesserp.sessionbean.ga.identite.GestionIdentiteLocal;
import com.yesserp.sessionbean.gta.gestioncontratGTA.GestionContraGtaLocal;
import com.yesserp.sessionbean.paramgta.gestionAnomalie.GestionAnomalieLocal;
import com.yesserp.sessionbean.paramgta.gestionFormuleCompteur.GestionFormuleCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionactiviteabsence.GestionActiviteAbsenceLocal;
import com.yesserp.sessionbean.paramgta.gestioncompteur.GestionCompteurLocal;
import com.yesserp.sessionbean.paramgta.gestionfiltre.GestionFiltreLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@ManagedBean
@ViewScoped
public class CompteurBean {
	private Libelle libelle = new Libelle();
	private Compteur compteur = new Compteur();
	private String syntaxe = "";
	private FormuleCompteur formuleCompteur = new FormuleCompteur();
	private List<Filtre> filtres = new ArrayList<>();
	private List<FormuleCompteur> formuleCompteurs = new ArrayList<>();
	private Anomalie anomalie = new Anomalie();
	private Filtre f = new Filtre();
	private List<FilsCompteur> filsCompteur = new ArrayList<>();
	ClasseEmp classeEmp = new ClasseEmp();
	private ParamClass paramClass = new ParamClass();
	private List<Integer> arbre = new ArrayList<Integer>();
	private List<ParamClass> paramClasses = new ArrayList<ParamClass>();
	String cls = "";
	@EJB
	GestionLibelleLocal gestionLibelleLocal;
	@EJB
	GestionCompteurLocal gestionCompteurLocal;
	GestionCompteurLocal gestionCompteurLocal2;
	@EJB
	GestionFiltreLocal filtreLocal;
	@EJB
	GestionFormuleCompteurLocal formuleCompteurLocal;
	@EJB
	GestionAnomalieLocal gestionAnomalieLocal;
	@EJB
	GestionContraGtaLocal contraGtaLocal;
	@EJB
	GestionActiviteAbsenceLocal absenceLocal;
	@EJB
	GestionIdentiteLocal gestionIdentiteLocal;

	@PostConstruct
	public void init() {
	}

	public void ajouter() throws IOException {

		// recuprer l'identitite
		Identite identite = gestionIdentiteLocal
				.trouverParNumeroDossier((long) 1);
		// recuprer le contrat gta
		ContratGta cg = contraGtaLocal.getContratGtaByUser(identite);

		// recuprer la classe de l'identitite
		cls = contraGtaLocal.getClassByContrat(cg);

		Matcher matcher = Pattern.compile("F\\s*(\\w+)").matcher(
				paramClass.getFormule());

		paramClass.setCodeClasse(cls);
		paramClass.setFormuleCompteur(formuleCompteur);
		paramClasses.add(paramClass);
		formuleCompteur.setParamClasses(paramClasses);
		while (matcher.find()) {
			filtres.add((filtreLocal.rechercheFilterParNom("F"
					+ matcher.group(1))));
		}
		if (filtres.size() > 0) {
			formuleCompteur.setFilters(filtres);
		}
		formuleCompteurs.add(formuleCompteur);

		compteur.setFormuleCompteurs(formuleCompteurs);

		anomalie.setCompteur(compteur);
		formuleCompteur.setCompteur(compteur);
		// gestionAnomalieLocal.ajouterAnomalie(anomalie);
		// compteur.setFils(null);
		compteur.setFils(filsCompteur);
		gestionCompteurLocal.ajouterCompteur(compteur);
		gestionLibelleLocal.ajoutLibelle(libelle);
		libelle.setCompteur(compteur);
		gestionLibelleLocal.modifierLibelle(libelle);

		libelle = new Libelle();
		compteur = new Compteur();
		anomalie = new Anomalie();
		filsCompteur = new ArrayList<FilsCompteur>();
		formuleCompteur = new FormuleCompteur();
		paramClass = new ParamClass();
		formuleCompteurs = new ArrayList<FormuleCompteur>();
		syntaxe = null;
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
						"Contact admin."));
		// ExternalContext ec = FacesContext.getCurrentInstance()
		// .getExternalContext();
		// ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		//
		// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Compteur ajouté", null);
		// FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void filscmp(int i) {
		arbre.add(i);
		System.err.println("okkkk1");
		Compteur cp = new Compteur();
		cp = gestionCompteurLocal.findCompteurById(i);
		System.err.println("okkkk2");
		List<Integer> cmps = new ArrayList<Integer>();
		cmps = gestionCompteurLocal.getFilsCompteur(cp);
		System.err.println("okkkk3");
		if (!(cmps.size() == 0)) {
			for (Integer e : cmps) {
				filscmp(e);
			}
		}

	}

	public void verifierSyntaxe() {
		if (!paramClass.getFormule().equals(null)) {
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine eng = sem.getEngineByName("JavaScript");

			Matcher matcher = Pattern.compile("CP\\s*(\\w+)").matcher(
					paramClass.getFormule());

			while (matcher.find()) {
				FilsCompteur fc = new FilsCompteur();

				fc.setIdFils(gestionCompteurLocal.findCompteurByCode(
						"CP" + matcher.group(1)).getIdc());
				fc.setCompteur(compteur);
				filsCompteur.add(fc);
				System.out.println("cp" + matcher.group(1));
			}

			String in = paramClass.getFormule();
			String inWithoutCp = in.replace("CP", "0");
			String inWihtoutF = inWithoutCp.replace("F", "0");
			try {
				if (eng.eval(inWihtoutF).toString().equals("Infinity"))
					syntaxe = "Erreur Syntaxe";
				else {

					if (filsCompteur.size() > 0) {

						filscmp(filsCompteur.get(0).getIdFils());
						Set<Integer> similar = new HashSet<Integer>(arbre);
						for (int i = 1; i < filsCompteur.size(); i++) {
							arbre = new ArrayList<Integer>();
							filscmp(filsCompteur.get(i).getIdFils());
							similar.retainAll(arbre);
						}

						if ((similar.size() > 0) && (filsCompteur.size() >= 2))
							syntaxe = "erreur dans l'arborescence";
						else
							syntaxe = "formule valide";

					} else
						syntaxe = "formule valide";
				}

			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				syntaxe = "Erreur Syntaxe";

			}
			arbre = new ArrayList<Integer>();
		}
	}

	public void verifierSyntaxeCondition() {

		if (!paramClass.getCondition().equals(null)) {
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine eng = sem.getEngineByName("JavaScript");

			String in = paramClass.getCondition();
			String inWithoutCp = in.replace("CP", "0");
			String inWihtoutF = inWithoutCp.replace("F", "0");
			try {
				if (eng.eval(inWihtoutF).toString().equals("Infinity"))
					syntaxe = "Erreur Syntaxe";
				else
					syntaxe = "formule valide";
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				syntaxe = "Erreur Syntaxe";
			}
		}

	}

	public Libelle getLibelle() {
		return libelle;
	}

	public void setLibelle(Libelle libelle) {
		this.libelle = libelle;
	}

	public Compteur getCompteur() {
		return compteur;
	}

	public void setCompteur(Compteur compteur) {
		this.compteur = compteur;
	}

	public List<Filtre> getFiltres() {
		return filtres;
	}

	public void setFiltres(List<Filtre> filtres) {
		this.filtres = filtres;
	}

	public FormuleCompteur getFormuleCompteur() {
		return formuleCompteur;
	}

	public void setFormuleCompteur(FormuleCompteur formuleCompteur) {
		this.formuleCompteur = formuleCompteur;
	}

	public List<FormuleCompteur> getFormulecompteurs() {
		return formuleCompteurs;
	}

	public void setFormulecompteurs(List<FormuleCompteur> formuleCompteurs) {
		this.formuleCompteurs = formuleCompteurs;
	}

	public String getSyntaxe() {
		return syntaxe;
	}

	public void setSyntaxe(String syntaxe) {
		this.syntaxe = syntaxe;
	}

	public Anomalie getAnomalie() {
		return anomalie;
	}

	public void setAnomalie(Anomalie anomalie) {
		this.anomalie = anomalie;
	}

	public Filtre getF() {
		return f;
	}

	public void setF(Filtre f) {
		this.f = f;
	}

	public ClasseEmp getClasseEmp() {
		return classeEmp;
	}

	public void setClasseEmp(ClasseEmp classeEmp) {
		this.classeEmp = classeEmp;
	}

	public List<ParamClass> getParamClasses() {
		return paramClasses;
	}

	public void setParamClasses(List<ParamClass> paramClasses) {
		this.paramClasses = paramClasses;
	}

	public ParamClass getParamClass() {
		return paramClass;
	}

	public void setParamClass(ParamClass paramClass) {
		this.paramClass = paramClass;
	}

}
