package mathEngine;


import confidenceIntervalCalculation.SimultaneousConfidenceIntervalCalculator;
import parametricModelChecking.AlgebraicExpression;



public interface MathsEngine {
	public MathsResult computeResult(AlgebraicExpression ae,SimultaneousConfidenceIntervalCalculator scic);
	public void stop();
}
