package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;

public class getATRThread extends  kpiThread{
	
	
	String instrument;
	int periods;
	String granularity;
	JsonCandlesRoot jcr;

	
	public getATRThread(String instrument,int periods,String granularity,JsonCandlesRoot jcr) {
		
	this.instrument=instrument;
	this.periods=periods;
	this.granularity=granularity;
	this.jcr=jcr;
	}
	
	public void run()	{
		ergebnis = kpiCalculator.getATR(instrument, periods, granularity, jcr);
	}
	
	

}
