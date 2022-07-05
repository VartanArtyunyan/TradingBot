package PyhtonConnection;

public class UpcomingEvent {
	
	String instrument;
	String time;
	String volatility;
	
	
	public UpcomingEvent(String instrument, String time, String volatility) {
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
	public String getVolatility() {
		return volatility;
	}
	public void setVolatility(String volatility) {
		this.volatility = volatility;
	}
	

}
