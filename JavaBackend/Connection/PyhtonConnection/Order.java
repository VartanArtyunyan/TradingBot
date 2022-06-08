package PyhtonConnection;

public class Order {
	
	String instrument;
	double faktor;
	int volatility; 
	boolean longShort;
	
	
	public Order(String instrument, double faktor, int volatility, boolean longShort) {
		this.instrument = instrument;
		this.faktor = faktor;
		this.volatility = volatility;
		this.longShort = longShort;
	}


	public String getInstrument() {
		return instrument;
	}


	public double getFaktor() {
		return faktor;
	}


	public int getVolatility() {
		return volatility;
	}


	public boolean isLong() {
		return longShort;
	}
	
	public boolean isShort() {
		return !longShort;
	}

}
