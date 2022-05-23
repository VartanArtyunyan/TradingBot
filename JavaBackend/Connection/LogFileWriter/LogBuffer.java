package LogFileWriter;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LogBuffer implements Closeable{
	
	boolean open;
	String filePath;
	String bufferPath;
	ArrayList<String> buffer;
	BufferedWriter bw;
	
	public LogBuffer(String path, BufferedWriter bw) {
		this.open = true;
		
		this.filePath = path;
		this.bufferPath = "/buffer/"+ filePath + ".buffer";
		this.bw = bw;
		
		if (!Files.exists(Paths.get(bufferPath))) importBuffer(bufferPath);	
		else this.buffer = new ArrayList<>();
		
	}
	
	
	public void put(String input) {
		open = false;
		buffer.add(input);
	}
	
	public void flush() throws IOException {
		while(isClosed()) {
			bw.write(get());
			remove();
		}
		
	}
	
	public boolean isClosed() {
		return !open;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	private String get() {
		if(buffer.size()>0) return buffer.get(0);
		return null;
	}
	
	private void remove() {
		buffer.remove(0);
	}
	
	
	private void importBuffer(String path) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));){			
			buffer = (ArrayList<String>) ois.readObject();			
		} catch (Exception e) {
			this.buffer = new ArrayList<>();
		} 
	}
	
	public void close() throws IOException {
		try {
			if(isClosed()) flush();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				oos = new ObjectOutputStream(new FileOutputStream(bufferPath));
				oos.writeObject(buffer);	
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	
	}
	
	

}
