package API;


import java.util.ArrayList;

import JsonParser.JsonParser;
import de.fhws.Softwareprojekt.JsonCandlesCandle;
import de.fhws.Softwareprojekt.JsonCandlesMid;
import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import positionen.trade;

public class ApiConnection {

	JsonParser jsonParser = new JsonParser();
	Connection connection = new Connection();
	
	public ApiConnection(Connection c) {
		connection = c;
	}

	public ArrayList<trade> getTrades() {
		ArrayList<trade> output = new ArrayList<>();
		String apiResponseString = connection.getApiResponseGET("trades");

		if (jsonParser.isValidTradeJson(apiResponseString)) 
			output = jsonParser.convertApiStringToTradesArray(apiResponseString);
		return output;
	}
	
	public JsonCandlesRoot getJsonCandlesRoot(int count, String instrument, String from, String to, String price, String granularity) {
		String apiResponseString = connection.getCandleStickData(count, instrument, from, to, price ,granularity);
		
		return jsonParser.convertAPiStringToCandlesRootModel(apiResponseString);
	}
	
	public JsonInstrumentsRoot getJsonInstrumentsRoot(){
		String apiResponseString = connection.getInstruments();
		
		
		return jsonParser.convertAPiStringToInstrumentsRootModel(apiResponseString);
	}

	public void cd() {
		String apiResponseString = connection.getCandleStickData(-1,"EUR_JPY", null, null, "M","D5");
		
		//String[] sArray = apiResponseString.split(",");
		//System.out.println(sArray.length + " test");
		//for(int i = 0; i < sArray.length; i++) {
		//System.out.println(sArray[i]);
		//}
		//System.out.println("testP2");
		////for (String s : mid) {
			//System.out.println(s);
		//}
		
		
		
		JsonCandlesRoot jcr = jsonParser.convertAPiStringToCandlesRootModel(apiResponseString);
		
		System.out.println(jcr.instrument);
		System.out.println(jcr.granularity);
		for(JsonCandlesCandle jcc : jcr.candles) {
			System.out.println(jcc.complete);
			System.out.println(jcc.mid.o);
		}
		
		
		//System.out.println(jsonParser.getVarFromJson(mid.get(0), "o"));
	}
	
	public double getKurs(String instrument) {
        return 1.09;
    }
	
	public void placeOrder(String instrument, double units, double takeProfit, double stopLoss) {
		
    }
	
	public double getBalance() {
		
		return 0;
		
	}
	
	public void closePosition(String instrument, int anzahl) {

    }
	
	public void closeWholePosition(String instrument) {

    }

}
