import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import API.ApiConnection;
import API.Connection;
import AccountMng.AccountMng;
import de.fhws.Softwareprojekt.Ema;
import de.fhws.Softwareprojekt.JsonInstrumentsInstrument;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.TestN;
import positionen.Verwaltung;

public class Main {

		public static void main(String[] args) {
			Connection con = new Connection();
			ApiConnection connection = new ApiConnection(con);
			Verwaltung verwaltung = new Verwaltung(connection);
			
		
			
			
			TestN testN = new TestN(connection, verwaltung);
			
			MainThread mainThrad = new MainThread(testN);
			
			
	
			
}
