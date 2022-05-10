package JsonParser;

public class JsonVar {
	
	String name;
	String content;
	
	public String getValue() {
		return content;
	}

	
	public void addName(String name) {
		
			this.name = name;
		
	}
	
	public void addContent(String content) {
	
			this.content = content;
		
	}
	
	public boolean equals(String input) {
		return input == name;
	}
	
	public String toString() {
		return name + " : " + content;
	}
}
