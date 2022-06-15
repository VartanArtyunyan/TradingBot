import java.util.Date;

import API.ApiConnection;
import API.Connection;
import JsonParser.JsonBuilder;
import LogFileWriter.LogFileWriter;
import de.fhws.Softwareprojekt.Signals;
import positionen.Verwaltung;

public class Main {
		
	static String granularity = "M30";
	static double einsatz = 0.02;
		
	public static void main(String[] args) {
	
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		
		
		
		
		Verwaltung verwaltung = new Verwaltung(connection, granularity, einsatz);
		
		verwaltung.startTraiding();
		

	}
}
