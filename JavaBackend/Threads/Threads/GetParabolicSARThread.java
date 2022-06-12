package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class GetParabolicSARThread extends  KpiThread{
	int periods;
	double tempBF;
	double inkrementBF;
	double maxBF;
	
	
	public GetParabolicSARThread(String instrument, String granularity, int periods,
			double tempBF, double inkrementBF, double maxBF, JsonCandlesRoot jcr) {
	
		
		super(instrument, granularity, jcr);
		this.periods = periods;
		this.tempBF = tempBF;
		this.inkrementBF = inkrementBF;
		this.maxBF = maxBF;
		
	}
	
	
	
	public void run() {
		ergebnis = kpiCalculator.getParabolicSAR(instrument, granularity, periods, tempBF, inkrementBF, maxBF, jcr);
	}
	
	

}
