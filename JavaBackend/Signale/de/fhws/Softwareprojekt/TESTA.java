package de.fhws.Softwareprojekt;

import API.ApiConnection;
import API.Connection;

public class TESTA {
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
		int zaehler;
		JsonInstrumentsRoot instrumentsRoot = e.getInstruments();
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) 
		{
			//if(instrument.type.compareTo("CURRENCY")==0)
		//	{
			Kpi kpi=e.aufrufAlles(instrument.name,200, 14, "M15", 0.02, 0.02, 0.02, 12,26,9, 0, 0);
			//String c="";
		if(pruefeMACD(kpi)!=0)
			{
			ausgabe("alles",kpi,instrument);
			System.out.println(kpi.prozent);
			
			System.out.println(pruefeMACD(kpi));
		}
		}
		
}
public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {
	
	System.out.println(kpi.instrument + " " + instrument.displayName+ " " + instrument.type + " " + emaName + ":  " + kpi.ema +" MCAD: "+kpi.macd+ " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: " +kpi.parabolicSAR+ " RSI: " +kpi.rsi +" ATR: "+ kpi.atr+ " ("
			+ kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: " + kpi.avg + "  " + kpi.firstTime
			+ " - " + kpi.lastTime + ")");
}

public static int pruefeMACD(Kpi werte) {
	// Optionale Prüfung, ob MACD-Trend in den Vorperioden optimal ist
	int zaehlerNegativ=0;
	int zaehlerPositiv=0;
	boolean Negativ=false;
	boolean Positiv=true;
	int rueckgabewert = 99;
	int aktuellerWert=0;
	for (int i = 1; i < 11; i++) {
	
		double macd = werte.macds.get(werte.macds.size() - i);
		double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size() - i);
		double macdVerhaeltnis = macd - trigger;
		if(i==1)
		{
			if(macdVerhaeltnis<0)
				Negativ=true;
		}
		if(macdVerhaeltnis>0)
		{
			Positiv=true;
	}
	
	else
	{
		if (macdVerhaeltnis < 0) {
			zaehlerNegativ++;
		} else if (macdVerhaeltnis > 0) {
			zaehlerPositiv++;
		} else /* macdVerhaeltnis = 0 */ {
			break;
		}
	}
}	// System.out.println(macdVerhaeltnis);
		// wenn das Verhältnis die letzten 5 Perioden das gleiche Vorzeichen haben
		// und dann das Vorzeichen sich ändert, gilt die Bedingung als erfüllt
	
//rueckgabewert=(zaehlerNegativ==5&&Positiv==true&&werte.ema>werte.lastPrice&&werte.lastPrice>werte.parabolicSAR)?-1:(zaehlerPositiv==5&&Negativ==true&&werte.lastPrice>werte.ema&&werte.parabolicSAR>werte.lastPrice)?1:0;
 rueckgabewert=(zaehlerNegativ==9&&Positiv==true)?-1:(zaehlerPositiv==9&&Negativ==true)?1:0;
// rueckgabewert=(rueckgabewert==1&&werte.ema>werte.lastPrice&&werte.lastPrice>werte.parabolicSAR)?-1:(rueckgabewert==1&&werte.lastPrice>werte.ema&&werte.parabolicSAR>werte.lastPrice)?1:0;
	return rueckgabewert;
}
}