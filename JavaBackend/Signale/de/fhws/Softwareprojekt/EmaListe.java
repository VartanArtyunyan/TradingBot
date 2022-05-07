package de.fhws.Softwareprojekt;

import java.util.Scanner;

import API.ApiConnection;
import API.Connection;

public class EmaListe {
	
	
	
	public static void main(String[] args) {
		
		
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		
		
		System.out.println("Instrumentliste - Suchbegriff:");
		String filter;
		try (Scanner scanner = new Scanner(System.in);) {
			filter = scanner.nextLine().toUpperCase();
		} catch (Exception e) {
			filter = "";
		}
		;

		Ema e = new Ema(connection);
		
		
		
		
		JsonInstrumentsRoot instrumentsRoot = e.getInstruments();
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) {
			/*if (instrument.type.compareTo("CURRENCY")==0) {
			if (instrument.name.toUpperCase().contains(filter) || instrument.displayName.toUpperCase().contains(filter)
					|| instrument.type.toUpperCase().contains(filter)) {*/
			//	Kpi kpi=e.getKpi(instrument.name, 14, "M15");
				//kpi=e.getATR(instrument.name,14 , "M15");
				//Kpi kpi=e.aufrufAlles(instrument.name,200, 14, "M15", 0.02, 0.02, 0.02, 12,26,9);
				//String c="";
				//ausgabe("alles",kpi,instrument);
			//ausgabe("EMA200d", e.getKpi(instrument.name, 200, "M15"),instrument);
	//ausgabe("EMA3d", e.getKpi(instrument.name, 3, "M15"),instrument);
			//ausgabe("EMA200d", e.getKpi(instrument.name, 200, "M15"), instrument);
		//	ausgabe("EMA200d",e.getMACD(instrument.name, "D"),instrument);
//		ausgabe("EMA25d", e.getEma(instrument.name, 25, "D"),instrument);
		//ausgabe("EMA200h", e.getEma(instrument.name, 200, "H1"),instrument);
		//ausgabe("EMA200h", e.getKpi("EUR_USD", 200, "H1"),instrument);
//		ausgabe("EMA25h", e.getEma(instrument.name, 25, "H1"),instrument);
		//ausgabe("EMA200M15", e.getKpi(instrument.name, 200, "M15"),instrument);
	//	ausgabe("EMA25M15", e.getMACD(instrument.name,  "M15",12,26,9),instrument);
//ausgabe("EMA25M15", e.parabolicSar(instrument.name, "M15",14, 0.02, 0.02, 0.2),instrument);
		//		System.out.println(e.getATR("EUR_USD",14,"M15"));
	//	ausgabe("RSI", e.getRSI(instrument.name, 14, "M15"),instrument);
		//	ausgabe("ATR",e.getATR(instrument.name, 14, "M15"),instrument);
		//Kpi kpi2=e.parabolicSar(instrument.name, "M15", 0.02, 0.02, 0.2);
	//System.out.println(e.getATR(instrument.name, 14, "M15"));
				//neu22222
				
				
				ausgabe("Test", kombiniereMACDEMAPSAR(connection), instrument);
			}
		//}}
		
		
		
	}

	public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {
		
		System.out.println(kpi.instrument + " " + instrument.displayName+ " " + instrument.type + " " + emaName + ":  " + kpi.ema +" MCAD: "+kpi.macd+ " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: " +kpi.parabolicSAR+ " RSI: " +kpi.rsi +" ATR: "+ kpi.atr+ " ("
				+ kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: " + kpi.avg + "  " + kpi.firstTime
				+ " - " + kpi.lastTime + ")");
	}
	
	public static Kpi kombiniereMACDEMAPSAR(ApiConnection connection) {
		// x = kurze Periode , y = lange Periode , z = Signallänge ; (Standardwerte: 12,26,9)
		/* 	
		
		a. Long Position == Kauf
			i. Candlestick-Daten sind über der EMA200 Linie
			ii. MACD-Line kreuzt Signallinie (davor ist MACD-Linie unter Signallinie)
			iii. PSAR ist unter der Candlestick
			iv. Long Position -> Stoploss = PSAR-Wert
			v. Profitziel -> 1:1 zu Stoploss
		b. Short Position == Verkauf
			i. Candlestick-Daten sind unter der EMA200 Linie
			ii. MACD-Line kreuzt Signallinie (davor ist MACD-Linie über Signallinie)
			iii. PSAR ist über der Candlestick
			iv. Stoploss = PSAR-Wert
			v. Profitziel -> 1:1 zu Stoploss
		c. Kombiniert
			i. EMA200 Linie auswerten
			ii. Candlestick-Daten auswerten und abgleichen wo sie zur EMA200 Linie steht
			iii. Entscheiden ob Long oder Short
			
			*/
	
		//Connection con = new Connection();
		//ApiConnection connection = new ApiConnection(con);
		Ema e = new Ema(connection);
		
		Kpi werte = e.aufrufAlles("EUR_USD", 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
		boolean kaufentscheidung = false;
		JsonCandlesRoot x = werte.root;

		//System.out.println(werte.lastPrice);
		//System.out.println(werte.parabolicSAR);
		//System.out.println(werte.ema);
		//System.out.println(werte.macd);
		//System.out.println(werte.macdTrigger);
		//System.out.println(pruefeMACD(werte));
		//System.out.println(pruefeEMA200(werte));
		//System.out.println(pruefePSAR(werte));
		
		//ToDo: Abgleichen der Werte von EMA200, MACD und PSAR
		//		Ermitteln welche Rückgabewerte zu einer Kaufentscheidung führt
		//		Kaufposition aufrufen

		
		return werte;

	}
	public static int pruefeMACD(Kpi werte) {
		// Optionale Prüfung, ob MACD-Trend in den Vorperioden optimal ist
				
		boolean verhaeltnisVorzeichenNegativ = false;
		boolean verhaeltnisVorzeichenPositiv = false;
		int rueckgabewert = 99;
		for (int i = 1; i<6; i++) {
			double macd = werte.macds.get(werte.macds.size()-i);
			double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size()-i);
			double macdVerhaeltnis = macd-trigger;
			if(macdVerhaeltnis < 0) {
				verhaeltnisVorzeichenNegativ = true;
				//verhaeltnisVorzeichenPositiv = false;
			}
			else if (macdVerhaeltnis > 0) {
				//verhaeltnisVorzeichenNegativ = false;
				verhaeltnisVorzeichenPositiv = true;
			}
			else /*macdVerhaeltnis = 0*/ {
				break;
			}
			
			System.out.println(macdVerhaeltnis);
			//wenn das Verhältnis die letzten 5 Perioden das gleiche Vorzeichen haben
			//und dann das Vorzeichen sich ändert, gilt die Bedingung als erfüllt
		}
		if(verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == false) {
			//die letzten 5 MACDs sind negativ
			rueckgabewert = -1;
		}
		else if (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == true) {
			//die letzten 5 MACDs sind positiv
			rueckgabewert = 1;
		}
		else if ((verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == true) || (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == false)){
			//die letzten 5 MACDs haben nicht das gleiche Vorzeichen
			rueckgabewert = 0;
		}
		return rueckgabewert;
	}
	
	public static int pruefeEMA200(Kpi werte) {
		//Prüfe, ob der aktuelle Preis unter oder über des Langzeittrends (EMA200) liegt
		//Ausgabewerte: 1 -> Kurs über Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich Preis
		int rueckgabewert = 99;
		double ema200 = werte.ema;
		double aktuellerKurs = werte.lastPrice;
		
		if (aktuellerKurs > ema200) {
			rueckgabewert = 1;
		}
		if (aktuellerKurs < ema200) {
			rueckgabewert = -1;
		}
		else /*aktuellerKurs = ema200*/{
			rueckgabewert = 0;
		}
		
		return rueckgabewert;
	}

	public static int pruefePSAR(Kpi werte) {
		//Prüfue, ob der aktuelle Preis unter oder über dem Parabolic SAR liegt
		//Ausgabewerte: 1 -> PSAR-Punkt unter Preis; -1 -> PSAR-Punkt über Preis; 0 -> PSAR-Punkt gleich Preis
		int rueckgabewert = 99;
		double aktuellerKurs = werte.lastPrice;
		double PSAR = werte.parabolicSAR;
		if (aktuellerKurs > PSAR) {
			rueckgabewert = 1;
		}
		if (aktuellerKurs < PSAR) {
			rueckgabewert = -1;
		}
		else /*aktuellerKurs = PSAR*/{
			rueckgabewert = 0;
		}
		return rueckgabewert;
		
	}

}
