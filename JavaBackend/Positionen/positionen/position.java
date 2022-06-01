package positionen;

import java.util.ArrayList;
import java.util.HashSet;

public class position {

	HashSet<Integer> tradeId;
	Instrumente währung;
	String instrument;
	double gesamtsumme;
	double einsatz;
	double aktuellerkurs;
	double grenzwert;
	int anzahlaktie;
	
	
	public position(String instrument) {
		this.instrument = instrument;
		this.tradeId = new HashSet<Integer>();
		
	}
	
	public void addID(int Id) {
		tradeId.add(Id);
		
	}
	public String getInstrument() {
		return instrument;
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
	public void setGesamtsumme(double P) {
		gesamtsumme= P;
		
	}
	public void setGesamtzahlaktien(int anzahl) {
		anzahlaktie = anzahl;
		
	}
	
	
	
	
	
	
	
	
	
	
}