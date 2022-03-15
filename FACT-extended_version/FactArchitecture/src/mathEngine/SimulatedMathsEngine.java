package mathEngine;

import java.util.Random;

import confidenceIntervalCalculation.SimultaneousConfidenceIntervalCalculator;
import parametricModelChecking.AlgebraicExpression;
/**
 * 
 * Simulates the output of a maths engine without the overhead of computing results.
 * For development use only.
 * 
 * */
public class SimulatedMathsEngine implements MathsEngine{


	private Random rand;
	public SimulatedMathsEngine(){
		this.rand = new Random();
	}

	@Override
	public MathsResult computeResult(AlgebraicExpression ae, SimultaneousConfidenceIntervalCalculator scic) {
		MathsResult mr = new MinMaxResult(rand.nextDouble(),rand.nextDouble());
		return mr;


	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
