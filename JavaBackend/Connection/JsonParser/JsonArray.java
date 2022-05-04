package JsonParser;

import java.util.ArrayList;

public class JsonArray {
	
	ArrayList<String> content;
	
	public JsonArray() {
		content = new ArrayList<>();
	}
	
	public JsonArray(String JsonArray) {
		content = new ArrayList<>();
		char[] cArray = JsonArray.toCharArray();
		
		String cache = "";
		
		int sqrBracketCounter = 0;

		int braceCounter = 0;
		
		
		
		for(int i = 1;i < cArray.length-1; i++) {
			
			if(cArray[i] == ',' && sqrBracketCounter <= 0 && braceCounter <= 0) {
				content.add(cache);
				cache="";
			} else if (cArray[i] == '[') {
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
			} else {
				cache += cArray[i];
			}
			
		}
		content.add(cache);
		cache="";
	}
	
	public String get(int index) {
		return content.get(index);
	}
	
	public int length() {
		return content.size();
	}
	
	public String toString() {
		String output = "";

		for(String s: content) {
			output+=s+'\n';
		}
		
		
		return output;
	}
	

}
