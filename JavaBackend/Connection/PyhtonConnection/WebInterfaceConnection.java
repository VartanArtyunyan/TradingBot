package PyhtonConnection;

import java.io.IOException;

import JsonParser.JsonBuilder;
import de.fhws.Softwareprojekt.Kpi;

public class WebInterfaceConnection extends SocketConnection{
	
	

	public WebInterfaceConnection(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}
	
	private void sendMessage(String message) throws IOException {
		bw.write(message);
		bw.newLine();
		bw.flush();
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
		message.addValue("stopLoss", kpi.getShortStopLoss());
		message.addValue("macd", kpi.getMacd());
		message.addValue("macdTriggered", kpi.getMacdTriggert());
		message.addValue("parabolicSAR", kpi.getParabolicSAR());
		message.addValue("ema", kpi.getEma());
		message.addValue("sma", 0);
		message.addValue("atr", 0);
		message.addValue("rsi", 0);
		message.closeObject();
		
		boolean repeat = true;
		
		while(repeat) {
			try {
				sendMessage(message.build());
				repeat = false;
			} catch (IOException e) {
				System.out.println("Problem mit der Verbindung zum WebInterface, IOException ist aufgetreten beim Versuch ein Signal zu senden");
			}
		}
		
	}
	
	public void addSellingPrice(int tradeID, double realizedPL) {
		
		JsonBuilder message = new JsonBuilder();
		
		message.openObject("update");
		message.addValue("id", tradeID);
		message.addValue("realizedPL", realizedPL);
		
	}

}
