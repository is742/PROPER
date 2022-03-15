package heuristic;

import java.util.ArrayList;
import java.util.Random;

import observations.TransitionParameter;
import confidenceIntervalCalculation.Alpha;
import confidenceIntervalCalculation.SimultaneousConfidenceIntervalCalculator;

public class HillClimbingOptimisationMethod implements AlphaOptimisationMethod{

	@Override
	public void optimise(SimultaneousConfidenceIntervalCalculator scic) {

		Random random  = new Random();

		//get the transition parameters
		ArrayList<TransitionParameter> parameters = scic.getTransitionParameters();   		      

		//choose some random transition parameters		
		TransitionParameter param1 = parameters.get(random.nextInt(parameters.size()));		
		TransitionParameter param2 = parameters.get(random.nextInt(parameters.size()));

		//make sure they are not the same ones!
		do {
			param2 = parameters.get(random.nextInt(parameters.size()));			
		} while (param1.compareTo(param2)==0);

		//now get their alpha values
		Alpha alpha1 = scic.getConfidenceInterval(param1).minusAlpha();
		Alpha alpha2 = scic.getConfidenceInterval(param2).minusAlpha();

		// Choose a "small" value by which to modify alpha1 and alpha2
		double delta;
		do {			
			delta = 0.9 + random.nextDouble()/2.5;
		} while (alpha1.alphaValue*delta>1 || alpha2.alphaValue/delta>1);

		//update the alpha values.
		alpha1.alphaValue *= delta;
		alpha2.alphaValue /= delta;	
	}
}