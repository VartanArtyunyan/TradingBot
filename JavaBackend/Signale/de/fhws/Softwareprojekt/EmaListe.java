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
				
				
				ausgabe("Test", kombiniereMACDEMAPSAR(), instrument);
			}
		//}}
		
		
		
	}

	public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {
		
		System.out.println(kpi.instrument + " " + instrument.displayName+ " " + instrument.type + " " + emaName + ":  " + kpi.ema +" MCAD: "+kpi.macd+ " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: " +kpi.parabolicSAR+ " RSI: " +kpi.rsi +" ATR: "+ kpi.atr+ " ("
				+ kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: " + kpi.avg + "  " + kpi.firstTime
				+ " - " + kpi.lastTime + ")");
	}
	
	public static Kpi kombiniereMACDEMAPSAR() {
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
	
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		Ema e = new Ema(connection);
		
		Kpi aufrufAlles = e.aufrufAlles("EUR_USD", 200, 14, "M15",0.02, 0.02, 0.02, 12,26,9,2,2);
		double ema200 = aufrufAlles.ema;
		double aktuellerKurs = aufrufAlles.lastPrice;
		boolean kaufentscheidung = false;
		JsonCandlesRoot x = aufrufAlles.root;
		Kpi macd = e.getMACD("EUR_USD", "M15", 12, 26, 9, x);
		double abfrageMACD = macd.macd;
		double trigger = macd.macdTriggert;
		/*System.out.println(aktuellerKurs);
		System.out.println(ema200);
		System.out.println(abfrageMACD);
		System.out.println(trigger);*/
		
		if (aktuellerKurs > ema200) {
			//Kurs liegt über EMA200
			//Abfragen, wie MACD zu Signallinie steht
			//Wenn optimal, dann PSAR abgleichen
			System.out.println("Kurswert liegt unter Trend");
			//MACD Differenz Berechnen
			double differenz = Math.round(abfrageMACD - trigger);
			
			
			
			System.out.println(differenz);

			kaufentscheidung = true;
		}
		else if (aktuellerKurs < ema200) {
			//Kurs liegt unter EMA200
			//Abfragen, wie MACD zu Signallinie steht
			//Wenn optimal, dann PSAR abgleichen
			System.out.println("Kurswert liegt über Trend");
			//MACD Differenz Berechnen
			double differenz = Math.round(abfrageMACD - trigger);
			System.out.println(differenz);
			
			kaufentscheidung = true;
		}
		else //Kurs liegt auf EMA200 -> kommt so gut wie nie vor
			kaufentscheidung = false;
		
		return aufrufAlles;

	}


}
