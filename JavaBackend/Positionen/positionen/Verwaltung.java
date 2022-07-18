package positionen;

import java.util.ArrayList;
import java.util.HashMap;

import GUI.GUI;
import LogFileWriter.LogFileWriter;
import PyhtonConnection.Order;
import PyhtonConnection.UpcomingEvent;
import PyhtonConnection.WebInterfaceConnection;
import PyhtonConnection.CalenderConnection;
import PyhtonConnection.CalenderOrder;
import Threads.StopableThread;
import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import de.fhws.Softwareprojekt.Kpi;
import de.fhws.Softwareprojekt.KpiCalculator;
import de.fhws.Softwareprojekt.Signals;
import randomTrader.RandomOrder;
import randomTrader.randomTrader;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;

import API.ApiConnection;

public class Verwaltung extends StopableThread {

	ApiConnection mainConnection;
	ApiConnection randomConnection;
	GUI gui;
	LogFileWriter logFileWriter;
	Signals signals;
	CalenderConnection calenderConnection;
	WebInterfaceConnection webInterfaceConnection;
	randomTrader rngTrader;
	ArrayList<position> positionen;
	ArrayList<trade> trades;
	String granularity;
	ArrayList<StopableThread> threads = new ArrayList<>();
	ArrayList<InstrumentOrderIdPair> blockedSignals;

	boolean showLog = false;

	double einsatz;

	public Verwaltung(ApiConnection connection, ApiConnection randomConnection, String granularity, double einsatz) {

		this.einsatz = einsatz;
		this.mainConnection = connection;
		blockedSignals = new ArrayList<>();
		// gui = new GUI();
		calenderConnection = new CalenderConnection(this, 12000);
		webInterfaceConnection = new WebInterfaceConnection(12001);
		logFileWriter = new LogFileWriter(this, webInterfaceConnection);
		this.granularity = granularity;
		signals = new Signals(this, logFileWriter, this.granularity);

		rngTrader = new randomTrader(this);
		positionen = new ArrayList<position>();
		trades = new ArrayList<trade>();
		// setTimer(1800000);
		
	}

	@Override
	public void onTick() {
		Scanner scanner = new Scanner(System.in);
		String input = "";

		input = scanner.nextLine();
		input = input.toLowerCase();

		switch (input) {
		case "starttrading":
			startThreads();
			break;
		case "stoptrading":
			stopThreads();
			break;
		case "log":
			toggleLog();
			break;
		case "trades":
			printPositions();
			break;
		case "close":
			stopThread();
			break;
		default:
			System.out.println("ungültige eingabe");
			break;
		}

	}

	@Override
	public void onTimer() {

	}

	@Override
	public void onClose() {
		stopThreads();
		webInterfaceConnection.stopThread();
		calenderConnection.stopThread();
		logFileWriter.stopThread();
	}

	public JsonInstrumentsRoot getJsonInstrumentsRoot() {
		return mainConnection.getJsonInstrumentsRoot();
	}

	public void toggleLog() {
		showLog = !showLog;
		mainConnection.toggleLog();
	}

	public ArrayList<Integer> getTradeIDs() {
		aktualisierePosition();
		ArrayList<Integer> output = new ArrayList<>();
		for (trade t : trades) {
			output.add(t.getId());
		}
		return output;
	}

	public void startTraiding() {
		startThreads();
	}

	public void runBot() {
		addThread(signals);
		addThread(rngTrader);
		
		webInterfaceConnection.start();
		calenderConnection.start();
		logFileWriter.start();
		this.start();
	}

	public void startThreads() {
		for (StopableThread st : threads) {
			if (!st.isAlive())
				st.start();
		}
	}
	

	
	public void stopThreads() {
		for (StopableThread st : threads) {
			st.stop();
		}
	}

	public void addThread(StopableThread st) {
		threads.add(st);
	}

	public boolean eneoughBalance() {
		double curBalance = mainConnection.getBalance();

		return curBalance > 100.0;
	}

	public void pushUpcommingEvent(UpcomingEvent upcomingEvent) {
		if (containsPosition(upcomingEvent.getInstrument()))
			return;
		if (!eneoughBalance()) {
			if (showLog)
				System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double kurs = getKurs(upcomingEvent.getInstrument());

		double upperLimit = kurs * 1.0015;
		double lowerLimit = kurs * 0.9985;

		double tsUpperLimt = kurs * 1.00045;
		double tsLowerLimit = kurs * 0.99955;

		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * einsatz;
		double units = buyingPrice / kurs;
		
		
		if (showLog)
			System.out.print("News Trader pushed Upcoming Event -> Following request was send to Oanda: ");
		mainConnection.placeLimitOrder(upcomingEvent.getInstrument(), upcomingEvent.getTime(), units, upperLimit,
				tsUpperLimt, lowerLimit);
		if (showLog)
			System.out.print("                                                                          ");
		mainConnection.placeLimitOrder(upcomingEvent.getInstrument(), upcomingEvent.getTime(), units * -1, lowerLimit,
				tsLowerLimit, upperLimit);
		if (showLog)
			System.out.println("\n");
		aktualisierePosition();
	}

	public void pushCalenderOrder(CalenderOrder calenderOrder) {
		if (containsPosition(calenderOrder.getInstrument()))
			return;
		
		if (!eneoughBalance()) {
			if (showLog)
				System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double kurs = getKurs(calenderOrder.getInstrument());
		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * einsatz;
		double units = buyingPrice;

		double volatility = (calenderOrder.getVolatility().equals("Medium")) ? 1 : 2;

		units = units * calenderOrder.getFaktor() * volatility;

		double tsAbweichung = 0.0005 * volatility;

		double takeProfit = 1.0;
		double stopLoss = 1.0;

		if (calenderOrder.isLong()) {
			takeProfit = (takeProfit + tsAbweichung) * kurs;
			stopLoss = (stopLoss - tsAbweichung) * kurs;
		} else {
			takeProfit = (takeProfit - tsAbweichung) * kurs;
			stopLoss = (stopLoss + tsAbweichung) * kurs;
			units *= -1;
		}
		if (showLog)
			System.out.print("News Trader pushed Order -> Following request was send to Oanda: ");
		OrderResponse order = mainConnection.placeOrder(calenderOrder.getInstrument(), units, takeProfit, stopLoss);

		if (order.wasSuccesfull()) {
			logFileWriter.logCalendar(order.getOrderID(), buyingPrice, calenderOrder);
		} else {
			if (showLog) {
				System.out.println("Unfortunatly this Order was rejected, Oanda says the reason is: "
						+ order.getReasonForRejection());

				System.out.println("\n");
			}
		}
		aktualisierePosition();

	}

	public void pushOrder(Order order) {

		if (!eneoughBalance()) {
			if (showLog)
				System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double curBalance = mainConnection.getBalance();
		double factor = einsatz * (order.isLong() ? 1 : -1);
		double buyingPrice = curBalance * factor * order.getFaktor();
		double kurs = mainConnection.getKurs(order.getInstrument());
		double units = buyingPrice / kurs;
		if (showLog)
			System.out.print("Something pushed an Order Manualy -> Following request was send to Oanda: ");
		OrderResponse orderResponse = mainConnection.placeOrder(order.getInstrument(), units);

		if (orderResponse.wasSuccesfull()) {

		} else {
			if (showLog)
				System.out.println("Unfortunatly this Order was rejected, Oanda says the reason is: "
						+ orderResponse.getReasonForRejection());
		}
		aktualisierePosition();
		if (showLog)
			System.out.println("\n");

	}

	public void pushSignal(Kpi kpi) {
		// if (containsPosition(kpi.getInstrument())) return;

		if (signalIsBlocked(kpi.getInstrument(), kpi.getSignalTyp()))
			return;

		if (!eneoughBalance()) {
			if (showLog)
				System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double factor = einsatz;
		if (kpi.isShort())
			factor = factor * -1;

		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * factor * kpi.getSignalStrenght();
		double units = buyingPrice / kpi.getUnitPrice(new KpiCalculator(this));

		if (showLog) System.out.print("Signal has been detected -> Following request was send to Oanda: ");
		OrderResponse order = mainConnection.placeOrder(kpi.instrument, units, kpi.getTakeProfit(), kpi.getStopLoss());

		if (order.wasSuccesfull()) {
			blockSignal(kpi.getInstrument(), kpi.getSignalTyp(), order.getOrderID());
			logFileWriter.logSignal(order.getOrderID(), buyingPrice, kpi);
		} else {
			if (showLog)
				System.out.println("Unfortunatly this Order was rejected, Oanda says the reason is: " + order.getReasonForRejection());
			if (showLog)
				System.out.println("\n");
		}
		
		aktualisierePosition();

	}

	public void pushRanomOrder(RandomOrder randomOrder) {
		if (containsPosition(randomOrder.getInstrument()))
			return;
		double factor = einsatz;
		if (randomOrder.isShort())
			factor = factor * -1;

		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * factor;
		double kurs = getKurs(randomOrder.getInstrument());
		double units = buyingPrice / kurs;

		double upperBorder = 1.001;
		double lowerBorder = 0.999;

		double takeProfit = kurs * (randomOrder.isLong() ? upperBorder : lowerBorder);
		double stopLoss = kurs * (randomOrder.isShort() ? upperBorder : lowerBorder);

		if (showLog)
			System.out.print(
					"RandomTrader decidet randomly that its time to trade -> Following request was send to Oanda: ");
		OrderResponse order = mainConnection.placeOrder(randomOrder.getInstrument(), units, takeProfit, stopLoss);

		if (order.wasSuccesfull()) {
			webInterfaceConnection.pushRandom(order.getOrderID(), buyingPrice, stopLoss, takeProfit, randomOrder);
		} else if (showLog)
			System.out.println(
					"Unfortunatly this Order was rejected, Oanda says the reason is: " + order.getReasonForRejection());

		if (showLog)
			System.out.println("\n");
		aktualisierePosition();
	}

	public void placeOrder(String instrument, double units, double takeProfit, double stopLoss) {

		if (!eneoughBalance()) {
			if (showLog)
				System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		mainConnection.placeOrder(instrument, units, takeProfit, stopLoss);

		aktualisierePosition();

	}

	private void blockSignal(String instrument, int signal, int id) {
		InstrumentOrderIdPair iop = new InstrumentOrderIdPair("" + id, signal, instrument);
		if (!blockedSignalContainsSignal(instrument, signal))
			blockedSignals.add(iop);

	}

	private void aktualisiereBlockedSignals() {
		ArrayList<Integer> ids = getTradeIDs();

		ArrayList<InstrumentOrderIdPair> iopsToRemove = new ArrayList<>();

		for (InstrumentOrderIdPair iopp : blockedSignals) {

			if (!ids.contains(Integer.parseInt(iopp.getId()))) {
				iopsToRemove.add(iopp);
			}

		}

		for (InstrumentOrderIdPair iopp : iopsToRemove) {
			blockedSignals.remove(iopp);
		}

	}

	private boolean signalIsBlocked(String instrument, int signal) {
		aktualisiereBlockedSignals();
		boolean output = blockedSignalContainsSignal(instrument, signal);

		// System.out.println("Instrument: " + instrument + " Signal: " + signal +
		// "Ergebnis: " + output);
		return output;

	}

	private boolean blockedSignalContainsSignal(String instrument, int signal) {
		boolean output = false;
		for (InstrumentOrderIdPair iopp : blockedSignals) {

			if (iopp.getInstrument().equals(instrument) && iopp.getSignal() == signal)
				output = true;
		}
		return output;
	}

	public JsonCandlesRoot getJsonCandlesRoot(int count, String instrument, String from, String to, String price,
			String granularity) {
		return mainConnection.getJsonCandlesRoot(count, instrument, from, to, price, granularity);
	}

	public double getKurs(String instrument) {
		return mainConnection.getKurs(instrument);
	}

	public void addManualPosition(String instrument) { // GAANZ WICHTIG! in der finalen Version nicht mehr verwenden
		position position1 = new position(instrument);
		addPosition(position1);

	}
	
	private void addPosition(position a) {
		positionen.add(a);
	}
	
	public void aktualisierePosition() {
		trades = mainConnection.getTrades();

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

	public ArrayList<trade> getTrades(ArrayList<Integer> IDs) {
		ArrayList<trade> output = new ArrayList<>();

		for (Integer i : IDs) {
			trade t = mainConnection.getTrade(i);
			if (t != null)
				output.add(t);
		}

		return output;
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

	public void printPositions() {
		aktualisierePosition();
		System.out.println(toString());
	}

	public String toString() {
		String output = "";
		output += "Positionen:";
		output += positionen.size();
		output += "\n";
		for (position p : positionen) {
			output += p;
			output += "\n";
		}
		output += "\n";
		output += "Trades:";
		output += trades.size();
		output += "\n";
		for (trade t : trades) {
			output += t;
			output += "\n";
		}

		return output;
	}
}
