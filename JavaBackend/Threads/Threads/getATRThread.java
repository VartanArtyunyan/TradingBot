package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class getATRThread extends  kpiThread{
	int periods;

	
	public getATRThread(String instrument,int periods,String granularity,JsonCandlesRoot jcr) {
		
	super(instrument, granularity, jcr);
	this.periods=periods;
	
	}
	
	
	
	public void run()	{
		ergebnis = kpiCalculator.getATR(instrument, periods, granularity, jcr);
	}
	
	

}
