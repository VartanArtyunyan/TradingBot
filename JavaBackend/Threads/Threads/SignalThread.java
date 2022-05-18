package Threads;

import de.fhws.Softwareprojekt.TestN;

public class SignalThread extends Thread {
	
	TestN t;
	
	boolean execute;
	
	public SignalThread(TestN t) {
		this.t = t;
	}
	
	public void run() {
		while(execute) {
			t.runSignals();
			}
	}

}
