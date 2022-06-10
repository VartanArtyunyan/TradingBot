package de.fhws.Softwareprojekt;

import java.util.ArrayList;

import API.ApiConnection;
import API.Connection;

public class TESTA {
public static void main(String[] args) {
		
		int zahl1=7;
		int zahl2=9;
		double wert=(double)zahl1/zahl2;
		//System.out.println(wert);
		Connection con = new Connection();
		ApiConnection connection = new ApiConnection(con);
		ArrayList<String>a=new ArrayList();
		a.add("JPY");
		a.add("HUF");
		a.add("USD_THB");
		a.add("USD_INR");
		/*System.out.println("Instrumentliste - Suchbegriff:");
		String filter;
		try (Scanner scanner = new Scanner(System.in);) {
			filter = scanner.nextLine().toUpperCase();
		} catch (Exception e) {
			filter = "";
		}
		;*/
	//	kombiniereMACDEMAPSAR(connection,"USD_JPY", 200, 14, "M15",0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
		KpiCalculator e = new KpiCalculator(connection);
		int zaehler=0;
		JsonInstrumentsRoot instrumentsRoot = e.getInstruments();

	for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) 
		{
	
			if(instrument.type.compareTo("CURRENCY")==0)
			{
			//	JsonCandlesRoot jcr=e.getCandles("EUR_USD", "M15");
			//Kpi kpi=e.getSMA(instrument.name,14,"M15", jcr);
			//System.out.println(kpi.sma);
				EmaListe es=new EmaListe();
				Kpi kpi2=e.getAll(instrument.name,200, 14, "M15", 0.02, 0.02, 0.02, 12,26,9);
				System.out.println(kpi2.sma );
				System.out.println(kpi2.atr);
				/*if(kpi.macd>1)
					System.out.println(kpi.macd);
				else
				{
					if(kpi.macd>1)
					System.out.println(kpi.macd);
				}*/
			//String c="";
	//	if((pruefeVorperioden(kpi, "MACD")!=0)&&((kpi.prozent>0.15)||(kpi.prozent<-0.25)))
				//{
			
				//
			//	if(instrument.name.contains("USD_THB")||instrument.name.contains("USD_INR")||(instrument.name.contains("JPY")||instrument.name.contains("HUF")))
				//if(instrument.displayPrecision==3)
				//{
				//	System.out.println(kpi.rsi);
					
					
					//	if((instrument.name.compareTo("EUR_USD")==0)||(instrument.name.compareTo("GBP_USD")==0))
					//	{
					//	System.out.println(kpi.instrument);
						//System.out.println(kpi.rsi);
						//System.out.println(kpi.atr);
						//System.out.println(kpi.sma);
						//}
					//}
						
					
				/*System.out.println(instrument.displayPrecision);
				System.out.println(kpi.lastPrice);
					System.out.println(kpi.parabolicSAR);
					System.out.println(kpi.getLimitPrice());
					System.out.println(kpi.getLongStopLoss());
					System.out.println(kpi.getLongTakeProfit());
					System.out.println(kpi.getShortStopLoss());
					System.out.println(kpi.getShortTakeProfit());
					}*/
				//	System.out.println(kpi.parabolicSAR+" " +kpi.lastPrice+es.kombiniereMACDEMAPSAR (kpi));
			//	System.out.println(	kpi.runden(kpi.parabolicSAR,3)+1/Math.pow(10, 3));
				//System.out.println(kpi.instrument);
				/*System.out.println("aufrunden: "+kpi.aufrunden(kpi.parabolicSAR, 3));
				System.out.println("abrunden " +kpi.abrunden(kpi.parabolicSAR,3));
				System.out.println("runden" +kpi.runden(kpi.parabolicSAR, 3));
				System.out.println(kpi.runden(kpi.parabolicSAR,3)+1/Math.pow(10, 3));*/
	
				}
		
		}
		//	System.out.println(zaehler);
				//System.out.println("macdTriggert " + kpi.atr +" "+kpi.macds.get(kpi.macds.size()-1));
		//	System.out.println("macd " +kpi.macd);
	//		System.out.println("OK");
			
			//ausgabe("alles",kpi,instrument);
 			//System.out.println(kpi.prozent);
		
			//System.out.println(pruefeVorperioden(kpi,"MACD"));
			}
		
		
		
	//System.out.println(zaehler);
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
public static int pruefeEMA200(Kpi werte) {
	// Prüfe, ob der aktuelle Preis unter oder über des Langzeittrends (EMA200)
	// liegt
	// Ausgabewerte: 1 -> Kurs über Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich
	// Preis
	int rueckgabewert = 99;
	//double faktorRundung = 1.001;
	double ema200 = werte.ema;// * faktorRundung;

	double aktuellerKurs = werte.lastPrice;

	if (aktuellerKurs > ema200) {
		rueckgabewert = 1;
	}
	else if (aktuellerKurs < ema200) {
		rueckgabewert = -1;
	} else /* aktuellerKurs = ema200 */ {
		rueckgabewert = 0;
	}

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
public static int kombiniereMACDEMAPSAR(ApiConnection connection, Kpi werte) {
	
	
	// public static Kpi kombiniereMACDEMAPSAR(ApiConnection connection) {
	// x = kurze Periode , y = lange Periode , z = Signallänge ; (Standardwerte:
	// 12,26,9)
	// Connection con = new Connection();
	// ApiConnection connection = new ApiConnection(con);
	boolean kaufentscheidung = false;
	JsonCandlesRoot h = werte.root;

	// System.out.println(werte.ema);
	// System.out.println(werte.macd);
	// System.out.println(werte.macdTrigger);
	// System.out.println(pruefeMACD(werte));
	// System.out.println(pruefeEMA200(werte));
	// System.out.println(pruefePSAR(werte));

	// ToDo: Abgleichen der Werte von EMA200, MACD und PSAR
	// Ermitteln welche Rückgabewerte zu einer Kaufentscheidung führt
	// Kaufposition aufrufen
	int rueckgabewert = 0;

	if (pruefeEMA200(werte) == 1) { // 1. liegt Trend (= 200 EMA) über Kurs?
		if (pruefeVorperioden(werte, "MACD") == -1) { // 2. liegt MACD-Linie in den letzten 5 Perioden unter
														// Signallinie?
			if ((werte.macd - werte.macdTriggert) >= 0) {
				if(werte.macdIntensity>0.20)
				{
				// 3. ist der aktuelle MACD auf oder über 0?
				// for (int i = 0; i < 2; i++) { //4. Schleifendurchlauf für nächste Bedingung
				if (pruefePSAR(werte) == 1) { // 5. ist der PSAR-Wert unter dem Kurs?
					// long //Long-Position
					// return werte;
					rueckgabewert = 1;
				}
				// }
			}
		}
		}
	}else if (pruefeEMA200(werte) == -1) { // 1. liegt Trend unter Kurs?
			if (pruefeVorperioden(werte, "MACD") == 1) { // 2. liegt MACD-Linie in den letzten 5 Perioden über
															// Signallinie?
				if ((werte.macd - werte.macdTriggert) <= 0) { 
					if(werte.macdIntensity<-0.25)
					{// 3. ist der aktuelle MACD auf oder unter 0?
				
					// 4. Schleifendurchlauf für nächste Bedingung
					if (pruefePSAR(werte) == -1) {
						// 5. ist der PSAR-Wert über dem Kurs?
						rueckgabewert = -1;
					}

				}
			}
			}
		}
		return rueckgabewert;
	}
public static int pruefePSAR(Kpi werte) {
	// Prüfue, ob der aktuelle Preis unter oder über dem Parabolic SAR liegt
	// Ausgabewerte: 1 -> PSAR-Punkt unter Preis; -1 -> PSAR-Punkt über Preis; 0 ->
	// PSAR-Punkt gleich Preis
	int rueckgabewert = 99;
	double aktuellerKurs = werte.lastPrice;
	double PSAR = werte.parabolicSAR;
	if (aktuellerKurs > PSAR) {
		rueckgabewert = 1;
	}
	else if (aktuellerKurs < PSAR) {
		rueckgabewert = -1;
	} else /* aktuellerKurs = PSAR */ {
		rueckgabewert = 0;
	}
	return rueckgabewert;

}
	// short //Short-Position
	// Verwaltung.placeOrder(String i, double wert, double kurs, double obergrenze,
	// double untergrenze);
	// Verwaltung.placeOrder(instrument, double wer, double kurs, double obergrenze,
	// double untergrenze);
	// return werte;

	/*
	 * else if (pruefePSAR(werte) != -1 && i <1) {//5.1 PSAR ist unter dem Kurs ->
	 * eine Periode warten Thread.sleep(berechneMillisekunden(werte.granularity));
	 * i++; }
	 */

	// wenn 0?
	/*
	 * LocalTime jetzt=LocalTime.now(); int minuten=jetzt.getMinute(); //Die
	 * Wertlinie 50 im RSI-Fenster muss von unten überquert werden. //Die Kursbalken
	 * müssen sich oberhalb der SMA10-Linie entwickeln. // Die beiden Linien des
	 * MACD-Indikators müssen sich unter der 0-Linie schneiden. if((
	 * VergleicheWerte(werte)==1) &&(pruefeSMA(werte)==1)&&(pruefeRSI(werte)==1) )
	 * //Long Trade bis Ende nächster Kerze sofort ausführen {
	 * Thread.sleep(berechneMillisekunden(granularity)*2-(minuten%15)*60000);
	 * //Verkaufen } //Die RSI 50-Linie wird von oben gekreuzt. //Die Kursbalken
	 * entwickeln sich unter der SMA10-Linie. //Die MACD-Linien schneiden sich über
	 * die 0-Linien. else if(( VergleicheWerte(werte)==1)
	 * &&(pruefeSMA(werte)==1)&&(pruefeRSI(werte)==1)) { //ShortTrade bis Ende
	 * nächster Kerze sofort ausführen
	 * Thread.sleep(berechneMillisekunden(granularity)*2-minuten%15); //Verkaufen }
	 * }
	 */

	// }

	/*
	 * public static int pruefeMACD(Kpi werte) { // Optionale Prüfung, ob MACD-Trend
	 * in den Vorperioden optimal ist int zaehlerNegativ=0; int zaehlerPositiv=0;
	 * boolean Negativ=false; boolean Positiv=true; int rueckgabewert = 99; int
	 * aktuellerWert=0; for (int i = 1; i < 7; i++) {
	 * 
	 * double macd = werte.macds.get(werte.macds.size() - i); double trigger =
	 * werte.macdsTriggert.get(werte.macdsTriggert.size() - i); double
	 * macdVerhaeltnis = macd - trigger; if(i==1) { if(macdVerhaeltnis<0)
	 * Negativ=true; } if(macdVerhaeltnis>0) { Positiv=true; }
	 * 
	 * else { if (macdVerhaeltnis < 0) { zaehlerNegativ++; } else if
	 * (macdVerhaeltnis > 0) { zaehlerPositiv++; // } else /* macdVerhaeltnis = 0
	 */ // {
	// break;

// }
// } // System.out.println(macdVerhaeltnis);
// wenn das Verhältnis die letzten 5 Perioden das gleiche Vorzeichen haben
// und dann das Vorzeichen sich ändert, gilt die Bedingung als erfüllt

// rueckgabewert=zaehlerNegativ==5&&Positiv==true?-1:zaehlerPositiv==5&&Negativ==true?1:0;

// return rueckgabewert;
// }
}