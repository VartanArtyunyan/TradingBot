import java.util.Date;

import API.ApiConnection;
import API.Connection;
import JsonParser.JsonBuilder;
import LogFileWriter.LogFileWriter;
import Threads.MainThread;
import Threads.SignalThread;
import Threads.timerThread;
import de.fhws.Softwareprojekt.Signals;
import positionen.Verwaltung;

public class Main {
		
	static String granularity = "M15";
	static double einsatz = 0.02;
		
	public static void main(String[] args) {
	
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		
		Verwaltung verwaltung = new Verwaltung(connection, granularity, einsatz);
		
		verwaltung.startTraiding();
		
		/*
		Signals testN = new Signals(connection, verwaltung, logFileWriter);
		
		MainThread mainThread = new MainThread();
		
		SignalThread signalThread = new SignalThread(testN, granularity);
		timerThread timerThread = new timerThread(testN, granularity);
		
		mainThread.addThread(signalThread);
		mainThread.addThread(timerThread);
		
		mainThread.start();*/

	}
}
