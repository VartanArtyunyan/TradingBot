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
		
	public static void main(String[] args) {
	
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		
		LogFileWriter logFileWriter = new LogFileWriter();
		Verwaltung verwaltung = new Verwaltung(connection, granularity);
		
		//connection.placeLimitOrder("TRY_JPY", 30.1111111, 22.1111111, 44.1111111, 55.1111111);
		//                        LimitPreis, units, takeProfit, stopLoss
		
		verwaltung.startTraiding();
		
		Signals testN = new Signals(connection, verwaltung, logFileWriter);
		
		MainThread mainThread = new MainThread();
		
		SignalThread signalThread = new SignalThread(testN, granularity);
		timerThread timerThread = new timerThread(testN, granularity);
		
		mainThread.addThread(signalThread);
		mainThread.addThread(timerThread);
		
		//mainThread.start();

	}
}
