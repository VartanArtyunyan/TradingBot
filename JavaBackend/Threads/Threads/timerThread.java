package Threads;

import de.fhws.Softwareprojekt.Signals;

public class timerThread extends stopableThread {

	Signals t;

	int time;

	public timerThread(Signals t, String granularity) {
		this.t = t;
		time = convertToTime(granularity);
	}

	public void run() {
		while(execute) {
			System.out.println("start timerThread");
		try {
			this.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		t.endPeriod();
		System.out.println("ende timerThread");
	}
	}

	public static int convertToTime(String granularity) {
		int output = 1000 * 60 * Integer.parseInt(granularity.substring(1));

		return output;
	}

}
