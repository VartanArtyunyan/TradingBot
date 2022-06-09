package de.fhws.Softwareprojekt;

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
import LogFileWriter.LogFileWriter;
import positionen.Verwaltung;

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
	
	//Tom
/*	 Kpi production=new Kpi();
	Verwaltung verwaltung;
	LogFileWriter logFileWriter;
	Signals s=new Signals(connection, verwaltung, logFileWriter);*/

	
	
//  startMe BeforeAll Methode has to be static
	@BeforeAll
	public void BeforeAll() {

/*		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) {

			if (instrument.type.compareTo("CURRENCY") == 0) {

				Kpi kpi = werte.getAll(instrument.name, 200, 14, "M15", 0.02, 0.02, 0.2, 12, 26, 9);
				currencies.add(kpi);
				JsonCandlesRoot jcr = werte.getCandles(instrument.name, "M15");

				basicKpi = werte.getBasisKpi(instrument.name, 200, "M15", jcr);
				basicKpiList.add(basicKpi);
			}
		}*/
		//ArrayList<String>s=new ArrayList<String>();
		//Object o=   "currenciesString.parallelStream().sorted().forEach(k->basicKpiList.add(werte.getBasisKpi(k, 200,\"M15\",werte.getCandles(k, \"M15\"))))";
		//String b="currenciesString.parallelStream().sorted().forEach(k->currencies.add(werte.getAll(k, 200, 14, \"M15\", 0.02, 0.02, 0.2, 12, 26, 9)))";
		
				// "M15",werte.getCandles(k, "M15"))))	;
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments)
		{
		if(instrument.type.compareTo("CURRENCY") == 0)
			currenciesString.add(instrument.name);
		}
		currenciesString.parallelStream().sorted().forEach(k->{currencies.add(werte.getAll(k, 200, 14, "M15", 0.02, 0.02, 0.2, 12, 26, 9));
		basicKpiList.add(werte.getBasisKpi(k, 200, "M15",werte.getCandles(k, "M15")));});
		
	//	currenciesString.parallelStream().sorted().forEach(k->currencies.add(werte.getBasisKpi(k, 200, "M15",werte.getCandles(k, "M15"))))	;
	//	currenciesString.parallelStream().sorted().forEach(k->basicKpiList.add(werte.getAll(k, 200, 14, "M15", 0.02, 0.02, 0.2, 12, 26, 9)));
	}
@Test
public void RSITest()
{
	try
	{
	currencies.forEach(kpi->assertTrue((kpi.rsi>=0)&&(kpi.rsi<=100)));
	}
	catch(Exception e)
	{
		fail("Hätte keine Ausnahme gefeurt");
	}
	
}
@Test
public void RSIIntensityTest()
{
	try
	{
	currencies.forEach(kpi->assertTrue((kpi.macdIntensity>=-1&&kpi.macdIntensity<=1)&&kpi.macdIntensitys.get(kpi.macdIntensitys.size()-1)==kpi.macdIntensity));
	}
	catch(Exception e)
	{
		fail("Hätte keine Ausnahme gefeurt");
	}
}
	@Test
	public void MacdTest() {
		//-1 und 1 sind keine Grenzwerte. Trotzdem wäre es verwunderlich, wenn ein Macd und MacdTriggert in dieser Größenordnung erscheint
	try
	{
currencies.forEach(kpi->assertTrue((kpi.macd<1&&kpi.macd>-1)&&(kpi.macdTriggert<1&&kpi.macdTriggert>-1)));
	}
	catch(Exception e)
	{
		fail("Hätte keine Ausnahme feuern duerfen");
	}
	
	}
@Test
public void parabiolicSarTest()
{
	
	int zaehlerUeberKurs=0;
	int zaehlerUnterKurs=0;
	for(Kpi k:currencies)
	{
		if(k.parabolicSAR>=k.lastPrice)
			zaehlerUeberKurs++;
		if(k.parabolicSAR<=k.lastPrice)
			zaehlerUnterKurs++;
	}
	if(zaehlerUeberKurs==currencies.size()||zaehlerUnterKurs==currencies.size())
		fail("Parabolic SAR entweder überall unter Kurs oder überall über den Kurs");
	
}
//Checken dass der macd 4 unter macdTriggert liegt
@Test
public void MacdMacdsTriggertlongCheck()
{
	int hauptzaehler=0;
	int count=0;
for(Kpi k:currencies)
{	
	count=0;
	
	int zaehler=0;
	for(int i=k.macds.size()-6;i<k.macds.size();i++)
	{
		if(k.macdsTriggert.get(i-8)>k.macds.get((i)))
		{
		zaehler++;
		count++;
		}

}
	
	hauptzaehler=zaehler==6?hauptzaehler+=1:hauptzaehler;
}

System.out.println(hauptzaehler);
if(hauptzaehler==0)
fail("Keine Chance auf ein Long Signal in dem Durchlauf");
}



@Test
public void emaTest()
{
	int zaehlerUeberKurs=0;
	int zaehlerUnterKurs=0;
	for(Kpi k:currencies)
	{
		if(k.ema>=k.lastPrice)
			zaehlerUeberKurs++;
		if(k.ema<=k.lastPrice)
			zaehlerUnterKurs++;
	}
	if(zaehlerUeberKurs==currencies.size()||zaehlerUnterKurs==currencies.size())
		fail("Ema entweder überall unter Kurs oder überall über den Kurs");
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
		double zahl = (double) zaehler / currencies.size();
		if(zahl<=0.5)
		fail("Die Zahl lautet" + zahl);  
	}
	
	//Tom Kombination Bereich
	@Test
	public void checkSchneiden()
	{
	}
}


