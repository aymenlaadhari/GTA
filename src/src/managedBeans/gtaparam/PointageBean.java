/*package managedBeans.gtaparam;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import com.yesserp.domain.access.Groupe;
import com.yesserp.domain.gta.Pointage;


@ManagedBean
@ViewScoped
public class PointageBean implements Serializable {
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1L;

//	@EJB
//	private PointageServiceRemote pointageServiceRemote;

	private Pointage pointage = new Pointage();

	private List<Pointage> pointages;
	
	private List<Pointage> pointagespardate;
	
	private List<SelectItem> filterOption;
	private List<Pointage> filtredPointages;

	public PointageBean() {
		super();
	}

	@PostConstruct
	public void init()
	{
		//pointages = pointageServiceRemote.listPointage();
	}
	
//	public void addPointage() {
//		pointageServiceRemote.updatePointage(pointage);
//
//		pointages = pointageServiceRemote.listPointage();
//
//		pointage = new Pointage();
//
//	}
//
//	public void deletePointage() {
//		pointageServiceRemote.deletePointage(pointage);
//
//		pointages = pointageServiceRemote.listPointage();
//
//		pointage = new Pointage();
//	}
	
	public void cancel()
	{
		pointage = new Pointage();
	}

	public Pointage getPointage() {
		return pointage;
	}

	public void setPointage(Pointage pointage) {
		this.pointage = pointage;
	}

	public List<Pointage> getPointages() {
		return pointages;
	}

	public void setPointages(List<Pointage> pointages) {
		this.pointages = pointages;
	}

	public List<SelectItem> getFilterOption() {
		return filterOption;
	}

	public void setFilterOption(List<SelectItem> filterOption) {
		this.filterOption = filterOption;
	}

	public List<Pointage> getFiltredPointages() {
		return filtredPointages;
	}

	public void setFiltredPointages(List<Pointage> filtredPointages) {
		this.filtredPointages = filtredPointages;
	}

	public List<Pointage> getPointagespardate() {
		return pointagespardate;
	}

	public void setPointagespardate(List<Pointage> pointagespardate) {
		this.pointagespardate = pointagespardate;
	}
}
*/