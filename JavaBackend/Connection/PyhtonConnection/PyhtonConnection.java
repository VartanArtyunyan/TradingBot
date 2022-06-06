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

	
	public PyhtonConnection(Verwaltung verwaltung) {
		 this.verwaltung = verwaltung;
	}
	
	
	public void run() {
		JsonInstrumentsRoot jir = verwaltung.getJsonInstrumentsRoot();
		String instrumente = makeInstrumentJson(jir);
		
		
		System.out.println("warte auf Client");
		int port = 12000;
		try (ServerSocket ss = new ServerSocket(port);
				Socket connection = ss.accept();
				InputStream is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));

				OutputStream os = connection.getOutputStream();
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

		) {
			System.out.println("Ein Client hat sich verbunden");

			bw.write(instrumente);
			bw.flush();

			while (getExecute()){
				try {
					String s = br.readLine();
					System.out.println(s);
					verwaltung.pushOrder(makeOrder(s));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public Order makeOrder(String input) {
		JsonObject orderJson = new JsonObject(input);
		
		String instrument = orderJson.getValue("instrument");
		double faktor = Double.parseDouble(orderJson.getValue("faktor"));
		boolean longShort = Boolean.parseBoolean(orderJson.getValue("longShort"));
		
		return new Order(instrument, faktor, longShort);
	}
	
	public String makeInstrumentJson(JsonInstrumentsRoot jir) {
		JsonBuilder jsonBuilder = new JsonBuilder();
		jsonBuilder.openArray("instrumente");
		for(JsonInstrumentsInstrument jii : jir.instruments) {
			jsonBuilder.addString(null, jii.name);
		}
		jsonBuilder.closeArray();
		return jsonBuilder.build();
	}
	
	
	
	 
}
