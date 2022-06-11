package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class GetRSIThread extends  KpiThread{
	int periods;
	
	
	public GetRSIThread(String instrument,int periods,String granularity,JsonCandlesRoot jcr) {
		
	super(instrument, granularity, jcr);
	this.periods=periods;

	}
	
	
	
	
	public void run() {
		ergebnis = kpiCalculator.getRSI(instrument, periods, granularity, jcr);
	}
	

	
	
}
