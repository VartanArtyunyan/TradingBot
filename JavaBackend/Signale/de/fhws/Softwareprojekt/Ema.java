package de.fhws.Softwareprojekt;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.time.LocalDate;



import API.ApiConnection;

public class Ema {
	
	private ApiConnection connection;
	
	public Ema(ApiConnection connection) {

		this.connection = connection;
	}

	public Kpi getKpi(String instrument, int periods, String granularity, JsonCandlesRoot jcr) {

		// HttpURLConnection connection;

		Kpi kpi = new Kpi(instrument, granularity, periods);

		
			kpi. root = jcr;

			// KPI's ermitteln **************************************
			int count = 0;
			double sum = 0.0;
			double sum2 = 0.0;
			double sf = 2.0 / (periods + 1); // SF = Smoothing Factor für EMA-Formel
			// For-Each-Schleife über alle Candles
			for (JsonCandlesCandle candle : kpi.root.candles) {
				count++;
				// Start-EMA ermitteln = Durchschnitt erste 8 Candles (s. admiralmarkets.com)
				if (count <= 8) {
					sum += candle.mid.c;
					kpi.ema = sum / count;
					if (count == 1)
						kpi.firstTime = candle.time.substring(0, 16);

				} else {
					// Mit allen anderen Candles ein möglichst genaues EMA errechnen
					// EMA-Formel: (Schlusskurs * SF) + (( 1 - SF ) * Vor-EMA)
					kpi.ema = candle.mid.c * sf + (1 - sf) * kpi.ema;
					kpi.emas.add(kpi.ema);
					if (count == kpi.root.candles.size() - 1) {
						kpi.vorema = candle.mid.c * sf + (1 - sf) * kpi.ema;
					}
					// Nur für die angeforderten Perioden die weiteren Kennzahlen ermitteln
					if (count > kpi.root.candles.size() - periods) {
						sum2 += candle.mid.c;
						kpi.max = candle.mid.c > kpi.max ? kpi.max = candle.mid.c : kpi.max;
						kpi.min = candle.mid.c < kpi.min || kpi.min == 0 ? kpi.min = candle.mid.c : kpi.min;
						kpi.lastTime = candle.time.substring(0, 16);
						kpi.lastPrice = candle.mid.c;
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

	public Kpi parabolicSar(String instrument, String granularity,int periods, double startBF, double inkrementBF, double maxBF,JsonCandlesRoot jcr) {
			Kpi kpi = getKpi(instrument,periods,granularity,jcr);
			double startBFAf = startBF;
			double extrempunkt = 0;
			double extrempunktAlt = 0;
			double hAlt = 0;
			double lAlt = 0;
			double startBFAlt = 0;
			int count = 0;
			kpi.trend = "bull";
			String vortrend = "bull";
			for (JsonCandlesCandle candle :kpi.root.candles) {
				// 1 Durchgang
				if (count == 0) {
					lAlt = kpi.parabolicSAR = candle.mid.l;
					hAlt = extrempunkt = candle.mid.h;

				} else {

					if (kpi.trend.compareTo("bull") == 0) {
						extrempunkt = candle.mid.h > hAlt ? candle.mid.h : hAlt;
						if ((candle.mid.h > hAlt) && (startBF != maxBF)) {
							startBF += inkrementBF;
						}
					} else {
						extrempunkt = candle.mid.l < lAlt ? candle.mid.l : lAlt;
						if ((candle.mid.h < hAlt) && (startBF != maxBF)) {
							startBF += inkrementBF;
						}
					}
				}

				kpi.parabolicSAR += (extrempunktAlt - kpi.parabolicSAR) * startBFAlt;
				kpi.parabolicSARs.add(kpi.parabolicSAR);
				extrempunktAlt = extrempunkt;
				startBFAlt = startBF;
				vortrend = kpi.trend;
				// Trendwechsel checken
				if (candle.mid.h > kpi.parabolicSAR) {
					if (kpi.trend.compareTo("bull") != 0) {
						startBF = startBFAf;
					}
					kpi.trend = "bull";
				}
				if (kpi.parabolicSAR > candle.mid.l) {
					if (kpi.trend.compareTo("bear") != 0) {
						startBF = startBFAf;
					}
					kpi.trend = "bear";
				}

				count++;
			}
			return kpi;
	}

	public Kpi getMACD(String instrument, String granularity, int x, int y, int z, JsonCandlesRoot jcr) {
		Kpi kpi1 = getKpi(instrument, x, granularity,jcr);
		Kpi kpi2 = getKpi(instrument, y, granularity,jcr);
		Kpi md = getKpi(instrument, z, granularity,jcr);
	double ergebnis = 0;
 //   double Vorergebnis=0;
    for(int i=md.periods;i<md.emas.size()+1;i++)
    {
    	if(i==md.periods)md.macds.add(kpi1.emas.get(i-1)-kpi2.emas.get(i-1));
    	
    	ergebnis=0;
    	if(i<md.emas.size())
    	md.macds.add(kpi1.emas.get(i)-kpi2.emas.get(i));
    	for(int b=i-md.periods;b<i;b++)
    	{
    		ergebnis+=kpi1.emas.get(b)-kpi2.emas.get(b);
    	}
    	ergebnis=ergebnis/md.periods;
    	
    	md.macdsTriggert.add(ergebnis);
    	
    }
 
    md.macd=md.macds.get(md.macds.size()-1);
    md.macdTriggert=ergebnis;
		/*for(int i= md.emas.size()-md.periods-1;i<md.emas.size();i++)
		{
		ergebnis=(i>=(md.emas.size()-md.periods))?ergebnis+(kpi1.emas.get(i)-kpi2.emas.get(i)):ergebnis;
		Vorergebnis=(i<md.emas.size()-1)?Vorergebnis+(kpi1.emas.get(i)-kpi2.emas.get(i)):Vorergebnis;
	}
		md.Vormacd=kpi1.vorema-kpi2.vorema;
		md.macd=kpi1.ema-kpi2.ema;
		md.VormacdTriggert=
		md.macdTriggert=ergebnis/md.periods;*/
		return md;
	}

	
	private String startDate(String granularity) {
		LocalDate date = LocalDate.now();
		int tage = 1;
		switch (granularity) {
		case ("D"): {
			tage = 1300;// Samstags keine Werte--> ca. 1000 Candle
			break;
		}
		case ("H1"): {
			tage = 110;// ein Monat ca. 500 Candles
			break;
		}
		case ("M15"): {
			tage = 20;// 30/4
			break;
		}
		case ("M10"): {
			tage = 6;
			break;
		}
		}
		date = date.minusDays(tage);

		String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue() : "" + date.getMonthValue();
		String day = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();

		return date.getYear() + "-" + month + "-" + day + "T00:00:00.000000000Z";

		/*
		 * durch API unterstützte Granularitäten S5 5 second candlesticks, minute
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

	public String getResponse(HttpURLConnection connection) throws IOException {
		BufferedReader br;
		String line;
		String jsonString = "";

		if (connection.getResponseCode() < 299) {
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = br.readLine()) != null) {
				jsonString += line;
			}
			br.close();
		} else {
			System.out.println("Fehler beim lesen");
		}
		return jsonString;
		
	}
	public Kpi aufrufAlles(String instrument, int emaperiods,int periods, String granularity,double startBF, double inkrementBF, double maxBF,int x, int y, int z,int multiplicatorUpper,int multiplicatorLower)
	{
		
		JsonCandlesRoot jcr = extracted(instrument, granularity);
		
		Kpi kpi=getKpi(instrument, emaperiods, granularity,jcr);
		Kpi kpi2=parabolicSar(instrument, granularity, periods, startBF, inkrementBF, maxBF,jcr);
		Kpi kpi3=getMACD(instrument, granularity, x, y, z,jcr);
		Kpi kpi4=getRSI(instrument, periods, granularity,jcr);
		Kpi kpi5=getATR(instrument, periods, granularity,jcr);
		Kpi kpi6=getSupertrend(instrument, periods, granularity, jcr, multiplicatorUpper, multiplicatorLower);
		kpi.atr=kpi5.atr;
		kpi.atrListe=kpi5.atrListe;
		kpi.macd=kpi3.macd;
		kpi.macds=kpi3.macds;
		kpi.macdsTriggert=kpi3.macdsTriggert;
		kpi.macdTriggert=kpi3.macdTriggert;
		kpi.parabolicSAR=kpi2.parabolicSAR;
		kpi.parabolicSARs=kpi2.parabolicSARs;
		kpi.rsi=kpi4.rsi;
		kpi.rsiListe=kpi4.rsiListe;
		kpi.trend=kpi2.trend;
		kpi.trendWechsel=kpi2.trendWechsel;
		kpi.superTrend=kpi6.superTrend;
		kpi.superTrends=kpi6.superTrends;
		return kpi;
	

	} 

	//Tom 
	public Kpi getATR(String instrument,int periods,String granularity, JsonCandlesRoot jcr)

	{

			Kpi kpi=getKpi(instrument,periods,granularity,jcr);
			double betrag=0;
			double prev=0;
			for(int i=1;i<kpi.root.candles.size();i++)
			{
				prev=i>periods?0:prev;
					betrag=(kpi.root.candles.get(i).mid.l-kpi.root.candles.get(i-1).mid.c)>((kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i-1).mid.c))?(kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i-1).mid.c+prev):(kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i-1).mid.c+prev);
					betrag=betrag>kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i).mid.l?betrag:kpi.root.candles.get(i).mid.h-kpi.root.candles.get(i).mid.l+prev;
					prev=betrag;
				    
				if(i>periods)
				{
					kpi.atr=((kpi.atr*(periods-1)+betrag)/periods);
					kpi.atrListe.add(kpi.atr);
				}
				
			}
			return kpi;
		}

	//Tom

	public Kpi getRSI(String instrument, int periods, String granularity,JsonCandlesRoot jcr) {
		Kpi kpi=getKpi(instrument,periods,granularity,jcr);

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
	public Kpi getSupertrend(String instrument, int periods, String granularity,JsonCandlesRoot jcr,int multiplicatorUpper,int multiplicatorLower)
	{
		Kpi kpi=getATR(instrument, periods, granularity, jcr);
		double upperband=0;
		double upperbandPrev=0;
		double lowerband=0;
		double lowerbandPrev=0;
		
		int count=0;
		for(int i=1;i<kpi.root.candles.size();i++)
		{
			if (i>periods)
			{
		
			//Wenn upperband kleiner als upperbandPrev oder vorherige Schlusskurz größer ist als upperbandPrev dann nehme upperband
				//Wenn lowerband größer ist als lowerbandPrev pder vorherige Schlusskurs kleiner als lowerbandPrev ist dann nehme lowerband
				upperband=((kpi.root.candles.get(i).mid.h+kpi.root.candles.get(i).mid.l)/2)+(multiplicatorUpper*kpi.atrListe.get(count));
				lowerband=((kpi.root.candles.get(i).mid.h+kpi.root.candles.get(i).mid.l)/2)-(multiplicatorLower*kpi.atrListe.get(count));
				if(i==periods+1)
					{
					upperbandPrev=upperband;
					lowerbandPrev=lowerband;
					kpi.superTrend=upperbandPrev;
					}
				
					
				upperband=upperband<upperbandPrev?upperband:upperbandPrev;
				if((upperband==upperbandPrev)&&(i!=periods+1))
				upperband=kpi.root.candles.get(i-1).mid.c>upperbandPrev?upperband:upperbandPrev;
				
				lowerband=lowerband>lowerbandPrev?lowerband:lowerbandPrev;
				if((lowerband==lowerbandPrev)&&(i!=periods+1))
				lowerband=kpi.root.candles.get(i-1).mid.c<lowerbandPrev?lowerband:lowerbandPrev;
				
				count++;
			
			if(kpi.superTrend==upperbandPrev)
			{
		if(kpi.root.candles.get(i).mid.c<upperband)
			kpi.superTrend=upperband;
		else if(kpi.root.candles.get(i).mid.c>upperband)
			kpi.superTrend=lowerband;
			}
			else if(kpi.superTrend==lowerbandPrev)
			{
				if(kpi.root.candles.get(i).mid.c>lowerband)
					kpi.superTrend=lowerband;
				else if(kpi.root.candles.get(i).mid.c<lowerband)
					kpi.superTrend=upperband;
			}
			
			lowerbandPrev=lowerband;
			upperbandPrev=upperband;
			kpi.superTrends.add(kpi.superTrend);
		}
		}
		return kpi;
	}

}

