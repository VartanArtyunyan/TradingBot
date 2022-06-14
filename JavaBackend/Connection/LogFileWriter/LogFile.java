package LogFileWriter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class LogFile {
	
	HashMap<Integer, LogLine> lines;
	
	
	public LogFile(String path) {
		
		lines = new HashMap<>();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
			
			String input = br.readLine();
			String[] items;
			
			while(input != null) {
				LogLine line = new LogLine();
				items = input.split(";");
				line.id = Integer.parseInt(items[0]);
				line.content = "";
				for(int i = 1; i < items.length-1; i++) {
					
					line.content += items[i] + ";";
						
				}
				
			   if(items[items.length-1].equals("Noch Nicht Verkauft")) line.sellingPriceIsMissing = true;
			   else {line.sellingPriceIsMissing = false;
			   		line.sellingPrice = items[items.length-1];
			   }
			   lines.put(line.id, line);
			   br.readLine();
			}
			
			
			
		} catch (FileNotFoundException e) {
			lines = new HashMap<>();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Entry<Integer, LogLine> ll : lines.entrySet()) {
			System.out.println(ll.getValue());
		}
		
	}
	
	public LogFile() {
		lines = new HashMap<>();
	}
	
	
	public ArrayList<Integer> getMissingIDs(){
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		for(Entry<Integer, LogLine> s : lines.entrySet()) {
			if(s.getValue().sellingPriceIsMissing) output.add(s.getValue().id);
		}
		
		return output;
		
	}

	public void addLine(LogLine input) {
		
	}

	
	
	
	private class LogLine{
		
		boolean sellingPriceIsMissing;
		
		int id;
		String content;
		String sellingPrice;
		
		public String toString()	{
			String output = "";
			
			output+=id + ";" + content;
			if(!sellingPriceIsMissing) output += sellingPrice + ";";
			output += "\n";
			
			return output;
		}
		
		
		
	}
}
