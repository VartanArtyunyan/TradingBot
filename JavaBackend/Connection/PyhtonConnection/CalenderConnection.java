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
	String input;

	public CalenderConnection(Verwaltung verwaltung, int port) {
		super(port, "Warte auf Client für Event basiertes Trading",
				"Client für eventbasiertes Trading hat sich verbunden");
		this.verwaltung = verwaltung;
		instrumente = makeInstrumentJson(verwaltung.getJsonInstrumentsRoot());

	}

	@Override
	public void onStart() {

		System.out.println("waiting for NewsTrader to Connect ...");
		connectToNewsTrader();

	}

	@Override
	public void onTick() {
		try {
			if (br != null)
				input = br.readLine();
			if (connection.isConnected() && input != null) {

				if (input != null)
					push(input);
			} else {
				System.out.println("Lost connection to NewsTrader, waiting for reconnect ...");
				connectToNewsTrader();
			}
			// verwaltung.pushOrder(makeOrder(s));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void connectToNewsTrader() {
		try {
			ss = new ServerSocket(port);
			connection = ss.accept();
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			bw.write(instrumente);
			bw.flush();
			System.out.println("NewsTrader connected at port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String convertMessageToJson(String input) {
		String[] sArray = input.split(" ");

		String json = "";

		for (int i = 0; i < sArray.length; i++) {
			json += sArray[i];
		}

		char[] jsonCharArray = json.toCharArray();

		for (int i = 0; i < jsonCharArray.length; i++) {
			if (jsonCharArray[i] == '\'')
				jsonCharArray[i] = '\"';
			if (jsonCharArray[i] == '/')
				jsonCharArray[i] = '_';
		}
		
		return new String(jsonCharArray);
	}

	private void push(String input) {

		

		String json = convertMessageToJson(input);
		// System.out.println(json);

		//System.out.println("Recived Message from NewsTrader: " + json);
		JsonObject order = new JsonObject(json);

		// System.out.println("Orderjson:" + order);

		if (order.contains("order")) {
			verwaltung.pushCalenderOrder(makeOrder(order));

		}
		if (order.contains("upcoming")) {
			verwaltung.pushUpcommingEvent(makeUpcomingEven(order));

		}
	}

	private CalenderOrder makeOrder(JsonObject orderJson) {

		JsonObject order = orderJson.getObject("order");

		String instrument = order.getValue("instrument");
		double faktor = Double.parseDouble(order.getValue("factor"));
		String volatility = order.getValue("volatility");
		boolean longShort = Boolean.parseBoolean(order.getValue("longShort"));

		return new CalenderOrder(instrument, faktor, volatility, longShort);
	}

	private UpcomingEvent makeUpcomingEven(JsonObject orderJson) {

		JsonObject order = orderJson.getObject("upcoming");

		String instrument = order.getValue("instrument");
		String time = order.getValue("time");
		String volatility = order.getValue("volatility");

		return new UpcomingEvent(instrument, time, volatility);

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
