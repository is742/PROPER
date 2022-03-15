package confidenceIntervalCalculation;

import inputOutput.Config;
import mathEngine.MathsEngine;
import observations.StateParameterObservation;
import parametricModelChecking.AlgebraicExpression;


public interface ConfidenceIntervalCalculationEngine {
	

	
	public ConfidenceInterval confidenceIntervalSynthesiser(Config config,
			SimultaneousConfidenceIntervalCalculator scic,MathsEngine mathsEngine,
			AlgebraicExpression e,Alpha alpha);
	

}
