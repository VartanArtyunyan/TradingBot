import java.util.Date;

import API.ApiConnection;
import API.Connection;
import ConfigFileReader.ConfigFileReader;
import JsonParser.JsonBuilder;
import LogFileWriter.LogFileWriter;
import de.fhws.Softwareprojekt.Signals;
import positionen.Verwaltung;

public class Main {

	static String granularity = "M30";
	static double einsatz = 0.01;

	public static void main(String[] args) {

		ConfigFileReader configFile = new ConfigFileReader("../config.txt");
		


		if (configFile.isValid()) {
			Connection con = new Connection();
			ApiConnection connection = new ApiConnection(con);
			
			ApiConnection randomConnection;
			
			boolean aktivateRandomTrader = Boolean.parseBoolean(configFile.get("random"));
		
			if(aktivateRandomTrader) {
				Connection randomCon = new Connection(configFile.get("randomAccountToken"));
				randomConnection = new ApiConnection(randomCon);
			}else {
				randomConnection = new ApiConnection(con);
			}

			Verwaltung verwaltung = new Verwaltung(connection, randomConnection, granularity, einsatz);

			verwaltung.startTraiding();
		} else {
			System.out.println("Config.txt ist unvollständig oder nicht richtig formatiert!");
			System.out.println("Bitte vervollständige die config.txt oder lösche sie, damit eine neue Konfigurationsdatei generiert wird\n\n");
			System.out.println("Richtiges Format:\n"
					+ "signal1 = (true/false){wenn true wird Signal1 beim signalbasierten Trading beachtet}\r\n"
					+ "signal2 = (true/false){wenn true wird Signal2 beim signalbasierten Trading beachtet}\r\n"
					+ "signal3 = (true/false){wenn true wird Signal3 beim signalbasierten Trading beachtet}\r\n"
					+ "eventBased = (true/false){true aktiviert das eventbasierte Trading\r\n"
					+ "random = (true/false){true aktiviert das Random-Trading(für das Random-Trading muss ein extra Account angegeben werden)}\r\n"
					+ "randomAccountToken = {token für den account der für das Random-Trading genutzt werden soll (nur relevant wenn random = true)}");
			System.out.println("\n\nDrücke Enter um das Programm zu beenden...");
			try{System.in.read();}
			catch(Exception e){}

		}
		
	}
}
