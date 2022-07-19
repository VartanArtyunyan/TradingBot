package ConfigFileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import Exeptions.noSuchValue;

public class ConfigFileReader {

	String path;

	boolean isValid;

	static String[][] standartConfig = new String[][] { { "signal1", "true" }, 
														{ "signal2", "true" },
														{ "signal3", "true" }, 
														{ "eventBased", "true" }, 
														{ "random", "false" }, 
														{ "randomAccountToken", "" } };

	HashMap<String, String> config;

	public ConfigFileReader(String path) {
		this.path = path;
		readFile();
		isValid = checkLogFile();
	
	}

	public void readFile() {

		HashMap<String, String> output = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
			String input = br.readLine();

			while (input != null) {
				if (input.contains("=")) {
					String[] sArray = input.split("=");
					output.put(removeCharFromString(sArray[0], ' '), removeCharFromString(sArray[1], ' '));
				}
				input = br.readLine();
			}
			config = output;
		} catch (FileNotFoundException e) {
			createFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createFile() {

		HashMap<String, String> output = new HashMap<>();

		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)))) {
			for (int i = 0; i < standartConfig.length; i++) {
				output.put(standartConfig[i][0], standartConfig[i][1]);
			}

			for (Entry<String, String> s : output.entrySet()) {
				bw.write(s.getKey() + "=" + s.getValue() + "\n");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean checkLogFile() {

		for (int i = 0; i < standartConfig.length; i++) {
			if (!config.containsKey(standartConfig[i][0]))
				return false;
		}

		for (int i = 0; i <= 5; i++) {
			if (!config.get(standartConfig[i][0]).equals("true") && !config.get(standartConfig[i][0]).equals("false"))
				return false;
		}
		return true;
	}

	public String removeCharFromString(String input, char remove) {
		StringWriter output = new StringWriter();

		char[] cArray = input.toCharArray();

		for (int i = 0; i < cArray.length; i++) {
			if (cArray[i] != remove)
				output.append(cArray[i]);
		}

		return output.toString();
	}
	
	public String get(String input) {
		if(!config.containsKey(input)) throw new noSuchValue();
		return config.get(input);
	}
	
	public boolean isValid() {
		return true;
	}

}
