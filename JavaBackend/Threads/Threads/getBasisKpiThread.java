package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class getBasisKpiThread extends kpiThread {
	int emaperiods;

	
public getBasisKpiThread(String instrument,int emaperiods, String granularity, JsonCandlesRoot jcr ) {
		super(instrument, granularity, jcr);
		this.emaperiods=emaperiods;
		// TODO Auto-generated constructor stub
	}



public void run()
{
	ergebnis=kpiCalculator.getBasisKpi(instrument, emaperiods, granularity, jcr);
}

}
