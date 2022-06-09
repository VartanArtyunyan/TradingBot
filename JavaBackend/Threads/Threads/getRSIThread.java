package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class getRSIThread extends  kpiThread{
	int periods;
	
	
	public getRSIThread(String instrument,int periods,String granularity,JsonCandlesRoot jcr) {
		
	super(instrument, granularity, jcr);
	this.periods=periods;

	}
	
	
	
	
	public void run() {
		ergebnis = kpiCalculator.getRSI(instrument, periods, granularity, jcr);
	}
	

	
	
}
