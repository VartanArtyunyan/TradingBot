package PyhtonConnection;

import java.io.IOException;

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
	
	public void pushSignal(int orderID, Kpi kpi) {
		
	}
	
	public void addSellingPrice(int tradeID, double realizedPL) {
		
	}

}
