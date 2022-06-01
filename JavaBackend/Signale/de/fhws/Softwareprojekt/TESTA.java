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
	
			if(instrument.type.compareTo("CURRENCY")==0)
			{
				Kpi kpi=e.aufrufAlles(instrument.name,200, 14, "M15", 0.02, 0.02, 0.02, 12,26,9, 0, 0);
			//String c="";
	//	if((pruefeVorperioden(kpi, "MACD")!=0)&&((kpi.prozent>0.15)||(kpi.prozent<-0.25)))
			{
				System.out.println(kpi.instrument);
				System.out.println(kpi.atr);
				//System.out.println("macdTriggert " + kpi.atr +" "+kpi.macds.get(kpi.macds.size()-1));
		//	System.out.println("macd " +kpi.macd);
	//		System.out.println("OK");
			
			ausgabe("alles",kpi,instrument);
 			System.out.println(kpi.prozent);
		
			//System.out.println(pruefeVorperioden(kpi,"MACD"));
			}
		}
		}
		/*	for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) 
				
			{
				Kpi kpi=e.aufrufAlles(instrument.name,200, 14, "M1", 0.02, 0.02, 0.02, 12,26,9, 0, 0);
			if(kpi.prozent>0.2)
			{
				ausgabe("alles",kpi,instrument);
				System.out.println(kpi.prozent+"Ja");
			}
			else
				System.out.println("Nein");
		}
		*/
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
}
	// System.out.println(macdVerhaeltnis);
		// wenn das Verhältnis die letzten 5 Perioden das gleiche Vorzeichen haben
		// und dann das Vorzeichen sich ändert, gilt die Bedingung als erfüllt
	
//rueckgabewert=(zaehlerNegativ==5&&Positiv==true&&werte.ema>werte.lastPrice&&werte.lastPrice>werte.parabolicSAR)?-1:(zaehlerPositiv==5&&Negativ==true&&werte.lastPrice>werte.ema&&werte.parabolicSAR>werte.lastPrice)?1:0;
 rueckgabewert=(zaehlerNegativ==9&&Positiv==true)?-1:(zaehlerPositiv==9&&Negativ==true)?1:0;
// rueckgabewert=(rueckgabewert==1&&werte.ema>werte.lastPrice&&werte.lastPrice>werte.parabolicSAR)?-1:(rueckgabewert==1&&werte.lastPrice>werte.ema&&werte.parabolicSAR>werte.lastPrice)?1:0;
	return rueckgabewert;
}
public static int pruefeVorperioden(Kpi werte, String entscheideSignal) {
	// Die Methode, soll die Vorperiode prüfen, ob bestimmte Ereignisse vorgefallen
	// sind oder nicht
	// Dabei werden die Methoden pruefeMACD() und pruefeRSI() zusammengelegt
	int ausgabe = 99;
	int MACDRueckgabewert = 99;
	int RSIRueckgabewert = 99;
	boolean verhaeltnisVorzeichenNegativ = false;
	boolean verhaeltnisVorzeichenPositiv = false;
	boolean RSIOverbought = false; // RSI über 70%
	boolean RSIOversold = false; // RSI unter 30%

	for (int i = 2; i < 7; i++) {
		double macd = werte.macds.get(werte.macds.size() - i);
		double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size() - i);
		double macdVerhaeltnis = macd - trigger;
		//System.out.println(werte.rsiListe.get(werte.rsiListe.size() - i));
		if (macdVerhaeltnis < 0) {
			verhaeltnisVorzeichenNegativ = true;
		} else if (macdVerhaeltnis > 0) {
			verhaeltnisVorzeichenPositiv = true;
		} else { // macdVerhaeltnis == 0
			break;
		}
		if (werte.rsiListe.get(werte.rsiListe.size() - i) > 70) {
			RSIOverbought = true;
		} else if (werte.rsiListe.get(werte.rsiListe.size() - i) < 30) {
			RSIOversold = true;
		} else { // "70 >= werte.rsiListe.get(werte.rsiListe.size()-i) >=30"
			break;
		}
	}
	if (verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == false) {
		// die letzten 5 MACDs sind negativ
		MACDRueckgabewert = -1;
	} else if (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == true) {
		// die letzten 5 MACDs sind positiv oder
		MACDRueckgabewert = 1;
	} else if ((verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == true)
			|| (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == false)) {
		// die letzten 5 MACDs haben nicht das gleiche Vorzeichen
		MACDRueckgabewert = 0;
	}
	if (RSIOversold == true && RSIOverbought == false) {
		// die letzten 5 RSIs sind Oversold, also unter 30%
		RSIRueckgabewert = -1;
	} else if (RSIOversold == false && RSIOverbought == true) {
		// die letzten 5 RSIs sind Overbought, also über 70%
		RSIRueckgabewert = 1;
	} else if ((RSIOversold == true && RSIOverbought == true) || (RSIOversold == false && RSIOverbought == false)) {
		// die letzten 5 RSIs schwanken oder liegen alle zwischen 30 und 70 Prozent
		RSIRueckgabewert = 0;
	}
	if (entscheideSignal == "MACD") {
		ausgabe = MACDRueckgabewert;
	} else if (entscheideSignal == "RSI") {
		ausgabe = RSIRueckgabewert;
	} else if (entscheideSignal != "MACD" && entscheideSignal != "RSI") {
		// keine Änderung von ausgabe
		ausgabe = 99;
	}
	return ausgabe;
}
}