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
import de.fhws.Softwareprojekt.Signals;
import randomTrader.RandomOrder;
import randomTrader.randomTrader;

import java.util.HashSet;
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
	HashMap<String, InstrumentOrderIdPair> blockedSignals;

	double einsatz;

	public Verwaltung(ApiConnection connection, ApiConnection randomConnection, String granularity, double einsatz) {

		this.einsatz = einsatz;
		this.mainConnection = connection;
		blockedSignals = new HashMap<>();
		// gui = new GUI();
		calenderConnection = new CalenderConnection(this, 12000);
		webInterfaceConnection = new WebInterfaceConnection(12001);
		logFileWriter = new LogFileWriter(this, webInterfaceConnection);
		this.granularity = granularity;
		signals = new Signals(this, logFileWriter, this.granularity);

		rngTrader = new randomTrader(this);
		positionen = new ArrayList<position>();
		trades = new ArrayList<trade>();

	}

	@Override
	public void onTick() {

	}

	public JsonInstrumentsRoot getJsonInstrumentsRoot() {
		return mainConnection.getJsonInstrumentsRoot();
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
		// addThread(webInterfaceConnection);
		// addThread(calenderConnection);
		addThread(signals);
		// addThread(rngTrader);
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
		double curBalance = mainConnection.getBalance();

		return curBalance > 100.0;
	}

	public void pushUpcommingEvent(UpcomingEvent upcomingEvent) {

		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double kurs = getKurs(upcomingEvent.getInstrument());

		double upperLimit = kurs * 1.0015;
		double lowerLimit = kurs * 0.9985;

		double tsUpperLimt = kurs * 1.0045;
		double tsLowerLimit = kurs * 0.9955;

		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * einsatz;
		double units = buyingPrice * kurs;

		mainConnection.placeLimitOrder(upcomingEvent.getInstrument(), upcomingEvent.getTime(), units, upperLimit,
				tsUpperLimt, lowerLimit);
		mainConnection.placeLimitOrder(upcomingEvent.getInstrument(), upcomingEvent.getTime(), units * -1, lowerLimit,
				tsLowerLimit, upperLimit);
	}

	public void pushCalenderOrder(CalenderOrder calenderOrder) {

		// {order:{instument:"EUR_UID",factor:2.3,volatility:2,longShort:true}}

		double kurs = getKurs(calenderOrder.getInstrument());
		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * einsatz;
		double units = buyingPrice * kurs;

		double volatility = (calenderOrder.getVolatility().equals("Medium")) ? 1 : 2;

		units = units * calenderOrder.getFaktor() * volatility;

		double tsAbweichung = 0.0015 * volatility;

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
		System.out.println("pushCalenderOrder");
		mainConnection.placeOrder(calenderOrder.getInstrument(), units, takeProfit, stopLoss);

	}

	public void pushOrder(Order order) {

		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double curBalance = mainConnection.getBalance();
		double factor = einsatz * (order.isLong() ? 1 : -1);
		double buyingPrice = curBalance * factor * order.getFaktor();
		double kurs = mainConnection.getKurs(order.getInstrument());
		double units = buyingPrice * kurs;

		mainConnection.placeOrder(order.getInstrument(), units);
		aktualisierePosition();
	}

	public void pushSignal(Kpi kpi) {
		if (signalIsBlocked(kpi.getInstrument(), kpi.getSignalTyp()))
			return;
		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		double factor = einsatz;
		if (kpi.isShort())
			factor = factor * -1;

		double curBalance = mainConnection.getBalance();
		double buyingPrice = curBalance * factor * kpi.getSignalStrenght();
		double units = buyingPrice * kpi.getLastPrice();

		OrderResponse order = mainConnection.placeOrder(kpi.instrument, units, kpi.getTakeProfit(), kpi.getStopLoss());
		blockSignal(kpi.getInstrument(), kpi.getSignalTyp(), "");
		if (order.wasSuccesfull()) {
			logFileWriter.logSignal(order.getOrderID(), buyingPrice, kpi);
			blockSignal(kpi.getInstrument(), kpi.getSignalTyp(), order.getOrderID());
		} else
			System.out.println("Order was rejected");
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
		double units = buyingPrice * kurs;

		double upperBorder = 1.001;
		double lowerBorder = 0.999;

		double takeProfit = kurs * (randomOrder.isLong() ? upperBorder : lowerBorder);
		double stopLoss = kurs * (randomOrder.isShort() ? upperBorder : lowerBorder);

		mainConnection.placeOrder(randomOrder.getInstrument(), units, takeProfit, stopLoss);
		aktualisierePosition();
	}

	public void placeOrder(String instrument, double units, double takeProfit, double stopLoss) {

		if (!eneoughBalance()) {
			System.out.println("Kauf wurde aufgrund von zu niedrigem Kontostand nicht ausgeführt");
			return;
		}

		mainConnection.placeOrder(instrument, units, takeProfit, stopLoss);

		aktualisierePosition();

	}

	private void blockSignal(String instrument, int signal, String id) {
		InstrumentOrderIdPair iop = new InstrumentOrderIdPair(signal, instrument);
		blockedSignals.put(id, iop);
	}

	private void aktualisiereBlockedSignals() {
		ArrayList<Integer> ids = getTradeIDs();

		ArrayList<String> idsToRemove = new ArrayList<>();

		for (String id : blockedSignals.keySet()) {

			if (!ids.contains(Integer.parseInt(id))) {
				idsToRemove.add(id);
			}
		}
		
		for(String id : idsToRemove) {
			blockedSignals.remove(id);
		}
	}

	private boolean signalIsBlocked(String instrument, int signal) {
		//aktualisiereBlockedSignals();
		
		boolean output = false;
		
		for(Entry<String, InstrumentOrderIdPair> e : blockedSignals.entrySet()) {
			if(e.getValue().getInstrument().equals(instrument) && e.getValue().getSignal() == signal) output = true;
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

	public void closeWholePosition(String i) {

		mainConnection.closeWholePosition(i);

		aktualisierePosition();
	}

	public void closePosition(String i, int anzahl) {
		mainConnection.closePosition(i, anzahl);

		aktualisierePosition();
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
			output.add(mainConnection.getTrade(i));
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
