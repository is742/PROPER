package application.model;

import java.util.ArrayList;

public class Observation {
	
	public String name;
	public String type;
	private ArrayList<Double> instances = null;
	public String error = "";
	

	
	Observation(){
		instances = new ArrayList<Double>();
	}
	
	/*
	 * Create an observation object from an instantiating string of the form
	 * // param: x 3414 2 2547
	 * This means there are three parameters x1,x2,x3 and they have been observed 3414, 2 and 2547
	 * times each.
	 * 
	 * A return value of false indicates that the parsing failed
	 */
	public Boolean initialise(String sInit){
		sInit = sInit.trim();
			if (sInit.endsWith(";")){
				sInit = sInit.substring(0, sInit.length()-1);
			} else{ 
				error = "Param parse error: No ;";
				return false;
			}
			
			if (sInit.startsWith("param")){
				sInit = sInit.substring(5).trim();
				String[] spVal = sInit.split(" ");
				
					if (spVal.length < 4) {
						error = "Param parse error: format incorrect";
						return false;
					}
					this.name = spVal[1];
					this.type = spVal[0];
					for (int i=3; i<spVal.length; i++)
						this.add(Double.parseDouble(spVal[i].trim()));
			
				return true;
			}
			else return false;

	}

	// Note that the order in which counts are added are important.
	public void add(double count){
		instances.add(count);
	}
	
	// return the number of parameters in this namespace.
	public double getSize(){
		return instances.size();
	}
	
	public double getCount(double which){
		return (double)instances.get((int) which);
	}
	
	public double getParamByIndex(double index){
		return (double)instances.get((int) index);
	}
	
	public double getTotal(){
		double total = 0;
		for (int i =0; i<instances.size(); i++){
			total += (double)instances.get(i); 
		}
		return total;
	}
	
}
