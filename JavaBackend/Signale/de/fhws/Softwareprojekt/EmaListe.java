package de.fhws.Softwareprojekt;

import java.util.Scanner;

import API.ApiConnection;
import API.Connection;

public class EmaListe {
	
	
	
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
		kombiniereMACDEMAPSAR(connection,"EUR_USD", 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
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
				
				//ausgabe("test", testausgabe, instrument);
				//ausgabe("Test", kombiniereMACDEMAPSAR(connection), instrument);
				
		}
		//}}
		
		
		
	}

	public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {
		
		System.out.println(kpi.instrument + " " + instrument.displayName+ " " + instrument.type + " " + emaName + ":  " + kpi.ema +" MCAD: "+kpi.macd+ " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: " +kpi.parabolicSAR+ " RSI: " +kpi.rsi +" ATR: "+ kpi.atr+ " ("
				+ kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: " + kpi.avg + "  " + kpi.firstTime
				+ " - " + kpi.lastTime + ")");
	}
	public static void kombiniereMACDEMAPSAR(ApiConnection connection, String instrument, int emaperiods,int periods, String granularity,double startBF, double inkrementBF, double maxBF,int x, int y, int z,int multiplicatorUpper,int multiplicatorLower) {
	//public static Kpi kombiniereMACDEMAPSAR(ApiConnection connection) {
		// x = kurze Periode , y = lange Periode , z = Signall�nge ; (Standardwerte: 12,26,9)

		
		//Connection con = new Connection();
		//ApiConnection connection = new ApiConnection(con);
		Ema ema= new Ema(connection);
		
		Kpi werte = ema.aufrufAlles(instrument, emaperiods, periods,  granularity, startBF,  inkrementBF,  maxBF, x,  y,  z, multiplicatorUpper, multiplicatorLower);
		
		boolean kaufentscheidung = false;
		JsonCandlesRoot h = werte.root;

		//System.out.println(werte.lastPrice);
		System.out.println(werte.parabolicSAR);
		//System.out.println(werte.ema);
		//System.out.println(werte.macd);
		//System.out.println(werte.macdTrigger);
		//System.out.println(pruefeMACD(werte));
		//System.out.println(pruefeEMA200(werte));
		//System.out.println(pruefePSAR(werte));
		
		//ToDo: Abgleichen der Werte von EMA200, MACD und PSAR
		//		Ermitteln welche R�ckgabewerte zu einer Kaufentscheidung f�hrt
		//		Kaufposition aufrufen
		
		try {
			if(pruefeEMA200(werte) == 1) {						//1. liegt Trend (= 200 EMA) �ber Kurs?
				if(pruefeMACD(werte) == -1) {					//2. liegt MACD-Linie in den letzten 5 Perioden unter Signallinie?
					if ((werte.macd-werte.macdTriggert) >= 0) {	//3. ist der aktuelle MACD auf oder �ber 0?
						for (int i = 0; i < 2; i++) {			//4. Schleifendurchlauf f�r n�chste Bedingung
							if(pruefePSAR(werte) == 1) {		//5. ist der PSAR-Wert unter dem Kurs?
								//long							//Long-Position
								//return werte;	
							}
							else if (pruefePSAR(werte) != 1 && i <1) {//5.1 PSAR ist �ber dem Kurs -> eine Periode warten
									Thread.sleep(berechneMillisekunden(granularity));
							}
							else if (pruefePSAR(werte) != 1 && i == 1) {//5.2 PSAR ist �ber dem Kurs nach der n�chsten Periode -> abbruch
								break;
							}
						}
					}
				}
			}
			else if (pruefeEMA200(werte) == -1){				//1. liegt Trend unter Kurs?
				if(pruefeMACD(werte) == 1) {					//2. liegt MACD-Linie in den letzten 5 Perioden �ber Signallinie?
					if((werte.macd-werte.macdTriggert) <= 0) {	//3. ist der aktuelle MACD auf oder unter 0?
						for (int i = 0; i < 2; i++) {			//4. Schleifendurchlauf f�r n�chste Bedingung
							if(pruefePSAR(werte) == -1) {		//5. ist der PSAR-Wert �ber dem Kurs?
								//short							//Short-Position 
								//Verwaltung.placeOrder(String i, double wert, double kurs, double obergrenze, double untergrenze);
								//Verwaltung.placeOrder(instrument, double wer, double kurs, double obergrenze, double untergrenze);
								//return werte;
							}
							else if (pruefePSAR(werte) != -1 && i <1) {//5.1 PSAR ist unter dem Kurs -> eine Periode warten
									Thread.sleep(berechneMillisekunden(granularity));
									i++;
							}
							else if (pruefePSAR(werte) != -1 && i == 1) {//5.2 PSAR ist unter dem Kurs nach der n�chsten Periode -> abbruch
								break;
							}
						}
					}
				}
			}
			//wenn 0?
			
				
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	public static int pruefeMACD(Kpi werte) {
		// Optionale Pr�fung, ob MACD-Trend in den Vorperioden optimal ist
				
		boolean verhaeltnisVorzeichenNegativ = false;
		boolean verhaeltnisVorzeichenPositiv = false;
		int rueckgabewert = 99;
		for (int i = 2; i<7; i++) {
			double macd = werte.macds.get(werte.macds.size()-i);
			double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size()-i);
			double macdVerhaeltnis = macd-trigger;
			if(macdVerhaeltnis < 0) {
				verhaeltnisVorzeichenNegativ = true;
			}
			else if (macdVerhaeltnis > 0) {
				verhaeltnisVorzeichenPositiv = true;
			}
			else /*macdVerhaeltnis = 0*/ {
				break;
			}
			
			//System.out.println(macdVerhaeltnis);
			//wenn das Verh�ltnis die letzten 5 Perioden das gleiche Vorzeichen haben
			//und dann das Vorzeichen sich �ndert, gilt die Bedingung als erf�llt
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
		//Pr�fe, ob der aktuelle Preis unter oder �ber des Langzeittrends (EMA200) liegt
		//Ausgabewerte: 1 -> Kurs �ber Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich Preis
		int rueckgabewert = 99;
		double faktorRundung = 1.001;
		double ema200 = werte.ema * faktorRundung;
		
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
		//Pr�fue, ob der aktuelle Preis unter oder �ber dem Parabolic SAR liegt
		//Ausgabewerte: 1 -> PSAR-Punkt unter Preis; -1 -> PSAR-Punkt �ber Preis; 0 -> PSAR-Punkt gleich Preis
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
	public static int berechneMillisekunden (String granularity) {
		int millisekunden = 0;
		
		switch (granularity) {
			case ("S5"): {
				millisekunden = 5000;
				return millisekunden;
			}
			case ("M1"): {
				millisekunden = 60000;
				return millisekunden;
			}
			case ("M5"): {
				millisekunden = 300000;
				return millisekunden;
			}
			case ("M15"): {
				millisekunden = 900000;
				return millisekunden;
			}
			case ("M30"): {
				millisekunden = 1800000;
				return millisekunden;
			}
			case ("H1"): {
				millisekunden = 3600000;
				return millisekunden;
			}
			case ("H4"): {
				millisekunden = 14400000;
				return millisekunden;
			}
			case ("D1"): {
				millisekunden = 86400000;
				return millisekunden;
			}
			case ("W1"): {
				millisekunden = 0; //Intervall zu gro� wegen Wochenende dazwischen
				return millisekunden;
			}
			case ("Mo1"): {		
				millisekunden = 0; //Intervall zu gro�
				return millisekunden;
			}
			default:{
				return millisekunden;
			}
		
		
		}

	}
}
