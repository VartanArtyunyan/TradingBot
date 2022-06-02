package positionen;

import java.util.ArrayList;
import GUI.GUI;
import LogFileWriter.LogFileWriter;
import de.fhws.Softwareprojekt.Signals;

import java.util.HashSet;

import API.ApiConnection;

public class Verwaltung {

	ApiConnection connection;
	GUI gui;
	LogFileWriter logFileWriter;
	Signals signals;
	ArrayList<position> positionen;
	ArrayList<trade> trades;
	String granularity;

	public Verwaltung(ApiConnection connection, String granularity) {
		this.connection = connection;
		//gui = new GUI();
		logFileWriter = new LogFileWriter();
		signals = new Signals(connection, this, logFileWriter);
		positionen = new ArrayList<position>();
		trades = new ArrayList<trade>();
	}

	
	public void onTick(){
		
	}
	
	public boolean eneoughBalance() {
		double curBalance = connection.getBalance();
		
		return curBalance > 100.0;
	}

	public void placeShortOrder(String instrument,double limitPrice, double takeProfit, double stopLoss, double kurs) {
		
		if(!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}
		
		double curBalance = connection.getBalance();

		double units = (curBalance * (-0.02)) / kurs;

		connection.placeLimitOrder(instrument, limitPrice, units, takeProfit, stopLoss);
		
		aktualisierePosition();
	}
	
	
	public void placeLongOrder(String instrument, double limitPrice, double takeProfit, double stopLoss, double kurs) {
		
		if(!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double curBalance = connection.getBalance();

		double units = (curBalance * 0.02) / kurs;

		connection.placeLimitOrder(instrument,limitPrice, units, takeProfit, stopLoss);
		
		aktualisierePosition();
	}
	
	public void addManualPosition(String instrument) { //GAANZ WICHTIG! in der finalen Version nicht mehr verwenden
		position position1 = new position(instrument);
		addPosition(position1);
		
	}
	private void addPosition(position a) {
		positionen.add(a);
	}
	
	

	public void closeWholePosition(String i) {

		connection.closeWholePosition(i);

		aktualisierePosition();
	}

	public void closePosition(String i, int anzahl) {
		connection.closePosition(i, anzahl);

		aktualisierePosition();
	}

	public void aktualisierePosition() {
		trades = connection.getTrades();
		HashSet<String> erstelltePosition = new HashSet<>();
		for (int i = 0; i < trades.size(); i++) {
			if (erstelltePosition.contains(trades.get(i).getInstrument())) {
				putTrade(trades.get(i), trades.get(i).getInstrument());
			} else {
				position p = new position(trades.get(i).getInstrument());
				erstelltePosition.add(trades.get(i).getInstrument());
				p.addID(trades.get(i).getId());
				addPosition(p);;
			}

		}
		zusammenschluss();
	}

	public void zusammenschluss() {

		for (int i = 0; i < positionen.size(); i++) {
			double summe = 0;
			int anzahlAktien = 0;
			int units = 0;
			for (int j = 0; j < trades.size(); j++) {
				if (positionen.get(i).contains(trades.get(j))) {
					summe = summe + (trades.get(j).getPrice() * trades.get(j).getUnits());
					anzahlAktien = anzahlAktien + trades.get(j).getUnits();

				}
			}
			positionen.get(i).setGesamtsumme(summe);
			positionen.get(i).setGesamtzahlaktien(anzahlAktien);
		}

	}

	public void putTrade(trade t, String b) {
		
		if(positionen == null) return;
		
		for (int i = 0; i < positionen.size(); i++) {
			if (positionen.get(i).getInstrument().equals(b)) {
				positionen.get(i).addID(t.getId());
			}
		}
	}
	
	public boolean containsPosition(String instrument) {
		aktualisierePosition();
		
		boolean output = false;
		for (position p : positionen) {
			if (p.getInstrument().equals(instrument)) output = true;
		}
		return output;
	}

	public boolean enthalten(trade t) {
		if (trades.size() <= 0) {
			return false;
		}
		for (int i = 0; i < trades.size(); i++) {
			if (trades.get(i).getId() == t.getId()) {
				return true;
			}

		}
		return false;
	}

	public String toString() {
		String output = "";
		output += "Positionen:";
		output += "\n";
		for (position p : positionen) {
			output += p;
			output += "\n";
		}
		output += "\n";
		output += "Trades:";
		output += "\n";
		for (trade t : trades) {
			output += t;
			output += "\n";
		}

		return output;
	}
}
