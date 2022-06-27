package PyhtonConnection;

public class CalenderOrder {

	
	String instrument;
	double factor;
	String volatility;
	boolean longShort;
	
	
	public CalenderOrder(String instrument, double faktor, String volatility, boolean longShort) {
		this.instrument = instrument;
		this.factor = faktor;
		this.volatility = volatility;
		this.longShort = longShort;
	}
	
	
	public String getInstrument() {
		return instrument;
	}


	public double getFaktor() {
		return factor;
	}


	public String getVolatility() {
		return volatility;
	}


	public boolean isLong() {
		return longShort;
	}
	
	public boolean isShort() {
		return !longShort;
	}
}
