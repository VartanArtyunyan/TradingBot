package de.fhws.Softwareprojekt;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.BeforeAll;

import API.ApiConnection;
import API.Connection;
import de.fhws.Softwareprojekt.JsonInstrumentsInstrument;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;

//Damit Before-All als nicht statisch deklariert werden kann
@TestInstance(Lifecycle.PER_CLASS)
public class Testing {
	ArrayList<Kpi> currencies = new ArrayList<>();
	// double [] macd=new double[] {1,1,1,1,1};
	Connection con = new Connection();
	ApiConnection connection = new ApiConnection(con);
	Kpi basicKpi;
	ArrayList<Kpi> basicKpiList = new ArrayList<>();
	KpiCalculator werte = new KpiCalculator(connection);
	JsonInstrumentsRoot instrumentsRoot = werte.getInstruments();
	ArrayList<String>currenciesString=new ArrayList<String>();
	
	
//  startMe BeforeAll Methode has to be static
	@BeforeAll
	public void first() {

/*		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) {

			if (instrument.type.compareTo("CURRENCY") == 0) {

				Kpi kpi = werte.getAll(instrument.name, 200, 14, "M15", 0.02, 0.02, 0.2, 12, 26, 9);
				currencies.add(kpi);
				JsonCandlesRoot jcr = werte.getCandles(instrument.name, "M15");

				basicKpi = werte.getBasisKpi(instrument.name, 200, "M15", jcr);
				basicKpiList.add(basicKpi);
			}
		}*/
		
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments)
		{
		if(instrument.type.compareTo("CURRENCY") == 0)
			currenciesString.add(instrument.name);
		}
		   currenciesString.parallelStream().sorted().forEach(k->basicKpiList.add(werte.getBasisKpi(k, 200, "M15",werte.getCandles(k, "M15"))))	;
				currenciesString.parallelStream().sorted().forEach(k->currencies.add(werte.getAll(k, 200, 14, "M15", 0.02, 0.02, 0.2, 12, 26, 9)));
	}

	@Test
	public void MacdTest() {
		
		int zaehlerMACD = 0;
		int zaehlerMACDTriggert = 0;

		for (Kpi kpi : currencies) {
			zaehlerMACD = ((kpi.macd < 1) && (kpi.macd > -1)) ? zaehlerMACD : zaehlerMACD++;
			zaehlerMACDTriggert = ((kpi.macdTriggert < 1) && (kpi.macdTriggert > -1)) ? zaehlerMACD : zaehlerMACD++;
		}
		assertTrue((zaehlerMACD <= 1) && (zaehlerMACDTriggert <= 1));
	}

	@Test
	public void ab() {
		try {
			assertEquals(1, 1);
		}

		catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	@Test
	public void EmaGrenzwerteCheck() {
		for (Kpi k : currencies) {
			assertTrue((k.ema > 0) && (!(k.ema > k.lastPrice * 1.04)) && (!(k.ema < k.lastPrice * 0.96)));
		}
	}
//Beweisen das wenn man getBasicKpi zuverlässig in getAll aufgerufen wird.
	@Test
	public void getKpiCheck() {
		int zaehler = 0;
		for (Kpi k : currencies) {

			for (Kpi l : basicKpiList) {
				if ((l.ema == k.ema) && (l.avg == k.avg) && (l.lastPrice == k.lastPrice))

					zaehler++;
			}

		}
		System.out.println(zaehler);
		double zahl = (double) zaehler / currencies.size();
		System.out.println(zahl);
		assertTrue(zahl > 0.7);
	}
}


