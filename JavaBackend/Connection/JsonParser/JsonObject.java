package JsonParser;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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
		
		StringWriter nameCache = new StringWriter();
		StringWriter varCache = new StringWriter();


		
		

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
						nameCache.append(cArray[i]);
					}
					break;

				case copyVar:
					if (cArray[i] == '[') {
						sqrBracketCounter++;
						varCache.append(cArray[i]);
					} else if (cArray[i] == '{') {
						braceCounter++;
						varCache.append(cArray[i]);
					} else if (cArray[i] == ']') {
						sqrBracketCounter--;
						varCache.append(cArray[i]);
					} else if (cArray[i] == '}') {
						braceCounter--;
						varCache.append(cArray[i]);
					} else if(cArray[i] == '"'  && sqrBracketCounter <= 0 && braceCounter <= 0){
						
					} else if(cArray[i] == ','  && sqrBracketCounter <= 0 && braceCounter <= 0){
						
						content.put(nameCache.toString(), varCache.toString());
						nameCache = new StringWriter();
						varCache = new StringWriter();
						state = CopyState.copyName;
						i++;
					} else {
						varCache.append(cArray[i]);
					}
					break;
					
				}

		}
		//System.out.println("nach schleife");
		String lastVar = varCache.toString();
		
		lastVar = lastVar.substring(0, lastVar.length() - 1);
		content.put(nameCache.toString(), lastVar);
		
		
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

		for (Map.Entry<String,String> e : content.entrySet()) {
			output += jv.toString() + "\n";
		}

		return output;
	}

}
