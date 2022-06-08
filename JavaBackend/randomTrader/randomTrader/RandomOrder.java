package randomTrader;

public class RandomOrder {
	String instrument;
	boolean longShort;
	double stopLoss;
	double takeProfit;
	
	public RandomOrder(String instrument, boolean longShort) {
		this.instrument = instrument;
		this.longShort = longShort;
	}
	
	public String getInstrument() {
		return instrument;
	}
	
	public boolean isLong() {
		return longShort;
	}
	
	public boolean isShort() {
		return !longShort ;
	}
	


	
	

}
