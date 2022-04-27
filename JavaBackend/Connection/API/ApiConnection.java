package API;


import java.util.ArrayList;

import JsonParser.JsonParser;

import positionen.trade;

public class ApiConnection {

	JsonParser jsonParser = new JsonParser();
	Connection connection = new Connection();

	public ArrayList<trade> getTrades() {
		ArrayList<trade> output = new ArrayList<>();
		String apiResponseString = connection.getApiResponseGET("trades");

		if (jsonParser.isValidTradeJson(apiResponseString))
			output = jsonParser.convertApiStringToTradesArray(apiResponseString);
		return output;
	}

	public void cd() {
		String apiResponseString = connection.getCandleStickData("EUR_JPY", null, null, "BAM","D5");

		ArrayList<String> ask = jsonParser.extractAskStrings(apiResponseString);
		for (String s : ask) {
			System.out.println(s);
		}
		ArrayList<String> mid = jsonParser.extractMidStrings(apiResponseString);
		for (String s : mid) {
			System.out.println(s);
		}
		ArrayList<String> bid = jsonParser.extractBidStrings(apiResponseString);
		for (String s : bid) {
			System.out.println(s);
		}
		
		System.out.println(jsonParser.getVarFromJson(bid.get(0), "o"));
	}
	
	

}
