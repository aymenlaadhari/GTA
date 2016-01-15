package managedBeans.gtaparam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.yesserp.domain.gta.Terminal;
import com.yesserp.sessionbean.paramgta.gestionterminalpointage.GestionTerminalPointageLocal;

@ManagedBean
@ViewScoped
public class TerminalPointageBean {
	private Terminal terminal = new Terminal();
	private Terminal selectedTerminal = new Terminal();
	private List<Terminal> terminals = new ArrayList<Terminal>();
	private boolean headerButtonsDisabled = true;

	@EJB
	GestionTerminalPointageLocal gestionTerminalPointageLocal;

	@PostConstruct
	public void init() {
		try {
			this.setTerminals(gestionTerminalPointageLocal.findAll());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void onRowSelectDataTable() {
		this.setHeaderButtonsDisabled(false);
	}
	
	public void ajouterTerminal() {
		try {
			gestionTerminalPointageLocal.ajouterTerminalPointage(terminal);
			terminals.add(terminal);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, terminal
							.getCode(), "<" + terminal.getLibelle()
							+ "> a été ajouté avec succès."));

			terminal = new Terminal();
		} catch (Exception exception) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erreur d'ajout! " + terminal.getCode(),
									"n'a pas pu être ajouté car elle est déjà définie par ce code."));
		}
	}

	public void modifierTypePointage() {
		try {
			gestionTerminalPointageLocal
					.modifierTerminalPointage(selectedTerminal);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, terminal
							.getCode(), "<" + selectedTerminal.getLibelle()
							+ "> a été modifié avec succès."));
		} catch (Exception exception) {
			exception.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de modification! "
									+ selectedTerminal.getCode(),
							"n'a pas pu être modifié."));
		}
	}

	public void supprimerTypePointage() {
		try {
			gestionTerminalPointageLocal
					.supprimerTerminalPointage(selectedTerminal);
			this.setHeaderButtonsDisabled(true);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							selectedTerminal.getCode(), "<"
									+ selectedTerminal.getLibelle()
									+ "> a été supprimé avec succès."));
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erreur de suppression! "
									+ selectedTerminal.getCode(),
							" n'a pas pu être supprimé."));
		}
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public Terminal getSelectedTerminal() {
		return selectedTerminal;
	}

	public void setSelectedTerminal(Terminal selectedTerminal) {
		this.selectedTerminal = selectedTerminal;
	}

	public List<Terminal> getTerminals() {
		return terminals;
	}

	public void setTerminals(List<Terminal> terminals) {
		this.terminals = terminals;
	}

	public boolean isHeaderButtonsDisabled() {
		return headerButtonsDisabled;
	}

	public void setHeaderButtonsDisabled(boolean headerButtonsDisabled) {
		this.headerButtonsDisabled = headerButtonsDisabled;
	}
}
