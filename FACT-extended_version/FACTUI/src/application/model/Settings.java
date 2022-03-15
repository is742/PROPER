package application.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import logging.FACTLogger;

public class Settings {

     String fileName = "./Configuration.txt";
	 public HashMap<String, String> collection;
	 
	 public Settings(){
		 collection = new HashMap<String, String>();
	 }
	 
	 public void add(String k, String v){
		 if (collection.get(k) != null){
			 collection.replace(k,v);
		 }
		 else
			 collection.put(k,v);
	 }
	 
	 public String get(String k){
		 return collection.get(k);
	 }
	 
	 public void write(){
		BufferedWriter fileWriter = null;
		// this should be set through something more easily managed.
		File file = new File(fileName);  
		
		try {
			// false means overwrite, don't append
			fileWriter = new BufferedWriter(new FileWriter(fileName, false));
			 Iterator<String> keySetIterator = collection.keySet().iterator(); 
			 while(keySetIterator.hasNext()){ 
				 String key = keySetIterator.next(); 
				 fileWriter.write(key + "," + collection.get(key)+"\n"); 
			} 
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 public void read(){
		BufferedReader fileReader = null;
		try {
			
			fileReader = new BufferedReader(new FileReader(fileName));
			String line = fileReader.readLine();
			
			while (line != null){
		
				line = line.trim();
				if (line.length() > 0){
					String[] v = line.split(",");
					if (v.length == 2) // i.e. key value pair
					{
						this.add(v[0].trim(), v[1].trim());
					}
				}
				line = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		 
	 }
	 
	 
	 public void display(FACTLogger flog){
		 flog.add("-------------------------------------------");
		 flog.add("Settings");
		 flog.add("-------------------------------------------");
		 Iterator<String> keySetIterator = collection.keySet().iterator(); 
		 while(keySetIterator.hasNext()){ 
			 String key = keySetIterator.next(); 
			 flog.add(key + "," + collection.get(key)); 
 		 } 
		 flog.add("-------------------------------------------");
	 }
}
