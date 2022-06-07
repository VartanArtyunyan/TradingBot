package de.fhws.Softwareprojekt;

import java.util.ArrayList;

public class Kpi implements Comparable<Kpi> {

	public Kpi(String instrument, String granularity, int periods) {
		this.instrument = instrument;
		this.granularity = granularity;
		this.periods = periods;
	}

	// werden in der Signals Klasse gefüllt
	boolean longShort; // false = short, long = true
	double signalStrenght = 1;

	JsonCandlesRoot root;

	// Konstruktor
	public String instrument;
	public String granularity;
	public int periods;

	// getBasicKpi
	double lastPrice = 0;
	ArrayList<Double> lastPrices = new ArrayList<>();
	double lastHighestPrice = 0;
	double lastLowestPrice = 0;
	String firstTime = "";
	String lastTime = "";
	double min = 0;
	double max = 0;
	double avg = 0;
	// ema
	double ema = 0;
	// double vorema=0;
	ArrayList<Double> emas = new ArrayList<>();

	// parabolicSar
	double parabolicSAR = 0;
	ArrayList<Double> parabolicSARs = new ArrayList<>();
	String trend = "";
	boolean trendWechsel = false;

	// MACD
	double macd = 0;
	ArrayList<Double> macds = new ArrayList<>();
	ArrayList<Double> macdsTriggert = new ArrayList<>();
	double macdTriggert = 0;
	ArrayList<Double> macdIntensitys = new ArrayList<Double>();
	double macdIntensity = 0;

	// atr
	double atr = 0;
	ArrayList<Double> atrListe = new ArrayList<Double>();
	int IntegerAtr = 0;
	ArrayList<Integer> IntegerAtrListe = new ArrayList<>();

	// Differnenz/max/min
	// sma
	double sma = 0;
	ArrayList<Double> smaList = new ArrayList<>();

	// Rsi
	double rsi = 0;
	ArrayList<Double> rsiListe = new ArrayList<>();
	
	//Signal 
	int signal; 

	// Die Eindeutigkeit der Kpi wird allein durch das Instrument bestimmt
	@Override
	public int hashCode() {
		return this.instrument.hashCode();
	}

	@Override
	//
	public boolean equals(Object o) {
		Kpi input = (Kpi) o;
		return this.instrument.equals(input.instrument);

	}

	public String getInstrument() {
		return instrument;
	}

	public double getSignalStrenght() {
		return signalStrenght;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public double getEma() {
		return ema;
	}

	public void setEma(double ema) {
		this.ema = ema;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public double getMacdTriggert() {
		return macdTriggert;
	}

	public void setMacdTriggert(double macdTriggert) {
		this.macdTriggert = macdTriggert;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public double getParabolicSAR() {
		return parabolicSAR;
	}

	public void setParabolicSAR(double parabolicSAR) {
		this.parabolicSAR = parabolicSAR;
	}

	public boolean isLong() {
		return longShort;
	}

	public boolean isShort() {
		return !longShort;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public double getKaufpreis() {
		return 0.0; // müsste eigentlich 0,02*kontostand zurückgeben
	}

	public double runden(double wert, int n) {
		return ((Math.round(wert * Math.pow(10, n)) / Math.pow(10, n)));
	}

	public double aufrunden(double wert, int n) {
		double number = runden(wert, n) + (1 / Math.pow(10, n));
		return runden(wert, n) > wert ? runden(wert, n) : (runden(number, n));
	}

	public double abrunden(double wert, int n) {
		double number = runden(wert, n) - (1 / Math.pow(10, n));
		return runden(wert, n) < wert ? runden(wert, n) : runden(number, n);
	}

	public double getLimitPrice() {
		if (instrument.contains("USD_THB") || instrument.contains("USD_INR")
				|| (instrument.contains("JPY") || instrument.contains("HUF"))) {
			if (longShort)
				return runden(lastPrice + 0.001, 3);
			return runden((lastPrice - 0.001), 3);
		} else {
			if (longShort)
				return runden(lastPrice + 0.0005, 4);
			return runden(lastPrice - 0.0005, 4);
		}
	}

	public double getStopLoss() {
		if (isLong())
			return getLongStopLoss();
		else
			return getShortStopLoss();
	}

	public double getTakeProfit() {
		if (isLong())
			return getLongTakeProfit();
		else
			return getShortTakeProfit();
	}

	public double getLongStopLoss() {
		// Hier muss mit der DisplayPrecision
		double wert = lastPrice * 0.9990;
		return checkPrecision(wert, false);
	}

	public double getLongTakeProfit() {
		double wert = lastPrice * 1.002;
		return checkPrecision(wert, true);
	}

	public double getShortStopLoss() {
		double wert = lastPrice * 1.001;
		return checkPrecision(wert, true);
	}

	public double getShortTakeProfit() {
		double wert = lastPrice * 0.998;
		return checkPrecision(wert, false);
	}

	public double convertIntegerATRInDouble(boolean plusMinus) {
		// ATR dient hier als Stoploss-/Takeprofit-Wert
		// Es wird der IntegerATR als String umgewandelt, um "0.00" davorzusetzen und zu
		// einem Doublewert umgewandelt
		// Es wird nicht unterschieden, ob der ATR-Wert(als Double) führende Nullen nach
		// dem Komma hat oder nicht
		// plusMinus: True=Plus, Minus=False

		String StringATR = String.valueOf(IntegerAtr);
		String add = "";
		if (plusMinus = true) {
			add = "0.00";
		} else { // plusMinus = false
			add = "-0.00";
		}
		StringATR = add + StringATR;
		double atr_neu = Double.parseDouble(StringATR);
		return atr_neu;
	}

	// ATR-Wert als Prozentsatz mit lastPrice und einem übergebenen Multiplikator
	// multipliziert:
	public double getLongStopLossATR(double multiplier) {
		double sl = 1 - (convertIntegerATRInDouble(false) * multiplier);
		double wert = sl * lastPrice;
		return checkPrecision(wert, false);
	}

	public double getLongTakeProfitATR(double multiplier) {
		double sl = 1 + (convertIntegerATRInDouble(false) * multiplier);
		double wert = sl * lastPrice;
		return checkPrecision(wert, true);
	}

	public double getShortStopLossATR(double multiplier) {
		double sl = 1 + (convertIntegerATRInDouble(false) * multiplier);
		double wert = sl * lastPrice;
		return checkPrecision(wert, true);
	}

	public double getShortTakeProfitATR(double multiplier) {
		double sl = 1 - (convertIntegerATRInDouble(false) * multiplier);
		double wert = sl * lastPrice;
		return checkPrecision(wert, false);
	}

	public double checkPrecision(double wert, boolean aufrunden) {
		if (instrument.contains("USD_THB") || instrument.contains("USD_INR")
				|| (instrument.contains("JPY") || instrument.contains("HUF"))) {
			if (aufrunden)
				return aufrunden(wert, 3);
			else
				return abrunden(wert, 3);
		} else
			return runden(wert, 5);
	}

	@Override
	public int compareTo(Kpi wert2) {
		// Beide long Positionen
		if ((this.macdIntensity > 0 && wert2.macdIntensity > 0)) {
			if (this.macdIntensity > wert2.macdIntensity)
				return 1;
			else if (this.macdIntensity < wert2.macdIntensity)
				return -1;

		}
		// Beide short Positionen
		if ((this.macdIntensity < 0 && wert2.macdIntensity < 0)) {
			if (this.macdIntensity < wert2.macdIntensity)
				return 1;
			else if (this.macdIntensity > wert2.macdIntensity)
				return -1;
		}

		// wert 1 long und wert 2 short
		if (this.macdIntensity > 0 && wert2.macdIntensity < 0) {
			if ((this.macdIntensity > wert2.macdIntensity * (-1)))
				return 1;
			else if ((this.macdIntensity < wert2.macdIntensity * (-1)))
				return -1;
			// Wenn gleich:Dann nehme long Position da seltener
			else
				return 1;
		}
		// wert 1 short und wert2 long
		if ((this.macdIntensity < 0 && wert2.macdIntensity > 0)) {
			if ((this.macdIntensity * (-1) > wert2.macdIntensity))
				return 1;
			else if ((this.macdIntensity * (-1) < wert2.macdIntensity))
				return -1;
			// Wenn gleich:Dann nehme long Position da seltener
			else
				return 1;
		}
		if (Math.abs(this.macdIntensity) == Math.abs(wert2.macdIntensity)) {
			// Auch hier im Zweifel long
			if (this.macdIntensity > wert2.macdIntensity)
				return 1;
			else if (this.macdIntensity < wert2.macdIntensity)
				return -1;
			else
				return 0;
		}
		return 1;
	}
}