package JsonParser;

public class JsonBuilder {
	
	String json;
	int writingObject;
	int writingArray;
	
	public JsonBuilder() {
		json = "{";
		this.writingObject = 0;
		this.writingArray = 0;
	}
	
	public void addString(String name, String string) {
		json += "\"" + name + "\":\"" + string +"\",";
	}
	
	public void addObject(String name, JsonObject jsonObject) {
		json += "\"" + name + "\":\"" + jsonObject.toString() +"\",";
	}
	
	public void addArray(String name, JsonArray jsonArray) {
		
	}
	
	public void openObject(String name) {
		json += "\"" + name + "\":{";
		writingObject++;
				
	}
	
	
	public void closeObject(String name) {
		json += "},";
		writingObject--;
	}
	
	
	public void openArray(String name) {
		json+= "\"" + name + "\":[";
		writingArray++;
	}
	
	
	public void closeArray(String name) {
		json += "],";
		writingArray--;
}
	public String build() {
		if(writingObject == 0 && writingArray == 0) {
			return json;
		}
		else  return null;
			
	}
}