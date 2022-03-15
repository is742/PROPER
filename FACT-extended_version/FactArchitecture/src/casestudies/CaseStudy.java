package casestudies;

import java.io.IOException;

import confidenceIntervalCalculation.AlphaStartEndStep;
import confidenceIntervalCalculation.ConfidenceInterval;
import framework.FactFramework;
import inputOutput.FactInput;
import inputOutput.FileUtils;
import logging.FACTLogger;
import mathEngine.MatLabEngine;
import mathEngine.SimulatedMathsEngine;
import parametricModelChecking.OperatingSystem;
import parametricModelChecking.PrismDTMCModel;
import parametricModelChecking.PrismPCTLProperty;
import parametricModelChecking.PrismParametricModelChecker;

public abstract class CaseStudy {	
	public FactFramework fact;
	public FactInput factInput;
	public static boolean MATLAB_ON=true;

	public static String PRISMLOCATION = "/Users/ColinPaterson/Documents/prism-4.2.beta1-osx64/bin/prism";

	
	//------------------------------------------------------
	public void checkEngine(){
		if(this.fact.getMathsEngine() == null){
			if(MATLAB_ON)
				this.fact.setMathsEngine(new MatLabEngine());
			else
				this.fact.setMathsEngine(new SimulatedMathsEngine());
			
		}
	}
	//------------------------------------------------------
	public ConfidenceInterval run(){
		if(factInput.algebraicExpression == null){
			this.modelCheck();
		}
		
		checkEngine();

		//fact.nextStepCalculateConfidenceInterval(input);
		fact.calculateConfidenceInterval(factInput);
		
		return fact.getLastComputedCI();		
	}
	//------------------------------------------------------
	public void modelCheck() {
		System.out.println("Prism is attempting parametric model checking");
		FACTLogger flog = null;
		factInput.algebraicExpression = fact.getPpmc().modelCheck(factInput.model, factInput.property, flog);	
	}
	//------------------------------------------------------
	public CaseStudy(String modelFileLocation,String propertyFileLocation){
		this.initialiseFactFramework();		
		this.factInput = new FactInput();
		try{
			factInput.model = new PrismDTMCModel();	
			factInput.model.setModel(FileUtils.readTextFileContents(modelFileLocation));

			factInput.property = new PrismPCTLProperty();		
			factInput.property.setProperty(FileUtils.readTextFileContents(propertyFileLocation));		
		}catch(IOException e){
			System.out.println("Error caught in case study");
		}

		factInput.alphaStartEndStep = new AlphaStartEndStep(0.97,0.99,0.01);
	
	}
	//------------------------------------------------------
	public void initialiseFactFramework(){		
		this.fact=new FactFramework();
		fact.setPpmc(new PrismParametricModelChecker(PRISMLOCATION,OperatingSystem.MAC));		
	}


}
