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

	public Signals(Verwaltung verwaltung, LogFileWriter logFileWriter, String granularity) {
		this.verwaltung = verwaltung;
		this.logFileWriter = logFileWriter;
		this.granularity = granularity;
		this.e = new KpiCalculator(verwaltung);
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
			Kpi kpi = e.getAll(instrument.name, "M15", 200, "sma", 20, "sma", 50, "atr", 14, "parabolicSAR",14, 0.02, 0.02,
					0.2, "macd", 12, 26, 9,"rsi",14);
			//für TP/SL-Entscheidung
			// nach kauf für 6 x granularität insturment sperren
			if (signal0 | signal1) {
				int r = kombiniereMACDEMAPSAR(kpi);
				if (r != 0) {
					// von der Methode zu überprüfen
					kpi.longShort = (r == 1) ? true : false;
					kpi = kpi.resetKpiElements(kpi, "atr", "sma", "sma50", "rsi");
					if (signal0)
						kpi.signalTyp = 1;
						verwaltung.pushSignal(kpi);
				} else if (signal1) {
					int s = kombiniereMACDPSAR(kpi);
					if (s != 0) {
						//sperrt ebenfalls signal 0 und 1 & signal 2 soll nur signal 2 sperren
						kpi.signalStrenght = 0.5;
						kpi.longShort = (s == 1) ? true : false;
						kpi.signalTyp = 1;
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
					kpi.signalStrenght = 0.5;
					kpi.longShort = (t == 1) ? true : false;
					kpi = kpi.resetKpiElements(kpi, "sma", "sma50", "rsi", "macd", "macdTriggert");
					kpi.signalTyp = 2;
					verwaltung.pushSignal(kpi);
				}
				kpi.useATRAsSLTP = false;
			}
			if (signal3) {
				int u = kombiniereMACDSMA(kpi);
				if (u != 0) {
					kpi.signalStrenght = 0.5;
					kpi.longShort = (u == 1) ? true : false;
					kpi = kpi.resetKpiElements(kpi, "rsi", "atr", "parabolicSAR", "ema");
					kpi.signalTyp = 3;
					verwaltung.pushSignal(kpi);
				}
			}
		}
	}

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

	public static int kombiniereMACDPSAR(Kpi kpi) {
		if (pruefePerioden(kpi, "MACD", 6) == -1) {
			if (pruefePSAR(kpi) == 1) {
				return 1;
			}
		} else if (pruefePerioden(kpi, "MACD", 6) == 1) {
			if (pruefePSAR(kpi) == -1) {
				return -1;
			}
		}
		return 0;
	}

	public static int kombiniereEMA200ATR(Kpi kpi) {
		// long
		if (pruefeEMA200(kpi) == 1) {
			if (pruefeATR(kpi) == 1)
				return 1;
		}
		// short
		else if (pruefeEMA200(kpi) == -1) {
			if (pruefeATR(kpi) == -1)
				return -1;
		}
		return 0;

	}

	public static int kombiniereMACDEMAPSAR(Kpi kpi) {
		int rueckgabewert = 0;
		if (pruefeEMA200(kpi) == 1) {					// 1. liegt Trend (= 200 EMA) über Kurs?
			if (pruefePerioden(kpi, "MACD", 5) == -1) { // 2. liegt MACD-Linie in den letzten 5 Perioden unter
														// der Signallinie?
				if (pruefePSAR(kpi) == 1) { 			// 3. ist der Kurs über dem PSAR-Wert?
					rueckgabewert = 1;
				}
			}
		}

		else if (pruefeEMA200(kpi) == -1) {				// 1. liegt Trend unter Kurs?
			if (pruefePerioden(kpi, "MACD", 5) == 1) { 	// 2. liegt MACD-Linie in den letzten 5 Perioden über
														// Signallinie?
				if (pruefePSAR(kpi) == -1) { 			// 3. ist der Kurs unter dem PSAR-Wert?
					rueckgabewert = -1;
				}
			}
		}
		return rueckgabewert;
	}

	public static int pruefeMACD(Kpi kpi) {
		//Optionale Prüfung, ob MACD-Trend in den Vorperioden optimal ist

		boolean verhaeltnisVorzeichenNegativ = false;
		boolean verhaeltnisVorzeichenPositiv = false;
		int rueckgabewert = 99;
		for (int i = 2; i < 7; i++) {
			double macd = kpi.macds.get(kpi.macds.size() - i);
			double trigger = kpi.macdsTriggert.get(kpi.macdsTriggert.size() - i);
			double macdVerhaeltnis = macd - trigger;
			if (macdVerhaeltnis < 0) {
				verhaeltnisVorzeichenNegativ = true;
			} else if (macdVerhaeltnis > 0) {
				verhaeltnisVorzeichenPositiv = true;
			} else /* macdVerhaeltnis = 0 */ {
				break;
			}
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

	public static int pruefeRSI(Kpi kpi) {

		int rueckgabwert = 0;
		boolean longPosition = true;
		boolean shortPosition = true;
		// Jeweils checken ob der Wert des RSI kliner 50 war und die Vorgänger auch long
		// Jeweils checken ob der Wert des RSI größer 50 sind und die Vorgänger auch
		// short
		for (int i = 2; i < 7; i++) {
			longPosition = (kpi.rsiListe.get(kpi.rsiListe.size() - i) < 50 && longPosition == true) ? true : false;
			shortPosition = (kpi.rsiListe.get(kpi.rsiListe.size() - i) > 50 && shortPosition == true) ? true
					: false;
		}
		// Checken ob Vorzeichenwechsel
		return rueckgabwert = (longPosition == true && kpi.rsi > 50) ? 1
				: shortPosition == true && kpi.rsi > 50 ? -1 : 0;
	}

	public static int pruefeSMA(Kpi kpi) {
		return kpi.sma > kpi.lastHighestPrice ? 1 : kpi.sma < kpi.lastLowestPrice ? -1 : 0;
	}

	public static int pruefeEMA200(Kpi kpi) {
		// Prüfe, ob der aktuelle Preis unter oder über des Langzeittrends (EMA200)
		// liegt
		// Ausgabewerte: 1 -> Kurs über Trend; -1 -> Kurs unter Trend; 0 -> Kurs gleich
		// Preis
		int rueckgabewert = 99;
		// double faktorRundung = 1.001;
		double ema200 = kpi.ema;// * faktorRundung;

		double aktuellerKurs = kpi.lastPrice;

		if (aktuellerKurs > ema200) {
			rueckgabewert = 1;
		} else if (aktuellerKurs < ema200) {
			rueckgabewert = -1;
		} else /* aktuellerKurs = ema200 */ {
			rueckgabewert = 0;
		}

		return rueckgabewert;
	}

	public static int pruefePSAR(Kpi kpi) {
		// Prüfue, ob der aktuelle Preis unter oder über dem Parabolic SAR liegt
		// Ausgabewerte: 1 -> PSAR-Punkt unter Preis; -1 -> PSAR-Punkt über Preis; 0 ->
		// PSAR-Punkt gleich Preis
		int rueckgabewert = 99;
		double aktuellerKurs = kpi.lastPrice;
		double PSAR = kpi.parabolicSAR;
		if (aktuellerKurs > PSAR) {
			rueckgabewert = 1;
		} else if (aktuellerKurs < PSAR) {
			rueckgabewert = -1;
		} else /* aktuellerKurs = PSAR */ {
			rueckgabewert = 0;
		}
		return rueckgabewert;

	}

	public static int pruefePerioden(Kpi kpi, String entscheideSignal, int anzahlVorperioden) {
		// Die Methode, soll die Vorperioden prüfen, ob bestimmte Ereignisse vorgefallen
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
			double macd = kpi.macds.get(kpi.macds.size() - i);
			double trigger = kpi.macdsTriggert.get(kpi.macdsTriggert.size() - i);

			double macdVerhaeltnis = macd - trigger;
			//System.out.println(i + ". Durchlauf: Verhältnis " + macdVerhaeltnis);
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
				if (kpi.rsiListe.get(kpi.rsiListe.size() - i) > 70) {
					RSIOverbought = true;
				} else if (kpi.rsiListe.get(kpi.rsiListe.size() - i) < 30) {
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

	public static int pruefeATR(Kpi kpi) {
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
		double relativesMinimum = 1000;
		double relativesMaximum = 0;
		for (int i = 2; i < kpi.atrListe.size() + 1; i++) {
			double vergleich = kpi.atrListe.get(kpi.atrListe.size() - i);
			if (vergleich < relativesMinimum) {
				relativesMinimum = vergleich;
			}
			if (vergleich > relativesMaximum) {
				relativesMaximum = vergleich;
			}
		}
		double prozentsatz = ((relativesMinimum * 100 / relativesMaximum) + 1) / 100;
		double aktuellerATR = kpi.atr + prozentsatz;
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

	
	
}