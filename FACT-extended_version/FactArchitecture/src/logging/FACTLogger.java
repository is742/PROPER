package logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FACTLogger {

	BufferedWriter fileWriter = null;
	boolean active = false;
	String logDir = "logs";
	String logStem = "logfile-";
	
	/* 
	 * this will look in the logDir and create a log file will the next possible 
	 * index. File names are built from the logStem and given an integer counter.
	 */
	public FACTLogger(){
	}
	

	public String newLog(){
		int counter = 1;
	    String fileName = logDir+"/"+logStem+counter+".txt";

		File file = new File(fileName);
		while(file.exists()){
		     counter++;
		     fileName = logDir+"/"+logStem+counter+".txt";
		     // reassign file this while will terminate when #.txt doesnt exist
		     file = new File(fileName);
		} // the while should end here
		// then we check again that #.txt doesnt exist and try to create it
		if(!file.exists()) {
			try {
				fileWriter = new BufferedWriter(new FileWriter(fileName, true));
				return fileName;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ""; // creation failed
	}
	
	public void close(){
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
 
	public void add(String s){
		// if file doesn't exists, then create it

		try {
			fileWriter.write(s+"\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
 
}
