import de.fhws.Softwareprojekt.Ema;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.TestN;

public class MainThread extends Thread{
	
	
	TestN t;
	int granularity;
	
	Thread signalThread;
	Thread timerThread;
	
	boolean execute;
	
	
	
	public MainThread(TestN t, String granularity) {
		
		this.t = t;
		
		this.signalThread = new Thread() {
			public void run() {
				while(execute) {
					t.runSignals();
					}
			}
		};
		
		this.timerThread = new Thread() {
			
			int granularity;
			
			public Thread(int granularity) {
				this.granularity = granularity;
			}
			
			public void run() {
				this.sleep();
			}
		};
		
	}
	
	public void run() {
		
		
			
	}
	

}
