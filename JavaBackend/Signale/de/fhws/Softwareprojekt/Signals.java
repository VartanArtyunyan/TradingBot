package de.fhws.Softwareprojekt;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import API.ApiConnection;
import API.Connection;
import LogFileWriter.LogFileWriter;
import positionen.Verwaltung;

public class Signals {

	ApiConnection connection;
	Verwaltung verwaltung;
	LogFileWriter logFileWriter;
	Ema e;
	ArrayList<JsonInstrumentsInstrument> instrumentsList;
	JsonInstrumentsRoot instrumentsRoot;
	HashSet<Kpi> signale;

	public Signals(ApiConnection connection, Verwaltung verwaltung, LogFileWriter logFileWriter) {

		this.connection = connection;
		this.verwaltung = verwaltung;
		this.logFileWriter = logFileWriter;
		this.e = new Ema(connection);

		this.instrumentsList = new ArrayList<>();
		this.instrumentsRoot = e.getInstruments();

		this.signale = new HashSet<>();

		for (JsonInstrumentsInstrument i : instrumentsRoot.instruments) {
			if (i.type.compareTo("CURRENCY") == 0)
				instrumentsList.add(i);
		}

	}

	public void runSignals(String granularity) {

		for (JsonInstrumentsInstrument instrument : instrumentsList) {
			// Kpi kpi=e.getKpi(instrument.name, 14, "M15");
			// kpi=e.getATR(instrument.name,14 , "M15");

			Kpi kpi = e.aufrufAlles(instrument.name, 200, 14, granularity, 0.02, 0.02, 0.2, 12, 26, 9, 2, 2);
			//nach kauf für 6 x granularität insturment sperren
			int r = kombiniereMACDEMAPSAR(connection, kpi);

			if (r != 0 && r != 2 && r != -2) {
				System.out.println(r);
				kpi.longShort = (r == 1) ? true : false;
				ausgabe("alles", kpi, instrument);
				signale.add(kpi);

			}

		}
		
		System.out.println("\n\nSIGNALE\n");
		for (Kpi s : signale) {
			System.out.println(s.instrument);
		}
		System.out.println("\nSIGNALE-ENDE\n\n");

	}

	public void endPeriod() {
		
		

		for (Kpi s : signale) {
			if (s.longShort ) {
				verwaltung.placeLongOrder(s.instrument, s.getLongTakeProfit(), s.getLongStopLoss(), s.lastPrice);

				logFileWriter.log(s.instrument, s.lastTime, s.lastPrice, s.getKaufpreis(), s.getLongTakeProfit(),
						s.getLongStopLoss(), s.macd, s.macdTriggert, s.parabolicSAR, s.ema);

			} else if (!s.longShort ) {
				verwaltung.placeShortOrder(s.instrument, s.getShortTakeProfit(), s.getShortStopLoss(), s.lastPrice);
				logFileWriter.log(s.instrument, s.lastTime, s.lastPrice, s.getKaufpreis(), s.getShortTakeProfit(),
						s.getShortStopLoss(), s.macd, s.macdTriggert, s.parabolicSAR, s.ema);
			}
		}

		signale = new HashSet<>();

	}

	

	public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {

		System.out.println(kpi.instrument + " " + instrument.displayName + " " + instrument.type + " " + emaName + ":  "
				+ kpi.ema + " MCAD: " + kpi.macd + " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: "
				+ kpi.parabolicSAR + " RSI: " + kpi.rsi + " ATR: " + kpi.atr + " Supertrend: " + kpi.superTrend
				+ " SMA: " + kpi.sma + " (" + kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: "
				+ kpi.avg + "  " + kpi.firstTime + " - " + kpi.lastTime + ")");
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
			if (pruefeMACD(werte) == -1) { // 2. liegt MACD-Linie in den letzten 5 Perioden unter Signallinie?
				if ((werte.macd - werte.macdTriggert) >= 0) { // 3. ist der aktuelle MACD auf oder über 0?
					// for (int i = 0; i < 2; i++) { //4. Schleifendurchlauf für nächste Bedingung
					if (pruefePSAR(werte) == 1) { // 5. ist der PSAR-Wert unter dem Kurs?
						// long //Long-Position
						// return werte;
						rueckgabewert = 1;
					} else
						rueckgabewert = 2;
				}
			}
		} else if (pruefeEMA200(werte) == -1) { // 1. liegt Trend unter Kurs?
			if (pruefeMACD(werte) == 1) { // 2. liegt MACD-Linie in den letzten 5 Perioden über Signallinie?
				if ((werte.macd - werte.macdTriggert) <= 0) { // 3. ist der aktuelle MACD auf oder unter 0?
					// 4. Schleifendurchlauf für nächste Bedingung
					if (pruefePSAR(werte) == -1) {
						// 5. ist der PSAR-Wert über dem Kurs?
						rueckgabewert = -1;
					} else
						rueckgabewert = -2;
				}

			}
		}
		return rueckgabewert;
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

	}

	public static int pruefeMACD(Kpi werte) {
		// Optionale Prüfung, ob MACD-Trend in den Vorperioden optimal ist

		boolean verhaeltnisVorzeichenNegativ = false;
		boolean verhaeltnisVorzeichenPositiv = false;
		int rueckgabewert = 99;
		for (int i = 2; i < 7; i++) {
			double macd = werte.macds.get(werte.macds.size() - i);
			double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size() - i);
			double macdVerhaeltnis = macd - trigger;
			if (macdVerhaeltnis < 0) {
				verhaeltnisVorzeichenNegativ = true;
			} else if (macdVerhaeltnis > 0) {
				verhaeltnisVorzeichenPositiv = true;
			} else /* macdVerhaeltnis = 0 */ {
				break;
			}

			// System.out.println(macdVerhaeltnis);
			// wenn das Verhältnis die letzten 5 Perioden das gleiche Vorzeichen haben
			// und dann das Vorzeichen sich ändert, gilt die Bedingung als erfüllt
		}
		if (verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == false) {
			// die letzten 5 MACDs sind negativ
			rueckgabewert = -1;
		} else if (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == true) {
			// die letzten 5 MACDs sind positiv
			rueckgabewert = 1;
		} else if ((verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == true)
				|| (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == false)) {
			// die letzten 5 MACDs haben nicht das gleiche Vorzeichen
			rueckgabewert = 0;
		}
		return rueckgabewert;
	}

	public static int VergleicheWerte(Kpi werte) {
		// Optionale Prüfung, ob MACD-Trend in den Vorperioden optimal ist
		boolean verhaeltnisVorzeichenNegativ = false;
		boolean verhaeltnisVorzeichenPositiv = false;
		int rueckgabewert = 99;
		// Checken letzten 5 Werte ab Vorletzten Wert Nicht den aktuellen
		// Wert(werte.macds.size()-1 mitdurchchecken)den hier
		// soll ja ein Vorzeichenwehsel stattfinden.Deswegen bei i=2 anfangen

		for (int i = 2; i <= 6; i++) {
			verhaeltnisVorzeichenNegativ = werte.macds.get(werte.macds.size() - i) < werte.macdsTriggert
					.get(werte.macdsTriggert.size() - i) ? true : false;
			if (verhaeltnisVorzeichenPositiv == false)
				break;//
		}
		for (int i = 2; i <= 6; i++) {
			verhaeltnisVorzeichenPositiv = werte.macds.get(werte.macds.size() - i) > werte.macdsTriggert
					.get(werte.macdsTriggert.size() - i) ? true : false;
			if (verhaeltnisVorzeichenPositiv == false)
				break;
		}

		// Checken letzten Wert auf Änderung und ob Schneiden unterhalb der 0 Linie
		rueckgabewert = (verhaeltnisVorzeichenPositiv == true && werte.macd < werte.macdTriggert
				&& (werte.macd < 0))
						? 1
						: (verhaeltnisVorzeichenNegativ == true && werte.macds
								.get(werte.macds.size() - 1) > werte.macdsTriggert.get(werte.macdsTriggert.size() - 1)
								&& werte.macd > 0) ? -1 : 0;
		return rueckgabewert;
	}

	public static int pruefeRSI(Kpi werte) {

		int rueckgabwert = 0;
		boolean longPosition = true;
		boolean shortPosition = true;
		// Jeweils checken ob der Wert des RSI kliner 50 war und die Vorgänger auch long
		// Jeweils checken ob der Wert des RSI größer 50 sind und die Vorgänger auch
		// short
		for (int i = 2; i < 7; i++) {
			longPosition = (werte.rsiListe.get(werte.rsiListe.size() - i) < 50 && longPosition == true) ? true : false;
			shortPosition = (werte.rsiListe.get(werte.rsiListe.size() - i) > 50 && shortPosition == true) ? true
					: false;
		}
		// Checken ob Vorzeichenwechsel
		return rueckgabwert = (longPosition == true && werte.rsi > 50) ? 1
				: shortPosition == true && werte.rsi > 50 ? -1 : 0;
	}

	public static int pruefeSMA(Kpi werte) {
		return werte.sma > werte.lastHighestPrice ? 1 : werte.sma < werte.lastLowestPrice ? -1 : 0;
	}

	// richtig
	public static int pruefeEMA200(Kpi werte) {
		// Prüfe, ob der aktuelle Preis unter oder über des Langzeittrends (EMA200)
		// liegt
		// Ausgabewerte: 1 -> Kurs über Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich
		// Preis
		int rueckgabewert = 99;
		double ema200 = werte.ema;
		double aktuellerKurs = werte.lastPrice;
		/*
		 * if (aktuellerKurs > ema200) { rueckgabewert = 1; } if (aktuellerKurs <
		 * ema200) { rueckgabewert = -1; } else /*aktuellerKurs = ema200{ rueckgabewert
		 * = 0; }
		 */
		// Beide Schreibweisen gleich
		rueckgabewert = aktuellerKurs > ema200 ? 1 : aktuellerKurs < ema200 ? -1 : 0;
		return rueckgabewert;
	}

//richtig
	public static int pruefePSAR(Kpi werte) {
		// Prüfue, ob der aktuelle Preis unter oder über dem Parabolic SAR liegt
		// Ausgabewerte: 1 -> PSAR-Punkt unter Preis; -1 -> PSAR-Punkt über Preis; 0 ->
		// PSAR-Punkt gleich Preis
		int rueckgabewert = 99;
		double aktuellerKurs = werte.lastPrice;
		double PSAR = werte.parabolicSAR;
		// Kannst du selber entscheiden welche Schreibweise dir lieber ist.
		/*
		 * if (aktuellerKurs > PSAR) { rueckgabewert = 1; } if (aktuellerKurs < PSAR) {
		 * rueckgabewert = -1; } else /*aktuellerKurs = PSAR
		 */
		rueckgabewert = 0;

		rueckgabewert = aktuellerKurs > PSAR ? 1 : aktuellerKurs < PSAR ? -1 : 0;
		return rueckgabewert;

	}

	public static int berechneMillisekunden(String granularity) {
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
			millisekunden = 0; // Intervall zu groß wegen Wochenende dazwischen
			return millisekunden;
		}
		case ("Mo1"): {
			millisekunden = 0; // Intervall zu groß
			return millisekunden;
		}
		default: {
			return millisekunden;
		}

		}

	}

}
