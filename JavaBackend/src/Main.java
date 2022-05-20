import API.ApiConnection;
import API.Connection;
import Threads.MainThread;
import Threads.SignalThread;
import Threads.timerThread;
import de.fhws.Softwareprojekt.TestN;
import positionen.Verwaltung;

public class Main {

	static String granularity = "M15";

	public static void main(String[] args) {
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		Verwaltung verwaltung = new Verwaltung(connection);

		TestN testN = new TestN(connection, verwaltung);

		MainThread mainThread = new MainThread();

		SignalThread signalThread = new SignalThread(testN, granularity);
		timerThread timerThread = new timerThread(testN, granularity);
		
		mainThread.addThread(signalThread);
		mainThread.addThread(timerThread);
		
		mainThread.start();

	}
}
