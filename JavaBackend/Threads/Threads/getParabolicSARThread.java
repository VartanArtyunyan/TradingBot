package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;

public class getParabolicSARThread extends  kpiThread{
	
	
	String instrument;
	String granularity;
	int periods;
	double tempBF;
	double inkrementBF;
	double maxBF;
	JsonCandlesRoot jcr;
	
	
	
	public getParabolicSARThread(String instrument, String granularity, int periods,
			double tempBF, double inkrementBF, double maxBF, JsonCandlesRoot jcr) {
	
		
		this.instrument = instrument;
		this.granularity = granularity;
		this.periods = periods;
		this.tempBF = tempBF;
		this.inkrementBF = inkrementBF;
		this.maxBF = maxBF;
		this.jcr = jcr;
	}
	
	public void run() {
		ergebnis = kpiCalculator.getParabolicSAR(instrument, granularity, periods, tempBF, inkrementBF, maxBF, jcr);
	}
	
	

}
