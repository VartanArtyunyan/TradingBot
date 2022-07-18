package PyhtonConnection;

import java.io.IOException;

import JsonParser.JsonBuilder;
import de.fhws.Softwareprojekt.Kpi;
import randomTrader.RandomOrder;

public class WebInterfaceConnection extends SocketConnection {

	public WebInterfaceConnection(int port) {
		super(port, "Warte auf WebInterface", "WebInterface hat sich verbunden");
	}

	private void sendMessage(String message) throws IOException {
		if (bw != null && !connection.isOutputShutdown()) {
			bw.write(message);
			bw.newLine();
			bw.flush();
		}
	}

	private void trySending(String message) {
		boolean repeat = true;

		while (repeat) {
			try {
				sendMessage(message);
				repeat = false;
			} catch (IOException e) {
				System.out.println(
						"Problem mit der Verbindung zum WebInterface, IOException ist aufgetreten beim Versuch ein Signal zu senden");
			}
		}
	}
	
	public void pushUpcoming(int orderID, double buyingPrice, UpcomingEvent upcomingEvent) {
		
		JsonBuilder message = new JsonBuilder();
		
		message.openObject("upcoming");
		message.addValue("instrument", upcomingEvent.instrument);
		message.addValue("buyingPrice", buyingPrice);
		message.addValue("volatility", upcomingEvent.getVolatility());
		message.addValue("name", "¯\\_( )_/¯");
		message.addValue("countryCode", "¯\\_( )_/¯");
		message.closeObject();
		
		trySending(message.build());
	}

	public void pushCalendar(int orderID, double buyingPrice, CalenderOrder calenderOrder) {
		JsonBuilder message = new JsonBuilder();

		message.openObject("calendar");
		message.addValue("id", orderID);
		message.addValue("instrument", calenderOrder.getInstrument());
		message.addValue("buyingPrice", buyingPrice);
		message.addValue("factor", calenderOrder.getFaktor());
		message.addValue("longShort", calenderOrder.longShort);
		message.addValue("name", "¯\\_( )_/¯");
		message.addValue("countryCode", "¯\\_( )_/¯");
		message.closeObject();
		
		trySending(message.build());
	}

	public void pushSignal(int orderID, double buyingPrice, Kpi kpi) {

		JsonBuilder message = new JsonBuilder();

		message.openObject("signal");
		message.addValue("id", orderID);
		message.addValue("instrument", kpi.getInstrument());
		message.addValue("lastTime", kpi.getLastTime());
		message.addValue("buyingPrice", buyingPrice);
		message.addValue("lastPrice", kpi.getLastPrice());
		message.addValue("takeProfit", kpi.getTakeProfit());
		message.addValue("stopLoss", kpi.getStopLoss());
		message.addValue("macd", kpi.getMacd());
		message.addValue("macdTriggered", kpi.getMacdTriggert());
		message.addValue("parabolicSAR14", kpi.getParabolicSAR());
		message.addValue("ema200", kpi.getEma());
		message.addValue("sma20", kpi.getSma());
		message.addValue("sma50", kpi.getSma2());
		message.addValue("atr14", kpi.getAtr());
		message.closeObject();
		
		trySending(message.build());
	}
	
	public void pushRandom(int orderID, double buyingPrice, double stopLoss, double takeProfit, RandomOrder randomOrder ) {
		
		JsonBuilder message = new JsonBuilder();
		
		message.openObject("random");
		message.addValue("instrument", randomOrder.getInstrument());
		message.addValue("buyingPrice", buyingPrice);
		message.addValue("stopLoss", stopLoss);
		message.addValue("takeProfit", takeProfit);
		message.closeObject();
		
		trySending(message.build());
	}

	public void addSellingPrice(int tradeID, double realizedPL) {

		JsonBuilder message = new JsonBuilder();

		message.openObject("update");
		message.addValue("id", tradeID);
		message.addValue("realizedPL", realizedPL);
		message.closeObject();
		
		trySending(message.build());
	}

}
