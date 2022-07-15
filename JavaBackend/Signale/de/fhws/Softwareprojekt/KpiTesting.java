package de.fhws.Softwareprojekt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import API.ApiConnection;
import API.Connection;
import positionen.Verwaltung;

//Damit Before-All als nicht statisch deklariert werden kann
@TestInstance(Lifecycle.PER_CLASS)
public class KpiTesting {
	ArrayList<Kpi> currencies = new ArrayList<>();
	Connection con = new Connection();
	ApiConnection connection = new ApiConnection(con);
	Verwaltung verwaltung=new Verwaltung(connection, connection, "M15", 2000);
	Kpi basicKpi;
	ArrayList<Kpi> basicKpiList = new ArrayList<>();
	KpiCalculator werte = new KpiCalculator(verwaltung);
	JsonInstrumentsRoot instrumentsRoot = werte.getInstruments();
	ArrayList<String> currenciesString = new ArrayList<String>();
	@BeforeAll
	public void beforeAll() {

		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) {
			if (instrument.type.compareTo("CURRENCY") == 0)
				currenciesString.add(instrument.name);
		}
		// public Kpi getAll(String instrument, String granularity, Object... signale)
		currenciesString.parallelStream().sorted().forEach(k -> {
			currencies.add(werte.getAll(k, "M15", 200, "sma", 20, "sma", 50, "atr", 14, "parabolicSAR", 14, 0.02, 0.02,
					0.2, "macd", 12, 26, 9, "rsi", 14));
			basicKpiList.add(werte.getBasisKpi(k, 200, "M15", werte.getCandles(k, "M15")));
		});
	}

	@Test
	public void rsiTest() {
		try {
			currencies.forEach(kpi -> assertTrue((kpi.rsi >= 0) && (kpi.rsi <= 100)));
		} catch (Exception e) {
			fail("Hätte keine Ausnahme feuern dürfen");
		}

	}

	@Test
	public void rsiIntensityTest() {
		try {
			currencies.forEach(kpi -> assertTrue((kpi.macdIntensity >= -1 && kpi.macdIntensity <= 1)
					&& kpi.macdIntensitys.get(kpi.macdIntensitys.size() - 1) == kpi.macdIntensity));
		} catch (Exception e) {
			fail("Hätte keine Ausnahme feuern dürfen");
		}
	}

	@Test
	public void macdTest() {
		// -1 und 1 sind keine Grenzwerte. Trotzdem wäre es verwunderlich, wenn ein Macd
		// und MacdTriggert in dieser Größenordnung erscheint
		try {
			currencies.forEach(kpi -> assertTrue(
					(kpi.macd < 2 && kpi.macd > -2) && (kpi.macdTriggert < 2 && kpi.macdTriggert > -2)));
		} catch (Exception e) {
			fail("Hätte keine Ausnahme feuern duerfen");
		}

	}

	@Test
	public void parabiolicSarTest() {

		int zaehlerUeberKurs = 0;
		int zaehlerUnterKurs = 0;
		for (Kpi k : currencies) {
			if (k.parabolicSAR >= k.lastPrice)
				zaehlerUeberKurs++;
			if (k.parabolicSAR <= k.lastPrice)
				zaehlerUnterKurs++;
		}
		if (zaehlerUeberKurs == currencies.size() || zaehlerUnterKurs == currencies.size())
			fail("Parabolic SAR entweder überall unter Kurs oder überall über den Kurs");

	}

//Checken dass der macd 4 unter macdTriggert liegt
	@Test
	public void macdMacdsTriggertlongCheck() {
		int hauptzaehler = 0;

		for (Kpi k : currencies) {

			int zaehler = 0;
			for (int i = k.macds.size() - 6; i < k.macds.size(); i++) {
				if (k.macdsTriggert.get(i - 8) > k.macds.get((i))) {
					zaehler++;

				}

			}

			hauptzaehler = zaehler == 6 ? hauptzaehler += 1 : hauptzaehler;
		}

		System.out.println(hauptzaehler);
		if (hauptzaehler == 0)
			fail("Keine Chance auf ein Long Signal in dem Durchlauf");
	}

	@Test
	public void emaTest() {
		int zaehlerUeberKurs = 0;
		int zaehlerUnterKurs = 0;
		for (Kpi k : currencies) {
			if (k.ema >= k.lastPrice)
				zaehlerUeberKurs++;
			if (k.ema <= k.lastPrice)
				zaehlerUnterKurs++;
		}
		if (zaehlerUeberKurs == currencies.size() || zaehlerUnterKurs == currencies.size())
			fail("Ema entweder überall unter Kurs oder überall über den Kurs");
	}

	@Test
	public void emaGrenzwerteCheck() {
		for (Kpi k : currencies) {
			assertTrue((k.ema > 0) && (!(k.ema > k.lastPrice * 1.04)) && (!(k.ema < k.lastPrice * 0.96)));
		}
	}

//Beweisen das getBasicKpi zuverlässig in getAll aufgerufen wird.
	@Test
	public void getKpiCheck() {

		int zaehler = 0;
		for (Kpi k : currencies) {
			for (Kpi l : basicKpiList) {

				if (l.ema == k.ema && k.avg == l.avg)

					zaehler++;
			}
		}
		double zahl = (double) zaehler / currencies.size();
		System.out.println(zahl);
		if (zahl <= 0.2) {
			fail("Die Zahl lautet" + zahl);
		}
	}

	@Test
	public void checkPerceicionTest() {

		for (Kpi k : currencies) {

			if (k.instrument.compareTo("USB_THB") == 0) {
				assertTrue((k.checkPrecision(k.lastPrice, true) > k.lastPrice)
						&& ((k.checkPrecision(k.lastPrice, true) - k.lastPrice < 0.001)));
				assertTrue((k.checkPrecision(k.lastPrice, false) < k.lastPrice)
						&& ((k.checkPrecision(k.lastPrice, true) - k.lastPrice > -0.001)));
			}
		}
	}

	@Test
	public void smaTest() {
		for (Kpi k : currencies) {

			if (k.sma > k.lastPrice) {
				assertFalse((k.sma > 1.05 * k.lastPrice));

			}
			if (k.sma < k.lastPrice)
				assertFalse(k.lastPrice > 1.05 * k.sma);
		}

	}

	@Test
	public void atrTest() {

		for (Kpi k : currencies) {
			assertTrue(k.lastPrice > k.atr * 100);
		}

	}

	@Test
	public void KpiList() {
		for (Kpi k : currencies) {
			assertTrue(k.KpiList.size() == 1);
			assertTrue(k.KpiList.get(0).sma > 0);
		}
	}

	// Tom Kombination Bereich
	@Test
	public void kombiniereMACDEMAPSARTest() {
		for (Kpi k : currencies) {
			int ausgabe = Signals.kombiniereMACDEMAPSAR(k);
			assertTrue(ausgabe == 0 || ausgabe == 1 || ausgabe == -1);
		}
	}

	@Test
	public void kombiniereEMA200ATRTest() {
		for (Kpi k : currencies) {
			int ausgabe = Signals.kombiniereEMA200ATR(k);
			assertTrue(ausgabe == 0 || ausgabe == 1 || ausgabe == -1);
		}
	}

	@Test
	public void kombiniereMACD_PSARTest() {
		for (Kpi k : currencies) {
			int ausgabe = Signals.kombiniereMACD_PSAR(k);
			assertTrue(ausgabe == 0 || ausgabe == 1 || ausgabe == -1);
		}
	}

	@Test
	public void kombiniereMACDSMATest() {
		for (Kpi k : currencies) {
			int ausgabe = Signals.kombiniereMACDSMA(k);
			assertTrue(ausgabe == 0 || ausgabe == 1 || ausgabe == -1);
		}
	}

	@Test
	public void pruefeSMACrossoverNullPeriodenTest() {

		// Grenzwert 0
		for (Kpi k : currencies) {
			// Schleife wird nicht durchlaufen, somit beide Boolean-Werte false --> Ausgabe
			// = 0
			assertTrue(Signals.pruefeSMACrossover(k, 0) == 0);
		}

	}

	//
	@Test
	public void pruefeSMACrossoverAllePeriodenTest() {

		// Über alle Perioden
		for (Kpi k : currencies) {
		//	int laenge = k.smaList.size()-1;
			int laenge=k.KpiList.get(0).smaList.size()-1;
			assertTrue(Signals.pruefeSMACrossover(k, laenge) == 0);
		}

	}

	@Test
	public void pruefeATRTest() {
		for (Kpi k : currencies) {
			// der Rückgabewert kann nur 1 oder -1 sein
			assertFalse(Signals.pruefeATR(k) == 0);
		}
	}

	@Test
	public void pruefePeriodenEntscheideSignalWrongInputTest() {

		for (Kpi k : currencies) {
			// entscheideSignal nicht wie in Methode gefordert
			assertTrue(Signals.pruefePerioden(k, "macd", 2) == 99);
			assertTrue(Signals.pruefePerioden(k, "rsi", 2) == 99);

		}
	}

	@Test
	public void pruefePeriodenAnzahlVorperiodenWrongInputTest() {

		for (Kpi k : currencies) {
			// anzahlVorperioden nicht >1
			assertTrue(Signals.pruefePerioden(k, "MACD", -10) == 99);

		}
	}

	@Test
	public void pruefePeriodenMACDRightInputTest() {
		for (Kpi k : currencies) {
			// Es kann nur 99, 1, 0, und -1 rauskommen
			// 101 wird durch die else in den if-Bedingungen überschrieben
			int ausgabe = Signals.pruefePerioden(k, "MACD", 5);
			assertTrue(ausgabe == 99 || ausgabe == 1 || ausgabe == 0 || ausgabe == -1);
		}
	}

	@Test
	public void pruefePeriodenRSIRightInputTest() {
		for (Kpi k : currencies) {
			// Es kann nur 99, 1, 0, und -1 rauskommen
			// 102 wird durch die else in den if-Bedingungen überschrieben
			int ausgabe = Signals.pruefePerioden(k, "RSI", 5);
			assertTrue(ausgabe == 99 || ausgabe == 1 || ausgabe == 0 || ausgabe == -1);
		}
	}

	@Test
	public void pruefePSARTest() {
		for (Kpi k : currencies) {
			assertTrue(Signals.pruefePSAR(k) != 99);
		}
	}

	@Test
	public void pruefeEMA200Test() {
		for (Kpi k : currencies) {
			assertTrue(Signals.pruefeEMA200(k) != 99);
		}
	}
}
