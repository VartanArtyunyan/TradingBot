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
	ArrayList<Kpi>currencies=new ArrayList<>();
	//double [] macd=new double[] {1,1,1,1,1}; 
	Connection con = new Connection();
	ApiConnection connection = new ApiConnection(con);

	KpiCalculator werte=new KpiCalculator(connection);
	JsonInstrumentsRoot instrumentsRoot = werte.getInstruments();

//  startMe BeforeAll Methode has to be static
	@BeforeAll
	public   void start()
	{
		
		for (JsonInstrumentsInstrument instrument : instrumentsRoot.instruments) 
		{
	
			if(instrument.type.compareTo("CURRENCY")==0)
			{
		
			Kpi kpi = werte.getAll(instrument.name, 200, 14, "M15", 0.02, 0.02, 0.2, 12, 26, 9);	
	currencies.add(kpi);
						
					
		}
	}
	}
	
	@Test
	public  void MacdTest()
	{
		int zaehlerMACD=0;
		int zaehlerMACDTriggert=0;

for(Kpi kpi:currencies)
{
     zaehlerMACD=((kpi.macd<1)&&(kpi.macd>-1))?zaehlerMACD:zaehlerMACD++;		
     zaehlerMACDTriggert=((kpi.macdTriggert<1)&&(kpi.macdTriggert>-1))?zaehlerMACD:zaehlerMACD++;
}
assertTrue((zaehlerMACD<=1)&&(zaehlerMACDTriggert<=1));					
	}					
				
		
		
		
	
@Test
public void ab()
{
	try
	{
		assertEquals(1,1);
	}
	
	catch(Exception e)
	{
		System.out.println(e.getMessage());
	
	}
}
@Test
public void Ema()
{
	for(Kpi k:currencies)
	{
		assertTrue((k.ema>0)&&(!(k.ema>k.lastPrice*1.02))&&(!(k.ema<k.lastPrice*0.98)));
	}
}
	}


