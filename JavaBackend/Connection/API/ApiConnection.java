package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;



import positionen.Instrumente;
import positionen.trade;

public class ApiConnection {
	
	private static String standartToken = "f65f26fa3004b13f58f04794df17cc30-bf43fa11c4789a9146937ce2c36f553e";
	private static String standartUrlString = "https://api-fxpractice.oanda.com/v3";
	private static String standartAccId = "101-012-22085247-001";
	
	HttpURLConnection connection;
	URL url;
	
	
	String token;
	String urlString;
	String accId;
	
	public ApiConnection() {
		urlString = standartUrlString;	
		token = standartToken;
		accId = standartAccId;
	}
	
	public ApiConnection(String token, String url) {
		urlString = url;
		this.token = token;
	}
	
	public String[] getAccounts() {
		String[] output = new String[1];
		
		return output;
	}
	
	
	
	public String getApiResponseGET(String call) {
		String jsonString = "";
		
		try {
		
			url = new URL(urlString + "/accounts/" + accId + "/" + call);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization","Bearer f65f26fa3004b13f58f04794df17cc30-bf43fa11c4789a9146937ce2c36f553e");
			
			BufferedReader br;
			String line;
			
			StringBuffer sb = new StringBuffer();
			int status;

			status = connection.getResponseCode();

			System.out.println(status + " " + connection.getResponseMessage());

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	public static String getVarFromJson(String json, String string) {
		String[] sArray = json.split(",");
		for (int i = 0; i < sArray.length; i++) {
			if (sArray[i].contains(string)) {
				String[] sArray2 = sArray[i].split("\":\"");
				if (sArray2[1].substring(sArray2[1].length() - 1, sArray2[1].length()).equals("\""))
					return sArray2[1].substring(0, sArray2[1].length() - 1);
				else
					return sArray2[1];
			}
		}
		return null;
	}
	
	public static String getArrayFromJson(String json) {
		int start = 0;
		int end = 0;
		String output = json;
		char[] charArray = json.toCharArray();
		
		for(int i = 0; i < charArray.length; i++) {
			if(charArray[i] == '[') start = i+1;
			if(charArray[i] == ']') end = i-start;
		}
		
		output = output.substring(start);
		output = output.substring(0,end);
		return output;
	}
	
	public ArrayList<trade> getTrades(){
		ArrayList<trade> output = new ArrayList<>();
		String apiResponseString =  getApiResponseGET("trades");
		
		
		output = 	convertApiStringToTradesArray(apiResponseString);
		return output;
	}
	
	public ArrayList<trade> convertApiStringToTradesArray(String json){
		ArrayList<trade> output = new ArrayList<trade>();
		String TradesStringJSONArray = getArrayFromJson(json);
		
		String[] TradesStringArray = TradesStringJSONArray.split("}");
		
		for(int i = 0; i < TradesStringArray.length; i++) {
			output.add(convertApiStringToTradeModel(TradesStringArray[i]));
		}
			
		return output;
	}
	
	public trade convertApiStringToTradeModel(String json) {
		return new trade(
									Integer.parseInt(getVarFromJson(json,"id")),
									Instrumente.valueOf(getVarFromJson(json,"instrument")),
									Double.parseDouble(getVarFromJson(json,"price")),
									getVarFromJson(json,"openTime"),
									Integer. parseInt(getVarFromJson(json,"initialUnits")),
									getVarFromJson(json,"initialMarginRequired"),
									Integer. parseInt(getVarFromJson(json,"currentUnits")),
									getVarFromJson(json,"realizedPL"),
									getVarFromJson(json,"unrealizedPL"),
									getVarFromJson(json,"marginUsed")
				
				);
					
	}

}






