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
		String apiResponseString = connection.getTrades();
		
		System.out.println(apiResponseString);

		
			output = jsonParser.convertApiStringToTradesArray(apiResponseString);
		
		for(trade t : output) {
			System.out.println(t.getInstrument()+ " <--");
		}
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

	
	
	public double getKurs(String instrument) {
        return 1.09;
    }
	
	public void placeOrder(String instrument, double units, double takeProfit, double stopLoss) {    //limitPreis ist der lastPreis (Limit Preis =lastPrice +0,0005) aus kpi
																									//
    }
	
	public double getBalance() {
		
		return 0;
		
	}
	
	public void closePosition(String instrument, int anzahl) {

    }
	
	public void closeWholePosition(String instrument) {

    }

}
