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
	String urlPrefix;
	String accId;

	public Connection() {
		urlPrefix = standartUrlString;
		token = standartToken;
		accId = standartAccId;
	}

	public Connection(String token) {
		this.token = token;
		urlPrefix = standartUrlString;
	}

	public String getAccountIDs() {
		return GET(urlPrefix + "/accounts");

	}

	public String getAccount() {
		return GET(urlPrefix + "/accounts/" + accId);
	}

	public String getTrades() {
		return GET(urlPrefix + "/accounts/" + accId + "/" + "trades");
	}

	public String getInstruments() {
		return GET(urlPrefix + "/accounts/" + accId + "/" + "instruments");
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

		String url = urlPrefix + "/instruments/" + instrument + "/" + "candles" + query;

		return GET(url);

	}

	public void placeLimitOrder(String requestJson) {
		System.out.println(requestJson);
	
		// POST((urlString + "/accounts/" + accId + "/orders"), requestJson);
	}

	private void setConnection(String urlString, String requestMethod) throws IOException {

		url = new URL(urlString);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty("Authorization", "Bearer " + token);
	}

	private String GET(String urlString) {

		try {
			setConnection(urlString, "GET");

			String output = getResponse();
			connection.disconnect();
			return output;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	private String POST(String urlString, String requestJson) {
		try {

			setConnection(urlString, "POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			connection.getOutputStream().write(requestJson.getBytes(), 0, requestJson.length());
			connection.getOutputStream().close();

			String output = getResponse();
			connection.disconnect();
			return output;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	public String getResponse() throws IOException {
		String jsonString = "";
		
		//System.out.println("Response wurde gstartet mit url: " + url.getPath());

		BufferedReader br;
		String line;

		int status;

		status = connection.getResponseCode();

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
			System.out.println("Fhelercode mit url: " + url.getPath() + "ResponseCode: " + status);
			String[] sArray = jsonString.split(",");
			for (int i = 0; i < sArray.length; i++) {
				System.out.println(sArray[i]);
			}
		}
		
		
		return jsonString;

	}

}
