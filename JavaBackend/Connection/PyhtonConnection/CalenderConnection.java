package PyhtonConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import JsonParser.JsonBuilder;
import JsonParser.JsonObject;
import Threads.StopableThread;
import de.fhws.Softwareprojekt.JsonInstrumentsInstrument;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import positionen.Verwaltung;

public class CalenderConnection extends SocketConnection {

	Verwaltung verwaltung;
	String instrumente;

	public CalenderConnection(Verwaltung verwaltung, int port) {
		super(port,"Warte auf Client für Event basiertes Trading", "Client für eventbasiertes Trading hat sich verbunden");
		this.verwaltung = verwaltung;
		instrumente = makeInstrumentJson(verwaltung.getJsonInstrumentsRoot());
		
	}

	@Override
	public void onStart() {
		try {
			ss = new ServerSocket(port);
			System.out.println("warte auf Client");
			connection = ss.accept();
			System.out.println("Ein Client hat sich verbunden");
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			
			System.out.println(instrumente);
			bw.write(instrumente);
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onTick() {
		try {
			String s = null;
			if(!connection.isInputShutdown()) {
			if(br != null) s = br.readLine();
			if (s != null) System.out.println(s);
			}
			// verwaltung.pushOrder(makeOrder(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Order makeOrder(String input) {
		JsonObject orderJson = new JsonObject(input);

		String instrument = orderJson.getValue("instrument");
		double faktor = Double.parseDouble(orderJson.getValue("factor"));
		int volatility = Integer.parseInt(orderJson.getValue("volatility"));
		boolean longShort = Boolean.parseBoolean(orderJson.getValue("longShort"));

		return new Order(instrument, faktor, volatility, longShort);
	}

	public String makeInstrumentJson(JsonInstrumentsRoot jir) {
		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.openArray("instrumente");
		for (JsonInstrumentsInstrument jii : jir.instruments) {
			if (jii.type.equals("CURRENCY"))
				jsonBuilder.addValue(null, jii.displayName);
		}
		jsonBuilder.closeArray();
		return jsonBuilder.build();
	}

}
