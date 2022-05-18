package Threads;

import de.fhws.Softwareprojekt.Ema;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.TestN;

public class MainThread extends Thread{
	
	
	TestN t;
	int granularity;
	
	SignalThread signalThread;
	timerThread timerThread;
	
	boolean execute;
	
	
	
	public MainThread(TestN t, String granularity) {
		
		this.t = t;
		
		signalThread = new SignalThread(t);
		timerThread = new timerThread(t,convertToTime(granularity));
		
		
		
	}
	
	public void run() {
		
		signalThread.run();
		timerThread.run();
			
	}
	
	public static int convertToTime(String granularity) {
		int output = Integer.parseInt(granularity.substring(1));
		
		return output;
	}
	

}
