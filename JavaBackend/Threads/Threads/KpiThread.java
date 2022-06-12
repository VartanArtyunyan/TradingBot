package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;

public class KpiThread extends Thread{
	KpiCalculator kpiCalculator;
	Kpi ergebnis;
	String instrument;
	String granularity;
	JsonCandlesRoot jcr;
	
	public KpiThread(String instrument,String granularity,JsonCandlesRoot jcr) {
		kpiCalculator = new KpiCalculator(null);
		this.instrument = instrument;
		this.granularity = granularity;
		this.jcr = jcr;
	}
	
	
	public Kpi getErgebnis() {
		return ergebnis;
	}

}
