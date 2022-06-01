package de.fhws.Softwareprojekt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



import API.ApiConnection;
import API.Connection;
import positionen.Verwaltung;

public class EmaListe {
	JsonInstrumentsRoot instrumentsRoot;

	
	//public Map<instrument, isUsed> instrumentenliste = new HashMap<>();
	
	
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
		
		Ema e = new Ema(connection);
		Kpi test = e.aufrufAlles("EUR_USD", 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
		//Kpi test2 = e.aufrufAlles("USD_JPY", 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
		//kombiniereMACDEMAPSAR(connection,test);
		//kombiniereMACDEMAPSAR(connection,test2);
		
		//boolean isUsed = false;
		Map<String, Boolean> instrumentenVerfuegbarkeit = new HashMap<>();
		JsonInstrumentsRoot instrumentsRoot = e.getInstruments();
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) {

			/*if (instrument.type.compareTo("CURRENCY")==0) {
			if (instrument.name.toUpperCase().contains(filter) || instrument.displayName.toUpperCase().contains(filter)
					|| instrument.type.toUpperCase().contains(filter)) {*/
			
			//Sind offene Positionen vorhanden (vom letzten Login?)
			//boolean isUsed = positionen.Verwaltung.containsPosition(instrument.name.toString());
			//instrumentenVerfuegbarkeit.put(instrument.name.toString(), false);
			System.out.println(instrument.name);
			kombiniereMACD_PSAR(test);
		}
		/*
		for(String k : instrumentenVerfuegbarkeit.keySet()) {
			System.out.println(k);
			Kpi werte = e.aufrufAlles(k, 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
			kombiniereMACDEMAPSAR(connection,werte);
	
			//System.out.println(pruefeEMA200(werte));
			boolean isUsed = instrumentenVerfuegbarkeit.get(k);
			if(isUsed = false) {
				//kombiniereMACDEMAPSAR(connection,k, 200, 14, "S5",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
			}
			else
				continue;
		}

		*/
		
	}

	public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {
		
		System.out.println(kpi.instrument + " " + instrument.displayName+ " " + instrument.type + " " + emaName + ":  " + kpi.ema +" MCAD: "+kpi.macd+ " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: " +kpi.parabolicSAR+ " RSI: " +kpi.rsi +" ATR: "+ kpi.atr+ " ("
				+ kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: " + kpi.avg + "  " + kpi.firstTime
				+ " - " + kpi.lastTime + ")");
	}
	
	  public static double runden(double x)
	  {

		  double gerundet = 0;
	      BigDecimal bd = new BigDecimal(x).setScale(2, RoundingMode.HALF_UP);
	      gerundet = bd.doubleValue();
	      return gerundet;
	  }

	  
	public static void kombiniereMACD_PSAR(Kpi werte) {
		if (pruefePerioden(werte, "MACD", 6) == -1) {
			if(pruefePSAR(werte)==1) {
				System.out.println("MACD_PSAR Long");
			}
		}
		else if (pruefePerioden(werte, "MACD", 6) == 1) {
			if (pruefePSAR(werte)==-1) {
				System.out.println("MACD_PSAR Short");
			}
		}
	}
	public static void kombiniereATR_MACD(ApiConnection connection, Kpi werte) {
		//
	}
	  
	  
	  
	public static void kombiniereMACDEMAPSAR(ApiConnection connection, Kpi werte) {
	//public static Kpi kombiniereMACDEMAPSAR(ApiConnection connection) {
		// x = kurze Periode , y = lange Periode , z = Signall�nge ; (Standardwerte: 12,26,9)

		
		//Connection con = new Connection();
		//ApiConnection connection = new ApiConnection(con);
		//Ema ema= new Ema(connection);
		
		//Kpi werte = ema.aufrufAlles(instrument, emaperiods, periods,  granularity, startBF,  inkrementBF,  maxBF, x,  y,  z, multiplicatorUpper, multiplicatorLower);
		
		System.out.println(pruefeATR(werte));
		//System.out.println("Letzter Preis " +werte.lastPrice);
		//System.out.println("SAR " +werte.parabolicSAR);
		//System.out.println("ema " +werte.ema);
		//System.out.println("macd " +werte.macd);
		//System.out.println("macd verh�ltnis " + (werte.macdTriggert-werte.macd));
		//System.out.println("macd trigger " +werte.macdTriggert);
		//System.out.println("macd methode " +pruefeMACD(werte));
		//System.out.println("ema200 methode " +pruefeEMA200(werte));
		//System.out.println("psar methode " +pruefePSAR(werte));
		//System.out.println("vorperioden methode " +pruefeVorperioden(werte, "RSI"));
		//System.out.println("rsi methode " +pruefeRSI(werte));
		
		
		//ToDo: Doppelten Code vermeiden -> Funktionen zusammenlegen
		//		Ermitteln welche R�ckgabewerte zu einer Kaufentscheidung f�hrt
		//		Kaufposition aufrufen
		//		MACD Periodencheck mit aktuellem MACD kombinieren?
		//		Verf�gbarkeit pr�fen -> Wird der
		//		pruefeVorperioden mit aktuellem MACD
		
		
		
		try {
			if(pruefeEMA200(werte) == 1) {
				System.out.println("1.versuch");												//1. liegt Trend (= 200 EMA) �ber Kurs?
				if(pruefePerioden(werte, "MACD", 5) == -1) {		//2. liegt MACD-Linie in den letzten 5 Perioden unter Signallinie?
							if(pruefePSAR(werte) == 1) {		//5. ist der PSAR-Wert unter dem Kurs?
								//long							//Long-Position
								//return werte;	
								System.out.println("long");
							}
						}
					}
				
			
			else if (pruefeEMA200(werte) == -1){System.out.println("2.versuch");				//1. liegt Trend unter Kurs?
				if(pruefePerioden(werte, "MACD", 5) == 1) {		//2. liegt MACD-Linie in den letzten 5 Perioden �ber Signallinie?
							if(pruefePSAR(werte) == -1) {		//5. ist der PSAR-Wert �ber dem Kurs?
								//short							//Short-Position 
								//Verwaltung.placeOrder(String i, double wert, double kurs, double obergrenze, double untergrenze);
								//Verwaltung.placeOrder(instrument, double wer, double kurs, double obergrenze, double untergrenze);
								//return werte;
								System.out.println("short");
							}
						}
					}
				
			
			//wenn 0?
			
				
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public static int pruefePerioden(Kpi werte, String entscheideSignal, int anzahlVorperioden) {
		//Die Methode, soll die Vorperiode pr�fen, ob bestimmte Ereignisse vorgefallen sind oder nicht
		//Dabei werden die Methoden pruefeMACD() und pruefeRSI() zusammengelegt
		int ausgabe = 99;
		int MACDRueckgabewert = 99;
		int RSIRueckgabewert = 99;
		boolean verhaeltnisVorzeichenNegativ = false;	
		boolean verhaeltnisVorzeichenPositiv = false;
		boolean RSIOverbought = false;	//RSI �ber 70% 
		boolean RSIOversold = false; 	//RSI unter 30%
		int MACDAktuell = 99;
		
		//anzahl Vorperioden falscher �bergabewert
		if(anzahlVorperioden<2) return ausgabe;
		
		for(int i = 1; i<anzahlVorperioden+1; i++) {
			double macd = werte.macds.get(werte.macds.size()-i);
			double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size()-i);
			//double macdVerhaeltnis = macd-trigger;
			double macdVerhaeltnis = trigger-macd;
			//System.out.println(werte.rsiListe.get(werte.rsiListe.size()-i));
			//Wie ist das aktuelle Verh�ltnis?:
			if (i ==1) {
				if(macdVerhaeltnis <=0) {
					MACDAktuell = -1;
				}
				else if (macdVerhaeltnis >= 0) {
					MACDAktuell = 1;
				}

			}
			//Vorperioden
			else if (i>0&&i!=1) {
				if(macdVerhaeltnis < 0) {
					verhaeltnisVorzeichenNegativ = true;
				}
				else if (macdVerhaeltnis > 0) {
					verhaeltnisVorzeichenPositiv = true;
				}
				else { //macdVerhaeltnis == 0   
					break;
				}
				if (werte.rsiListe.get(werte.rsiListe.size()-i) > 70) {
					RSIOverbought = true;
				}
				else if (werte.rsiListe.get(werte.rsiListe.size()-i) < 30) {
					RSIOversold = true;
				}
				else { //"70 >= werte.rsiListe.get(werte.rsiListe.size()-i) >=30"
					break;
				}
			}
		}
		if(verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == false && MACDAktuell ==1) {
			//die letzten MACDs sind negativ und der Aktuelle positiv oder null
			MACDRueckgabewert = -1;
			
		}
		else if (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == true && MACDAktuell == -1) {
			//die letzten MACDs sind positiv und der Aktuelle negativ oder null
			MACDRueckgabewert = 1;
		}
		else if ((verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == true) || (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == false)){
			//die letzten MACDs haben nicht das gleiche Vorzeichen 
			MACDRueckgabewert = 0;
		}
		if (RSIOversold == true && RSIOverbought == false) {
			//die letzten 5 RSIs sind Oversold, also unter 30%
			RSIRueckgabewert = -1;
		}
		else if (RSIOversold == false && RSIOverbought == true) {
			//die letzten 5 RSIs sind Overbought, also �ber 70%
			RSIRueckgabewert = 1;
		}
		else if ((RSIOversold == true && RSIOverbought == true) || (RSIOversold == false && RSIOverbought == false)) {
			//die letzten 5 RSIs schwanken oder liegen alle zwischen 30 und 70 Prozent
			RSIRueckgabewert = 0;
		}
		if (entscheideSignal == "MACD") {
			ausgabe = MACDRueckgabewert;
		}
		else if (entscheideSignal =="RSI") {
			ausgabe = RSIRueckgabewert;
		}
		else if (entscheideSignal != "MACD" && entscheideSignal != "RSI") {
			//keine �nderung von ausgabe
			ausgabe = 99;
		}
		
		return ausgabe;
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
			System.out.println("trigger "+trigger);
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
	
	public static int pruefeRSI(Kpi werte) {
		//pr�fen, ob RSI in den Vorperioden immer �ber 70% oder immer unter 30% liegt
		int rueckgabewert = 99;
		boolean RSIOverbought = false;	//RSI �ber 70% 
		boolean RSIOversold = false; 	//RSI unter 30%
		
		for (int i = 2; i<7; i++) {
			System.out.println(werte.rsiListe.get(werte.rsiListe.size()-i));
			if(werte.rsiListe.get(werte.rsiListe.size()-i) > 70) {
				RSIOverbought = true;
			}
			else if (werte.rsiListe.get(werte.rsiListe.size()-i) < 30) {
				RSIOversold = true;
			}
			else { //RSI zwischen 30 und 70% (einschlie�lich)
				break;
			}
		}
		if(RSIOversold == true && RSIOverbought == false) {
			//die letzten 5 RSIs sind alle Oversold, also unter 30%
			rueckgabewert = -1;
		}
		else if (RSIOversold == false && RSIOverbought == true) {
			//die letzten 5 RSIs sind Overbought, also �ber 70%
			rueckgabewert = 1;
		}
		else if ((RSIOversold == true && RSIOverbought == true) || (RSIOversold == false && RSIOverbought == false)){
			//die letzten 5 RSIs sind nicht gleich bzw. schwanken
			rueckgabewert = 0;
		}
		return rueckgabewert;		
	}
	
	
	public static int pruefeEMA200(Kpi werte) {
		//Pr�fe, ob der aktuelle Preis unter oder �ber des Langzeittrends (EMA200) liegt
		//Ausgabewerte: 1 -> Kurs �ber Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich Preis
		int rueckgabewert = 99;
		double faktorRundung = 1.001;
		//double ema200 = werte.ema * faktorRundung;
		double ema200 = werte.ema;
		double aktuellerKurs = werte.lastPrice;
		System.out.println("*ema200 " +ema200);
		System.out.println("*letzter preis " +aktuellerKurs);
		
		
		if (aktuellerKurs > ema200) {
			rueckgabewert = 1;
		}
		else if (aktuellerKurs < ema200) {
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
		else if (aktuellerKurs < PSAR) {
			rueckgabewert = -1;
		}
		else /*aktuellerKurs = PSAR*/{
			rueckgabewert = 0;
		}
		return rueckgabewert;
		
	}
	
	public static int pruefeATR(Kpi werte) {
		//Wo ist der relativ niedrigste Punkt in den letzten ca 200 Candles?
		//Dieser Wert ist der Vergleichspunkt mit dem aktuellen ATR Wert
		//ATR gibt aus, wann der Markt seinen Wert extrem steigert oder f�llt
		int rueckgabe = 0;
		System.out.println("vanilla " + werte.atr);
		double y = werte.atr*100000;
		System.out.println("test "+ runden(y));
		
		
		double x = runden(werte.atr);
		x *=1000;
		System.out.println(x);
		
		double relativesMinimum = 100;
		for(int i = 2; i<200; i++) {
			double vergleich = werte.atrListe.get(werte.atrListe.size()-i); 
			if (vergleich < relativesMinimum) {
				relativesMinimum=vergleich;
			}
		}
		
		if (relativesMinimum <= werte.atr) {
			//aktueller ATR ist am niedrigsten Punkt
			rueckgabe = 1;
		}
		else if (relativesMinimum> werte.atr) {
			rueckgabe = -1;
		} 
		
		return rueckgabe;
		
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
