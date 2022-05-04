package JsonParser;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonObject {

	ArrayList<JsonVar> contents;;

	public JsonObject() {
		contents = new ArrayList<>();
	}

	 public JsonObject(String Json) {
		contents = new ArrayList<>();
		//System.out.println("vor umwandlung zu CharArray");
		char[] cArray = Json.toCharArray();
		//System.out.println("nach umwandlung zu CharArray");
		CopyState state = CopyState.copyName;

		JsonVar var = new JsonVar();

		String cache = "";

		int sqrBracketCounter = 0;

		int braceCounter = 0;
		
		//System.out.println("vor schleife");
		for (int i = 2; i < cArray.length - 1; i++) {

				switch (state) {

				case copyName:
					if (cArray[i] == '"') {
						var.addName(cache);
						cache = "";
						state = CopyState.copyVar;
						i++;
					} else {
						cache += cArray[i];
					}
					break;

				case copyVar:
					if (cArray[i] == '[') {
						sqrBracketCounter++;
						cache += '[';
					} else if (cArray[i] == '{') {
						braceCounter++;
						cache += '{';
					} else if (cArray[i] == ']') {
						sqrBracketCounter--;
						cache += ']';
					} else if (cArray[i] == '}') {
						braceCounter--;
						cache += '}';
					} else if(cArray[i] == '"'  && sqrBracketCounter <= 0 && braceCounter <= 0){
						
					}else if(cArray[i] == ','  && sqrBracketCounter <= 0 && braceCounter <= 0){
						var.addContent(cache);
						contents.add(var);
						var = new JsonVar();
						cache = "";
						state = CopyState.copyName;
						i++;
					}else {
						cache += cArray[i];
					}
					break;
				default:
					break;
				}

		}
		//System.out.println("nach schleife");
		cache = cache.substring(0, cache.length() - 1);
		var.addContent(cache);
		contents.add(var);
		var = new JsonVar();
		cache = "";
		
		//System.out.println(contents.size());

	}
	
	public JsonObject getObject(String input) {
		int index = searchIndexOf(input);
		if(index < 0) return new JsonObject();
		return new JsonObject(contents.get(index).content);
	}

	public JsonArray getArray(String input) {
		int index = searchIndexOf(input);
		if(index < 0) return new JsonArray();
		return new JsonArray(contents.get(index).content);
	}
	
	public JsonVar getVar(String input) {
		int index = searchIndexOf(input);
		if(index < 0) return new JsonVar();
		return contents.get(index);
	}
	
	public String getValue(String input) {
		return getVar(input).content;
	}

	public int searchIndexOf(String input) {
		int index = 0;
		Iterator<JsonVar> iterator = contents.iterator();

		while (iterator.hasNext()) {
			JsonVar jv = iterator.next();
			if (jv.name.equals(input))
				return index;
			index++;
		}

		return -1;
	}

	public String toString() {
		String output = "";

		for (JsonVar jv : contents) {
			output += jv.toString() + "\n";
		}

		return output;
	}

}
