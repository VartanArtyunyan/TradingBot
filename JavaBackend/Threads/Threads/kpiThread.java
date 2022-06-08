package Threads;

import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;

public class kpiThread extends Thread{
	KpiCalculator kpiCalculator;
	Kpi ergebnis;
	
	public kpiThread() {
		kpiCalculator = new KpiCalculator(null);
	}
	
	
	public Kpi getErgebnis() {
		return ergebnis;
	}

}
