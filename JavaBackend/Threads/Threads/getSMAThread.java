package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class getSMAThread extends  kpiThread {
	int periods;


	public getSMAThread(String instrument, int periods,String granularity,JsonCandlesRoot jcr) {
	super(instrument, granularity, jcr);
		this.periods = periods;
	
	}

	
	
	public void run() {
		ergebnis = kpiCalculator.getSMA(instrument, periods, granularity, jcr);
	}

}
