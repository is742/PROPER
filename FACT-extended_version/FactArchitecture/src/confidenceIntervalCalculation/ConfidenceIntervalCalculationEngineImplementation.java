package confidenceIntervalCalculation;

import inputOutput.Config;
import heuristic.HillClimbingOptimisationMethod;
import mathEngine.MathsEngine;
import mathEngine.MathsResult;
import mathEngine.MinMaxResult;
import observations.StateParameterObservation;
import parametricModelChecking.AlgebraicExpression;

public class ConfidenceIntervalCalculationEngineImplementation implements ConfidenceIntervalCalculationEngine {

	/**
	 * Implements Algorithm 1.
	 * 
	 * */
	@Override
	public ConfidenceInterval confidenceIntervalSynthesiser(Config config,
			SimultaneousConfidenceIntervalCalculator scic,
			MathsEngine mathsEngine, 
			AlgebraicExpression e, 			
			Alpha alpha) {


		//initialise optimisation technique
		HillClimbingOptimisationMethod hillClimbing = new HillClimbingOptimisationMethod();

		//simultaneous confidence interval computation...	
		scic.initialiseParameterConfidenceIntervals(alpha);

		//keep the bestResult so far.
		MinMaxResult bestResult = new MinMaxResult();

		//while loop
		int iterationsWithoutImprovement = 0;
		int nIterations=0;

		int hillClimbingCount=0;

		do{
			//		nIterations++;
			if(hillClimbingCount<config.MAX_ITERATIONS){
				hillClimbingCount++;
				System.out.println("Hill climbing count: "+hillClimbingCount+" of "+config.MAX_ITERATIONS+" iterations.");
				hillClimbing.optimise(scic);

			}

			scic.updateSimultaneousConfidenceIntervals(alpha);

			if(mathsEngine == null) throw 
			new IllegalArgumentException("Maths engine cannot be null!");

			//ask the math engine to compute the result (e.g. max,min)
			MinMaxResult minMaxResult=(MinMaxResult) mathsEngine.computeResult(e, scic);			


			if(minMaxResult.compareTo(bestResult)<0){
				bestResult = minMaxResult;	
				iterationsWithoutImprovement=0;				
			}else{
				iterationsWithoutImprovement++;
			}


		}	while ((scic.stateParameterObservations().size()>1) &&(hillClimbingCount<config.MAX_ITERATIONS));


		System.out.println("Completed confidenceIntervalSynthesiser with "+hillClimbingCount+" iterations of hillclimbing.");

		return new ConfidenceInterval(bestResult.getMin(),bestResult.getMax(),alpha);
	}


}
