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
		Verwaltung verwaltung = new Verwaltung(connection);
		
		//verwaltung.placeShortOrder("TRY_JPY", 30, 22, 44, 55);
		
		Signals testN = new Signals(connection, verwaltung, logFileWriter);

		MainThread mainThread = new MainThread();

		SignalThread signalThread = new SignalThread(testN, granularity);
		timerThread timerThread = new timerThread(testN, granularity);
		
		mainThread.addThread(signalThread);
		mainThread.addThread(timerThread);
		
		mainThread.start();

	}
}
