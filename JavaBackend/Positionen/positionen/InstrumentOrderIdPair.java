package positionen;

public class InstrumentOrderIdPair {
	
	int signal;
	String id;
	String instrument;
	public InstrumentOrderIdPair(String id, int signal, String instrument) {
		super();
		this.id = id;
		this.signal = signal;
		this.instrument = instrument;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

}
