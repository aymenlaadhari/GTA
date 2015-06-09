package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;

import com.yesserp.domain.gtaparam.CodeJour;
import com.yesserp.domain.gtaparam.Libelle;
import com.yesserp.sessionbean.paramgta.gestioncodejour.GestionCodeJourLocal;
import com.yesserp.sessionbean.paramgta.gestionlibelle.GestionLibelleLocal;

@javax.faces.bean.ManagedBean
@ViewScoped
public class CodeJourBean {
	private CodeJour codeJour1 = new CodeJour();
	private CodeJour codeJour2 = new CodeJour();
	private CodeJour codeJour3 = new CodeJour();
	private CodeJour codeJour4 = new CodeJour();
	private CodeJour codeJour5 = new CodeJour();
	private CodeJour codeJour6 = new CodeJour();
	private CodeJour codeJour7 = new CodeJour();
	private Libelle libelle1 = new Libelle();
	private Libelle libelle2 = new Libelle();
	private Libelle libelle3 = new Libelle();
	private Libelle libelle4 = new Libelle();
	private Libelle libelle5 = new Libelle();
	private Libelle libelle6 = new Libelle();
	private Libelle libelle7 = new Libelle();
	private List<CodeJour> liCodeJours = new ArrayList<CodeJour>();
	private List<CodeJour> allCodeJours = new ArrayList<CodeJour>();
    private CodeJour selectedCodeJours;
    private boolean headerButtonsDisabled = true;

	@EJB
	GestionCodeJourLocal gestionCodeJourLocal;
	@EJB
	GestionLibelleLocal gestionLibelleLocal;

	@PostConstruct
	private void init() {
		 allCodeJours= gestionCodeJourLocal.listeCodeJours();
		if(allCodeJours!=null && allCodeJours.size()>0){
			try {
				
			
			codeJour1=allCodeJours.get(0);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour1.getIdcj());
				codeJour1.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			codeJour2=allCodeJours.get(1);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour2.getIdcj());
				codeJour2.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			codeJour3=allCodeJours.get(2);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour3.getIdcj());
				codeJour3.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			codeJour4=allCodeJours.get(3);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour4.getIdcj());
				codeJour4.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			codeJour5=allCodeJours.get(4);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour5.getIdcj());
				codeJour5.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			codeJour6=allCodeJours.get(5);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour6.getIdcj());
				codeJour6.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			codeJour7=allCodeJours.get(6);
			try {
				List<Libelle>li= gestionLibelleLocal.findLibelleByCodeJour(codeJour7.getIdcj());
				codeJour7.setLibelles(li);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	
	public List<CodeJour> getAllCodeJours() {
		return allCodeJours;
	}


	public void setAllCodeJours(List<CodeJour> allCodeJours) {
		this.allCodeJours = allCodeJours;
	}


	public void ajouterCodeJour() {
		if (codeJour1.getIdcj() != 1) {
			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle1);
			this.liCodeJours.add(codeJour1);
			codeJour1.setLibelles(libs);
			libelle1.setCodeJour(codeJour1);
			this.liCodeJours.add(codeJour1);
			gestionCodeJourLocal.ajouterCodeJour(codeJour1);
			
		}
		if (codeJour2.getIdcj() != 2) {
			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle2);
			codeJour2.setLibelles(libs);
			libelle2.setCodeJour(codeJour2);
			this.liCodeJours.add(codeJour2);
			gestionCodeJourLocal.ajouterCodeJour(codeJour2);
		}
		if (codeJour3.getIdcj() != 3) {
			
			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle3);
			codeJour3.setLibelles(libs);
			libelle3.setCodeJour(codeJour3);
			this.liCodeJours.add(codeJour3);
			gestionCodeJourLocal.ajouterCodeJour(codeJour3);
		}
		if (codeJour4.getIdcj() != 4) {
			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle4);
			codeJour4.setLibelles(libs);
			libelle4.setCodeJour(codeJour4);
			this.liCodeJours.add(codeJour4);
			gestionCodeJourLocal.ajouterCodeJour(codeJour4);
		}
		if (codeJour5.getIdcj() != 5) {
		
			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle5);
			codeJour5.setLibelles(libs);
			libelle5.setCodeJour(codeJour5);
			this.liCodeJours.add(codeJour5);
			gestionCodeJourLocal.ajouterCodeJour(codeJour5);
		}
		if (codeJour6.getIdcj() != 6) {

			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle6);
			codeJour6.setLibelles(libs);
			libelle6.setCodeJour(codeJour6);
			this.liCodeJours.add(codeJour6);
			gestionCodeJourLocal.ajouterCodeJour(codeJour6);
		}
		if (codeJour7.getIdcj() != 7) {
			
			ArrayList<Libelle> libs=new ArrayList<>();
			libs.add(libelle7);
			codeJour7.setLibelles(libs);
			libelle7.setCodeJour(codeJour7);
			this.liCodeJours.add(codeJour7);
			gestionCodeJourLocal.ajouterCodeJour(codeJour7);
		}
		
		 
		

	}
	public void modifierCodeJour() {
		if (codeJour1.getIdcj() == 1) {
			gestionCodeJourLocal.modifierCodeJour(codeJour1);
		
		}
		if (codeJour2.getIdcj() == 2) {
			
			gestionCodeJourLocal.modifierCodeJour(codeJour2);
			
		}
		if (codeJour3.getIdcj() == 3) {
			
			gestionCodeJourLocal.modifierCodeJour(codeJour3);
		}
		if (codeJour4.getIdcj() == 4) {
			
			gestionCodeJourLocal.modifierCodeJour(codeJour4);
		}
		if (codeJour5.getIdcj() == 5) {
			
			gestionCodeJourLocal.modifierCodeJour(codeJour5);
			
		}
		if (codeJour6.getIdcj() == 6) {
			
			gestionCodeJourLocal.modifierCodeJour(codeJour6);
			
		}
		if (codeJour7.getIdcj() == 7) {
			
			gestionCodeJourLocal.modifierCodeJour(codeJour7);
			
		}

	}


	public String redirect() {

		return "/gtaparam2/Gestionsemainetype.jsf?faces-redirect=true";
	}

	public void listerCodeJour() {
		gestionCodeJourLocal.listeCodeJours();
	}

	public CodeJour getCodeJour1() {
		return codeJour1;
	}

	public void setCodeJour1(CodeJour codeJour1) {
		this.codeJour1 = codeJour1;
	}

	public CodeJour getCodeJour2() {
		return codeJour2;
	}

	public void setCodeJour2(CodeJour codeJour2) {
		this.codeJour2 = codeJour2;
	}

	public CodeJour getCodeJour3() {
		return codeJour3;
	}

	public void setCodeJour3(CodeJour codeJour3) {
		this.codeJour3 = codeJour3;
	}

	public CodeJour getCodeJour4() {
		return codeJour4;
	}

	public void setCodeJour4(CodeJour codeJour4) {
		this.codeJour4 = codeJour4;
	}

	public CodeJour getCodeJour5() {
		return codeJour5;
	}

	public void setCodeJour5(CodeJour codeJour5) {
		this.codeJour5 = codeJour5;
	}

	public CodeJour getCodeJour6() {
		return codeJour6;
	}

	public void setCodeJour6(CodeJour codeJour6) {
		this.codeJour6 = codeJour6;
	}

	public CodeJour getCodeJour7() {
		return codeJour7;
	}

	public void setCodeJour7(CodeJour codeJour7) {
		this.codeJour7 = codeJour7;
	}

	public Libelle getLibelle1() {
		return libelle1;
	}

	public void setLibelle1(Libelle libelle1) {
		this.libelle1 = libelle1;
	}

	public Libelle getLibelle2() {
		return libelle2;
	}

	public void setLibelle2(Libelle libelle2) {
		this.libelle2 = libelle2;
	}

	public Libelle getLibelle3() {
		return libelle3;
	}

	public void setLibelle3(Libelle libelle3) {
		this.libelle3 = libelle3;
	}

	public Libelle getLibelle4() {
		return libelle4;
	}

	public void setLibelle4(Libelle libelle4) {
		this.libelle4 = libelle4;
	}

	public Libelle getLibelle5() {
		return libelle5;
	}

	public void setLibelle5(Libelle libelle5) {
		this.libelle5 = libelle5;
	}

	public Libelle getLibelle6() {
		return libelle6;
	}

	public void setLibelle6(Libelle libelle6) {
		this.libelle6 = libelle6;
	}

	public Libelle getLibelle7() {
		return libelle7;
	}

	public void setLibelle7(Libelle libelle7) {
		this.libelle7 = libelle7;
	}

	public List<CodeJour> getLiCodeJours() {
		return liCodeJours;
	}

	public void setLiCodeJours(List<CodeJour> liCodeJours) {
		this.liCodeJours = liCodeJours;
	}

	public CodeJour getSelectedCodeJours() {
		return selectedCodeJours;
	}

	public void setSelectedCodeJours(CodeJour selectedCodeJours) {
		this.selectedCodeJours = selectedCodeJours;
	}
	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}


}
