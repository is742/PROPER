package casestudies;

import confidenceIntervalCalculation.Interval;
import observations.ObservationCount;
import observations.StateParameterObservation;
import observations.TransitionParameter;

public class Nand extends CaseStudy{

	public Nand( ) {
		super("casestudies/nand/nand_10-1.pm","casestudies/nand/nand.pctl");

		//parameters for parametric model checking
		factInput.model.addParameter("perr1", new Interval(0,1));
		factInput.model.addParameter("prob1",new Interval(0,1));

		//observations for FACT confidence interval calculation
		StateParameterObservation perrStateParameter = new StateParameterObservation();

		ObservationCount perr1Transitionobs = new ObservationCount();
		perr1Transitionobs.transitionObservationCount=990;
		perr1Transitionobs.totalObservationCountForStateParameter=1000;
		perrStateParameter.addObservation(new TransitionParameter("perr1"), perr1Transitionobs);

		StateParameterObservation probStateParameter = new StateParameterObservation();
		ObservationCount prob1Transitionobs = new ObservationCount();
		prob1Transitionobs.transitionObservationCount=500;
		prob1Transitionobs.totalObservationCountForStateParameter=1000;
		probStateParameter.addObservation(new TransitionParameter("prob1"), prob1Transitionobs);	

		//add the observations to the input object and update the confidence interval calculator.
		factInput.observations.add(perrStateParameter);
		factInput.observations.add(probStateParameter);		
		fact.resetSimultaneousConfidenceIntervalCalculator(factInput);
		
	}

}
