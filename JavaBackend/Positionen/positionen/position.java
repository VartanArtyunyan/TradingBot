package positionen;

import java.util.ArrayList;
import java.util.HashSet;

public class position {

	HashSet<Integer> tradeId;
	Instrumente währung;
	double gesamtsumme;
	double einsatz;
	double aktuellerkurs;
	double grenzwert;
	
	public position(Instrumente währung) {
		this.währung = währung;
		this.tradeId = new HashSet<Integer>();
		
	}
	
	public void addID(int Id) {
		tradeId.add(Id);
		
	}
	public Instrumente getInstrument() {
		return währung;
}
	
	public boolean contains(trade t) {
		return tradeId.contains(t.getId());
		
	}
	
	public String toString() {
		String output = "";
		output+= "Instrument: " + währung;
		output+= ", IDs: ";
		
		for(Integer i : tradeId) {
			output+= i + ", ";
		}
		
		return output;
	}

}