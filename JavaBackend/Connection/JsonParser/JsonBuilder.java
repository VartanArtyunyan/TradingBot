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

	public void addString(String name, String string) {
		json += "\"" + name + "\":\"" + string + "\",";
	}

	public void addObject(String name, JsonObject jsonObject) {
		json += "\"" + name + "\":\"" + jsonObject.toString() + "\",";
	}

	public void addArray(String name, JsonArray jsonArray) {

	}

	public void pushObject() {
		stack.push(false);
	}

	public void pushArray() {
		stack.push(true);
	}

	public boolean popObject() {
		boolean output = !stack.peek();

		if (output) {
			stack.pop();
		}

		return output;
	}

	public boolean popArray() {
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

	public void removeLastChar() {
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
