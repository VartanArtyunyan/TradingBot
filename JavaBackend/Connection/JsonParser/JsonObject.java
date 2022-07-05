package JsonParser;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonObject {
	
	HashMap<String,String> content;

	public JsonObject() {
		content = new HashMap<>();
	}

	public void addVar(String name, String inhalt) {
		content.put(name, inhalt);
	}
	
	
	
	
	
	
	 public JsonObject(String Json) {
		
		content = new HashMap<>();
		
		if(Json == null) return;
		//System.out.println("vor umwandlung zu CharArray");
		final char[] cArray = Json.toCharArray();
		//System.out.println("nach umwandlung zu CharArray");
		CopyState state = CopyState.copyName;
		
		StringWriter nameCache = new StringWriter();
		StringWriter varCache = new StringWriter();


		
		

		int sqrBracketCounter = 0;

		int braceCounter = 0;
	
		
		//System.out.println("vor schleife");
		for (int i = 2; i < cArray.length; i++) {

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
		
		String lastVar = varCache.toString();
		//System.out.println(lastVar);
		int cutoff = lastVar.length() -1;
		if(cutoff < 0) cutoff = 0;
		lastVar = lastVar.substring(0, cutoff);
		content.put(nameCache.toString(), lastVar);
		
		
		
		//System.out.println(contents.size());

	}
	 
	public boolean contains(String input) {
		return content.containsKey(input);
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
		
		StringWriter cache = new StringWriter();
		cache.append("{");
		for (Map.Entry<String,String> e : content.entrySet()) {
			cache.append('"');
			cache.append(e.getKey());
			cache.append("\":\"");
			cache.append(e.getValue());
			cache.append("\",");
			
		}
		cache.append("}");
		
		return cache.toString();
	}

}
