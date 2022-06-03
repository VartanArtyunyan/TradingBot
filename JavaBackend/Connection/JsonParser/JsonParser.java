package JsonParser;

import java.util.ArrayList;

import de.fhws.Softwareprojekt.JsonCandlesCandle;
import de.fhws.Softwareprojekt.JsonCandlesMid;
import de.fhws.Softwareprojekt.JsonCandlesRoot;
import de.fhws.Softwareprojekt.JsonInstrumentsInstrument;
import de.fhws.Softwareprojekt.JsonInstrumentsRoot;
import positionen.Instrumente;
import positionen.trade;

public class JsonParser {

	public ArrayList<trade> convertApiStringToTradesArray(String json){
		

        JsonArray input = new JsonObject(json).getArray("trades");
        ArrayList<trade> output = new ArrayList<>();
        
        if(input.get(0) == null || input.get(0).length() == 0) return new ArrayList<trade>();

        for(int i = 0; i < input.length(); i++) {
        	

            JsonObject jo = new JsonObject(input.get(i));

            int id = Integer.parseInt(jo.getValue("id"));
            String instrument = jo.getValue("instrument");
            double price = Double.parseDouble(jo.getValue("price"));
            String openTime = jo.getValue("openTime");
            int initialUnits = Integer.parseInt(jo.getValue("initialUnits"));
            String initialMarginRequired = jo.getValue("initialMarginRequired");
            int currentunits = Integer.parseInt(jo.getValue("currentUnits"));
            String realizedPL = jo.getValue("realizedPL");
            String unrealizedPL = jo.getValue("unrealizedPL");
            String marginUsed = jo.getValue("marginUsed");

            output.add(new trade(id,instrument, price, openTime, initialUnits, initialMarginRequired, currentunits, realizedPL, unrealizedPL, marginUsed));

        }


        return output;
    }
	
	public String makeOrederRequestJson(String instrument, double units, double takeProfit, double stopLoss) {
		JsonBuilder output = new JsonBuilder();
		
		output.openObject("order");
		output.addString("type", "MARKET");
		output.addString("instrument", instrument);
		
		
		
		
		output.addString("units", Double.toString(round( units,0)));
		//output.addString("price", Double.toString(price));
		output.openObject("takeProfitOnFill");
		output.addString("price", Double.toString(takeProfit));
		output.closeObject();
		output.openObject("stopLossOnFill");
		output.addString("price", Double.toString(stopLoss));
		output.closeObject();
		output.closeObject();
		
		
		return output.build();
	}
	
	public double round(double input, double places) {
		
		double multiplicant = Math.pow(10, places);
		
		int i = (int) (input * multiplicant);
		return ((double)i)/multiplicant;
	}
	
	public double getBalanceFromAccountJson(String json) {
		JsonObject input = new JsonObject(json);
		JsonObject account = input.getObject("account");
		
		return Double.parseDouble(account.getValue("balance"));
	}
	
	//Methoden für die Signale
	
	public JsonCandlesRoot convertAPiStringToCandlesRootModel(String json) {
	//	System.out.println("converAPIStringTOCandlesRootModel aufgerufen");
	
		
		
		
		JsonObject input = new JsonObject(json);
		//System.out.println("JsonObject erstellt");
		//System.out.println(input);
		JsonArray jsonCandlesArray = input.getArray("candles");
	//	System.out.println("JsonArrayObjektn erstellt");
		
		JsonCandlesRoot output = new JsonCandlesRoot();
	//	System.out.println("JsonCandlesRootObject erstellt");
		
		output.instrument = input.getValue("instrument");
	//	System.out.println("instrument in JsonCandlesRootObject eingetragen");
		output.granularity = input.getValue("granularity");
	//	System.out.println("granularität in JsonCandlesRootObject eingetragen");
		output.candles = new ArrayList<JsonCandlesCandle>();
		
	//	System.out.println(jsonCandlesArray.length() + " Candles übergeben");
		
		for(int i = 0; i < jsonCandlesArray.length(); i++) {
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
	
	public void parseLastCandleFromAPIString(String json) {
		int length = json.length();
		
		int counter = - 1;
		
		
		
		
		int end = json.length();
		int start = end - 136;
		String s = json.substring(start,end);
		
		System.out.println(s);
	}
	
	public JsonInstrumentsRoot convertAPiStringToInstrumentsRootModel(String json){
		JsonInstrumentsRoot output = new JsonInstrumentsRoot();
		JsonObject input = new JsonObject(json);
		//System.out.println(input);
		JsonArray jsonInstrumentsArray = input.getArray("instruments");
		
		output.lastTransactionID = input.getValue("lastTransactionID");
		output.instruments = new ArrayList<>();
		
		for(int i = 0; i < jsonInstrumentsArray.length(); i++ ) {
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
	
	

}
