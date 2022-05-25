package positionen;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.HashSet;

import API.ApiConnection;

public class Verwaltung {

	ApiConnection connection;
	ArrayList<position> positionen;
	ArrayList<trade> trades;

	public Verwaltung(ApiConnection connection) {
		this.connection = connection;
		positionen = new ArrayList<position>();
		trades = new ArrayList<trade>();

	}

	// public void placeOrder(String i, double wert, double kurs, double obergrenze,
	// double untergrenze) {
	// int anzahlAktien;

	// anzahlAktien = (int) (wert / kurs);

	// connection.placeOrder(i, anzahlAktien, obergrenze, untergrenze);

	// aktualisierePosition();
	// }

	public void placeShortOrder(String instrument, double takeProfit, double stopLoss, double kurs) {

		double curBalance = connection.getBalance();

		double units = (curBalance * (-0.02)) / kurs;

		connection.placeOrder(instrument, units, takeProfit, stopLoss);

	}

	public void placeLongOrder(String instrument, double takeProfit, double stopLoss, double kurs) {

		double curBalance = connection.getBalance();

		double units = (curBalance * 0.02) / kurs;

		connection.placeOrder(instrument, units, takeProfit, stopLoss);

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
		HashSet<Instrumente> erstelltePosition = new HashSet<>();
		for (int i = 0; i < trades.size(); i++) {
			if (erstelltePosition.contains(trades.get(i).getInstrument())) {
				putTrade(trades.get(i), trades.get(i).getInstrument());
			} else {
				position p = new position(trades.get(i).getInstrument());
				erstelltePosition.add(trades.get(i).getInstrument());
				p.addID(trades.get(i).getId());
				positionen.add(p);
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

	public void putTrade(trade t, Instrumente b) {

		for (int i = 0; i < positionen.size(); i++) {
			if (positionen.get(i).getInstrument() == b) {
				positionen.get(i).addID(t.getId());
			}
		}
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
