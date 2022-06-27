package de.fhws.Softwareprojekt;

import java.util.ArrayList;
import java.util.HashSet;

import API.ApiConnection;
import LogFileWriter.LogFileWriter;
import Threads.StopableThread;
import positionen.Verwaltung;

public class Signals extends StopableThread {

	ApiConnection connection;
	Verwaltung verwaltung;
	LogFileWriter logFileWriter;
	String granularity;
	KpiCalculator e;
	ArrayList<JsonInstrumentsInstrument> instrumentsList;
	JsonInstrumentsRoot instrumentsRoot;
	HashSet<Kpi> signale;

	JsonCandlesRoot jcr;

	boolean signal0 = true;
	boolean signal1 = true;
	boolean signal2 = true;
	boolean signal3 = true;

	public Signals(ApiConnection connection, Verwaltung verwaltung, LogFileWriter logFileWriter, String granularity) {

		this.connection = connection;
		this.verwaltung = verwaltung;
		this.logFileWriter = logFileWriter;
		this.granularity = granularity;
		this.e = new KpiCalculator(connection);

		this.instrumentsList = new ArrayList<>();
		this.instrumentsRoot = e.getInstruments();

		this.signale = new HashSet<>();

		for (JsonInstrumentsInstrument i : instrumentsRoot.instruments) {
			if (i.type.compareTo("CURRENCY") == 0)
				instrumentsList.add(i);
		}
	}

	@Override
	public void onTick() {
		runSignals(granularity);
	}

	public void runSignals(String granularity) {

		for (JsonInstrumentsInstrument instrument : instrumentsList) {
			// Kpi kpi = e.getAll(instrument.name, 200,20, 14, granularity, 0.02, 0.02, 0.2,
			// 12, 26, 9);
			Kpi kpi = e.getAll(instrument.name, "M15", 200, "sma", 20, "sma", 50, "atr", 14, "parabolicSAR",14, 0.02, 0.02,
					0.2, "macd", 12, 26, 9,"rsi",14);
			//für TP/SL-Entscheidung
			boolean containsATR = false;
			/*
			 * JsonCandlesRoot jcr = e.getCandles(instrument.name, granularity); Kpi sma20 =
			 * e.getSMA(instrument.name, 20, granularity, jcr); Kpi sma50 =
			 * e.getSMA(instrument.name, 50, granularity, jcr);
			 */

			// nach kauf für 6 x granularität insturment sperren
			if (signal0 | signal1) {
				int r = kombiniereMACDEMAPSAR(kpi);
				if (r != 0) {
					System.out.println(r);
					// kpi.longShort = (r == 1) ? true : false; //wird temporär geändert, um Signale
					// von der Methode zu überprüfen
					kpi.longShort = (r == 1) ? true : false;
					ausgabe("alles", kpi, instrument);
					kpi = kpi.resetKpiElements(kpi, "atr", "sma", "sma50", "rsi");
					if (signal0)
						verwaltung.pushSignal(kpi);

					// verwaltung.placeShortOrder(kpi.instrument,kpi.getLimitPrice(),
					// kpi.getShortTakeProfit(), kpi.getShortStopLoss(),kpi.lastPrice);
					// verwaltung.addManualPosition(instrument.name);

					// signale.add(kpi);

				} else if (signal1) {

					int s = kombiniereMACD_PSAR(kpi);
					if (s != 0) {
						// sperrt ebenfalls signal 0 und 1 & signal 2 soll nur signal 2 sperren
						System.out.println(s);
						kpi.signalStrenght = 0.5;
						kpi.longShort = (s == 1) ? true : false;
						ausgabe("alles", kpi, instrument);
						kpi = kpi.resetKpiElements(kpi, "atr", "sma", "sma50", "rsi", "ema");
						verwaltung.pushSignal(kpi);
					}
				}
			}

			if (signal2) {
				// andere Kombiniere Methoden
				int t = kombiniereEMA200ATR(kpi);
				kpi.useATRAsSLTP = true;
				
				if (t != 0) {
					System.out.println(t);
					kpi.signalStrenght = 0.5;
					kpi.longShort = (t == 1) ? true : false;
					// kpi.longShort = (t == 1) ? false : true;
					ausgabe("alles", kpi, instrument);
					kpi = kpi.resetKpiElements(kpi, "sma", "sma50", "rsi", "macd", "macdTriggert");

					verwaltung.pushSignal(kpi);
				}
				kpi.useATRAsSLTP = false;
			}
			if (signal3) {
				int u = kombiniereMACDSMA(kpi);
				if (u != 0) {
					System.out.println(u);
					kpi.signalStrenght = 0.5;
					kpi.longShort = (u == 1) ? true : false;
					// kpi.longShort = (u == 1) ? false : true;
					ausgabe("alles", kpi, instrument);
					kpi = kpi.resetKpiElements(kpi, "rsi", "atr", "parabolicSAR", "ema");
					verwaltung.pushSignal(kpi);
				}
			}

		}
	}

	/*
	 * System.out.println("\n\nSIGNALE\n"); for (Kpi s : signale) {
	 * System.out.println(s.instrument); } System.out.println("\nSIGNALE-ENDE\n\n");
	 */

	/*
	 * public void endPeriod() { //Hier an dieser Stelle soll das Hash Set gemäß der
	 * compareTo Methode sortiert werden // Collections.sort(signale,
	 * Signals.class); TreeSet<Kpi> sortedSignals = new TreeSet<>(signale);
	 * 
	 * for (Kpi s : sortedSignals) { if (s.longShort) { System.out.println("long");
	 * // verwaltung.placeLongOrder(s.instrument,s.lastPrice+0.001,
	 * s.getLongTakeProfit(), s.getLongStopLoss(), s.lastPrice);
	 * if((s.getLimitPrice()<s.getLongTakeProfit())&&(s.getLimitPrice()>s.
	 * getShortStopLoss())) {
	 * verwaltung.placeLongOrder(s.instrument,s.getLimitPrice(),
	 * s.getLongTakeProfit(), s.getLongStopLoss(), s.lastPrice);
	 * 
	 * 
	 * logFileWriter.log(s.instrument, s.lastTime,(connection.getBalance()*0.02),
	 * s.lastPrice, s.getLongTakeProfit(), s.getLongStopLoss(), s.macd,
	 * s.macdTriggert, s.parabolicSAR, s.ema); } else {
	 * System.out.println("InvalidValues: ");
	 * if(s.getLimitPrice()>=s.getLongTakeProfit())
	 * System.out.println("getLimitPrice>=getLongTakeProfit()" +" :"
	 * +s.getLimitPrice()+">="+s.getLongTakeProfit() ); else
	 * if(s.getLimitPrice()<=s.getLongStopLoss())
	 * System.out.println("getLimitPrice<=getLongStopLoss() :"
	 * +s.getLimitPrice()+">="+s.getLongStopLoss() ); }
	 * 
	 * } else if (!s.longShort) { System.out.println("short"); //
	 * verwaltung.placeShortOrder(s.instrument,s.lastPrice-0.001,
	 * s.getShortTakeProfit(), s.getShortStopLoss(), s.lastPrice); //
	 * logFileWriter.log(s.instrument, s.lastTime, s.lastPrice, s.getKaufpreis(),
	 * s.getShortTakeProfit());
	 * if((s.getLimitPrice()>s.getShortTakeProfit())&&(s.getLimitPrice()<s.
	 * getShortStopLoss())) {
	 * verwaltung.placeShortOrder(s.instrument,s.getLimitPrice(),
	 * s.getShortTakeProfit(), s.getShortStopLoss(), s.lastPrice);
	 * logFileWriter.log(s.instrument, s.lastTime,(connection.getBalance()*0.02),
	 * s.lastPrice, s.getShortTakeProfit(),
	 * 
	 * s.getShortStopLoss(), s.macd, s.macdTriggert, s.parabolicSAR, s.ema);
	 * 
	 * }
	 * 
	 * else { System.out.println("InvalidValues: ");
	 * if(s.getLimitPrice()<=s.getShortTakeProfit())
	 * System.out.println("getLimitPrice<=getShortTakeProfit()" +" :"
	 * +s.getLimitPrice()+"<="+s.getShortTakeProfit() ); else
	 * if(s.getLimitPrice()>=s.getShortStopLoss())
	 * System.out.println("getLimitPrice>=getShortStopLoss() :"
	 * +s.getLimitPrice()+">="+s.getShortStopLoss() ); } } }
	 * 
	 * signale = new HashSet<>();
	 * 
	 * }
	 */

	public static void ausgabe(String emaName, Kpi kpi, JsonInstrumentsInstrument instrument) {

		System.out.println(kpi.instrument + " " + instrument.displayName + " " + instrument.type + " " + emaName + ":  "
				+ kpi.ema + " MCAD: " + kpi.macd + " MACDTriggert: " + kpi.macdTriggert + " ParaboliocSAR: "
				+ kpi.parabolicSAR + " RSI: " + kpi.rsi + " ATR: " + kpi.atr /* + " Supertrend: " + kpi.superTrend */
				+ " SMA: " + kpi.sma + " (" + kpi.lastPrice + " min: " + kpi.min + " max: " + kpi.max + " avg: "
				+ kpi.avg + "  " + kpi.firstTime + " - " + kpi.lastTime + ")");
	}

	public static int kombiniereMACDSMA(Kpi kpi) {
		// long
		if (pruefePerioden(kpi, "MACD", 6) == -1) {
			if (pruefeSMACrossover(kpi, 6) == 1) {
				return 1;
			}
		}
		// short
		else if (pruefePerioden(kpi, "MACD", 6) == 1) {
			if (pruefeSMACrossover(kpi, 6) == -1) {
				return -1;
			}
		}
		return 0;
	}

	public static int kombiniereMACD_PSAR(Kpi werte) {
		if (pruefePerioden(werte, "MACD", 6) == -1) {
			if (pruefePSAR(werte) == 1) {
				// System.out.println("MACD_PSAR Long");
				return 1;
			}
		} else if (pruefePerioden(werte, "MACD", 6) == 1) {
			if (pruefePSAR(werte) == -1) {
				// System.out.println("MACD_PSAR Short");
				return -1;
			}
		}
		return 0;
	}

	// Testweise:
	public static int kombiniereEMA200ATR(Kpi werte) {
		// long
		if (pruefeEMA200(werte) == 1) {
			if (pruefeATR(werte) == 1)
				return 1;
		}
		// short
		else if (pruefeEMA200(werte) == -1) {
			if (pruefeATR(werte) == -1)
				return -1;
		}
		return 0;

	}

	public static int kombiniereMACDEMAPSAR(Kpi werte) {
		// public static Kpi kombiniereMACDEMAPSAR(ApiConnection connection) {
		// x = kurze Periode , y = lange Periode , z = Signallänge ; (Standardwerte:
		// 12,26,9)

		int rueckgabewert = 0;
		// Connection con = new Connection();
		// ApiConnection connection = new ApiConnection(con);
		// Ema ema= new Ema(connection);

		// Kpi werte = ema.aufrufAlles(instrument, emaperiods, periods, granularity,
		// startBF, inkrementBF, maxBF, x, y, z, multiplicatorUpper,
		// multiplicatorLower);

		// System.out.println(pruefeATR(werte));
		// System.out.println("Letzter Preis " +werte.lastPrice);
		// System.out.println("SAR " +werte.parabolicSAR);
		// System.out.println("ema " +werte.ema);
		// System.out.println("macd " +werte.macd);
		// System.out.println("macd verhältnis " + (werte.macdTriggert-werte.macd));
		// System.out.println("macd trigger " +werte.macdTriggert);
		// System.out.println("macd methode " +pruefeMACD(werte));
		// System.out.println("ema200 methode " +pruefeEMA200(werte));
		// System.out.println("psar methode " +pruefePSAR(werte));
		// System.out.println("vorperioden methode " +pruefeVorperioden(werte, "RSI"));
		// System.out.println("rsi methode " +pruefeRSI(werte));

		// ToDo: Doppelten Code vermeiden -> Funktionen zusammenlegen
		// Ermitteln welche Rückgabewerte zu einer Kaufentscheidung führt
		// Kaufposition aufrufen
		// MACD Periodencheck mit aktuellem MACD kombinieren?
		// Verfügbarkeit prüfen -> Wird der
		// pruefeVorperioden mit aktuellem MACD

		if (pruefeEMA200(werte) == 1) {
			// System.out.println("1.versuch"); // 1. liegt Trend (= 200 EMA) über Kurs?
			if (pruefePerioden(werte, "MACD", 5) == -1) { // 2. liegt MACD-Linie in den letzten 5 Perioden unter
															// Signallinie?
				if (pruefePSAR(werte) == 1) { // 5. ist der PSAR-Wert unter dem Kurs?
					// long //Long-Position
					// return werte;
					// System.out.println("long");
					rueckgabewert = 1;
				}
			}
		}

		else if (pruefeEMA200(werte) == -1) {
			// System.out.println("2.versuch"); // 1. liegt Trend unter Kurs?
			if (pruefePerioden(werte, "MACD", 5) == 1) { // 2. liegt MACD-Linie in den letzten 5 Perioden über
															// Signallinie?
				if (pruefePSAR(werte) == -1) { // 5. ist der PSAR-Wert über dem Kurs?
					// short //Short-Position
					// Verwaltung.placeOrder(String i, double wert, double kurs, double obergrenze,
					// double untergrenze);
					// Verwaltung.placeOrder(instrument, double wer, double kurs, double obergrenze,
					// double untergrenze);
					// return werte;
					// System.out.println("short");
					rueckgabewert = -1;
				}
			}
		}

		// wenn 0

		return rueckgabewert;
	}

	/*
	 * // alter Code public static int kombiniereMACDEMAPSAR(ApiConnection
	 * connection, Kpi werte) {
	 * 
	 * 
	 * // public static Kpi kombiniereMACDEMAPSAR(ApiConnection connection) { // x =
	 * kurze Periode , y = lange Periode , z = Signallänge ; (Standardwerte: //
	 * 12,26,9) // Connection con = new Connection(); // ApiConnection connection =
	 * new ApiConnection(con); boolean kaufentscheidung = false; JsonCandlesRoot h =
	 * werte.root;
	 * 
	 * // System.out.println(werte.ema); // System.out.println(werte.macd); //
	 * System.out.println(werte.macdTrigger); //
	 * System.out.println(pruefeMACD(werte)); //
	 * System.out.println(pruefeEMA200(werte)); //
	 * System.out.println(pruefePSAR(werte));
	 * 
	 * // ToDo: Abgleichen der Werte von EMA200, MACD und PSAR // Ermitteln welche
	 * Rückgabewerte zu einer Kaufentscheidung führt // Kaufposition aufrufen int
	 * rueckgabewert = 0;
	 * 
	 * if (pruefeEMA200(werte) == 1) { // 1. liegt Trend (= 200 EMA) über Kurs? if
	 * (pruefeVorperioden(werte, "MACD") == -1) { // 2. liegt MACD-Linie in den
	 * letzten 5 Perioden unter // Signallinie? if ((werte.macd -
	 * werte.macdTriggert) >= 0) { if(werte.prozent>0.0) { // 3. ist der aktuelle
	 * MACD auf oder über 0? // for (int i = 0; i < 2; i++) { //4.
	 * Schleifendurchlauf für nächste Bedingung if (pruefePSAR(werte) == 1) { // 5.
	 * ist der PSAR-Wert unter dem Kurs? // long //Long-Position // return werte;
	 * rueckgabewert = 1; } // } } } } }else if (pruefeEMA200(werte) == -1) { // 1.
	 * liegt Trend unter Kurs? if (pruefeVorperioden(werte, "MACD") == 1) { // 2.
	 * liegt MACD-Linie in den letzten 5 Perioden über // Signallinie? if
	 * ((werte.macd - werte.macdTriggert) <= 0) { if(werte.prozent<-0.0) {// 3. ist
	 * der aktuelle MACD auf oder unter 0?
	 * 
	 * // 4. Schleifendurchlauf für nächste Bedingung if (pruefePSAR(werte) == -1) {
	 * // 5. ist der PSAR-Wert über dem Kurs? rueckgabewert = -1; }
	 * 
	 * } } } } return rueckgabewert; }
	 */
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

	public static int pruefeEMA200(Kpi werte) {
		// Prüfe, ob der aktuelle Preis unter oder über des Langzeittrends (EMA200)
		// liegt
		// Ausgabewerte: 1 -> Kurs über Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich
		// Preis
		int rueckgabewert = 99;
		// double faktorRundung = 1.001;
		double ema200 = werte.ema;// * faktorRundung;

		double aktuellerKurs = werte.lastPrice;

		if (aktuellerKurs > ema200) {
			rueckgabewert = 1;
		} else if (aktuellerKurs < ema200) {
			rueckgabewert = -1;
		} else /* aktuellerKurs = ema200 */ {
			rueckgabewert = 0;
		}

		return rueckgabewert;
	}
	// richtig
	/*
	 * public static int pruefeEMA200(Kpi werte) { // Prüfe, ob der aktuelle Preis
	 * unter oder über des Langzeittrends (EMA200) // liegt // Ausgabewerte: 1 ->
	 * Kurs über Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich // Preis int
	 * rueckgabewert = 99; double ema200 = werte.ema; double aktuellerKurs =
	 * werte.lastPrice; /* if (aktuellerKurs > ema200) { rueckgabewert = 1; } if
	 * (aktuellerKurs < ema200) { rueckgabewert = -1; } else /*aktuellerKurs =
	 * ema200{ rueckgabewert = 0; }
	 */
	// Beide Schreibweisen gleich
	// rueckgabewert = aktuellerKurs > ema200 ? 1 : aktuellerKurs < ema200 ? -1 : 0;
	// return rueckgabewert;
//	}

//richtig
	/*
	 * public static int pruefePSAR(Kpi werte) { // Prüfue, ob der aktuelle Preis
	 * unter oder über dem Parabolic SAR liegt // Ausgabewerte: 1 -> PSAR-Punkt
	 * unter Preis; -1 -> PSAR-Punkt über Preis; 0 -> // PSAR-Punkt gleich Preis int
	 * rueckgabewert = 99; double aktuellerKurs = werte.lastPrice; double PSAR =
	 * werte.parabolicSAR; // Kannst du selber entscheiden welche Schreibweise dir
	 * lieber ist. /* if (aktuellerKurs > PSAR) { rueckgabewert = 1; } if
	 * (aktuellerKurs < PSAR) { rueckgabewert = -1; } else /*aktuellerKurs = PSAR
	 */
	// rueckgabewert = 0;

	// rueckgabewert = aktuellerKurs > PSAR ? 1 : aktuellerKurs < PSAR ? -1 : 0;
	// return rueckgabewert;

	// }

	public static int pruefePSAR(Kpi werte) {
		// Prüfue, ob der aktuelle Preis unter oder über dem Parabolic SAR liegt
		// Ausgabewerte: 1 -> PSAR-Punkt unter Preis; -1 -> PSAR-Punkt über Preis; 0 ->
		// PSAR-Punkt gleich Preis
		int rueckgabewert = 99;
		double aktuellerKurs = werte.lastPrice;
		double PSAR = werte.parabolicSAR;
		if (aktuellerKurs > PSAR) {
			rueckgabewert = 1;
		} else if (aktuellerKurs < PSAR) {
			rueckgabewert = -1;
		} else /* aktuellerKurs = PSAR */ {
			rueckgabewert = 0;
		}
		return rueckgabewert;

	}

	public static int pruefePerioden(Kpi werte, String entscheideSignal, int anzahlVorperioden) {
		// Die Methode, soll die Vorperiode prüfen, ob bestimmte Ereignisse vorgefallen
		// sind oder nicht
		// Dabei werden die Methoden pruefeMACD() und pruefeRSI() zusammengelegt
		int ausgabe = 99;
		int MACDRueckgabewert = 101;
		int RSIRueckgabewert = 102;
		boolean verhaeltnisVorzeichenNegativ = false;
		boolean verhaeltnisVorzeichenPositiv = false;
		boolean RSIOverbought = false; // RSI über 70%
		boolean RSIOversold = false; // RSI unter 30%
		int MACDAktuell = 99;

		// anzahl Vorperioden falscher Übergabewert
		if (anzahlVorperioden < 2)
			return ausgabe;

		for (int i = 1; i < anzahlVorperioden + 2; i++) {
			double macd = werte.macds.get(werte.macds.size() - i);
			double trigger = werte.macdsTriggert.get(werte.macdsTriggert.size() - i);

			double macdVerhaeltnis = macd - trigger;
			System.out.println(i + ". Durchlauf: Verhältnis " + macdVerhaeltnis);
			// System.out.println(MACDAktuell);
			// Wie ist das aktuelle Verhältnis?:
			if (i == 1) {
				if (macdVerhaeltnis < 0) {
					MACDAktuell = -1;
					// System.out.println("kleiner "+MACDAktuell);
				} else if (macdVerhaeltnis > 0) {
					MACDAktuell = 1;
					// System.out.println("größer "+MACDAktuell);
				} else if (macdVerhaeltnis == 0) {
					MACDAktuell = 0;
				}

			}
			// Vorperioden
			else if (i > 0 && i != 1) {
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
		}
		if (verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == false
				&& (MACDAktuell == 1 || MACDAktuell == 0)) {
			// die letzten MACDs sind negativ und der Aktuelle positiv oder null
			MACDRueckgabewert = -1;

		} else if (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == true
				&& (MACDAktuell == -1 || MACDAktuell == 0)) {
			// die letzten MACDs sind positiv und der Aktuelle negativ oder null
			MACDRueckgabewert = 1;
		} else if ((verhaeltnisVorzeichenNegativ == true && verhaeltnisVorzeichenPositiv == true)
				|| (verhaeltnisVorzeichenNegativ == false && verhaeltnisVorzeichenPositiv == false)) {
			// die letzten MACDs haben nicht das gleiche Vorzeichen
			MACDRueckgabewert = 0;
		} else {
			MACDRueckgabewert = 99;
		}

		if (RSIOversold == true && RSIOverbought == false) {
			// die letzten x RSIs sind Oversold, also unter 30%
			RSIRueckgabewert = -1;
		} else if (RSIOversold == false && RSIOverbought == true) {
			// die letzten x RSIs sind Overbought, also über 70%
			RSIRueckgabewert = 1;
		} else if ((RSIOversold == true && RSIOverbought == true) || (RSIOversold == false && RSIOverbought == false)) {
			// die letzten x RSIs schwanken oder liegen alle zwischen 30 und 70 Prozent
			RSIRueckgabewert = 0;
		} else {
			RSIRueckgabewert = 99;
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

	public static int pruefeATR(Kpi werte) {
		// Wo ist der niedrigste und Höchste ATR-Wert
		// Dieser Wert ist der Vergleichspunkt mit dem aktuellen ATR Wert
		// Jedoch soll der Wert nur ungefär gleich sein, weil eine genaue
		// Übereinstimmung zu unrealistisch ist
		// Und weil der Indikator nur ein Hilfsindikator ist
		// der ATR gibt aus, wie oft das aktuelle Instrument in den letzten 14 Perioden
		// den Wert geändert hat
		// Wenn der ATR am niedrigsten Punkt ist, ist auszugehen, dass das Instrument in
		// den nächsten Perioden stärker
		// nachgefragt wird, jedoch gibt er keine Auskunft in welche Richtung
		int rueckgabe = 0;
		// System.out.println("vanilla " + werte.atr);
		/*
		 * double y = werte.atr*100000; System.out.println("test "+ runden(y));
		 * 
		 * 
		 * double x = runden(werte.atr); x *=1000; System.out.println(x);
		 */

		double relativesMinimum = 1000;
		double relativesMaximum = 0;
		for (int i = 2; i < werte.atrListe.size() + 1; i++) {
			double vergleich = werte.atrListe.get(werte.atrListe.size() - i);
			if (vergleich < relativesMinimum) {
				relativesMinimum = vergleich;
			}
			if (vergleich > relativesMaximum) {
				relativesMaximum = vergleich;
			}
		}
		// System.out.println("relativesMaximum " + relativesMaximum + ";
		// relativesMinimum " + relativesMinimum);
		double prozentsatz = ((relativesMinimum * 100 / relativesMaximum) + 1) / 100;
		// System.out.println("prozentsatz " + prozentsatz);
		double aktuellerATR = werte.atr + prozentsatz;
		// System.out.println("aktueller atr " + werte.atr + " neuer ATR " +
		// aktuellerATR);

		if (relativesMinimum < aktuellerATR) {
			// aktueller ATR ist nicht am niedrigsten Punkt
			rueckgabe = -1;
		} else if (relativesMinimum >= aktuellerATR) {
			rueckgabe = 1;
		}

		return rueckgabe;

	}

	public static int pruefeSMACrossover(Kpi kpi, int anzahlVorperioden) {

		int ausgabe = 99;

		// Kpi SMA20 = e.getSMA(instrument,20,granularity,jcr);
		// Kpi SMA50 = KpiCalculator.getSMA(instrument,50,granularity,jcr);
		double sma20Aktuell = kpi.sma;
		double sma50Aktuell = kpi.KpiList.get(0).sma;

		boolean SMA20KleinerSMA50 = false;
		boolean SMA20GroesserSMA50 = false;

		for (int i = 2; i < anzahlVorperioden +2; i++) {
			double sma20 = kpi.smaList.get(kpi.smaList.size() - i);
		double sma50 = kpi.KpiList.get(0).smaList.get(kpi.KpiList.get(0).smaList.size() - i);
		/*	Kpi kpi2=kpi.KpiList.get(0);
			double sma50=kpi2.smaList.get(kpi2.smaList.size() - i);*/
			if (sma20 < sma50) {
				SMA20KleinerSMA50 = true;
			} else if (sma20 > sma50) {
				SMA20GroesserSMA50 = true;
			}

		}

		if (SMA20KleinerSMA50 == true && SMA20GroesserSMA50 == false && sma20Aktuell >= sma50Aktuell) {
			// SMA20 nähert sich von unten an den Crossover
			ausgabe = 1;

		} else if (SMA20KleinerSMA50 == false && SMA20GroesserSMA50 == true && sma20Aktuell <= sma50Aktuell) {
			// SMA20 nähert sich von oben an den Crossover
			ausgabe = -1;
		} else if ((SMA20KleinerSMA50 == true && SMA20GroesserSMA50 == true)
				|| (SMA20KleinerSMA50 == false && SMA20GroesserSMA50 == false)) {
			// Mehrere Crossover --> keine Prüfung der aktuellen Werte erforderlich
			ausgabe = 0;

		} else { // wenn if oder erstes else if die ersten beiden bedingungn wahr sind
			ausgabe = 0;
		}

		return ausgabe;
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