package parametricModelChecking;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import confidenceIntervalCalculation.Interval;
import inputOutput.FileUtils;

/**
 * 
 * 
 * PrismDTMCModel is a concrete representation of the abstract class ParametricProbabilisticModel. 
 * 
 * A PrismDTMCModel object has an instance variable "model" which contains the text of a parametric DTMC
 * model expressed in Prism's high-level modelling language.
 * 
 *  
 * @author kjohnson
 * Version 1.0
 * 
 * 
 */
public class PrismDTMCModel extends ParametricProbabilisticModel{

	//------------------------------------------------------------------------------------------
	public PrismDTMCModel(){
		super("");		
	}	
	//------------------------------------------------------------------------------------------
	
	public PrismDTMCModel(String prismModelFileNameAndLocation) throws IOException{
		super(FileUtils.readTextFileContents(prismModelFileNameAndLocation));
		
		String model="";

		Scanner in = null;
		try {
			in = new Scanner(new FileReader(prismModelFileNameAndLocation));
			while(in.hasNext()){
				model+= in.nextLine()+"\n";
			}
		}
		finally{
			if(in != null){
				in.close();
			}
			this.setModel(model);
		}
	}
	//------------------------------------------------------------------------------------------
}