package JsonParser;

import java.util.Stack;

import Exeptions.invalidJsonOperation;

public class JsonBuilder {
	String json;

	Stack<Boolean> stack; // false = Object, true = Array;

	public JsonBuilder() {
		json = "{";
		stack = new Stack<>();
		
	}

	public void addValue(String name, String value) {
		if (inArray()) json += "\"" + value + "\",";
		else json += "\"" + name + "\":\"" + value + "\",";
	}
	
	public void addValue(String name, double value) {
		if (inArray()) json +=  value + ",";
		else json += "\"" + name + "\":" + value + ",";
	}
	
	public void addValue(String name, int value) {
		if (inArray()) json +=  value + ",";
		else json += "\"" + name + "\":" + value + ",";
	}
	
	public void addValue(String name, boolean value) {
		if (inArray()) json +=  value + ",";
		else json += "\"" + name + "\":" + value + ",";
	}
	
	private void pushObject() {
		stack.push(false);
	}
	
	private void pushArray() {
		stack.push(true);
	}
	
	private boolean popObject() {
		boolean output = !stack.peek();

		if (output) {
			stack.pop();
		}

		return output;
	}

	private boolean popArray() {
		boolean output = stack.peek();

		if (output) {
			stack.pop();
		}

		return output;
	}

	public void openObject(String name) {
		json += "\"" + name + "\":{";

		pushObject();
	}

	public void closeObject() {
		if (popObject()) {
			removeLastChar();
			json += "},";
		} else
			throw new invalidJsonOperation("can not close Object");
	}

	public void openArray(String name) {
		json += "\"" + name + "\":[";

		pushArray();
	}

	public void closeArray() {

		if (popArray()) {
			removeLastChar();
			json += "],";

		}

		else
			throw new invalidJsonOperation("can not close Array");
	}

	public boolean inArray() {
		return stack.peek();
	}

	private void removeLastChar() {
		json = json.substring(0, json.length() - 1);
	}

	public String build() {
		if (stack.size() == 0) {
			removeLastChar();
			return json + '}';
		} else
			throw new invalidJsonOperation("can not build Json, check if all Objects and Arrays have been closed");

	}
}
