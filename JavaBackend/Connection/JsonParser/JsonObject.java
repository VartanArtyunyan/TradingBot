package JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class JsonObject {

	HashMap<String,String> content;

	public JsonObject() {
		content = new HashMap<>();
	}

	 public JsonObject(String Json) {
		
		content = new HashMap<>();
		//System.out.println("vor umwandlung zu CharArray");
		final char[] cArray = Json.toCharArray();
		//System.out.println("nach umwandlung zu CharArray");
		CopyState state = CopyState.copyName;


		String nameCache = "";
		String varCache = "";
		

		int sqrBracketCounter = 0;

		int braceCounter = 0;
	
		
		//System.out.println("vor schleife");
		for (int i = 2; i < cArray.length - 1; i++) {

				switch (state) {

				case copyName:
					if (cArray[i] == '"') {
						state = CopyState.copyVar;
						i++;
					} else {
						nameCache += cArray[i];
					}
					break;

				case copyVar:
					if (cArray[i] == '[') {
						sqrBracketCounter++;
						varCache += cArray[i];
					} else if (cArray[i] == '{') {
						braceCounter++;
						varCache += cArray[i];
					} else if (cArray[i] == ']') {
						sqrBracketCounter--;
						varCache += cArray[i];
					} else if (cArray[i] == '}') {
						braceCounter--;
						varCache += cArray[i];
					} else if(cArray[i] == '"'  && sqrBracketCounter <= 0 && braceCounter <= 0){
						
					} else if(cArray[i] == ','  && sqrBracketCounter <= 0 && braceCounter <= 0){
						
						content.put(nameCache, varCache);
						nameCache = "";
						varCache = "";
						state = CopyState.copyName;
						i++;
					} else {
						varCache += cArray[i];
					}
					break;
					
				}

		}
		//System.out.println("nach schleife");
		
		varCache = varCache.substring(0, varCache.length() - 1);
		content.put(nameCache, varCache);
		
		
		//System.out.println(contents.size());

	}
	
	public JsonObject getObject(String input) {
		return new JsonObject(content.get(input));
	}

	public JsonArray getArray(String input) {
		return new JsonArray(content.get(input));
	}
	
	
	public String getValue(String input) {
		return content.get(input);
	}



	public String toString() {
		String output = "";

	//	for (String s1; String s2 : content.entrySet()) {
	//		output += jv.toString() + "\n";
	//	}

		return output;
	}

}
