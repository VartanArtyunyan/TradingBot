package JsonParser;

import java.util.ArrayList;

import positionen.Instrumente;
import positionen.trade;

public class JsonParser {

	public String getVarFromJson(String json, String string) {
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

		for (int i = 0; i < TradesStringArray.length; i++) {
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
			int i = s.indexOf("bid");
			output.add(s.substring(i >= 0 ? i : 2));
		}
		return output;
	}

}
