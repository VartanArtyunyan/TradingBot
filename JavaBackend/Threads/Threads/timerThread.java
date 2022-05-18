package Threads;

import de.fhws.Softwareprojekt.TestN;

public class timerThread extends Thread {
	
	TestN t;
	
	int time;
	boolean execute;
	
	public timerThread(TestN t, int time) {
		this.t = t;
	}
	
	public void run() {
		try {
			this.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t.endPeriod();
	}

}
