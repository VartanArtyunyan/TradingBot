package de.fhws.Softwareprojekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import API.ApiConnection;

public class Ema {
	private String oanda = "https://api-fxpractice.oanda.com/v3/";
	private String account = "accounts/101-012-22115816-001/";
	private String token = "Bearer 91dec921714f6128f5ed7f199560852d-1fb0ae23b9e48ab85aec80682b096f5f";
	
	ApiConnection api;

	public Ema(String account, String token, ApiConnection api) {
		this.account = "accounts/" + account + "/";
		this.token = "Bearer " + token;
		this.api = api;
	}

	public Kpi getKpi(String instrument, int periods, String granularity) {

		// HttpURLConnection connection;

		Kpi kpi = new Kpi(instrument, granularity, periods);

		try {
			// Abruf Candle-Liste vorbereiten und Verbindung aufbauen
			// dabei so viele Candles wie möglich holen für genauere EMA-Ermittlung
			JsonCandlesRoot root = extracted(instrument, granularity);

			// KPI's ermitteln **************************************
			int count = 0;
			double sum = 0.0;
			double sum2 = 0.0;
			double sf = 2.0 / (periods + 1); // SF = Smoothing Factor für EMA-Formel
			// For-Each-Schleife über alle Candles
			for (JsonCandlesCandle candle : root.candles) {
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
					if (count == root.candles.size() - 1) {
						kpi.vorema = candle.mid.c * sf + (1 - sf) * kpi.ema;
					}
					// Nur für die angeforderten Perioden die weiteren Kennzahlen ermitteln
					if (count > root.candles.size() - periods) {
						sum2 += candle.mid.c;
						kpi.max = candle.mid.c > kpi.max ? kpi.max = candle.mid.c : kpi.max;
						kpi.min = candle.mid.c < kpi.min || kpi.min == 0 ? kpi.min = candle.mid.c : kpi.min;
						kpi.lastTime = candle.time.substring(0, 16);
						kpi.lastPrice = candle.mid.c;
					}
				}
			}
			kpi.avg = root.candles.isEmpty() ? 0 : sum2 / periods;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return kpi;
		}
		return kpi;
	}

	public JsonCandlesRoot extracted(String instrument, String granularity){
	//		throws MalformedURLException, IOException, JsonProcessingException, JsonMappingException {
	//	HttpURLConnection connection;
	//	URL url = new URL(oanda + account + "instruments/" + instrument + "/candles?count=999&granularity="
	//			+ granularity + "&from=" + startDate(granularity) + "&alignmentTimezone=Europe/Berlin");
	//	connection = (HttpURLConnection) url.openConnection();
	//	connection.setRequestProperty("Authorization", token);
	//	// Candle-Liste abrufen
	//	String jsonString = getResponse(connection);
	//	// JSON in Objekte mappen
	//	ObjectMapper om = new ObjectMapper();
		
		
//hab deinen code jetzt erstmal nur auskommentiert, m�sstest mal testen ob das so funktioniert wie es soll, m�sste es aber eigentlich tun
//Mit dem JsonInstrumentRoot hatte ich ein paar schwierigkeiten und bin deswegen nicht fertig geworden, mach das dann morgen fertig
		
		JsonCandlesRoot root = api.getJsonCandlesRoot(instrument, startDate(granularity), null, "M", granularity);
		return root;
	}

	public Kpi parabolicSar(String instrument, String granularity, double startBF, double inkrementBF, double maxBF) {
		try {
			Kpi kpi = new Kpi(instrument, granularity, 14);
			double startBFAf = startBF;
			JsonCandlesRoot root = extracted(instrument, granularity);
			double extrempunkt = 0;
			double extrempunktAlt = 0;
			double hAlt = 0;
			double lAlt = 0;
			double startBFAlt = 0;
			int count = 0;
			kpi.trend = "bull";
			String vortrend = "bull";
			for (JsonCandlesCandle candle : root.candles) {
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
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	public Kpi getMACD(String instrument, String granularity, int x, int y, int z) {
		Kpi kpi1 = getKpi(instrument, x, granularity);
		Kpi kpi2 = getKpi(instrument, y, granularity);
		Kpi md = getKpi(instrument, y, granularity);

		double sf1 = 2.0 / (kpi1.periods + 1);
		double sf2 = 2.0 / (kpi2.periods + 1);
		double sf3 = 2.0 / (md.periods);
		int zaehler = 0;

		for (Double last : kpi1.emas) {
			if (kpi1.emas.size() - 1 != zaehler) {

				{
					md.macdTriggert = (kpi1.emas.get(zaehler) - kpi2.emas.get(zaehler))
							+ sf3 * (kpi1.emas.get(zaehler + 1) - kpi2.emas.get(zaehler));
					/*
					 * md.signals.add(md.macdTriggert); md.macds.add(kpi1.emas.get(zaehler-1) +
					 * ((sf1) * (kpi1.lastPrice -
					 * kpi1.emas.get(zaehler-1)))-kpi2.emas.get(zaehler-1) + ((sf2) *
					 * (kpi2.lastPrice - kpi2.emas.get(zaehler-1))));
					 */
				}
			}
			zaehler++;
		}

		double ergebnis1 = kpi1.vorema + ((sf1) * (kpi1.lastPrice - kpi1.vorema));
		double ergebnis2 = kpi2.vorema + ((sf2) * (kpi2.lastPrice - kpi2.vorema));
		md.macd = ergebnis1 - ergebnis2;

		// $NON-NLS-N$
		return md;
	}

	public Kpi getRSI(String instrument, int periods, String granularity) {
		Kpi kpi = new Kpi(instrument, granularity, periods);
		// aktueller Schlusspreis
		double temp = 0;
		ArrayList<Double> differenceList = new ArrayList<>();
		int position = 0;
		double x;
		double gain = 0;
		double loss = 0;
		double gainTemp = 0;
		double lossTemp = 0;
		int anzahlGains = 0;
		int anzahlLoss = 0;

		try {
			JsonCandlesRoot root = extracted(instrument, granularity);
			// Liste über Candles, um die Differenzen zu berechnen
			for (JsonCandlesCandle candle : root.candles) {
				x = position == 0 ? temp : candle.mid.c - temp;
				differenceList.add(x);
				temp = candle.mid.c;
				position++;
			}
			position = periods + 1;
			for (int i = differenceList.size() - periods; i < differenceList.size(); i++) {
				// gain = differenceList.get(i) >0 ? gain += differenceList.get(i) : gain;
				if (differenceList.get(i) > 0) {
					gain += differenceList.get(i);
				}
				// anzahlGains = gain > gainTemp ? anzahlGains++ : anzahlGains;
				if (gain > gainTemp)
					//anzahlGains++;
				gainTemp = gain;
				// loss = differenceList.get(i) <0 ? loss += differenceList.get(i) : loss;
				if (differenceList.get(i) < 0) {
					loss += (differenceList.get(i)) * (-1);
					anzahlLoss++;
				}

				if (loss < lossTemp)
					lossTemp = loss;

			}
			kpi.rsi = 100.0 - (100.0 / ((1.0 + (((gain / periods) / (loss / periods))))));
System.out.println(periods);
			return kpi;
		} catch (Exception e) {
			e.getMessage();
			return kpi;
		}

	}

	private String startDate(String granularity) {
		LocalDate date = LocalDate.now();
		int tage = 1;
		switch (granularity) {
		case ("D"): {
			tage = 1150;// Samstags keine Werte--> ca. 1000 Candles
			break;
		}
		case ("H1"): {
			tage = 55;// ein Monat ca. 500 Candles
			break;
		}
		case ("M15"): {
			tage = 7;// 30/4
			break;
		}
		case ("M10"): {
			tage = 3;
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
		try {
			URL url = new URL(oanda + account + "instruments");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", token);

			String jsonString = getResponse(connection);
			// JSON in Objekte mappen
			ObjectMapper om = new ObjectMapper();
			return om.readValue(jsonString, JsonInstrumentsRoot.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
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
}
