package JsonParser;

public class JsonVar {
	
	String name;
	String content;

	
	public void addName(String name) {
		if(this.name==null) {
			this.name = name;
		}
	}
	
	public void addContent(String content) {
		if(this.content==null) {
			this.content = content;
		}
	}
	
	public String toString() {
		return name + " : " + content;
	}
}
