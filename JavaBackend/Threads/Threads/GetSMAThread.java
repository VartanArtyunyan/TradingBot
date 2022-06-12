package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class GetSMAThread extends  KpiThread {
	int periods;


	public GetSMAThread(String instrument, int periods,String granularity,JsonCandlesRoot jcr) {
	super(instrument, granularity, jcr);
		this.periods = periods;
	
	}

	
	
	public void run() {
		ergebnis = kpiCalculator.getSMA(instrument, periods, granularity, jcr);
	}

}
