package PyhtonConnection;

public class Order {
	
	String instrument;
	double Faktor;
	boolean longShort;
	
	
	public Order(String instrument, double faktor, boolean longShort) {
		this.instrument = instrument;
		Faktor = faktor;
		this.longShort = longShort;
	}

}
