package positionen;

import java.util.ArrayList;
import GUI.GUI;
import LogFileWriter.LogFileWriter;
import PyhtonConnection.Order;
import PyhtonConnection.PyhtonConnection;

import Threads.StopableThread;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.Signals;
import randomTrader.RandomOrder;
import randomTrader.randomTrader;

import java.util.HashSet;

import API.ApiConnection;

public class Verwaltung extends stopableThread{

	ApiConnection connection;
	GUI gui;
	LogFileWriter logFileWriter;
	Signals signals;
	PyhtonConnection pythonConnection;
	randomTrader rngTrader;
	ArrayList<position> positionen;
	ArrayList<trade> trades;
	String granularity;
	ArrayList<StopableThread> threads = new ArrayList<>();

	double einsatz;

	public Verwaltung(ApiConnection connection, String granularity, double einsatz) {

		this.einsatz = einsatz;
		this.connection = connection;
		// gui = new GUI();
		logFileWriter = new LogFileWriter();
		this.granularity = granularity;
		signals = new Signals(connection, this, logFileWriter, this.granularity);
		pythonConnection = new PyhtonConnection(this);
		rngTrader = new randomTrader(this);
		positionen = new ArrayList<position>();
		trades = new ArrayList<trade>();
		
	}
	
	public void onTick() {
		//System.out.println(postionsHaveChanged());
	}

	public JsonInstrumentsRoot getJsonInstrumentsRoot() {
		return connection.getJsonInstrumentsRoot();
	}
	
	public void updateLog() {
		if(postionsHaveChanged()) {
			
		}
	}
	
	public boolean postionsHaveChanged() {
		
		ArrayList<Integer> oldTrades =getTradeIDs();
		aktualisierePosition();
		ArrayList<Integer> newTrades = getTradeIDs();
		
		return !newTrades.equals(oldTrades);
	}
	
	public ArrayList<Integer> getTradeIDs(){
		ArrayList<Integer> output = new ArrayList<>();
		
		for(trade t: trades) {
			output.add(t.getId());
		}
		
		return output;
	}

	public void startTraiding() {
		addThread(pythonConnection);
		 addThread(signals);
		addThread(rngTrader);
		 addThread(this);
		startThreads();
	}

	public void startThreads() {
		for (StopableThread st : threads) {
			st.start();
		}
	}

	public void stopThreads() {
		for (StopableThread st : threads) {
			st.stopThread();
		}
	}

	public void addThread(StopableThread st) {
		threads.add(st);
	}

	public boolean eneoughBalance() {
		double curBalance = connection.getBalance();

		return curBalance > 100.0;
	}

	public void pushOrder(Order order) {

		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double curBalance = connection.getBalance();
		double factor = einsatz * (order.isLong() ? 1 : -1);
		double buyingPrice = curBalance * factor * order.getFaktor();
		double kurs = connection.getKurs(order.getInstrument());
		double units = buyingPrice / kurs;

		connection.placeOrder(order.getInstrument(), units);
		aktualisierePosition();
	}

	public void pushSignal(Kpi kpi) {
		if (containsPosition(kpi.getInstrument()))
			return;
		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double factor = einsatz;
		if (kpi.isShort())
			factor = factor * -1;

		double curBalance = connection.getBalance();
		double buyingPrice = curBalance * factor * kpi.getSignalStrenght();
		double units = buyingPrice / kpi.getLastPrice();
		
		OrderResponse order = connection.placeOrder(kpi.instrument, units, kpi.getTakeProfit(), kpi.getStopLoss());
		
		if(order.wasSuccesfull()) {
		logFileWriter.log(order.getOrderID(), kpi.getInstrument(), kpi.getLastTime(), buyingPrice, kpi.getLastPrice(), kpi.getTakeProfit(),
				kpi.getStopLoss(), kpi.getMacd(), kpi.getMacdTriggert(), kpi.getParabolicSAR(), kpi.getEma());
		
		
		}
		else System.out.println("Order was rejected");
		aktualisierePosition();

	}

	public void pushRanomOrder(RandomOrder randomOrder) {
		if (containsPosition(randomOrder.getInstrument()))
			return;
		double factor = einsatz;
		if (randomOrder.isShort())
			factor = factor * -1;

		double curBalance = connection.getBalance();
		double buyingPrice = curBalance * factor;
		double kurs = getKurs(randomOrder.getInstrument());
		double units = buyingPrice / kurs;

		double upperBorder = 1.001;
		double lowerBorder = 0.999;

		double takeProfit = kurs * (randomOrder.isLong() ? upperBorder : lowerBorder);
		double stopLoss = kurs * (randomOrder.isShort() ? upperBorder : lowerBorder);

		connection.placeOrder(randomOrder.getInstrument(), units, takeProfit, stopLoss);
		aktualisierePosition();
	}

	public void placeOrder(String instrument, double units, double takeProfit, double stopLoss) {

		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		connection.placeOrder(instrument, units, takeProfit, stopLoss);

		aktualisierePosition();

	}

	public double getKurs(String instrument) {
		return connection.getKurs(instrument);
	}

	public void addManualPosition(String instrument) { // GAANZ WICHTIG! in der finalen Version nicht mehr verwenden
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
				addPosition(p);
				;
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

		if (positionen == null)
			return;

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
			if (p.getInstrument().equals(instrument))
				output = true;
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
