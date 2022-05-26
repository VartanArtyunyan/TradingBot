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
		
		JsonBuilder jb = new JsonBuilder();
		
		jb.addString("type", "LIMIT");
		jb.addString("instrument", "EUR_USD");
		jb.addString("units", "10");
		jb.addString("price", "22");
		jb.addString("TimeInForce", "GTC");
		jb.addString("positionFill", "OPEN_ONLY");
		jb.openObject("takeProfitOnFill");
		jb.addString("price", "30");
		jb.closeObject();
		jb.openObject("stopLossOnFill");
		jb.addString("price", "30");
		jb.closeObject();
		
		System.out.println(jb.build());
		

		Signals testN = new Signals(connection, verwaltung, logFileWriter);

		MainThread mainThread = new MainThread();

		SignalThread signalThread = new SignalThread(testN, granularity);
		timerThread timerThread = new timerThread(testN, granularity);
		
		mainThread.addThread(signalThread);
		mainThread.addThread(timerThread);
		
		//mainThread.start();

	}
}
