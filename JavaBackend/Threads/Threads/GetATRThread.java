package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class GetATRThread extends  KpiThread{
	int periods;

	
	public GetATRThread(String instrument,int periods,String granularity,JsonCandlesRoot jcr) {
		
	super(instrument, granularity, jcr);
	this.periods=periods;
	
	}
	
	
	
	public void run()	{
		ergebnis = kpiCalculator.getATR(instrument, periods, granularity, jcr);
	}
	
	

}
