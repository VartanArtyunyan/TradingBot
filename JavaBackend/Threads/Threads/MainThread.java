package Threads;

import java.util.ArrayList;
import java.util.Scanner;

import de.fhws.Softwareprojekt.KpiCalculator;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.Signals;

public class MainThread extends Thread {



	ArrayList<stopableThread> threads = new ArrayList<>();

	boolean execute;

	public MainThread() {

	
	}

	public void addThread(stopableThread srt) {
		threads.add(srt);
	}

	public void run() {

		for (stopableThread st : threads) {
		st.start();
		}
		

		Scanner scanner = new Scanner(System.in);
		String input = "";

		boolean run = true;
		while (run) {

			input = scanner.nextLine();
			input = input.toLowerCase();

			if (input.equals("stop")) {
				run = false;
				stopThreads();

			} else {
				System.out.println("ungültige eingabe");
			}

		}

	}
	
	public void stopThreads() {
		for (stopableThread st : threads) {
			st.stopThread();
		}
	}

	

}
