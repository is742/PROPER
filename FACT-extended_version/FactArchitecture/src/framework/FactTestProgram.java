package framework;

import inputOutput.FactInput;
import inputOutput.FileUtils;
import logging.FACTLogger;

import java.io.IOException;
import java.util.Queue;
import confidenceIntervalCalculation.Interval;
import mathEngine.MatLabEngine;
import parametricModelChecking.OperatingSystem;
import parametricModelChecking.PrismDTMCModel;
import parametricModelChecking.PrismPCTLProperty;
import parametricModelChecking.PrismParametricModelChecker;


public class FactTestProgram {
	public static String webInputFileLocation = "/Users/kjohnson/Desktop/fact/test/original/input/originalInput.txt";	//
	public static String prismExecutable = "/Users/kjohnson/prism-4.3/bin/prism";
	
	
	public static void testModelChecker(){
		PrismParametricModelChecker prism = new PrismParametricModelChecker(prismExecutable, OperatingSystem.MAC);		
		PrismDTMCModel model=null;

		try {
			model = new PrismDTMCModel("/Users/kjohnson/prism-4.3/bin/brp-param.pm");
			model.addParameter("kFail", new Interval(0.01,0.02));
			model.addParameter("lFail",new Interval(0.01,0.02));
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
		PrismPCTLProperty property = new PrismPCTLProperty();
		try {
			property.setProperty(FileUtils.readTextFileContents("/Users/kjohnson/prism-4.3/bin/p1.pctl"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FACTLogger flog=null;
		prism.modelCheck(model, property, flog);
		//GenPolynomial x;
		
		//create a fact input object here.
		
	}
	
	
	
	public static void main(String[] args) {
		
		//testModelChecker();
		
		
		
		FactFramework fact = new FactFramework();

		Queue<FactInput> q=  FactInput.readFactInputFromOriginalFile(webInputFileLocation);
		FactInput fi = q.poll();
		System.out.println("Analysing: "+fi.algebraicExpression);

		//fact.setMathsEngine(new OctaveEngine("/usr/local/Cellar/octave/4.0.0/bin/octave","/Users/kjohnson/yalmip"));		
		fact.setMathsEngine(new MatLabEngine());

		fact.calculateConfidenceInterval(fi);
		System.out.println("Done.");

	}

}
