package PyhtonConnection;

public class UpcomingEvent {
	
	String instrument;
	String time;
	int volatility;
	
	
	public UpcomingEvent(String instrument, String time, int volatility) {
		this.instrument = instrument;
		this.time = time;
		this.volatility = volatility;
	}
	
	
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getVolatility() {
		return volatility;
	}
	public void setVolatility(int volatility) {
		this.volatility = volatility;
	}
	

}
