package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class GetBasisKpiThread extends KpiThread {
	int emaperiods;

	
public GetBasisKpiThread(String instrument,int emaperiods, String granularity, JsonCandlesRoot jcr ) {
		super(instrument, granularity, jcr);
		this.emaperiods=emaperiods;
		// TODO Auto-generated constructor stub
	}



public void run()
{
	ergebnis=kpiCalculator.getBasisKpi(instrument, emaperiods, granularity, jcr);
}

}
