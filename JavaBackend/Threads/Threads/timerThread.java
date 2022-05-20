package Threads;

import de.fhws.Softwareprojekt.TestN;

public class timerThread extends stopableThread {

	TestN t;

	int time;

	public timerThread(TestN t, String granularity) {
		this.t = t;
		time = convertToTime(granularity);
	}

	public void run() {
		while(execute) {
		try {
			this.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		t.endPeriod();
	}
	}

	public static int convertToTime(String granularity) {
		int output = 1000 * 60 * Integer.parseInt(granularity.substring(1));

		return output;
	}

}
