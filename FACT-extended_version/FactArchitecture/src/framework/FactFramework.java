package framework;
import inputOutput.FactInput;
import inputOutput.Time;
import confidenceIntervalCalculation.*;
import mathEngine.MatLabEngine;
import mathEngine.MathsEngine;
import mathEngine.SimulatedMathsEngine;
import parametricModelChecking.*;

/**
 * 
 * The Fact class is...
 * 
 * 
 * */

public class FactFramework {
	private ParametricProbabilisticModelChecker ppmc;
	private SimultaneousConfidenceIntervalCalculator scic;
	private MathsEngine mathsEngine;
	private ConfidenceIntervalCalculationEngine cice;
	private ConfidenceInterval _lastComputedCI;
	//--------------------------------------------------------------------------------
	public void terminate(){
		if (this.mathsEngine != null)
			this.mathsEngine.stop();
	}
	//--------------------------------------------------------------------------------
	public void nextStepCalculateConfidenceInterval(FactInput input){

		//FactInput input = factOutput.getInput();
		AlphaStartEndStep alphas = input.alphaStartEndStep;

		if(alphas.hasNext()){
			//processes each alpha in the "experiment"		
			Alpha anAlpha = alphas.next();			

			//measure how long it takes to compute ci.
			input.timing = new Time();

			ConfidenceInterval ci = 
					cice.confidenceIntervalSynthesiser(input.config,scic, mathsEngine, input.algebraicExpression, anAlpha);

			input.timing.stop();
			
			//update the fact data structure
			input.ciResults.add(ci);
			//factOutput.confidenceIntervals.put(ci,timing);
			//record the last input confidence interval
			this._lastComputedCI = ci;

			//needs to be directed to an appropriate logger.
			//System.out.println(ci);
		} else
		{
			this._lastComputedCI=null;
		}
		//return factOutput;	
	}
	//--------------------------------------------------------------------------------
	public void resetSimultaneousConfidenceIntervalCalculator(FactInput input){
		//create the simultaneous confidence interval object
		scic = new Proposition2(input.observations);

	}
	//--------------------------------------------------------------------------------
	//computes the confidence interval algorithm using the appropriate FACT components...
	public void calculateConfidenceInterval(FactInput input){

		
//		FactOutput factOutput = new FactOutput(input);

		if (input.observations.size()<=1)
			throw new IllegalArgumentException("Not observations have been input.");

		//create the simultaneous confidence interval object
		scic = new Proposition2(input.observations);

		//create the maths engine with the confidence intervals for parameters and an algebraic expression
		this.mathsEngine = new MatLabEngine();

		//this.mathsEngine = new OctaveEngine();

		AlphaStartEndStep alphas = input.alphaStartEndStep;

		//processes each alpha in the "experiment"
		while(alphas.hasNext()){
			Alpha anAlpha = alphas.next();			
			Time timing = new Time();//measure how long it takes to compute ci.
			
			//add the newly calculated confidence interval to the list of confidence intervals
			ConfidenceInterval ci =
					cice.confidenceIntervalSynthesiser(input.config,scic, mathsEngine, input.algebraicExpression, anAlpha);
			timing.stop();
			input.ciResults.add(ci);
			
			//factOutput.confidenceIntervals.put(ci,timing);
			this._lastComputedCI = ci;//update the last recorded confidence interval
//			System.out.println(input.ciResults+"\n");
		}

		mathsEngine.stop();
		//return factOutput;
	}
	//--------------------------------------------------------------------------------
	public FactFramework(ParametricProbabilisticModelChecker ppmc,
			SimultaneousConfidenceIntervalCalculator scic,
			MathsEngine mathsEngine,
			ConfidenceIntervalCalculationEngine cice){

		//set each component of the FACT framework
		this.setPpmc(ppmc);
		this.setScic(scic);
		this.setMathsEngine(mathsEngine);
		this.setCice(cice);	
	}
	//--------------------------------------------------------------------------------
	/*
	 * Default constructor provides the standard FACT components;
	 * */
	public FactFramework() {
		this.cice = new ConfidenceIntervalCalculationEngineImplementation();
		this._lastComputedCI = null;
	}
	//--------------------------------------------------------------------------------
	public ParametricProbabilisticModelChecker getPpmc() {
		return ppmc;
	}
	//--------------------------------------------------------------------------------
	public void setPpmc(ParametricProbabilisticModelChecker ppmc) {
		this.ppmc = ppmc;
	}
	//--------------------------------------------------------------------------------
	public SimultaneousConfidenceIntervalCalculator getScic() {
		return scic;
	}
	//--------------------------------------------------------------------------------
	public void setScic(SimultaneousConfidenceIntervalCalculator scic) {
		this.scic = scic;
	}
	//--------------------------------------------------------------------------------
	public MathsEngine getMathsEngine() {
		return mathsEngine;
	}
	//--------------------------------------------------------------------------------
	public void setMathsEngine(MathsEngine mathsEngine) {
		this.mathsEngine = mathsEngine;
	}
	//--------------------------------------------------------------------------------
	public ConfidenceIntervalCalculationEngine getCice() {
		return cice;
	}
	//--------------------------------------------------------------------------------
	public void setCice(ConfidenceIntervalCalculationEngine cice) {
		this.cice = cice;
	}
	//--------------------------------------------------------------------------------
	public ConfidenceInterval getLastComputedCI() {
		return _lastComputedCI;
	}
	//--------------------------------------------------------------------------------
}