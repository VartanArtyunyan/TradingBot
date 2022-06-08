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
import Threads.stopableThread;
import de.fhws.Softwareprojekt.JsonInstrumentsInstrument;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import positionen.Verwaltung;

public class PyhtonConnection extends stopableThread{
	
	Verwaltung verwaltung;
	
	int port;
	ServerSocket ss;
	Socket connection;
	BufferedReader br;
	BufferedWriter bw;
	String instrumente;
	
	public PyhtonConnection(Verwaltung verwaltung) {
		 this.verwaltung = verwaltung;
		
		instrumente = makeInstrumentJson(verwaltung.getJsonInstrumentsRoot());
	
	}
	
	@Override
	public void onStart(){
		 try {
				ss = new ServerSocket(port);
				System.out.println("warte auf Client");
				connection = ss.accept();
				System.out.println("Ein Client hat sich verbunden");
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
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
		String s = br.readLine();
		System.out.println(s);
		verwaltung.pushOrder(makeOrder(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Order makeOrder(String input) {
		JsonObject orderJson = new JsonObject(input);
		
		String instrument = orderJson.getValue("instrument");
		double faktor = Double.parseDouble(orderJson.getValue("faktor"));
		int volatility = Integer.parseInt(orderJson.getValue("volatility"));
		boolean longShort = Boolean.parseBoolean(orderJson.getValue("longShort"));
		
		return new Order(instrument, faktor, volatility, longShort);
	}
	
	public String makeInstrumentJson(JsonInstrumentsRoot jir) {
		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.openArray("instrumente");
		for(JsonInstrumentsInstrument jii : jir.instruments) {
			jsonBuilder.addString(null, jii.displayName);
		}
		jsonBuilder.closeArray();
		return jsonBuilder.build();
	}
	
	
	
	 
}
