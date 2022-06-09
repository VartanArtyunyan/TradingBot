package Threads;

import de.fhws.Softwareprojekt.JsonCandlesRoot;

public class getMACDThread extends  kpiThread{
	int x;
	int y;
	int z;
	
	public getMACDThread(String instrument, String granularity, int x, int y, int z, JsonCandlesRoot jcr) {
		
		super(instrument, granularity, jcr);
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	
	
	public void run() {
		ergebnis = kpiCalculator.getMACD(instrument, granularity, x, y, z, jcr);
	}
	
	

}
