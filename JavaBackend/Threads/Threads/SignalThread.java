package Threads;

import de.fhws.Softwareprojekt.TestN;

public class SignalThread extends stopableThread{
	
	TestN t;
	String granularity;
	
	
	public SignalThread(TestN t, String granularity) {
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
