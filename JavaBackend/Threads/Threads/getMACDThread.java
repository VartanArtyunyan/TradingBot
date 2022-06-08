package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;

public class getMACDThread extends  kpiThread{
	
	
	
	String instrument;
	String granularity;
	int x;
	int y;
	int z;
	JsonCandlesRoot jcr;
	public getMACDThread(String instrument, String granularity, int x, int y, int z, JsonCandlesRoot jcr) {
		
		this.instrument = instrument;
		this.granularity = granularity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.jcr = jcr;
	}
	
	public void run() {
		ergebnis = kpiCalculator.getMACD(instrument, granularity, x, y, z, jcr);
	}
	
	

}
