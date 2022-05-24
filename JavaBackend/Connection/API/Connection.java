package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import AccountMng.Account;
import JsonParser.JsonObject;

public class Connection {

	private static String standartToken = "f65f26fa3004b13f58f04794df17cc30-bf43fa11c4789a9146937ce2c36f553e";
	private static String standartUrlString = "https://api-fxpractice.oanda.com/v3";
	private static String standartAccId = "101-012-22085247-001";

	HttpURLConnection connection;
	URL url;
	String token;
	String urlString;
	String accId;

	public Connection() {
		urlString = standartUrlString;
		token = standartToken;
		accId = standartAccId;
	}

	public Connection(String token) {
		this.token = token;
		urlString = standartUrlString;
	}

	public String getAccountIDs() {

		return getApiResponse(urlString + "/accounts", "GET");

	}
	
	public String getInstruments() {
		return getApiResponse(urlString + "/accounts/" + accId + "/" + "instruments", "GET");
	}

	public String getCandleStickData(int count, String instrument, String from, String to, String price,
			String granularity) {

		ArrayList<String> querys = new ArrayList<>();

		if (count >= 0)
			querys.add("count=" + count);
		if (price != null)
			querys.add("price=" + price);
		if (from != null)
			querys.add("from=" + from);
		if (to != null)
			querys.add("to=" + to);
		if (granularity != null)
			querys.add("granularity=" + granularity);

		String query = "";

		if (querys.size() > 0)
			query += "?";

		Iterator<String> iterator = querys.iterator();
		while (iterator.hasNext()) {
			String q = iterator.next();
			query += q;
			if (iterator.hasNext()) {
				query += "&";
			}
		}

		String url = urlString + "/instruments/" + instrument + "/" + "candles" + query;

		return getApiResponse(url, "GET");

	}

	public String getApiResponse(String urlString, String requestMethod) {
		String jsonString = "";

		try {

			url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("Authorization", "Bearer " + token);

			BufferedReader br;
			String line;

			int status;

			status = connection.getResponseCode();

			// System.out.println(status + " " + connection.getResponseMessage());

			if (status < 299) {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = br.readLine()) != null) {
					jsonString += line;
				}
				br.close();
			} else {
				br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = br.readLine()) != null) {
					jsonString += line;
				}
				br.close();
			}
			connection.disconnect();
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
			e.printStackTrace();
		} catch (ProtocolException e) {
			System.out.println("ProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}

		return jsonString;
	}

}
