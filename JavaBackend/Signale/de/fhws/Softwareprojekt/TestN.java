package de.fhws.Softwareprojekt;

import API.ApiConnection;
import API.Connection;

public class TestN {
public static void main(String[] args) {
		
		
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		
		
		/*System.out.println("Instrumentliste - Suchbegriff:");
		String filter;
		try (Scanner scanner = new Scanner(System.in);) {
			filter = scanner.nextLine().toUpperCase();
		} catch (Exception e) {
			filter = "";
		}
		;*/
	//	kombiniereMACDEMAPSAR(connection,"USD_JPY", 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
		Ema e = new Ema(connection);
		
		JsonInstrumentsRoot instrumentsRoot = e.getInstruments();
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) 
		{
			if(instrument.type.compareTo("CURRENCY")==0)
			{
			Kpi kpi=e.aufrufAlles(instrument.name,200, 14, "M15", 0.02, 0.02, 0.02, 12,26,9, 0, 0);
			//String c="";
			ausgabe("alles",kpi,instrument);
			}
		}
		
}
public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {
	
	System.out.println(kpi.instrument + " " + instrument.displayName+ " " + instrument.type + " " + emaName + ":  " + kpi.ema +" MCAD: "+kpi.macd+ " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: " +kpi.parabolicSAR+ " RSI: " +kpi.rsi +" ATR: "+ kpi.atr+ " ("
			+ kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: " + kpi.avg + "  " + kpi.firstTime
			+ " - " + kpi.lastTime + ")");
}

}