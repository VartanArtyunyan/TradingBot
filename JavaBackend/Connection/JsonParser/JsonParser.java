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

	public String getVarFromJson(String json, String string) {
		String[] sArray = json.split(",");
		for (int i = 0; i < sArray.length; i++) {
			if (sArray[i].contains(string)) {
				String[] sArray2 = sArray[i].split("\":\"");
				if(sArray2.length==1) sArray2 = sArray[i].split("\":");
				if (sArray2[1].substring(sArray2[1].length() - 1, sArray2[1].length()).equals("\""))
					return sArray2[1].substring(0, sArray2[1].length() - 1);
				else
					return sArray2[1];
			}
		}
		return null;
	}

	public boolean containsVar(String json, String var) {

		String[] sArray = json.split(",");
		for (int i = 0; i < sArray.length; i++) {
			if (sArray[i].contains(var)) {
				return true;

			}
		}
		return false;

	}

	public static String getArrayFromJson(String json) {
		int start = 0;
		int end = 0;
		String output = json;
		char[] charArray = json.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '[')
				start = i + 1;
			if (charArray[i] == ']')
				end = i - start;
		}

		output = output.substring(start);
		output = output.substring(0, end);
		return output;
	}

	public ArrayList<trade> convertApiStringToTradesArray(String json) {
		ArrayList<trade> output = new ArrayList<trade>();
		String TradesStringJSONArray = getArrayFromJson(json);

		String[] TradesStringArray = TradesStringJSONArray.split("}");

		for (int i = 1; i < TradesStringArray.length; i++) {
			output.add(convertApiStringToTradeModel(TradesStringArray[i]));
		}

		return output;
	}

	public trade convertApiStringToTradeModel(String json) {
		return new trade(Integer.parseInt(getVarFromJson(json, "id")),
				Instrumente.valueOf(getVarFromJson(json, "instrument")),
				Double.parseDouble(getVarFromJson(json, "price")), getVarFromJson(json, "openTime"),
				Integer.parseInt(getVarFromJson(json, "initialUnits")), getVarFromJson(json, "initialMarginRequired"),
				Integer.parseInt(getVarFromJson(json, "currentUnits")), getVarFromJson(json, "realizedPL"),
				getVarFromJson(json, "unrealizedPL"), getVarFromJson(json, "marginUsed"));
	}

	public boolean isValidTradeJson(String json) {
		return containsVar(json, "id") && containsVar(json, "instrument") && containsVar(json, "price")
				&& containsVar(json, "openTime") && containsVar(json, "initialUnits")
				&& containsVar(json, "initialMarginRequired") && containsVar(json, "currentUnits")
				&& containsVar(json, "realizedPL") && containsVar(json, "unrealizedPL")
				&& containsVar(json, "marginUsed");
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
	
	public JsonCandlesMid convertApiStringToCandlesMidModel(String json) {
		JsonCandlesMid output = new JsonCandlesMid();
		
		output.o = Double.parseDouble(getVarFromJson(json, "o"));
		output.h = Double.parseDouble(getVarFromJson(json, "h"));
		output.l = Double.parseDouble(getVarFromJson(json, "l"));
		output.c = Double.parseDouble(getVarFromJson(json, "c"));
		
		return output;
	}
	
	public JsonCandlesCandle convertApiStringToCandleModel(String json) {
		JsonCandlesCandle output = new JsonCandlesCandle();
		
		output.complete = Boolean.parseBoolean(getVarFromJson(json, "complete"));
		output.volume = Integer.parseInt(getVarFromJson(json, "volume"));
		output.time = getVarFromJson(json, "time");
		output.mid= convertApiStringToCandlesMidModel( extractMidStrings(json).get(0));
		
		return output;
	}

	public ArrayList<String> extractBidStrings(String json) {
		return extractCandelPriceStrings(json, "bid");
	}

	public ArrayList<String> extractMidStrings(String json) {
		return extractCandelPriceStrings(json, "mid");
	}

	public ArrayList<String> extractAskStrings(String json) {
		return extractCandelPriceStrings(json, "ask");
	}

	public ArrayList<String> extractCandelPriceStrings(String json, String price) {
		String[] sArray = json.split("}}");
		ArrayList<String> sAL = new ArrayList<>();

		for (int i = 0; i < sArray.length; i++) {
			for (int j = 0; j < sArray[i].split("}").length; j++) {
				if (sArray[i].split("}")[j].contains(price))
					sAL.add(sArray[i].split("}")[j]);
			}
		}

		ArrayList<String> output = new ArrayList<>();

		for (String s : sAL) {
			int i = s.indexOf(price);
			output.add(s.substring(i >= 0 ? i : 2));
		}
		return output;
	}

}
