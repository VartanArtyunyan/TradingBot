package API;

import java.util.ArrayList;

import JsonParser.JsonParser;
import de.fhws.Softwareprojekt.JsonCandlesCandle;
import de.fhws.Softwareprojekt.JsonCandlesMid;
import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import positionen.trade;

public class ApiConnection {

	JsonParser jsonParser;
	Connection connection;
	CandleCache candleCache;

	public ApiConnection(Connection c) {
		jsonParser = new JsonParser();
		connection = c;
	}

	public ArrayList<trade> getTrades() {
		ArrayList<trade> output = new ArrayList<>();
		String apiResponseString = connection.getTrades();

		output = jsonParser.convertApiStringToTradesArray(apiResponseString);

		return output;
	}

	public JsonCandlesRoot getJsonCandlesRoot(int count, String instrument, String from, String to, String price,
			String granularity) {
		
		
		String apiResponseString = connection.getCandleStickData(count, instrument, from, to, price, granularity);
		
	//	jsonParser.parseLastCandleFromAPIString(apiResponseString);

		return jsonParser.convertAPiStringToCandlesRootModel(apiResponseString);
	}
	

	public JsonInstrumentsRoot getJsonInstrumentsRoot() {
		String apiResponseString = connection.getInstruments();

		return jsonParser.convertAPiStringToInstrumentsRootModel(apiResponseString);
	}

	public double getKurs(String instrument) {
		return 1.09;
	}

	public void placeLimitOrder(String instrument, double limitPrice, double units, double takeProfit, double stopLoss) { 

		String limitOrderJson = jsonParser.makeLimitOrederRequestJson(instrument, limitPrice, units, takeProfit,
				stopLoss); //

		connection.placeLimitOrder(limitOrderJson);
	}

	public double getBalance() {

		String accountJson = connection.getAccount();

		return jsonParser.getBalanceFromAccountJson(accountJson);

	}

	public void closePosition(String instrument, int anzahl) {

	}

	public void closeWholePosition(String instrument) {

	}

}
