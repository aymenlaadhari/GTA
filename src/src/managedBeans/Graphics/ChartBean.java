package managedBeans.Graphics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.PieChartModel;

import com.yesserp.domain.gta.Pointage;
import com.yesserp.domain.gta.TypePointage;
import com.yesserp.sessionbean.gta.gestionpointage.GestionPointageLocal;

@ManagedBean
@SessionScoped
public class ChartBean implements Serializable {

	private List<TypePointage> collectionSens;
	private PieChartModel pieModel;
	@EJB
	private GestionPointageLocal gestionPointageLocal;
	private List<Pointage> pointages = new ArrayList<Pointage>();

	public ChartBean() {

	}

	public void itemSelect(ItemSelectEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Item selected", "Item Index: " + event.getItemIndex()
						+ ", Series Index:" + event.getSeriesIndex());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<TypePointage> getCollectionSens() {

		collectionSens = gestionPointageLocal.ChercherSensPointage();
		return collectionSens;
	}

	public void setCollectionSens(List<TypePointage> collectionSens) {
		this.collectionSens = collectionSens;
	}

	public PieChartModel getPieModel() {
		pieModel = new PieChartModel();
		pointages = gestionPointageLocal.listPointage();
		collectionSens = gestionPointageLocal.ChercherSensPointage();
		Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
		// le "key" est la valeur sur laquelle on va faire nos statistiques et
		// "value" est le nombre d'occurence de la valeur
		for (int i = 0; i < collectionSens.size(); i++) {
			circle1.put(collectionSens.get(i).getLibelle(),
					nbreOccurence(collectionSens.get(i).getId()));
		}
		
		pieModel.setData(circle1);
		return pieModel;
	}

	private int nbreOccurence(Long sensCherche) {
		int nbOccurence = 0;
	
		for (int i = 0; i < pointages.size(); i++) {
			
			if (pointages.get(i).getSens().getId() == sensCherche)
				nbOccurence++;
		}

		return nbOccurence;

	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}
}