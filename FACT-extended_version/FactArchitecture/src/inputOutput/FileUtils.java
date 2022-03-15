package inputOutput;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import logging.FACTLogger;


public class FileUtils {


	static public String executeWinBatch(String executionFileNameAndLocation, String cliParms, FACTLogger flog){
		String output = "";
		try {
			String line;
			
			int splitAt = executionFileNameAndLocation.lastIndexOf('\\');
			String path = executionFileNameAndLocation.substring(0, splitAt+1);
			String batfile = executionFileNameAndLocation.substring(splitAt+1);
			
			
			
			Process process = Runtime.getRuntime().exec(executionFileNameAndLocation+" "+cliParms,null,new File(path));
			BufferedReader bri = new BufferedReader
					(new InputStreamReader(process.getInputStream()));
			BufferedReader bre = new BufferedReader
					(new InputStreamReader(process.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				output+=line+"\n";
				flog.add(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
			flog.add(line);
			}
			bre.close();
			process.waitFor();
			flog.add("Done.");
			return output;
		}
		catch (Exception err) {
			err.printStackTrace();
		}

		return "";	

	}

	
	static public String execute(String executionFileNameAndLocation, String cliParms, FACTLogger flog){
		String output = "";
		try {
			String line;
			Process process = Runtime.getRuntime().exec(executionFileNameAndLocation+" "+cliParms);
			BufferedReader bri = new BufferedReader
					(new InputStreamReader(process.getInputStream()));
			BufferedReader bre = new BufferedReader
					(new InputStreamReader(process.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				output+=line+"\n";
				flog.add(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
			flog.add(line);
			}
			bre.close();
			process.waitFor();
			flog.add("Done.");
			return output;
		}
		catch (Exception err) {
			err.printStackTrace();
		}

		return "";	

	}


	/**
	 * Reads and returns the contents of a text file as a String 
	 * 
	 * */


	//---------------------------------------------------------------------------------------------------------
	public static File writeTemporaryTextFile(String filename,String contents){
		BufferedWriter fileoutput = null;
		File tempFile = null;
		try {
			tempFile = File.createTempFile(filename,null);	
			fileoutput = new BufferedWriter(new FileWriter(tempFile));
			fileoutput.write(contents);

		} catch ( IOException e ) {
			e.printStackTrace();
		} finally {
			if (fileoutput != null )
				try {
					fileoutput.close();					
				} catch (IOException e) {				
					e.printStackTrace();
				}
		}
		return tempFile;
	}
	//---------------------------------------------------------------------------------------------------------
	public static String readTextFileContents(String fileNameAndPath)throws IOException{
		String fileTextContents="";
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(fileNameAndPath));
			while(in.hasNext()){
				fileTextContents+= in.nextLine()+"\n";
			}
		}
		finally{
			if(in != null){
				in.close();
			}
		}
		return fileTextContents;
	}

	//---------------------------------------------------------------------------------------------------------
	public static String readTextFileContents(File file) throws IOException {
		Scanner in = null;
		String fileTextContents="";
		try {
			in = new Scanner(file);
			while(in.hasNext()){
				fileTextContents+= in.nextLine()+"\n";
			}
		}
		finally{
			if(in != null){
				in.close();
			}
		}
		return fileTextContents;
	}
}
//--------------------------------------------------------------------------------------------------------------------------------------------