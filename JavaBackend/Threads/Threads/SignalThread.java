package Threads;

import de.fhws.Softwareprojekt.Signals;

public class SignalThread extends stopableThread{
	
	Signals t;
	String granularity;
	
	
	public SignalThread(Signals t, String granularity) {
		this.t = t;
		this.granularity = granularity;
	}
	
	public void run() {
		while(execute) {
			System.out.println("start signalThread");
			t.runSignals(granularity);
			System.out.println("ende signalThread");
			}
	}

	
	
	

}
