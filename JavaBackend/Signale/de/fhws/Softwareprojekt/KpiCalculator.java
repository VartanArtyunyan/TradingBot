package de.fhws.Softwareprojekt;


import java.time.LocalDate;

import API.ApiConnection;
//import sun.security.mscapi.CKeyStore.ROOT;

public class KpiCalculator {
	
	private ApiConnection connection;
	
	public KpiCalculator(ApiConnection connection) {

		this.connection = connection;
	}
	public Kpi getAll(String instrument, int emaperiods,int periods, String granularity, double startBF, double inkrementBF, double maxBF,int x, int y, int z)
	{
		// Candles von Oanda-Api holen und in Json-Object mappen
		JsonCandlesRoot jcr = extracted(instrument, granularity);
		
		// Einfache Kennzahlen und EMA (Exponential Moving Average) berechnen
		Kpi kpi=getBasisKpi(instrument, emaperiods, granularity,jcr);
		
		// Parabolic SAR berechnen
		Kpi kpiTemp=getParabolicSar(instrument, granularity, periods, startBF, inkrementBF, maxBF,jcr);
		kpi.parabolicSAR=kpiTemp.parabolicSAR;
		kpi.parabolicSARs=kpiTemp.parabolicSARs;
		kpi.trend=kpiTemp.trend;
		kpi.trendWechsel=kpiTemp.trendWechsel;
		
		// MACD (Moving Average Convergence/Divergence) berechnen 
		kpiTemp=getMACD(instrument, granularity, x, y, z,jcr);
		kpi.macd=kpiTemp.macd;
		kpi.macds=kpiTemp.macds;
		kpi.macdsTriggert=kpiTemp.macdsTriggert;
		kpi.macdTriggert=kpiTemp.macdTriggert;
		kpi.prozent=kpiTemp.prozent;
		kpi.Prozent=kpiTemp.Prozent;
	
		// Relative Strength Index (RSI) berechnen mit exponentiell gleitenden Durchschnitt
		kpiTemp=getRSI(instrument, periods, granularity,jcr);
		kpi.rsi=kpiTemp.rsi;
		kpi.rsiListe=kpiTemp.rsiListe;
		
		// Average True Range (ATR) berechnen
		kpiTemp=getATR(instrument, periods, granularity,jcr);
		kpi.atr=kpiTemp.atr;
		kpi.atrListe=kpiTemp.atrListe;
		kpi.IntegerAtr=kpiTemp.IntegerAtr;
		kpi.IntegerAtrListe=kpiTemp.IntegerAtrListe;
		
		// Simple Moving Average (SMA) berechnen
		kpiTemp=getSMA(instrument, periods, granularity, jcr);
		kpi.sma=kpiTemp.sma;
		kpi.smaList=kpiTemp.smaList;
		
		return kpi;
		
	} 
	public Kpi getBasisKpi(String instrument, int periods, String granularity, JsonCandlesRoot jcr) {

		// HttpURLConnection connection;

		Kpi kpi = new Kpi(instrument, granularity, periods);

		
			kpi. root = jcr;

			// KPI's ermitteln **************************************
			int count = 0;
			double sum = 0.0;
			double sum2 = 0.0;
			double sf = 2.0 / (periods + 1); // SF = Smoothing Factor f�r EMA-Formel
			// For-Each-Schleife �ber alle Candles
			for (JsonCandlesCandle candle : kpi.root.candles) {
				count++;
				// Start-EMA ermitteln = Durchschnitt erste 8 Candles (s. admiralmarkets.com)
				if (count <= 8) {
					sum += candle.mid.c;
					kpi.ema = sum / count;
					if (count == 1)
						kpi.firstTime = candle.time.substring(0, 16);

				} else {
					// Mit allen anderen Candles ein m�glichst genaues EMA errechnen
					// EMA-Formel: (Schlusskurs * SF) + (( 1 - SF ) * Vor-EMA)
					kpi.ema = candle.mid.c * sf + (1 - sf) * kpi.ema;
					kpi.lastPrices.add(candle.mid.c);
					kpi.emas.add(kpi.ema);
					/*if (count == kpi.root.candles.size() - 1) {
						kpi.vorema = candle.mid.c * sf + (1 - sf) * kpi.ema;
					}*/
					// Nur f�r die angeforderten Perioden die weiteren Kennzahlen ermitteln
					if (count > kpi.root.candles.size() - periods) {
						sum2 += candle.mid.c;
						kpi.max = candle.mid.c > kpi.max ? kpi.max = candle.mid.c : kpi.max;
						kpi.min = candle.mid.c < kpi.min || kpi.min == 0 ? kpi.min = candle.mid.c : kpi.min;
						kpi.lastTime = candle.time.substring(0, 16);
						kpi.lastPrice = candle.mid.c;
						kpi.lastHighestPrice=candle.mid.h;
						kpi.lastLowestPrice=candle.mid.l;
					}
				}
			}
			kpi.avg = kpi.root.candles.isEmpty() ? 0 : sum2 / periods;

		
		return kpi;
	}

	public JsonCandlesRoot extracted(String instrument, String granularity){
		//HttpURLConnection connection;
		//URL url = new URL(oanda + account + "instruments/" + instrument + "/candles?count=4900&granularity="
			//	+ granularity + "&from=" + startDate(granularity)+"&alignmentTimezone=Europe/Berlin&dailyAlignment=22");
		//+ "&alignmentTimezone=Europe/Berlin"
		//connection = (HttpURLConnection) url.openConnection();
		//connection.setRequestProperty("Authorization", token);
		// Candle-Liste abrufen
	//	String jsonString = getResponse(connection);
		// JSON in Objekte mappen
	//	ObjectMapper om = new ObjectMapper();
		JsonCandlesRoot root = connection.getJsonCandlesRoot(4900, instrument, startDate(granularity), null, "M", granularity);
		return root;
	}

	public Kpi getParabolicSar(String instrument, String granularity,int periods, double startBF, double inkrementBF, double maxBF,JsonCandlesRoot jcr) {
			Kpi kpi = getBasisKpi(instrument,periods,granularity,jcr);
			double startBFAf = startBF;
			double extrempunkt = 0;
			double extrempunktAlt = 0;
			double faktor=0;
		/*	double hAlt = 0;
			double lAlt = 0;
			double startBFAlt = 0;*/
			int count = 0;
			kpi.trend = "bull";
			String vortrend = "bull";
			for (JsonCandlesCandle candle :kpi.root.candles) {
				// 1 Durchgang 
				if (count == 0) {
					 kpi.parabolicSAR = candle.mid.l;
					 extrempunkt = candle.mid.h;

				} else {
					//parabolicSAR Berechnung
 faktor=(extrempunkt-kpi.parabolicSAR)*startBF;
 if(kpi.trend.compareTo("bull")==0)
 kpi.parabolicSAR=((kpi.parabolicSAR+=faktor)>candle.mid.l)?extrempunkt:kpi.parabolicSAR+faktor;
 else
	 kpi.parabolicSAR=((kpi.parabolicSAR+=faktor)<candle.mid.h)?extrempunkt:kpi.parabolicSAR+faktor;
 //Bestimmung trend
 kpi.trend=(kpi.parabolicSAR<candle.mid.h)?"bull":kpi.parabolicSAR>candle.mid.l?"bear":"";
 //Bestimmung Extrempunkt
 if(kpi.trend.compareTo("bull")==0)
 extrempunkt=(candle.mid.h>extrempunkt)?candle.mid.h:extrempunkt;
 else
	 extrempunkt=candle.mid.l<extrempunkt?candle.mid.l:extrempunkt;
 //
 //AccelarationFaktor

	 if((kpi.trend.compareTo(vortrend)==0)&&(kpi.trend.compareTo("bull")==0))
	 {
	startBF=(extrempunkt<=extrempunktAlt)?startBF:(startBF!=maxBF)?startBF+inkrementBF:startBF;
	kpi.trendWechsel=false;
	 }
	else if((kpi.trend.compareTo(vortrend)==0)&&(kpi.trend.compareTo("bear")==0))
	{
		startBF=(extrempunkt>=extrempunktAlt)?startBF:(startBF!=maxBF)?startBF+inkrementBF:startBF;
		kpi.trendWechsel=false;
	}
	else
	{
		kpi.trendWechsel=true;
		startBF=startBFAf;
	}
				}
				count++;
				vortrend=kpi.trend;
				extrempunktAlt=extrempunkt;
			}
		
				
			return kpi;
				
	}

	public Kpi getMACD(String instrument, String granularity, int x, int y, int z, JsonCandlesRoot jcr) {
		Kpi kpi1 = getBasisKpi(instrument, x, granularity,jcr);
		Kpi kpi2 = getBasisKpi(instrument, y, granularity,jcr);
		Kpi md = getBasisKpi(instrument, z, granularity,jcr);
	double ergebnis = 0;
	double maxDifferenz=0;
	double minDifferenz=0;

	for(int i=0;i<md.emas.size();i++)
	{
		md.macd=(kpi1.emas.get(i)-kpi2.emas.get(i));
		md.macds.add(md.macd);
		if(i>=z-1)
		{
		for(int j=i-z+1;j<=i;j++)
		{
			
			ergebnis+=md.macds.get(j);
		}
		md.macdTriggert=ergebnis/z;
		md.macdsTriggert.add(md.macdTriggert);
		//Differnz zwischen Macd und MacdTriggert
		double differenz=md.macd-md.macdTriggert;
		//Maximale Diffenenz zwischen macd und macdTriggert
		maxDifferenz=differenz>maxDifferenz?differenz:maxDifferenz;
		//Minimale Differenz zwischen macd und macdTriggert
		minDifferenz=differenz<minDifferenz?differenz:minDifferenz;
		//Wert f�r Array List bei negativer Differnz muss der Wert *(-1=) genommen werden, da -*- + ergibt und das Ergenis negativ sein soll zur Unterscheidung
		double wert=differenz==0?0:differenz>0?differenz/maxDifferenz:differenz/minDifferenz*(-1);
		md.Prozent.add(wert);
		md.prozent=wert;
		//md.Prozent.add((md.macdTriggert>md.macdTriggert)?((md.macd-md.macdTriggert)/md.maxProzent)):md.macd>md.macdTriggert?;
		
		}
		
			ergebnis=0;
	}
		return md;
	}

	
	private String startDate(String granularity) {
		LocalDate date = LocalDate.now();
		int tage = 1;
		switch (granularity) {
		case ("D"): {
			tage = 4900;// Samstags keine Werte--> ca. 1000 Candle
			break;
		}
		case ("H1"): {
			tage = 200;// ein Monat ca. 500 Candles
			break;
		}
		case ("M15"): {
			tage = 65;// 30/4
			break;
		}
		case ("M10"): {
			tage = 43;
			break;
		}
		case("M5"):
		{
			tage=22;
			break;
			
		}
		case("M1"):{
			tage=3;
			break;
		}
		}
		date = date.minusDays(tage);

		String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue() : "" + date.getMonthValue();
		String day = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();

		return date.getYear() + "-" + month + "-" + day + "T00:00:00.000000000Z";

		/*
		 * durch API unterst�tzte Granularit�ten S5 5 second candlesticks, minute
		 * alignment S10 10 second candlesticks, minute alignment S15 15 second
		 * candlesticks, minute alignment S30 30 second candlesticks, minute alignment
		 * M1 1 minute candlesticks, minute alignment M2 2 minute candlesticks, hour
		 * alignment M4 4 minute candlesticks, hour alignment M5 5 minute candlesticks,
		 * hour alignment M10 10 minute candlesticks, hour alignment M15 15 minute
		 * candlesticks, hour alignment M30 30 minute candlesticks, hour alignment H1 1
		 * hour candlesticks, hour alignment H2 2 hour candlesticks, day alignment H3 3
		 * hour candlesticks, day alignment H4 4 hour candlesticks, day alignment H6 6
		 * hour candlesticks, day alignment H8 8 hour candlesticks, day alignment H12 12
		 * hour candlesticks, day alignment D 1 day candlesticks, day alignment W 1 week
		 * candlesticks, aligned to start of week M 1 month candlesticks, aligned to
		 * first day of the month
		 */
	}

	public JsonInstrumentsRoot getInstruments() {
	//	try {
	//		URL url = new URL(oanda + account + "instruments");
	//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	//		connection.setRequestProperty("Authorization", token);

	//		String jsonString = getResponse(connection);
			// JSON in Objekte mappen
	//		ObjectMapper om = new ObjectMapper();
			return connection.getJsonInstrumentsRoot();
	//	} catch (Exception e) {
	//		System.out.println(e.getMessage());
	//	}
	//	return null;
	}

	
	
	public Kpi getATR(String instrument,int periods,String granularity, JsonCandlesRoot jcr)

	{

			Kpi kpi=getBasisKpi(instrument,periods,granularity,jcr);
			double betrag=0;
			double prev=0;
			double wert=0;
			for(int i=1;i<kpi.root.candles.size();i++)
			{
				prev=i>periods?0:prev;
					betrag=(kpi.root.candles.get(i).mid.l-kpi.root.candles.get(i-1).mid.c)>((kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i-1).mid.c))?(kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i-1).mid.c+prev):(kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i-1).mid.c+prev);
					betrag=betrag>kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i).mid.l?betrag:kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i).mid.l+prev;
					prev=betrag;
				    
				if(i>periods)
				{
					wert=(((wert*(periods-1)+betrag)/periods));
					kpi.atr=wert;
					kpi.atrListe.add(kpi.atr);
					kpi.IntegerAtr=(int)(wert*10000);
					kpi.IntegerAtrListe.add(kpi.IntegerAtr);
					
				}
				
			}
			return kpi;
		}
	
	
	public Kpi getRSI(String instrument, int periods, String granularity,JsonCandlesRoot jcr) {
		Kpi kpi=getBasisKpi(instrument,periods,granularity,jcr);

		double gain=0;
		double loss=0;
		
			double startPreis=kpi.root.candles.get(0).mid.c;
			double currentG=0;
			double currentL=0;
			
			for(int z=1;z<kpi.root.candles.size();z++)
			{
			if(z<=periods)
			{
				gain=kpi.root.candles.get(z).mid.c-startPreis>0?gain+kpi.root.candles.get(z).mid.c-startPreis:gain;
				loss=startPreis-kpi.root.candles.get(z).mid.c>0?loss+startPreis-kpi.root.candles.get(z).mid.c:loss;
						if(z==periods)
						{
					gain=gain/periods;
					loss=loss/periods;
						}
			}
				if(z>periods)
				{
					currentG=kpi.root.candles.get(z).mid.c-startPreis>0?kpi.root.candles.get(z).mid.c-startPreis:0;
					currentL=startPreis-kpi.root.candles.get(z).mid.c>0?startPreis-kpi.root.candles.get(z).mid.c:0;
					gain=(gain*(periods-1)+currentG)/periods;
					loss=(loss*(periods-1)+currentL)/periods;
				}
				startPreis=kpi.root.candles.get(z).mid.c;
				kpi.rsi=100-(100/((gain/loss)+1));
				kpi.rsiListe.add(kpi.rsi);
			}
	return kpi;
	}
	public Kpi getSMA(String instrument, int periods, String granularity,JsonCandlesRoot jcr)
	{
		Kpi kpi=getBasisKpi(instrument,periods,granularity,jcr);
	
		for(int i=0;i< kpi.root.candles.size();i++)
		{
			double ergebnis=0;
			if(i>=periods-1)
			{
			for(int z=i-periods+1;z<=i;z++)
			{
			ergebnis+=kpi.root.candles.get(z).mid.c;
			}
			ergebnis/=periods;
			kpi.sma=ergebnis;
			kpi.smaList.add(kpi.sma);
			
			}
			
		}
		return kpi;
	}

}

