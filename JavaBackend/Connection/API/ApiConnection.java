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
	JsonInstrumentsRoot availableInstruemts;
	CandleCache candleCache;

	public ApiConnection(Connection c) {
		jsonParser = new JsonParser();
		connection = c;
		candleCache = new CandleCache();

	}

	public ArrayList<trade> getTrades() {
		ArrayList<trade> output = new ArrayList<>();
		String apiResponseString = connection.getTrades();

		// System.out.println(apiResponseString);

		output = jsonParser.convertApiStringToTradesArray(apiResponseString);

		return output;
	}

	public JsonCandlesRoot getJsonCandlesRoot(int count, String instrument, String from, String to, String price,
			String granularity) {

		String apiResponseString = connection.getCandleStickData(1, instrument, null, null, price, granularity);

		JsonCandlesCandle lastCandle = jsonParser.parseLastCandleFromAPIString(apiResponseString);

		if (candleCache.needsUpdate(instrument, lastCandle)) {
			apiResponseString = connection.getCandleStickData(count, instrument, from, to, price, granularity);
			candleCache.update(jsonParser.convertAPiStringToCandlesRootModel(apiResponseString));
		}
			

		return candleCache.get(instrument, lastCandle);
		// return jsonParser.convertAPiStringToCandlesRootModel(apiResponseString);
	}

	public JsonInstrumentsRoot getJsonInstrumentsRoot() {
		if (availableInstruemts == null) {
			String apiResponseString = connection.getInstruments();
			
			availableInstruemts = jsonParser.convertAPiStringToInstrumentsRootModel(apiResponseString);
		}
		return availableInstruemts;
	}

	public double getKurs(String instrument) {
		String apiResponseString = connection.getCandleStickData(1, instrument, null, null, "M", "S5");

		JsonCandlesCandle lastCandle = jsonParser.parseLastCandleFromAPIString(apiResponseString);

		return lastCandle.mid.c;
	}

	public void placeOrder(String instrument, double units, double takeProfit, double stopLoss) {
		if(units == 0) return;

		String orderJson = jsonParser.makeOrederRequestJson(instrument, units, takeProfit, stopLoss); //

		connection.placeLimitOrder(orderJson);
	}

	public void placeOrder(String instrument, double units) {

		String orderJson = jsonParser.makeOrederRequestJson(instrument, units); //

		connection.placeLimitOrder(orderJson);
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
