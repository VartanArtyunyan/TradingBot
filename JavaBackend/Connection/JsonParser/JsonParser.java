package JsonParser;

import java.util.ArrayList;

import de.fhws.Softwareprojekt.JsonCandlesCandle;
import de.fhws.Softwareprojekt.JsonCandlesMid;
import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.JsonInstrumentsInstrument;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import positionen.Instrumente;
import positionen.OrderResponse;
import positionen.trade;

public class JsonParser {

	public ArrayList<trade> convertApiStringToTradesArray(String json) {

		JsonArray input = new JsonObject(json).getArray("trades");
		ArrayList<trade> output = new ArrayList<>();

		if (input.get(0) == null || input.get(0).length() == 0)
			return new ArrayList<trade>();

		for (int i = 0; i < input.length(); i++) {

			output.add(convertAPIStringToTrade(input.get(i)));

		}

		return output;
	}

	public String makeOrederRequestJson(String instrument, double units, double takeProfit, double stopLoss) {
		JsonBuilder output = new JsonBuilder();

		output.openObject("order");
		output.addValue("type", "MARKET");
		output.addValue("instrument", instrument);

		output.addValue("units", Double.toString(round(units, 0)));
		// output.addString("price", Double.toString(price));
		output.openObject("takeProfitOnFill");
		output.addValue("price", Double.toString(round(takeProfit,3)));
		output.closeObject();
		output.openObject("stopLossOnFill");
		output.addValue("price", Double.toString(round(stopLoss,3)));
		output.closeObject();
		output.closeObject();

		return output.build();
	}

	public String makeOrederRequestJson(String instrument, double units) {
		JsonBuilder output = new JsonBuilder();

		output.openObject("order");
		output.addValue("type", "MARKET");
		output.addValue("instrument", instrument);
		output.addValue("units", Double.toString(round(units, 0)));
		// output.addString("price", Double.toString(price));

		output.closeObject();

		return output.build();
	}

	public double round(double input, double places) {

		double multiplicant = Math.pow(10, places);

		int i = (int) (input * multiplicant);
		return (double) (i / multiplicant);
	}

	public double getBalanceFromAccountJson(String json) {
		JsonObject input = new JsonObject(json);
		JsonObject account = input.getObject("account");

		return Double.parseDouble(account.getValue("balance"));
	}

	// Methoden für die Signale

	public JsonCandlesRoot convertAPiStringToCandlesRootModel(String json) {
		// System.out.println("converAPIStringTOCandlesRootModel aufgerufen");

		JsonObject input = new JsonObject(json);
		// System.out.println("JsonObject erstellt");
		// System.out.println(input);
		JsonArray jsonCandlesArray = input.getArray("candles");
		// System.out.println("JsonArrayObjektn erstellt");

		JsonCandlesRoot output = new JsonCandlesRoot();
		// System.out.println("JsonCandlesRootObject erstellt");

		output.instrument = input.getValue("instrument");
		// System.out.println("instrument in JsonCandlesRootObject eingetragen");
		output.granularity = input.getValue("granularity");
		// System.out.println("granularität in JsonCandlesRootObject eingetragen");
		output.candles = new ArrayList<JsonCandlesCandle>();

		// System.out.println(jsonCandlesArray.length() + " Candles übergeben");

		for (int i = 0; i < jsonCandlesArray.length(); i++) {
			JsonObject candle = new JsonObject(jsonCandlesArray.get(i));
			JsonObject mid = candle.getObject("mid");

			JsonCandlesCandle jcc = new JsonCandlesCandle();
			JsonCandlesMid jcm = new JsonCandlesMid();

			jcc.complete = Boolean.parseBoolean(candle.getValue("complete"));
			jcc.time = candle.getValue("time");
			jcc.volume = Integer.parseInt(candle.getValue("volume"));

			jcm.c = Double.parseDouble(mid.getValue("c"));
			jcm.h = Double.parseDouble(mid.getValue("h"));
			jcm.l = Double.parseDouble(mid.getValue("l"));
			jcm.o = Double.parseDouble(mid.getValue("o"));

			jcc.mid = jcm;

			output.candles.add(jcc);

		}

		return output;
	}

	public JsonCandlesCandle parseLastCandleFromAPIString(String json) {
		int length = json.length();

		int counter = 2;
		int index = length;

		while (counter > 0) {
			index--;
			if (json.charAt(index) == '{') {
				counter--;
			}

		}
		int end = json.length() - 2;

		String candleJson = json.substring(index, end);
		JsonObject candle = new JsonObject(candleJson);
		JsonObject mid = candle.getObject("mid");

		JsonCandlesCandle lastCandle = new JsonCandlesCandle();
		JsonCandlesMid jcm = new JsonCandlesMid();

		jcm.o = Double.parseDouble(mid.getValue("o"));
		jcm.h = Double.parseDouble(mid.getValue("h"));
		jcm.l = Double.parseDouble(mid.getValue("l"));
		jcm.c = Double.parseDouble(mid.getValue("c"));

		lastCandle.complete = Boolean.parseBoolean(candle.getValue("complete"));
		lastCandle.volume = Integer.parseInt(candle.getValue("volume"));
		lastCandle.time = candle.getValue("time");
		lastCandle.mid = jcm;

		return lastCandle;
	}

	public JsonInstrumentsRoot convertAPiStringToInstrumentsRootModel(String json) {
		JsonInstrumentsRoot output = new JsonInstrumentsRoot();
		JsonObject input = new JsonObject(json);
		// System.out.println(input);
		JsonArray jsonInstrumentsArray = input.getArray("instruments");

		output.lastTransactionID = input.getValue("lastTransactionID");
		output.instruments = new ArrayList<>();

		for (int i = 0; i < jsonInstrumentsArray.length(); i++) {
			JsonObject instrument = new JsonObject(jsonInstrumentsArray.get(i));

			JsonInstrumentsInstrument jii = new JsonInstrumentsInstrument();

			jii.displayName = instrument.getValue("displayName");
			jii.displayPrecision = Integer.parseInt(instrument.getValue("displayPrecision"));
			jii.marginRate = instrument.getValue("marginRate");
			jii.maximumOrderUnits = instrument.getValue("maximumOrderUnits");
			jii.maximumPositionSize = instrument.getValue("maximumPositionSize");
			jii.maximumTrailingStopDistance = instrument.getValue("maximumTrailingStopDistance");
			jii.minimumTradeSize = instrument.getValue("minimumTradeSize");
			jii.minimumTrailingStopDistance = instrument.getValue("minimumTrailingStopDistance");
			jii.name = instrument.getValue("name");
			jii.pipLocation = Integer.parseInt(instrument.getValue("pipLocation"));
			jii.tradeUnitsPrecision = Integer.parseInt(instrument.getValue("tradeUnitsPrecision"));
			jii.type = instrument.getValue("type");

			output.instruments.add(jii);

		}

		return output;
	}
	
	public trade convertAPIStringToTrade(String json) {
		
		JsonObject jo = new JsonObject(json);
		
		if(jo.contains("trade")) jo = jo.getObject("trade");
		
		int id = Integer.parseInt(jo.getValue("id"));
		String instrument = jo.getValue("instrument");
		double price = Double.parseDouble(jo.getValue("price"));
		String openTime = jo.getValue("openTime");
		int initialUnits = 0;//Integer.parseInt(jo.getValue("initialUnits"));
		String initialMarginRequired = jo.getValue("initialMarginRequired");
		int currentunits = 0;//Integer.parseInt(jo.getValue("currentUnits"));
		String realizedPL = jo.getValue("realizedPL");
		String unrealizedPL = jo.getValue("unrealizedPL");
		String marginUsed = jo.getValue("marginUsed");
					
		return new trade(id, instrument, price, openTime, initialUnits, initialMarginRequired, currentunits,
				realizedPL, unrealizedPL, marginUsed);
	}
	
	

	public OrderResponse makeOrderResponseFromJson(String input) {
		JsonObject responseObject = new JsonObject(input);
		boolean wasSuccessfull = !responseObject.contains("orderCancelTransaction");
		JsonObject orderFillTransaction = responseObject.getObject("orderFillTransaction");
		JsonObject tradeOpened = orderFillTransaction.getObject("tradeOpened");
		String id = tradeOpened.getValue("tradeID");

		return new OrderResponse(wasSuccessfull, id);
	}



}
